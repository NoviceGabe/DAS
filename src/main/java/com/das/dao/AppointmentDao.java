package com.das.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.das.helper.AppointmentColumn;
import com.das.helper.ScheduleColumn;
import com.das.helper.UploadData;
import com.das.helper.UserColumn;
import com.das.model.AppointmentModel;
import com.das.model.ScheduleModel;
import com.das.model.UserModel;

public class AppointmentDao extends Dao<AppointmentColumn, AppointmentModel>{

	public AppointmentDao(String table) {
		super(table);
	}

	@Override
	public int insert(AppointmentModel model) {
		 
		String sql = "INSERT INTO "+table;
		PreparedStatement statement;
		int rowsInserted = 0;
		
		try {
			if(model == null) {
				throw new Exception("Data model is null");
			}
			
			ArrayList<UploadData<AppointmentColumn, ?>> data = new ArrayList<UploadData<AppointmentColumn, ?>>();
			
			data.add(new UploadData<AppointmentColumn, String>(new AppointmentColumn().status(), model.getStatus().toString().toUpperCase()));
			data.add(new UploadData<AppointmentColumn, String>(new AppointmentColumn().prescription(), model.getPresc()));
			data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().patient_id(), model.getPatient().getId()));
			data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().doctor_id(), model.getDoctor().getId()));
			data.add(new UploadData<AppointmentColumn, Integer>(new AppointmentColumn().sched_id(), model.getSchedule().getId()));
			data.add(new UploadData<AppointmentColumn, java.sql.Date>(new AppointmentColumn().date_created(), model.getDate()));
			
			int count = 0;
			int size = data.size();
			sql += " (";
			String values = "VALUES (";
			for(UploadData<AppointmentColumn, ?> d : data) {
				sql+= d.getColumnName();
				values += "?";
	            if(count < size-1){
	            	sql+= ", ";
	            	values += ", ";
	            }
	            count++;
			}
			sql += ") ";
			values += ") ";
			sql += values;
			
			//System.out.println(sql);
			statement = db.getCon().prepareStatement(sql);
			
			count = 1;
			for(UploadData<AppointmentColumn, ?> d : data) {
				statement.setObject(count, d.getValue());
				count++;
			}
			
			rowsInserted = statement.executeUpdate();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} 
		return rowsInserted;
	}

	@Override
	public ArrayList<AppointmentModel> get(ArrayList<UploadData<AppointmentColumn, ?>> set) {
		ArrayList<AppointmentModel> appModels = new ArrayList<AppointmentModel>();
		
		String sql = 
				"SELECT "+
				"a.id AS app_id, a.status AS app_status, a.prescription, a.date_created AS app_date," +
				"s.id AS sched_id, s.status AS sched_status, s.day, s.start_time, s.end_time, s.date_created AS sched_date, " +
				"p.id AS patient_id, p.name AS patient_name, p.age AS patient_age, p.email AS patient_email, p.password AS patient_password, p.address AS patient_address, p.contact_no AS patient_contact, p.role AS patient_role, p.description AS patient_descp, p.joined AS patient_date, "+
				"d.id AS doc_id, d.name AS doc_name, d.age AS doc_age, d.email AS doc_email, d.password AS doc_password, d.address AS doc_address, d.contact_no AS doc_contact, d.role AS doc_role,d.description AS doc_descp, d.joined AS doc_date "+
				"FROM "+table+ " a " +
				"JOIN SCHEDULES s ON " +
				"a.doctor_id = s.doctor_id " +
				"JOIN USERS d ON " +
				"a.doctor_id = d.id "+
				"JOIN USERS p ON " +
				"a.patient_id = p.id " + 
				"WHERE ";
		 
		PreparedStatement statement;
		
		try {
			if(set == null) {
				throw new Exception("Where clause is null");
			}
			
			int count = 0;
			
			for(UploadData<AppointmentColumn, ?> data : set) {
				sql+= data.getColumnName()+"=?";
	            if(count < set.size()-1){
	            	sql+=" AND ";
	            }
	            count++;
			}
			System.out.println(sql);
			statement = db.getCon().prepareStatement(sql);
			count = 1;
			for(UploadData<AppointmentColumn, ?> data : set) {
				System.out.println(data.getColumnName() + " " + data.getValue());
				statement.setObject(count, data.getValue());
				count++;
			}
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				
				ScheduleModel schedModel = new ScheduleModel();
				int schedId = Integer.parseInt(result.getString("sched_id"));
				ScheduleModel.Day day = ScheduleModel.Day.valueOf(result.getString("day"));
				String start_time = result.getString("start_time");
				String end_time = result.getString("end_time");
				Date schedDate = result.getDate("sched_date");
				
				schedModel.setId(schedId);
				schedModel.setDay(day);
				schedModel.setStart_time(start_time);
				schedModel.setEnd_time(end_time);
				schedModel.setDate(schedDate);
				
				UserModel patient = new UserModel();
				int patientId = Integer.parseInt(result.getString("patient_id"));
				String patientName = result.getString("patient_name");
				int patientAge = 0;
				if(result.getString("patient_age") != null) {
					patientAge = Integer.parseInt(result.getString("patient_age"));
				}
				String patientEmail = result.getString("patient_email");
				String patientPassword = result.getString("patient_password");
				String patientAddress = result.getString("patient_address");
				String patientContact = result.getString("patient_contact");
				UserModel.Role patientRole = UserModel.Role.valueOf(result.getString("patient_role"));
				String patientDescp = result.getString("patient_descp");
				Date patientDate = result.getDate("patient_date");
				
				patient.setId(patientId);
				patient.setName(patientName);
				patient.setAge(patientAge);
				patient.setEmail(patientEmail);
				patient.setPassword(patientPassword);
				patient.setAddress(patientAddress);
				patient.setContact(patientContact);
				patient.setRole(patientRole);
				patient.setDescp(patientDescp);
				patient.setDate(patientDate);
				
				UserModel doc = new UserModel();
				int docId = Integer.parseInt(result.getString("doc_id"));
				String docName = result.getString("doc_name");
				int docAge = 0;
				if(result.getString("doc_age") != null) {
					docAge = Integer.parseInt(result.getString("doc_age"));
				}
				String docEmail = result.getString("doc_email");
				String docPassword = result.getString("doc_password");
				String docAddress = result.getString("doc_address");
				String docContact = result.getString("doc_contact");
				UserModel.Role docRole = UserModel.Role.valueOf(result.getString("doc_role"));
				String docDescp = result.getString("doc_descp");
				Date docDate = result.getDate("doc_date");
				
				doc.setId(docId);
				doc.setName(docName);
				doc.setAge(docAge);
				doc.setEmail(docEmail);
				doc.setPassword(docPassword);
				doc.setAddress(docAddress);
				doc.setContact(docContact);
				doc.setRole(docRole);
				doc.setDescp(docDescp);
				doc.setDate(docDate);
				
				schedModel.setDoctor(doc);
				
				AppointmentModel appModel = new AppointmentModel();
				int appId = Integer.parseInt(result.getString("app_id"));
				AppointmentModel.Status status = AppointmentModel.Status.valueOf(result.getString("app_status"));
				String presc = result.getString("prescription");
				Date appDate = result.getDate("app_date");
				
				appModel.setId(appId);
				appModel.setStatus(status);
				appModel.setPresc(presc);
				appModel.setPatient(patient);
				appModel.setSchedule(schedModel);
				appModel.setDate(appDate);
				
				appModels.add(appModel);
			 }
			
			result.close();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} 
		
		return appModels;
	}

	@Override
	public ArrayList<AppointmentModel> get() {
		ArrayList<AppointmentModel> appModels = new ArrayList<AppointmentModel>();
		
		String sql = 
				"SELECT "+
				"a.id AS app_id, a.status AS app_status, a.prescription, a.date_created AS app_date," +
				"s.id AS sched_id, s.status AS sched_status, s.day, s.start_time, s.end_time, s.date_created AS sched_date, " +
				"p.id AS patient_id, p.name AS patient_name, p.age AS patient_age, p.email AS patient_email, p.password AS patient_password, p.address AS patient_address, p.contact_no AS patient_contact, p.role AS patient_role, p.description AS patient_descp, p.joined AS patient_date, "+
				"d.id AS doc_id, d.name AS doc_name, d.age AS doc_age, d.email AS doc_email, d.password AS doc_password, d.address AS doc_address, d.contact_no AS doc_contact, d.role AS doc_role,d.description AS doc_descp, d.joined AS doc_date "+
				"FROM "+table+ " a " +
				"JOIN SCHEDULES s ON " +
				"a.doctor_id = s.doctor_id " +
				"JOIN USERS d ON " +
				"a.doctor_id = d.id "+
				"JOIN USERS p ON " +
				"a.patient_id = p.id ";
				
		 
		Statement statement;

		try {
			statement = db.getCon().createStatement();
			ResultSet result = statement.executeQuery(sql);
			//printCols(result);
			while(result.next()) {
				
				ScheduleModel schedModel = new ScheduleModel();
				int schedId = Integer.parseInt(result.getString("sched_id"));
				ScheduleModel.Day day = ScheduleModel.Day.valueOf(result.getString("day"));
				String start_time = result.getString("start_time");
				String end_time = result.getString("end_time");
				Date schedDate = result.getDate("sched_date");
				
				schedModel.setId(schedId);
				schedModel.setDay(day);
				schedModel.setStart_time(start_time);
				schedModel.setEnd_time(end_time);
				schedModel.setDate(schedDate);
				
				UserModel patient = new UserModel();
				int patientId = Integer.parseInt(result.getString("patient_id"));
				String patientName = result.getString("patient_name");
				int patientAge = 0;
				if(result.getString("patient_age") != null) {
					patientAge = Integer.parseInt(result.getString("patient_age"));
				}
				String patientEmail = result.getString("patient_email");
				String patientPassword = result.getString("patient_password");
				String patientAddress = result.getString("patient_address");
				String patientContact = result.getString("patient_contact");
				UserModel.Role patientRole = UserModel.Role.valueOf(result.getString("patient_role"));
				String patientDescp = result.getString("patient_descp");
				Date patientDate = result.getDate("patient_date");
				
				patient.setId(patientId);
				patient.setName(patientName);
				patient.setAge(patientAge);
				patient.setEmail(patientEmail);
				patient.setPassword(patientPassword);
				patient.setAddress(patientAddress);
				patient.setContact(patientContact);
				patient.setRole(patientRole);
				patient.setDescp(patientDescp);
				patient.setDate(patientDate);
				
				UserModel doc = new UserModel();
				int docId = Integer.parseInt(result.getString("doc_id"));
				String docName = result.getString("doc_name");
				int docAge = 0;
				if(result.getString("doc_age") != null) {
					docAge = Integer.parseInt(result.getString("doc_age"));
				}
				String docEmail = result.getString("doc_email");
				String docPassword = result.getString("doc_password");
				String docAddress = result.getString("doc_address");
				String docContact = result.getString("doc_contact");
				UserModel.Role docRole = UserModel.Role.valueOf(result.getString("doc_role"));
				String docDescp = result.getString("doc_descp");
				Date docDate = result.getDate("doc_date");
				
				doc.setId(docId);
				doc.setName(docName);
				doc.setAge(docAge);
				doc.setEmail(docEmail);
				doc.setPassword(docPassword);
				doc.setAddress(docAddress);
				doc.setContact(docContact);
				doc.setRole(docRole);
				doc.setDescp(docDescp);
				doc.setDate(docDate);
				
				schedModel.setDoctor(doc);
				
				AppointmentModel appModel = new AppointmentModel();
				int appId = Integer.parseInt(result.getString("app_id"));
				AppointmentModel.Status status = AppointmentModel.Status.valueOf(result.getString("app_status"));
				String presc = result.getString("prescription");
				Date appDate = result.getDate("app_date");
				
				appModel.setId(appId);
				appModel.setStatus(status);
				appModel.setPresc(presc);
				appModel.setPatient(patient);
				appModel.setSchedule(schedModel);
				appModel.setDate(appDate);
				
				appModels.add(appModel);
			 }
			result.close();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return appModels;
	}

	@Override
	public int update(AppointmentModel obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(ArrayList<UploadData<AppointmentColumn, ?>> set, int id) {
		String sql ="UPDATE "+table+" SET ";
		PreparedStatement statement;
		int rowsUpdated = 0;
		
		try {
			
			if(set == null) {
				throw new Exception("Data is null");
			}
			
			int count = 0;
			
			for(UploadData<AppointmentColumn, ?> data : set) {
				sql+= data.getColumnName()+"=?";
	            if(count < set.size()-1){
	            	sql+=", ";
	            }
	            count++;
			}
	        
			sql+=" WHERE id=?";
			System.out.println(sql);
			statement = db.getCon().prepareStatement(sql);
			count = 1;
			for(UploadData<AppointmentColumn, ?> data : set) {
				statement.setObject(count, data.getValue());
				count++;
			}
			
			statement.setString(count, Integer.toString(id));
			
			rowsUpdated = statement.executeUpdate();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} 
		
		return rowsUpdated;
	}
	
	public int update(ArrayList<UploadData<AppointmentColumn, ?>> set, ArrayList<UploadData<AppointmentColumn, ?>> where) {
		String sql ="UPDATE "+table+" SET ";
		PreparedStatement statement;
		int rowsUpdated = 0;
		
		try {
			
			if(set == null) {
				throw new Exception("Data is null");
			}
			
			int count = 0;
			// data
			for(UploadData<AppointmentColumn, ?> data : set) {
				sql+= data.getColumnName()+"=?";
	            if(count < set.size()-1){
	            	sql+=", ";
	            }
	            count++;
			}
	        
			sql+=" WHERE ";
			// condition
			count = 0;
			for(UploadData<AppointmentColumn, ?> data : where) {
				sql+= data.getColumnName()+"=?";
				System.out.println("count : " + count);
	            if(count < where.size()-1){
	            	sql+=" AND ";
	            }
	            count++;
			}
			System.out.println(sql);
			
			statement = db.getCon().prepareStatement(sql);
			count = 1;
			for(UploadData<AppointmentColumn, ?> data : set) {
				System.out.println("value  : " + data.getValue());
				statement.setObject(count, data.getValue());
				count++;
			}
			for(UploadData<AppointmentColumn, ?> data : where) {
				System.out.println("value  : " + data.getValue());
				statement.setObject(count, data.getValue());
				count++;
			}
			
			
			rowsUpdated = statement.executeUpdate();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} 
		
		return rowsUpdated;
	}
	

	@Override
	public int delete(int id) {
		String sql = "DELETE FROM "+table+" WHERE id=?";
		
		PreparedStatement statement;
		int rowsUpdated = 0;
		
		try {
			statement = db.getCon().prepareStatement(sql);
			statement.setString(1, Integer.toString(id));
			
			rowsUpdated = statement.executeUpdate();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return rowsUpdated;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void printCols(ResultSet result) {
		try {
			ResultSetMetaData rsMetaData = result.getMetaData();
			int count = rsMetaData.getColumnCount();
			for(int i = 1; i<=count; i++) {
				System.out.println(rsMetaData.getColumnName(i));
		    }	
		}catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public int delete(ArrayList<UploadData<AppointmentColumn, ?>> set) {
		String sql = "DELETE FROM "+table+" WHERE ";
		
		PreparedStatement statement;
		int rowsUpdated = 0;
		
		try {
			if(set == null) {
				throw new Exception("Where clause is null");
			}
			
			int count = 0;
			
			for(UploadData<AppointmentColumn, ?> data : set) {
				sql+= data.getColumnName()+"=?";
	            if(count < set.size()-1){
	            	sql+=" AND ";
	            }
	            count++;
			}
			System.out.println(sql);
			statement = db.getCon().prepareStatement(sql);
			count = 1;
			for(UploadData<AppointmentColumn, ?> data : set) {
				System.out.println(data.getColumnName() + " " + data.getValue());
				statement.setObject(count, data.getValue());
				count++;
			}
			
			rowsUpdated = statement.executeUpdate();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return rowsUpdated;
	}

	@Override
	public ArrayList<AppointmentModel> get(int id) {
		ArrayList<AppointmentModel> appModels = new ArrayList<AppointmentModel>();
		
		String sql = 
				"SELECT "+
				"a.id AS app_id, a.status AS app_status, a.prescription, a.date_created AS app_date," +
				"s.id AS sched_id, s.status AS sched_status, s.day, s.start_time, s.end_time, s.date_created AS sched_date, " +
				"p.id AS patient_id, p.name AS patient_name, p.age AS patient_age, p.email AS patient_email, p.password AS patient_password, p.address AS patient_address, p.contact_no AS patient_contact, p.role AS patient_role, p.description AS patient_descp, p.joined AS patient_date, "+
				"d.id AS doc_id, d.name AS doc_name, d.age AS doc_age, d.email AS doc_email, d.password AS doc_password, d.address AS doc_address, d.contact_no AS doc_contact, d.role AS doc_role,d.description AS doc_descp, d.joined AS doc_date "+
				"FROM "+table+ " a " +
				"JOIN SCHEDULES s ON " +
				"a.doctor_id = s.doctor_id " +
				"JOIN USERS d ON " +
				"a.doctor_id = d.id "+
				"JOIN USERS p ON " +
				"a.patient_id = p.id " + 
				"WHERE d.id = ?";
		 
		PreparedStatement statement;
		
		try {
		
			statement = db.getCon().prepareStatement(sql);
			System.out.println(sql);
			statement.setString(1, Integer.toString(id));
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				
				ScheduleModel schedModel = new ScheduleModel();
				int schedId = Integer.parseInt(result.getString("sched_id"));
				ScheduleModel.Day day = ScheduleModel.Day.valueOf(result.getString("day"));
				String start_time = result.getString("start_time");
				String end_time = result.getString("end_time");
				Date schedDate = result.getDate("sched_date");
				
				schedModel.setId(schedId);
				schedModel.setDay(day);
				schedModel.setStart_time(start_time);
				schedModel.setEnd_time(end_time);
				schedModel.setDate(schedDate);
				
				UserModel patient = new UserModel();
				int patientId = Integer.parseInt(result.getString("patient_id"));
				String patientName = result.getString("patient_name");
				int patientAge = 0;
				if(result.getString("patient_age") != null) {
					patientAge = Integer.parseInt(result.getString("patient_age"));
				}
				String patientEmail = result.getString("patient_email");
				String patientPassword = result.getString("patient_password");
				String patientAddress = result.getString("patient_address");
				String patientContact = result.getString("patient_contact");
				UserModel.Role patientRole = UserModel.Role.valueOf(result.getString("patient_role"));
				String patientDescp = result.getString("patient_descp");
				Date patientDate = result.getDate("patient_date");
				
				patient.setId(patientId);
				patient.setName(patientName);
				patient.setAge(patientAge);
				patient.setEmail(patientEmail);
				patient.setPassword(patientPassword);
				patient.setAddress(patientAddress);
				patient.setContact(patientContact);
				patient.setRole(patientRole);
				patient.setDescp(patientDescp);
				patient.setDate(patientDate);
				
				UserModel doc = new UserModel();
				int docId = Integer.parseInt(result.getString("doc_id"));
				String docName = result.getString("doc_name");
				int docAge = 0;
				if(result.getString("doc_age") != null) {
					docAge = Integer.parseInt(result.getString("doc_age"));
				}
				String docEmail = result.getString("doc_email");
				String docPassword = result.getString("doc_password");
				String docAddress = result.getString("doc_address");
				String docContact = result.getString("doc_contact");
				UserModel.Role docRole = UserModel.Role.valueOf(result.getString("doc_role"));
				String docDescp = result.getString("doc_descp");
				Date docDate = result.getDate("doc_date");
				
				doc.setId(docId);
				doc.setName(docName);
				doc.setAge(docAge);
				doc.setEmail(docEmail);
				doc.setPassword(docPassword);
				doc.setAddress(docAddress);
				doc.setContact(docContact);
				doc.setRole(docRole);
				doc.setDescp(docDescp);
				doc.setDate(docDate);
				
				schedModel.setDoctor(doc);
				
				AppointmentModel appModel = new AppointmentModel();
				int appId = Integer.parseInt(result.getString("app_id"));
				AppointmentModel.Status status = AppointmentModel.Status.valueOf(result.getString("app_status"));
				String presc = result.getString("prescription");
				Date appDate = result.getDate("app_date");
				
				appModel.setId(appId);
				appModel.setStatus(status);
				appModel.setPresc(presc);
				appModel.setPatient(patient);
				appModel.setSchedule(schedModel);
				appModel.setDate(appDate);
				
				appModels.add(appModel);
			 }
			
			result.close();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		} 
		
		return appModels;
	
	}

}
