package com.das.db;


import java.sql.Connection;
import java.sql.DriverManager;

import com.das.util.Provider;

public class Db implements Provider{
	private Connection con;
	private boolean error = false;
	private String message;
	
	public Db(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");  

			con = DriverManager.getConnection(connUrl, username, password);
			if(con == null) {
				throw new Exception("Unable to connect to Oracle Database");
			}
			message = "Connected to Oracle Database";
		} catch (Exception e) {
			error = true;
			message = e.getMessage();
		}
	}
	
	public Connection getCon() {
		return con;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		return "Error: " + isError() + 
				"\nMessage: " + getMessage();
	}
	
	
}

