package com.das.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.das.dao.UserDao;
import com.das.helper.UploadData;
import com.das.helper.UserColumn;
import com.das.model.UserModel;

/**
 * Servlet implementation class UpdateController
 */
@WebServlet("/UpdateController")
public class UpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean status = false;
	private String message = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateController() {
        super();
        // TODO Auto-generated constructor stub
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
		this.status = false;
		String name = request.getParameter("name");
		int age = 0;
		try {
			age = Integer.parseInt(request.getParameter("age"));
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		String address = request.getParameter("address");
		String contact = request.getParameter("contact");
		String email = request.getParameter("email");
		int userId = Integer.parseInt(request.getParameter("userId"));
		
		UserDao userDao = new UserDao("USERS");
		UserModel user = new UserModel();
		
		//user.setId(userId);
		//user.setName(name);
		//user.setAddress(address);
		//user.setContact(contact);
		//user.setEmail(email);
		//user.setAge(age);
		
		ArrayList<UploadData<UserColumn, ?>> data = new ArrayList<UploadData<UserColumn, ?>>();
		data.add(new UploadData<UserColumn, String>(new UserColumn().name(),name));
		if(age > 0) {
			data.add(new UploadData<UserColumn, Integer>(new UserColumn().age(),age));
		}
		data.add(new UploadData<UserColumn, String>(new UserColumn().address(),address));
		data.add(new UploadData<UserColumn, String>(new UserColumn().contact(),contact));
		data.add(new UploadData<UserColumn, String>(new UserColumn().email(),email));
		
		int result = userDao.update(data, userId);
		
		if(result == 0) {
			message = "Unable to update profile";
		}else {
			this.status = true;
			message = "You have successfully update your profile";
		}
		
		
		
		System.out.println("status: "+this.status);
		System.out.println("message: "+message);

		response.setContentType("application/json");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", this.status);
		jsonObject.put("message", message);
		
		response.getWriter().write(jsonObject.toString());
	}

}
