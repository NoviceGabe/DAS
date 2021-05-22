<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="com.das.model.UserModel" %>
<%@ page import="com.das.model.ScheduleModel" %>
<%@ page import="com.das.dao.ScheduleDao" %>
<%@ page import="java.util.ArrayList" %>

<%	
	UserModel user = (UserModel) session.getAttribute("user");
	if(user == null || !user.getRole().toString().toUpperCase().equals(UserModel.Role.PATIENT.toString().toUpperCase())){
		response.sendRedirect("index.jsp");
	}
	
	ScheduleDao schedDao = new ScheduleDao("SCHEDULES");
	ArrayList<ScheduleModel> scheds = schedDao.get();
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
                     <h2>Patient</h2>
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
      <h1 class="h3 mb-2 text-gray-800">BOOK APPOINTMENT</h1>

    
<!-- DataTales Example -->
      <div class="card shadow mb-4">
        <div class="card-header py-3">
          <h6 class="m-0 font-weight-bold text-primary">AVAILABLE LIST</h6>
        </div>
        <div class="card-body">
     
          <div class="table-responsive">
            <table class="table table-bordered" id="schedule_table" width="100%" cellspacing="0">
              <thead>
                <tr>
				   <th>#</th>
				   <th>Doctor Name</th>
				   <th>Department</th>
				   <th>Appointment Day</th>
				   <th>Available Time</th>
				   <th>Action</th>
			  	</tr>
              </thead>
              <tbody>
              <%
              	int count = 1;
              	for(ScheduleModel sched: scheds){
              %>
		        <tr id="<%= sched.getId() %>" data-doc-id="<%= sched.getDoctor().getId()%>">
		            <td><%= count %></td>
		            <td><%= sched.getDoctor().getName() %></td>
		            <td><%= (sched.getDoctor().getDescp() != null) ? sched.getDoctor().getDescp(): "not specified" %></td>
		            <td><%= sched.getDay() %></td>
		            <td><%= sched.getStart_time() + "-" + sched.getEnd_time() %></td>
		            <td>
		            <% 
		            String status = "BOOK";
		            String btn = "btn-success";
		            
		            if(sched.getStatus().toString().toUpperCase().equals(ScheduleModel.Status.RESERVED.toString().toUpperCase())){
		            	status = "BOOKED";
		            	btn = "btn-danger";
		            }		            
		            %>
		            <button type="button" name="approve" class="btn <%= btn %> book" data-status="<%= status %>">
		            	<%= status %>
		            </button>
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

<jsp:include page="includes/profile_modal.jsp" />
<jsp:include page="includes/scripts.jsp" />

<script>
$(document).ready(function(){
	$('#schedule_table').DataTable();
	
	$(".book").click(function(e) {
	    e.preventDefault();
	    e.stopPropagation();
	    if(e.target.dataset.status == "BOOKED" || e.target.classList.contains('btn-danger')){
	    	return false;
	    }
	    Swal.fire({
	        title: "Do you want to book this schedule?",
	        showCancelButton: true,
	        confirmButtonColor: "#1FAB45",
	        confirmButtonText: "Book",
	        cancelButtonText: "Cancel",
	        buttonsStyling: true
	    }).then(function (result) {   
	    	if(result.isConfirmed){
	    		const row = e.target.parentElement.parentElement;
	    	    const id = row.getAttribute('id');
	    	    const docId = row.dataset.docId;
	    	    const userId = <%= (user != null)?user.getId():0%>
	    	   // console.log(userId);
	    		 $.ajax({
	 	            type: "POST",
	 	            url: "BookController",
	 	            data: { 
	 	            	'schedId': id,
	 	            	'docId': docId,
	 	            	'userId': userId
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
	 		 	   	 	  		e.target.innerHTML = "BOOKED";
	 	            			e.target.classList.add('btn-danger');
	 	            			e.target.classList.remove('btn-success');
	 	            			
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
	 	                "Oops, unable to book appointment.", // had a missing comma
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
	            "Canceled book",
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




 