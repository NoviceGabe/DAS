<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.das.model.UserModel" %>
<%
 UserModel user = (UserModel) session.getAttribute("user");
	if(user == null){
		response.sendRedirect("index.jsp");
	}
 %>
<!-- Modal -->
<div class="modal fade profile" id="profile" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
  <div class="modal-dialog  modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">Update your profile</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      
      <form class="needs-validation" novalidate>
  <div class="form-row">
    <div class="col-md-4 mb-3">
      <label for="validationCustom02">Name</label>
      <input type="text" class="form-control" id="validationCustom02" placeholder="Fullname" value="<%= (user != null)?user.getName():""%>" required
      	data-value="update-name" pattern="[a-zA-Z ]{2,30}">
      <div class="valid-feedback">
        Looks good!
      </div>
       <div class="invalid-feedback">
         Please provide a valid name.
      </div>
    </div>
  </div>
  <div class="form-row">
    <div class="col-md-6 mb-3">
      <label for="validationCustom03">Address</label>
      <input type="text" class="form-control" id="validationCustom03" placeholder="Address" required value="<%= (user != null)?user.getAddress():""%>"
      data-value="update-address">
      <div class="invalid-feedback">
        Please provide a valid address.
      </div>
    </div>
  </div>
  <div class="form-row"> 
  
  <div class="col-md-3 mb-3">
      <label for="validationCustom04">Age</label>
      <input type="text" class="form-control"  id="validationCustom04" pattern="^[0-9]{1,3}$" placeholder="Age" data-value="update-age" value="<%= (user != null && user.getAge() > 0)?user.getAge():""%>">
    <div class="invalid-feedback">
        Please provide a valid age.
      </div>
    </div>
    
    <div class="col-md-3 mb-3">
      <label for="validationCustom04">Mobile number</label>
      <input type="tel" class="form-control" placeholder="(+63)" pattern="^(\+63)[0-9]{10}$" id="validationCustom04" value='<%= (user != null)?user.getContact():""%>'
      data-value="update-contact" required>
      <div class="invalid-feedback">
        Please provide a valid mobile number.
      </div>
    </div>

    <div class="col-md-6 mb-3">
      <label for="validationCustom04">Email address</label>
      <input type="email" class="form-control" id="validationCustom04" placeholder="Email" required value="<%= (user != null)?user.getEmail():""%>"
      data-value="update-email">
      <div class="invalid-feedback">
        Please provide a valid email address.
      </div>
    </div>
  </div>

  <!--  div class="form-row"> 
  <div class="col-md-6 mb-3">
      <label for="validationCustom04">Type old password</label>
      <input type="password" class="form-control" id="validationCustom04" placeholder="Old password" >
      
    </div>
    <div class="col-md-6 mb-3">
      <label for="validationCustom04">Type new password</label>
      <input type="password" class="form-control" id="validationCustom04" placeholder="New password" >
      
    </div>
  </div-->

  

  </div>
      
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        
        <button class="btn btn-primary" type="submit">Save Changes</button>
      
      </div>
      </form>

    </div>
  </div>
</div> 



<script>
// Example starter JavaScript for disabling form submissions if there are invalid fields
$(document).ready(function(){
	(function() {
		  'use strict';
		  window.addEventListener('load', function() {
		    // Fetch all the forms we want to apply custom Bootstrap validation styles to
		    var forms = document.getElementsByClassName('needs-validation');
		    // Loop over them and prevent submission
		    var validation = Array.prototype.filter.call(forms, function(form) {
		      form.addEventListener('submit', function(event) {
		    	  event.preventDefault();
		    	  
		        if (form.checkValidity() === false) {
		          event.stopPropagation();
		        }
		        
		        form.classList.add('was-validated');
		        
		        const name = document.querySelector('input[data-value="update-name"]').value;
		        const address = document.querySelector('input[data-value="update-address"]').value;
		        const age = document.querySelector('input[data-value="update-age"]').value;
		        const contact = document.querySelector('input[data-value="update-contact"]').value;
		        const email = document.querySelector('input[data-value="update-email"]').value;
		        const userId = <%= (user != null)?user.getId():0%>
		        
		        if(isEmpty(name) || isEmpty(address) || isEmpty(email) || isEmpty(contact)){
		        	Swal.fire(
           				  'Error!',
           				 'Please fill up the required fields'
           				  ,
           				  'error'
           				)
           				return false;
		        }else if(!isNameValid(name)){
		        	Swal.fire(
	           				  'Error!',
	           				 'Invalid name'
	           				  ,
	           				  'error'
	           				)
	           				return false;
		        }else if(!isPhoneNumberValid(contact)){
		        	Swal.fire(
	           				  'Error!',
	           				 'Invalid contact number format'
	           				  ,
	           				  'error'
	           				)
	           				return false;
		        }else if(!isEmailValid(email)){
		        	Swal.fire(
	           				  'Error!',
	           				 'Invalid email'
	           				  ,
	           				  'error'
	           				)
	           				return false;
		        }else if(!isAgeValid(age)){
		        	Swal.fire(
	           				  'Error!',
	           				 'Invalid age'
	           				  ,
	           				  'error'
	           				)
	           				return false;
		        }
		        
		        
		        $.ajax(
		        		{
		        			type: "POST",
			 	            url: "UpdateController",
			 	            data: { 
			 	            	'userId': userId,
			 	            	'name': name,
			 	            	'address': address,
			 	            	 'age': age,
			 	            	'contact': contact,
			 	            	'email': email
			 	            	},
		        			success: function(response) {
			 	            	if(response || response.length > 0){
			 	            		if(response.status){
			 	            			Swal.fire(
				 	            				  'Success!',
				 	            				 response.message,
				 	            				  'success'
				 	            				)
			 	            		}else{
			 	            			Swal.fire(
				 	            				  'Error!',
				 	            				 response.message
				 	            				  ,
				 	            				  'error'
				 	            				)
			 	            		}
			 	            		
			 	            		
			 	            	}
			 	            	
			 	            },
			 	            failure: function (response) {
			 	            
			 	            }
		        		});
		      }, false);
		    });
		  }, false);
		})();
});

</script>


</html>