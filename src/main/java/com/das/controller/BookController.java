package com.das.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.das.dao.AppointmentDao;
import com.das.dao.ScheduleDao;
import com.das.dao.UserDao;
import com.das.helper.AppointmentColumn;
import com.das.helper.ScheduleColumn;
import com.das.helper.UploadData;
import com.das.helper.UserColumn;
import com.das.model.AppointmentModel;
import com.das.model.ScheduleModel;
import com.das.model.UserModel;

/**
 * Servlet implementation class BookController
 */
@WebServlet("/BookController")
public class BookController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean status = false;
	private String message = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookController() {
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

		try {
			int schedId = Integer.parseInt(request.getParameter("schedId"));
			int docId = Integer.parseInt(request.getParameter("docId"));
			int userId = Integer.parseInt(request.getParameter("userId"));
			
			ArrayList<UploadData<ScheduleColumn, ?>> schedData = new ArrayList<UploadData<ScheduleColumn, ?>>();
			schedData.add(new UploadData<ScheduleColumn, Integer>(new ScheduleColumn().id("s.id"), schedId));
			
			ScheduleDao schedDao = new ScheduleDao("SCHEDULES");
			ArrayList<ScheduleModel> scheds = schedDao.get(schedData);
			
			
			ArrayList<UploadData<UserColumn, ?>> userData = new ArrayList<UploadData<UserColumn, ?>>();
			userData.add(new UploadData<UserColumn, Integer>(new UserColumn().id(), userId));
			
			UserDao userDao = new UserDao("USERS");
			ArrayList<UserModel> users = userDao.get(userData);
					
			if(users.size() == 0) {
				message = "User doesn't exists";
			}else if(scheds.size() == 0) {
				message = "Schedule doesn't exists";
			}else {
				bookAppointment(docId, scheds.get(0), users.get(0));
			}
		}catch(Exception e) {
			message = e.getMessage();
		}
		System.out.println("status: "+status);
		System.out.println("message: "+message);

		response.setContentType("application/json");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", status);
		jsonObject.put("message", message);
		
		response.getWriter().write(jsonObject.toString());
	}
	
	public static boolean isDoctorAvailable(int docId, int patientId) {
		AppointmentDao appDao = new AppointmentDao("APPOINTMENTS");
		ArrayList<UploadData<AppointmentColumn, ?>> data = new ArrayList<UploadData<AppointmentColumn, ?>>();
		data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().id("a.doctor_id"), docId));
		data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().patient_id(), patientId));
		data.add(new UploadData<AppointmentColumn, String>(new AppointmentColumn().status(), AppointmentModel.Status.BOOKED.toString().toUpperCase()));
		
		ArrayList<AppointmentModel> appos = appDao.get(data);
		return (appos.size() == 0);
	}
	
	public static boolean hasAppointment(int id) {
		AppointmentDao appDao = new AppointmentDao("APPOINTMENTS");
		
		ArrayList<UploadData<AppointmentColumn, ?>> data = new ArrayList<UploadData<AppointmentColumn, ?>>();
		data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().patient_id(), id));
		data.add(new UploadData<AppointmentColumn, String>(new AppointmentColumn().status(), AppointmentModel.Status.BOOKED.toString().toUpperCase()));
		
		ArrayList<AppointmentModel> appos = appDao.get(data);
		return (appos.size() > 0);
	}
	
	public void bookAppointment(int docId, ScheduleModel sched, UserModel user) {
		status = false;
		if(sched == null) {
			message = "No doctor schedules available!";
			return;
		}
		
		AppointmentDao appDao = new AppointmentDao("APPOINTMENTS");
		
		// check if doctor is available
		if(!isDoctorAvailable(docId, user.getId())) {
			message = "Doctor has already been booked";
			return;
		}
		// check if user already set an appointment
		if(hasAppointment(user.getId())) {
			message = "You can't book another appointment until you finished your current appointment";
			return;
		}
		
		// else book an appointment
		AppointmentModel appModel = new AppointmentModel();
		
		appModel.setStatus(AppointmentModel.Status.BOOKED);
		appModel.setPatient(user);
		appModel.setSchedule(sched);
		java.util.Date date = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime()); 
		appModel.setDate(sqlDate);
		
		int query = appDao.insert(appModel);

		if(query == 0) {
			message = "Unable to book appointment for " + appModel.getDoctor().getName();
			 return;
		}
		
		ScheduleDao schedDao = new ScheduleDao("SCHEDULES");
		sched.setStatus(ScheduleModel.Status.RESERVED);
		int result = schedDao.update(sched);
		
		if(result == 0) {
			message = "Error on updating schedule status";
			return;
		}
		
		status = true;
		message = "You have successfully booked an appoinment!";
		
		
	
	}

}
