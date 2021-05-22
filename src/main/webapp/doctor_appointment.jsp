<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="com.das.model.UserModel" %>
<%@ page import="com.das.model.AppointmentModel" %>
<%@ page import="com.das.helper.AppointmentColumn" %>
<%@ page import="com.das.helper.UploadData" %>
<%@ page import="com.das.dao.AppointmentDao" %>
<%@ page import="java.util.ArrayList" %>

<%	
	UserModel user = (UserModel) session.getAttribute("user");
	if(user == null || !user.getRole().toString().toUpperCase().equals(UserModel.Role.DOCTOR.toString().toUpperCase())){
		response.sendRedirect("index.jsp");
	}

	
	AppointmentDao appDao = new AppointmentDao("APPOINTMENTS");
	ArrayList<AppointmentModel> appos = appDao.get(user.getId());
%>

<jsp:include page="includes/head.jsp" />
<jsp:include page="includes/sidebar.jsp" />
    
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

      <!-- Main Content -->
      <div id="content">

        <!-- Topbar -->
        <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

          <!-- Sidebar Toggle (Topbar) -->
          <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
            <i class="fa fa-bars"></i>
          </button>


          <form class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
                        <div class="input-group">
                         <h2>Doctor</h2>
                            <div class="input-group-append">
                               
                            </div>
                        </div>
                    </form>


          <!-- Topbar Navbar -->
          <ul class="navbar-nav ml-auto">

             <!-- Nav Item - User Information -->
            <li class="nav-item dropdown no-arrow">
              <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <span class="mr-2 d-none d-lg-inline text-gray-600 small">
            	<%= (user != null)?user.getName():"" %></span>
                <img class="img-profile rounded-circle" src="resources/assets/images/icon.png">
              </a>
              <!-- Dropdown - User Information -->
              <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="userDropdown">
                <a class="dropdown-item" href="#" data-toggle="modal" data-target=".profile">
                  <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                  Profile
                </a>
                
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="" data-toggle="modal" data-target="#logoutModal">
                  <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                  Logout
                </a>
              </div>
            </li>

          </ul>

        </nav>
        <!-- End of Topbar -->

        <!-- Begin Page Content -->
          <div class="container-fluid">

          <!-- Page Heading -->
          <h1 class="h3 mb-2 text-gray-800">VIEW APPOINTMENT</h1>

          
      

  <!-- DataTales Example -->
          <div class="card shadow mb-4">
            <div class="card-header py-3">
              <h6 class="m-0 font-weight-bold text-primary">LIST</h6>
            </div>
            <div class="card-body">
         
              <div class="table-responsive">
                <table class="table table-bordered" id="patient_table" width="100%" cellspacing="0">
                
        
        

                  <thead>
                    <tr>

        
       <th>#</th>
       <th>Patient Name</th>
       <th>Doctor Name</th>
       <th>Appointment Date</th>
       <th>Available Time</th>
       <th>Status</th>
       <th>Action</th>
 
      </tr>
                  </thead>
                  <tbody>
               <%
              	int count = 1;
              	for(AppointmentModel appo: appos){
              %>
            <tr id="<%= appo.getId() %>" data-doc-id="<%= appo.getDoctor().getId()%>" data-sched-id="<%= appo.getSchedule().getId()%>"
            data-patient-id="<%= appo.getPatient().getId() %>">
                <td><%= count %></td>
                <td><%= appo.getPatient().getName() %></td>
		        <td><%= appo.getDoctor().getName() %></td>
                <td><%= appo.getDate() %></td>
                <td><%= appo.getSchedule().getStart_time() %></td>
                <td class="status"><%= appo.getStatus().toString().toUpperCase() %></td>
         		<td >
         		<% String status = appo.getStatus().toString().toUpperCase();
         		   String btns = "<button type='button' name='approve' class='btn btn-primary btn-circle btn-sm view' id=''> <i class='fas fa-eye'></i></button> "+
         	              "<button type='button' name='approve' class='btn btn-success btn-circle btn-sm complete' id='' > <i class='fas fa-check'></i></button> " +
                          "<button type='button' name='decline' class='btn btn-danger btn-circle btn-sm cancel' id='' > <i class='fas fa-ban'></i></button>" +
                          "<button type='button' name='approve' class='btn btn-info btn-circle btn-sm add' id=''   data-toggle='modal' data-target='#exampleModalCenter'> <i class='fas fa-clipboard'></i></button>" +
                          "<button type='button' name='decline' class='btn btn-danger btn-circle btn-sm remove' id=''> <i class='fas fa-times'></i></button> ";
         			if(status.equals(AppointmentModel.Status.COMPLETED.toString().toUpperCase()) || status.equals(AppointmentModel.Status.CANCELLED.toString().toUpperCase())){
         				btns = "<button type='button' name='approve' class='btn btn-primary btn-circle btn-sm view' id=''> <i class='fas fa-eye'></i></button> "+
                              "<button type='button' name='approve' class='btn btn-info btn-circle btn-sm add' id=''   data-toggle='modal' data-target='#exampleModalCenter'> <i class='fas fa-clipboard'></i></button>" +
                              "<button type='button' name='decline' class='btn btn-danger btn-circle btn-sm remove' id=''> <i class='fas fa-times'></i></button> ";
         			}
         			out.print(btns);
         		%>
         			
         		</td>
            </tr>
             <%
		        count++;
		        } 
		        %>
                  </tbody>
                   
           
                </table>
              </div>
            </div>
          </div>
       


         <!-- Button trigger modal -->
<!--<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenter">
  Launch demo modal
</button> -->

<!-- Modal -->
<div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">Patient's Details</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">

      <form class="" novalidate>
  <div class="form-row">
    <div class="col-md-4 mb-3">
      <label for="validationCustom01">First name: Tyrone</label>
    </div> &emsp;
    
      <div class="col-md-4 mb-3">
      <label for="validationCustom02">Last name: Morato</label>

    </div>
  </div>
  <div class="form-row">

    <div class="col-md-10 mb-3">
      <label for="validationCustom03">Address: 174 Ave. San Roque, Marikina City</label>
      
    </div>
  
  </div>
  <div class="form-row"> 
  <div class="col-md-3 mb-3">
      <label for="validationCustom04">Age: 22</label>

    </div>
    &emsp;&emsp;&emsp;
    <div class="col-md-7 mb-3">
      <label for="validationCustom04">Mobile number: +639772086676</label>
     
    </div>

    
  </div>
  <div class="form-row"> 
  <div class="col-md-10 mb-3">
      <label for="validationCustom04">Email address: moratotj33@gmail.com</label>
     
    </div>
  </div>

  

  </form>


      <h4>Prescription (Rx)</h4>
      <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save Prescriptions</button>
      </div>
    </div>
  </div>
</div>


<jsp:include page="includes/profile_modal.jsp" />
<jsp:include page="includes/scripts.jsp" />


<script>
$(document).ready(function(){
	$('#schedule_table').DataTable();
	$(".complete").click(function(e) {
	    e.preventDefault();
	    e.stopPropagation();
	    
	    let row = e.target.parentElement.parentElement;
		const tag = e.target.tagName;
		if(tag == 'I'){
		   row = row.parentElement;
		}
		
	    Swal.fire({
	        title: "Mark appointment as complete?",
	        showCancelButton: true,
	        confirmButtonColor: "#1FAB45",
	        confirmButtonText: "Yes",
	        cancelButtonText: "No",
	        buttonsStyling: true
	    }).then(function (result) {   
	    	if(result.isConfirmed){
	    	    const id = row.getAttribute('id');
	    	    const patientId = row.dataset.patientId;
	    	    const schedId = row.dataset.schedId;
	    	    const docId = <%= (user != null)?user.getId():0%>;
				const role = '<%= (user != null)?user.getRole().toString().toUpperCase():""%>';
				const data = { 
	 	            	'appoId': id,
	 	            	'schedId': schedId,
	 	            	'docId': docId,
	 	            	'patientId': patientId,
	 	            	'role': role,
	 	            	'action': 'complete'
	 	            	}
				console.log(data);
	    	    $.ajax({
	 	            type: "POST",
	 	            url: "AppointmentController",
	 	            data: data,
	 	            cache: false,
	 	            success: function(response) {
	 	            	console.log(response)
	 	            	if(response != null || response.length > 0){
	 	            		if(response.status){
	 	            			Swal.fire(
	 		 	   	 	                "Success!",
	 		 	   	 	          response.message,
	 		 	   	 	                "success"
	 		 	   	 	        )
	 		 	   	 	  const check = row.querySelector('.complete');
	 		 	   	 	  const cancel = row.querySelector('.cancel');
	 		 	   	 	  const status = row.querySelector('.status');
	 		 	   	 	  const parent = check.parentElement;
			 		 	  parent.removeChild(check);
			 		 	  parent.removeChild(cancel);
			 		 	  status.innerHTML = "COMPLETED";
			 		 	  console.log(status)
	 	            		}else{
	 	            			Swal.fire(
	 		 	   	 	                "Error!",
	 		 	   	 	          response.message,
	 		 	   	 	                "error"
	 		 	   	 	        )
	 	            		}
	 	            		
	 	            	}
	 	            	
	 	            },
	 	            failure: function (response) {
	 	            	Swal.fire(
	 	                "Internal Error",
	 	                "Oops, unable to complete appointment.", // had a missing comma
	 	                "error"
	 	                )
	 	            }
	 	        });
	    	}
	    }, 
	    function (dismiss) {
	      if (dismiss === "cancel") {
	    	  Swal.fire(
	          "Cancelled",
	            "Canceled action",
	          "error"
	        )
	      }
	    })
	});	
	$(".cancel").click(function(e) {
	    e.preventDefault();
	    e.stopPropagation();
	    
	    let row = e.target.parentElement.parentElement;
		const tag = e.target.tagName;
		if(tag == 'I'){
		   row = row.parentElement;
		}
	    Swal.fire({
	        title: "Do you want to cancel this appointment?",
	        showCancelButton: true,
	        confirmButtonColor: "#1FAB45",
	        confirmButtonText: "Yes",
	        cancelButtonText: "No",
	        buttonsStyling: true
	    }).then(function (result) {   
	    	if(result.isConfirmed){
	    	    const id = row.getAttribute('id');
	    	    const schedId = row.dataset.schedId;
	    	    const docId = <%= (user != null)?user.getId():0%>;
				const role = '<%= (user != null)?user.getRole().toString().toUpperCase():""%>';
				console.log(row);
				console.log("app"+id);
				console.log("doc"+docId);
				console.log("sched"+schedId);
	    	    $.ajax({
	 	            type: "POST",
	 	            url: "AppointmentController",
	 	            data: { 
	 	            	'appoId': id,
	 	            	'schedId': schedId,
	 	            	'docId': docId,
	 	            	'role': role,
	 	            	'action': 'cancel'
	 	            	},
	 	            cache: false,
	 	            success: function(response) {
	 	            	console.log(response)
	 	            	if(response != null || response.length > 0){
	 	            		if(response.status){
	 	            			Swal.fire(
	 		 	   	 	                "Success!",
	 		 	   	 	          response.message,
	 		 	   	 	                "success"
	 		 	   	 	        )
	 		 	   	 			// delete row
	 		 	   	 	  const check = row.querySelector('.complete');
	 		 		 	   	 	  const cancel = row.querySelector('.cancel');
	 		 		 	   	 	  const status = row.querySelector('.status');
	 		 		 	   	 	  const parent = check.parentElement;
	 				 		 	  parent.removeChild(check);
	 				 		 	  parent.removeChild(cancel);
	 				 		 	  status.innerHTML = "CANCELLED";
	 	            		}else{
	 	            			Swal.fire(
	 		 	   	 	                "Error!",
	 		 	   	 	          response.message,
	 		 	   	 	                "error"
	 		 	   	 	        )
	 	            		}
	 	            		
	 	            	}
	 	            	
	 	            },
	 	            failure: function (response) {
	 	            	Swal.fire(
	 	                "Internal Error",
	 	                "Oops, unable to cancel appointment.", // had a missing comma
	 	                "error"
	 	                )
	 	            }
	 	        });
	    	}
	    }, 
	    function (dismiss) {
	      if (dismiss === "cancel") {
	    	  Swal.fire(
	          "Cancelled",
	            "Canceled action",
	          "error"
	        )
	      }
	    })
	});	
	
	$(".remove").click(function(e) {
	    e.preventDefault();
	    e.stopPropagation();
	    
	    let row = e.target.parentElement.parentElement;
		const tag = e.target.tagName;
		if(tag == 'I'){
		   row = row.parentElement;
		}
	    Swal.fire({
	        title: "Do you want to remove this appointment?",
	        showCancelButton: true,
	        confirmButtonColor: "#1FAB45",
	        confirmButtonText: "Yes",
	        cancelButtonText: "No",
	        buttonsStyling: true
	    }).then(function (result) {   
	    	if(result.isConfirmed){
	    		 const id = row.getAttribute('id');
		    	    const patientId = row.dataset.patientId;
		    	    const schedId = row.dataset.schedId;
		    	    const docId = <%= (user != null)?user.getId():0%>;
					const role = '<%= (user != null)?user.getRole().toString().toUpperCase():""%>';
					 const status = row.querySelector('.status');
					const data = { 
		 	            	'appoId': id,
		 	            	'schedId': schedId,
		 	            	'docId': docId,
		 	            	'patientId': patientId,
		 	            	'role': role,
		 	            	'action': 'remove'
		 	            	}
				if(status.innerHTML != "COMPLETED"){
					Swal.fire(
	 	   	 	                "Error!",
	 	   	 	           		'Appointment was not completed yet',
	 	   	 	                "error"
	 	   	 	        )
					return false;
				}
	    	    $.ajax({
	 	            type: "POST",
	 	            url: "AppointmentController",
	 	            data: { 
	 	            	'appoId': id,
	 	            	'schedId': schedId,
	 	            	'docId': docId,
	 	            	'role': role,
	 	            	'action': 'remove'
	 	            	},
	 	            cache: false,
	 	            success: function(response) {
	 	            	console.log(response)
	 	            	if(response != null || response.length > 0){
	 	            		if(response.status){
	 	            			Swal.fire(
	 		 	   	 	                "Success!",
	 		 	   	 	          response.message,
	 		 	   	 	                "success"
	 		 	   	 	        )
	 		 	   	 			// delete row
	 		 	   	 	  const check = row.querySelector('.complete');
	 		 		 	   	 	  const cancel = row.querySelector('.cancel');
	 		 		 	   	 	  
	 		 		 	   	 	  const parent = check.parentElement;
	 				 		 	  parent.removeChild(check);
	 				 		 	  parent.removeChild(cancel);
	 				 		 	  status.innerHTML = "CANCELLED";
	 	            		}else{
	 	            			Swal.fire(
	 		 	   	 	                "Error!",
	 		 	   	 	          response.message,
	 		 	   	 	                "error"
	 		 	   	 	        )
	 	            		}
	 	            		
	 	            	}
	 	            	
	 	            },
	 	            failure: function (response) {
	 	            	Swal.fire(
	 	                "Internal Error",
	 	                "Oops, unable to remove appointment.", // had a missing comma
	 	                "error"
	 	                )
	 	            }
	 	        });
	    	}
	    }, 
	    function (dismiss) {
	      if (dismiss === "cancel") {
	    	  Swal.fire(
	          "Cancelled",
	            "Canceled action",
	          "error"
	        )
	      }
	    })
	});	
});	
</script>

   </div>
   <!-- /.container-fluid -->

 </div>
 <!-- End of Main Content -->

