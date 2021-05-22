package com.das.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.das.helper.UploadData;
import com.das.helper.UserColumn;
import com.das.model.UserModel;

public class UserDao extends Dao<UserColumn, UserModel>{

	public UserDao(String table) {
		super(table);
	}

	@Override
	public int insert(UserModel model) {
		 
		String sql = "INSERT INTO "+table;
		PreparedStatement statement;
		int rowsInserted = 0;
		
		try {
			if(model == null) {
				throw new Exception("Data model is null");
			}
			
			ArrayList<UploadData<UserColumn, ?>> data = new ArrayList<UploadData<UserColumn, ?>>();
			
			data.add(new UploadData<UserColumn, String>(new UserColumn().name(), model.getName()));
			data.add(new UploadData<UserColumn, String>(new UserColumn().email(), model.getEmail()));
			data.add(new UploadData<UserColumn, String>(new UserColumn().password(), model.getPassword()));
			data.add(new UploadData<UserColumn, String>(new UserColumn().address(), model.getAddress()));
			data.add(new UploadData<UserColumn, String>(new UserColumn().contact(), model.getContact()));
			data.add(new UploadData<UserColumn, String>(new UserColumn().role(), model.getRole().toString().toUpperCase()));
			data.add(new UploadData<UserColumn, String>(new UserColumn().description(), model.getDescp()));
			data.add(new UploadData<UserColumn, java.sql.Date>(new UserColumn().joined(), model.getDate()));
			
			int count = 0;
			int size = data.size();
			sql += " (";
			String values = "VALUES (";
			for(UploadData<UserColumn, ?> d : data) {
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
			
			System.out.println(sql);
			statement = db.getCon().prepareStatement(sql);
			
			count = 1;
			for(UploadData<UserColumn, ?> d : data) {
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
	public ArrayList<UserModel> get() {
		ArrayList<UserModel> userModels = new ArrayList<UserModel>();
		
		String sql = "SELECT * FROM " + table;
		 
		Statement statement;
		try {
			statement = db.getCon().createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int id = Integer.parseInt(result.getString("id"));
				String name = result.getString("name");
				int age = Integer.parseInt(result.getString("age"));
				String email = result.getString("email");
				String address = result.getString("address");
				String contact = result.getString("contact_no");
				UserModel.Role role = UserModel.Role.valueOf(result.getString("role").toUpperCase());
				String descp = result.getString("description");
				Date date = result.getDate("joined");
				userModels.add(new UserModel(id, name, age, email, null, address, contact, role, descp, date));
			 }
			result.close();
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return userModels;
	}
	
	@Override
	public ArrayList<UserModel> get(ArrayList<UploadData<UserColumn, ?>> set) {
		ArrayList<UserModel> userModels = new ArrayList<UserModel>();
		
		String sql = "SELECT * FROM " + table + " WHERE ";
		 
		PreparedStatement statement;
		
		try {
			if(set == null) {
				throw new Exception("Where clause is null");
			}
			
			int count = 0;
			
			for(UploadData<UserColumn, ?> data : set) {
				sql+= data.getColumnName()+"=?";
	            if(count < set.size()-1){
	            	sql+=" AND ";
	            }
	            count++;
			}
			
			statement = db.getCon().prepareStatement(sql);
			count = 1;
			for(UploadData<UserColumn, ?> data : set) {
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
				String address = result.getString("address");
				String contact = result.getString("contact_no");
				UserModel.Role role = UserModel.Role.valueOf(result.getString("role").toUpperCase());
				String descp = result.getString("description");
				Date date = result.getDate("joined");
				userModels.add(new UserModel(id, name, age, email, null, address, contact, role, descp, date));
				
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
		
		return userModels;
	}

	@Override
	public int update(UserModel model) {
		String sql = "UPDATE "+table+" SET ";
		PreparedStatement statement;
		int rowsUpdated = 0;
		
		try {
			
			if(model == null) {
				throw new Exception("Data model is null");
			}
						
			ArrayList<UploadData<UserColumn, ?>> data = new ArrayList<UploadData<UserColumn, ?>>();
			
			if(model.getName() != null) {
				data.add(new UploadData<UserColumn, String>(new UserColumn().name(), model.getName()));
			}
			
			if(model.getAge() > 0) {
				data.add(new UploadData<UserColumn, Integer>(new UserColumn().age(), model.getAge()));
			}
			
			if(model.getEmail() != null) {
				data.add(new UploadData<UserColumn, String>(new UserColumn().email(), model.getEmail()));
			}
			
			if(model.getAddress() != null) {
				data.add(new UploadData<UserColumn, String>(new UserColumn().address(), model.getAddress()));
			}
			
			if(model.getContact() != null) {
				data.add(new UploadData<UserColumn, String>(new UserColumn().contact(), model.getContact()));
			}
			
			if(model.getDescp() != null) {
				data.add(new UploadData<UserColumn, String>(new UserColumn().description(), model.getDescp()));
			}
		
			int count = 0;
			int size = data.size();
			
			if(size == 0) {
				return 0;
			}
			
			for(UploadData<UserColumn, ?> d : data) {
				sql+= d.getColumnName()+"=?";
	            if(count < size-1){
	            	sql+=", ";
	            }
	            count++;
			}
	        
			sql+=" WHERE id=?";
			System.out.println(sql);
			statement = db.getCon().prepareStatement(sql);
		
			count = 1;
			for(UploadData<UserColumn, ?> d : data) {
				System.out.println(d.getColumnName() + ":" +d.getValue());
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
	public int update(ArrayList<UploadData<UserColumn, ?>> set, int id) {
		
		String sql ="UPDATE "+table+" SET ";
		PreparedStatement statement;
		int rowsUpdated = 0;
		
		try {
			
			if(set == null) {
				throw new Exception("Data is null");
			}
			
			int count = 0;
			
			for(UploadData<UserColumn, ?> data : set) {
				sql+= data.getColumnName()+"=?";
	            if(count < set.size()-1){
	            	sql+=", ";
	            }
	            count++;
			}
	        
			sql+=" WHERE id=?";
			
			statement = db.getCon().prepareStatement(sql);
			count = 1;
			for(UploadData<UserColumn, ?> data : set) {
				System.out.println(data.getColumnName() + " " + data.getValue());
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
		String sql = "SELECT COUNT(*) AS rowcount FROM "+table;
		
		Statement statement;
		int rowCount = 0;
		try {
			statement = db.getCon().createStatement();
			ResultSet result = statement.executeQuery(sql);
			result.next(); 
			rowCount = result.getInt("rowcount");  
			result.close(); 
			
		} catch (SQLException e) {
			db.setError(true);
			db.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return rowCount;
	}
	
	public Connection getCon() {
		return db.getCon();
	}
	
	public String getMessage() {
		return db.getMessage();
	}
	
	public boolean isError() {
		return db.isError();
	}
	
	public String toString() {
		return db.toString();
	}

	@Override
	public int delete(ArrayList<UploadData<UserColumn, ?>> set) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<UserModel> get(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
