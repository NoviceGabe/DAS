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
import com.das.helper.AppointmentColumn;
import com.das.helper.ScheduleColumn;
import com.das.helper.UploadData;
import com.das.model.AppointmentModel;
import com.das.model.ScheduleModel;
import com.das.model.UserModel;

/**
 * Servlet implementation class AppointmentController
 */
@WebServlet("/AppointmentController")
public class AppointmentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean status = false;
	private String message = "";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppointmentController() {
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
			int appoId = Integer.parseInt(request.getParameter("appoId"));
			int schedId = Integer.parseInt(request.getParameter("schedId"));
			int docId = Integer.parseInt(request.getParameter("docId"));
			int userId = 0, patientId = 0;
			if(request.getParameter("userId") != null) {
				userId = Integer.parseInt(request.getParameter("userId"));
			}
			if(request.getParameter("patientId") != null) {
				patientId = Integer.parseInt(request.getParameter("patientId"));
			}
			String role = request.getParameter("role");
			String action = request.getParameter("action");
			
			if(role.equals(UserModel.Role.PATIENT.toString().toUpperCase())) {
				if(action.equals("cancel")) {
					cancelAppointment(appoId, schedId, docId, userId);
				}
			}else if(role.equals(UserModel.Role.DOCTOR.toString().toUpperCase())) {
				if(action.equals("complete")) {
					finishAppointment(appoId, schedId, docId, patientId);
				}else if(action.equals("cancel")) {
					cancelAppointment(appoId, schedId, docId);
				}else if(action.equals("add-prescription")) {
					
				}else if(action.equals("remove")) {
					removeAppointment(appoId, schedId, docId, patientId);
				}
			}
			
		
			
		}catch(Exception e) {
			e.printStackTrace();
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
	
	private void cancelAppointment(int appoId, int schedId, int docId, int userId) {
		status = false;
		ArrayList<UploadData<AppointmentColumn, ?>> data = new ArrayList<UploadData<AppointmentColumn, ?>>();
		data.add(new UploadData<AppointmentColumn, String>(new AppointmentColumn().status(), AppointmentModel.Status.CANCELLED.toString().toUpperCase()));
	
		
		ArrayList<UploadData<AppointmentColumn, ?>> where = new ArrayList<UploadData<AppointmentColumn, ?>>();
		where.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().id(), appoId));
		where.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().sched_id(), schedId));
		where.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().doctor_id(), docId));
		where.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().patient_id(), userId));
		
		AppointmentDao appoDao = new AppointmentDao("APPOINTMENTS");
		int status = appoDao.update(data, where);
		
		if(status == 0) {
			message = "Unable to cancel appointment";
		}else {
			this.status = true;
			message = "You have cancelled an appoinment!";
			ScheduleDao schedDao = new ScheduleDao("SCHEDULES");
			
			ArrayList<UploadData<ScheduleColumn, ?>> sched = new ArrayList<UploadData<ScheduleColumn, ?>>();
			sched.add(new UploadData<ScheduleColumn, String>(new ScheduleColumn().status(), ScheduleModel.Status.ACTIVE.toString().toUpperCase()));
			int result = schedDao.update(sched, schedId);
			
			if(result == 0) {
				this.status = false;
				message  = "Unable to update schedule status";
			}
		}
		
		
	}
	
	private void cancelAppointment(int appoId, int schedId, int docId) {
		status = false;
		ArrayList<UploadData<AppointmentColumn, ?>> data = new ArrayList<UploadData<AppointmentColumn, ?>>();
		data.add(new UploadData<AppointmentColumn, String>(new AppointmentColumn().status(), AppointmentModel.Status.CANCELLED.toString().toUpperCase()));
	
		
		ArrayList<UploadData<AppointmentColumn, ?>> where = new ArrayList<UploadData<AppointmentColumn, ?>>();
		where.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().id(), appoId));
		where.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().sched_id(), schedId));
		where.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().doctor_id(), docId));
		
		AppointmentDao appoDao = new AppointmentDao("APPOINTMENTS");
		int status = appoDao.update(data, where);
		
		if(status == 0) {
			message = "Unable to cancel appointment";
		}else {
			this.status = true;
			message = "You have cancelled an appoinment!";
			ScheduleDao schedDao = new ScheduleDao("SCHEDULES");
			
			ArrayList<UploadData<ScheduleColumn, ?>> sched = new ArrayList<UploadData<ScheduleColumn, ?>>();
			sched.add(new UploadData<ScheduleColumn, String>(new ScheduleColumn().status(), ScheduleModel.Status.ACTIVE.toString().toUpperCase()));
			int result = schedDao.update(sched, schedId);
			
			if(result == 0) {
				this.status = false;
				message  = "Unable to update schedule status";
			}
		}
		
	
	}
	
	private void finishAppointment(int appoId, int schedId, int docId, int userId) {
		status = false;
		
		ArrayList<UploadData<AppointmentColumn, ?>> data = new ArrayList<UploadData<AppointmentColumn, ?>>();
		data.add(new UploadData<AppointmentColumn, String>(new AppointmentColumn().status(),  AppointmentModel.Status.COMPLETED.toString().toUpperCase()));
		
		ArrayList<UploadData<AppointmentColumn, ?>> where = new ArrayList<UploadData<AppointmentColumn, ?>>();
		where.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().doctor_id(), docId));
		where.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().patient_id(), userId));
		
		AppointmentDao appoDao = new AppointmentDao("APPOINTMENTS");
		int status = appoDao.update(data, where);
		
		if(status == 0) {
			message = "Unable to finish appointment";
		}else {
			this.status = true;
			message = "You mark appoinment as completed!";
			ScheduleDao schedDao = new ScheduleDao("SCHEDULES");
			
			ArrayList<UploadData<ScheduleColumn, ?>> sched = new ArrayList<UploadData<ScheduleColumn, ?>>();
			sched.add(new UploadData<ScheduleColumn, String>(new ScheduleColumn().status(), ScheduleModel.Status.ACTIVE.toString().toUpperCase()));
			int result = schedDao.update(sched, schedId);
			
			if(result == 0) {
				this.status = false;
				message  = "Unable to update schedule status";
			}
		}
		
	}
	
	private void removeAppointment(int appoId, int schedId, int docId, int userId) {
		status = false;
		ArrayList<UploadData<AppointmentColumn, ?>> data = new ArrayList<UploadData<AppointmentColumn, ?>>();
		data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().id(), appoId));
		data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().doctor_id(), docId));
		data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().patient_id(), userId));
		data.add(new UploadData<AppointmentColumn, String>(new AppointmentColumn().status(), AppointmentModel.Status.COMPLETED.toString().toUpperCase()));
		
		AppointmentDao appoDao = new AppointmentDao("APPOINTMENTS");
		int status = appoDao.delete(data);
		
		if(status == 0) {
			message = "Unable to remove appointment";
		}else {
			this.status = true;
			message = "You have removed an appoinment!";
			ScheduleDao schedDao = new ScheduleDao("SCHEDULES");
			
			ArrayList<UploadData<ScheduleColumn, ?>> sched = new ArrayList<UploadData<ScheduleColumn, ?>>();
			sched.add(new UploadData<ScheduleColumn, String>(new ScheduleColumn().status(), ScheduleModel.Status.ACTIVE.toString().toUpperCase()));
			int result = schedDao.update(sched, schedId);
			
			if(result == 0) {
				this.status = false;
				message  = "Unable to update schedule status";
			}
		}
		
		
	}
}
