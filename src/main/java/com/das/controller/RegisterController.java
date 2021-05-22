package com.das.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.das.dao.UserDao;
import com.das.helper.UploadData;
import com.das.helper.UserColumn;
import com.das.model.UserModel;
import com.das.util.Validator;

/**
 * Servlet implementation class RegisterController
 */
@WebServlet("/register")
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao dao;
	private UserModel user;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterController() {
        super();
        this.user = new UserModel();
		this.dao = new UserDao("USERS");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String repassword = request.getParameter("repassword");
		String contact = request.getParameter("contact");
		String address = request.getParameter("address");
		
		String page = "index.jsp";
		String message = "";
		boolean isError = true;
		
		if(Validator.isEmpty(fname)) {
			message = "Empty firstname";
		}else if(Validator.isEmpty(lname)) {
			message = "Empty lastname";
		}else if(Validator.isEmpty(email)) {
			message = "Empty password";
		}else if(Validator.isEmpty(password)) {
			message = "Empty confirm password";
		}else if(Validator.isEmpty(contact)) {
			message = "Empty contact number";
		}else if(Validator.isEmpty(address)) {
			message = "Empty address";
		}else if(!Validator.isNameValid(fname) || !Validator.isNameValid(lname)) {
			message = "Invalid name";
		}else if(!Validator.isEmailValid(email)) {
			message = "Invalid email";
		}else if(!Validator.isPasswordValid(password)) {
			message = "Invalid password. Password must be atleast 8 characters in length and starts with a capital letter and also contains number";
		}else if(!Validator.isPhoneNumberValid(contact)) {
			message = "Invalid contact number format";
		}else if(!password.equals(repassword)){
			message = "Passwords do not match";
		}else {
			user.setName(fname+" "+lname);
			user.setEmail(email);
			user.setPassword(password);
			user.setContact(contact);
			user.setAddress(address);
			user.setRole(UserModel.Role.PATIENT);
			
			java.util.Date date = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime()); 
			user.setDate(sqlDate);
			
			System.out.println(user.toString());
			
			if(isEmailExist(email)) {
				message = "Email already exists";
			}else {
				long query = dao.insert(user); // Insert query
				
				if(query == 0) {
					message = "Unable to register user";
				}else {
					message = "User was registered successfully ";
					isError = false;
				}	
			}
		}
		
		System.out.println(message);
		request.setAttribute("message", message);
		request.setAttribute("error", isError);
		RequestDispatcher dispatcher = request.getRequestDispatcher(page);
		dispatcher.forward( request, response ); 
	}
	
	private boolean isEmailExist(String email) {
		ArrayList<UploadData<UserColumn, ?>> data = new ArrayList<UploadData<UserColumn, ?>>();
		data.add(new UploadData<UserColumn, String>(new UserColumn().email(), email));
				
		return dao.get(data).size() > 0;
	}

}
