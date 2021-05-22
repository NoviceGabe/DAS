package com.das.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.das.helper.AppointmentColumn;
import com.das.helper.ScheduleColumn;
import com.das.helper.UploadData;
import com.das.helper.UserColumn;
import com.das.model.ScheduleModel;
import com.das.model.UserModel;

public class ScheduleDao extends Dao<ScheduleColumn, ScheduleModel>{
	
	public ScheduleDao(String table) {
		super(table);
	}

	@Override
	public int insert(ScheduleModel schedule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<ScheduleModel> get(ArrayList<UploadData<ScheduleColumn, ?>> set) {
		ArrayList<ScheduleModel> scheduleModels = new ArrayList<ScheduleModel>();
		
		String sql = "SELECT u.*, s.*, s.id AS sid FROM USERS u "
				+ "JOIN " + table + " s "
				+ "ON u.id = s.doctor_id "
				+ "WHERE ";
		 
		PreparedStatement statement;
		
		try {
			if(set == null) {
				throw new Exception("Where clause is null");
			}
			
			int count = 0;
			
			for(UploadData<ScheduleColumn, ?> data : set) {
				sql+= data.getColumnName()+"=?";
	            if(count < set.size()-1){
	            	sql+=" AND ";
	            }
	            count++;
			}
			System.out.println(sql);
			statement = db.getCon().prepareStatement(sql);
			count = 1;
			for(UploadData<ScheduleColumn, ?> data : set) {
				statement.setObject(count, data.getValue());
				count++;
			}
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				
				int id = Integer.parseInt(result.getString("id"));
				String name = result.getString("name");
				int age = 0;
				if(result.getString("age") != null) {
					age = Integer.parseInt(result.getString("age"));
				}
				String email = result.getString("email");
				String password = result.getString("password");
				String address = result.getString("address");
				String contact = result.getString("contact_no");
				UserModel.Role role = UserModel.Role.valueOf(result.getString("role").toUpperCase());
				String descp = result.getString("description");
				Date date = result.getDate("joined");
				UserModel doctor = new UserModel(id, name, age, email, password, address, contact, role, descp, date);
				
				int sid = Integer.parseInt(result.getString("sid")); 
				ScheduleModel.Day day = ScheduleModel.Day.valueOf(result.getString("day").toUpperCase());
				String start_time = result.getString("start_time");
				String end_time = result.getString("end_time");
				ScheduleModel.Status status = ScheduleModel.Status.valueOf(result.getString("status").toUpperCase());
				Date date1 = result.getDate("date_created");
				
				scheduleModels.add(new ScheduleModel(sid, day, start_time, end_time, doctor, status, date1));
			 }
			result.close();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return scheduleModels;
	}

	@Override
	public ArrayList<ScheduleModel> get() {
		ArrayList<ScheduleModel> scheduleModels = new ArrayList<ScheduleModel>();
				
		String sql = "SELECT u.*, s.*, s.id AS sid FROM USERS u "
				+ "JOIN " + table + " s "
				+ "ON u.id = s.doctor_id";
		 
		Statement statement;
		try {
			statement = db.getCon().createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			while(result.next()) {
				
				int id = Integer.parseInt(result.getString("id"));
				String name = result.getString("name");
				int age = 0;
				if(result.getString("age") != null) {
					age = Integer.parseInt(result.getString("age"));
				}
				String email = result.getString("email");
				String password = result.getString("password");
				String address = result.getString("address");
				String contact = result.getString("contact_no");
				UserModel.Role role = UserModel.Role.valueOf(result.getString("role").toUpperCase());
				String descp = result.getString("description");
				Date date = result.getDate("joined");
				UserModel doctor = new UserModel(id, name, age, email, password, address, contact, role, descp, date);
				
				int sid = Integer.parseInt(result.getString("sid")); 
				ScheduleModel.Day day = ScheduleModel.Day.valueOf(result.getString("day").toUpperCase());
				String start_time = result.getString("start_time");
				String end_time = result.getString("end_time");
				ScheduleModel.Status status = ScheduleModel.Status.valueOf(result.getString("status").toUpperCase());
				Date date1 = result.getDate("date_created");
				
				scheduleModels.add(new ScheduleModel(sid, day, start_time, end_time, doctor, status, date1));
			 }
			result.close();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return scheduleModels;
	}

	@Override
	public int update(ScheduleModel model) {
		String sql = "UPDATE "+table+" SET ";
		PreparedStatement statement;
		int rowsUpdated = 0;
		
		try {
			
			if(model == null) {
				throw new Exception("Data model is null");
			}
						
			ArrayList<UploadData<ScheduleColumn, ?>> data = new ArrayList<UploadData<ScheduleColumn, ?>>();
			
			data.add(new UploadData<ScheduleColumn, String>(new ScheduleColumn().day(), model.getDay().toString().toUpperCase()));
			data.add(new UploadData<ScheduleColumn, String>(new ScheduleColumn().start_time(), model.getStart_time()));
			data.add(new UploadData<ScheduleColumn, String>(new ScheduleColumn().end_time(), model.getEnd_time()));
			data.add(new UploadData<ScheduleColumn, String>(new ScheduleColumn().status(), model.getStatus().toString().toUpperCase()));
		
			int count = 0;
			int size = data.size();
			
			for(UploadData<ScheduleColumn, ?> d : data) {
				sql+= d.getColumnName()+"=?";
	            if(count < size-1){
	            	sql+=", ";
	            }
	            count++;
			}
	        
			sql+=" WHERE id=?";
			
			statement = db.getCon().prepareStatement(sql);
		
			count = 1;
			for(UploadData<ScheduleColumn, ?> d : data) {
				statement.setObject(count, d.getValue());
				count++;
			}
			
			statement.setString(count, Integer.toString(model.getId()));
			
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
	public int update(ArrayList<UploadData<ScheduleColumn, ?>> set, int id) {
		String sql ="UPDATE "+table+" SET ";
		PreparedStatement statement;
		int rowsUpdated = 0;
		
		try {
			
			if(set == null) {
				throw new Exception("Data is null");
			}
			
			int count = 0;
			
			for(UploadData<ScheduleColumn, ?> data : set) {
				sql+= data.getColumnName()+"=?";
	            if(count < set.size()-1){
	            	sql+=", ";
	            }
	            count++;
			}
	        
			sql+=" WHERE id=?";
			
			statement = db.getCon().prepareStatement(sql);
			count = 1;
			for(UploadData<ScheduleColumn, ?> data : set) {
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

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(ArrayList<UploadData<ScheduleColumn, ?>> set) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<ScheduleModel> get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
