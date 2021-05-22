<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.das.model.UserModel" %>
 <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

      <!-- Sidebar - Brand -->
      <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.php">
        <div class="sidebar-brand-icon">
        	<img src="resources/assets/images/hospital.png" alt="cpelogos" width="50" height="50">
       
        </div>
        <div class="sidebar-brand-text mx-3">MINDTECH HOSPITAL</div>

      </a>

      <!-- Divider -->
      <hr class="sidebar-divider my-0">

      <!-- Nav Item - Dashboard -->
      

      <!-- Divider -->
      <hr class="sidebar-divider">

      <!-- Heading -->
      

      <!-- Nav Item - Pages Collapse Menu -->
      

      <!-- Nav Item - Utilities Collapse Menu -->
      <% 
      
      UserModel user = (UserModel) session.getAttribute("user");
      
      if(user != null && user.getRole().toString().toUpperCase().equals(UserModel.Role.PATIENT.toString().toUpperCase())){ 
    	  String li = "<li class='nav-item'>"+
    		        "<a class='nav-link' href='patient_home.jsp'>" +
    		        "<i class='fas fa-calendar-day'></i>" +
    		         " <span>Book Appointment</span></a>"+
    		      "</li>";
	      out.print(li);
      }
      %>

      <li class="nav-item">
        <a class="nav-link" href="patient_appointment.jsp">
        <i class="far fa-calendar-check"></i>
          <span>View Appointment</span></a>
      </li>

      <!--li class="nav-item">
        <a class="nav-link" href="adding_form.php">
        <i class="fas fa-envelope-open-text"></i>
          <span>Message Doctor</span></a>
      </li-->


    





      <!-- Divider -->
      <hr class="sidebar-divider">

      
      

     

      
     

   

      <!-- Sidebar Toggler (Sidebar) -->
      <div class="text-center d-none d-md-inline">
        <button class="rounded-circle border-0" id="sidebarToggle"></button>
      </div>

    </ul>
    <!-- End of Sidebar -->




  <!-- Scroll to Top Button-->
  <a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
  </a>




     <!-- Logout Modal-->
  <div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
          <button class="close" type="button" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">×</span>
          </button>
        </div>
        <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
        <div class="modal-footer">
          <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
          <a class="btn btn-primary" href="logout.jsp">Logout</a>
        </div>
      </div>
    </div>
  </div>
