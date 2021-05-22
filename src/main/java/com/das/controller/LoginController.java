package com.das.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.das.dao.UserDao;
import com.das.helper.UploadData;
import com.das.helper.UserColumn;
import com.das.model.UserModel;
import com.das.util.Validator;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao dao;
	private UserModel user;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
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
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		boolean isError = true;
        String message = "";
        String page = "index.jsp";
      
        if(Validator.isEmpty(email) || Validator.isEmpty(password)) {
        	message = "Please enter your email and password";
        }else if(!Validator.isEmailValid(email)) {
        	message = "Invalid email";
            }else {
   
            	ArrayList<UploadData<UserColumn, ?>> data = new ArrayList<UploadData<UserColumn, ?>>();
    			data.add(new UploadData<UserColumn, String>(new UserColumn().email(), email));
    			data.add(new UploadData<UserColumn, String>(new UserColumn().password(), password));
    			
    			ArrayList<UserModel> users = dao.get(data);
    			
    			if(users.size() == 0) {
    				message = "Invalid username or password";
			}else {
				message = "Success";
				user = users.get(0);
				//System.out.println(user.toString());
				isError = false;
			}
        }
        
        System.out.println(message);
		
		if(!isError) {
			if(user.getRole().equals(UserModel.Role.PATIENT)) {
				page = "patient_home.jsp";
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
			}else if(user.getRole().equals(UserModel.Role.DOCTOR)) {
				page = "doctor_appointment.jsp";
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
			}
		}
        
		request.setAttribute("message", message);
		request.setAttribute("error", isError);
		
		response.sendRedirect(page);
	}

}
