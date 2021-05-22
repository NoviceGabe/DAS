package com.das.model;

import java.sql.Date;

public class ScheduleModel extends Model{
	private int id;
	private Day day;
	private String start_time;
	private String end_time;
	private UserModel user;
	private Status status;
	private Date date;
	
	
	public ScheduleModel() {}
	
	public ScheduleModel(int id, Day day, String start_time, String end_time, UserModel user, Status status, Date date) {
		super();
		this.id = id;
		this.day = day;
		this.start_time = start_time;
		this.end_time = end_time;
		this.user = user;
		this.status = status;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public UserModel getDoctor() {
		return user;
	}

	public void setDoctor(UserModel user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public enum Day {
        SUNDAY("sunday", 0),
        MONDAY("monday", 1),
		TUESDAY("tuesday", 2),
		WEDNESDAY("wednesday", 3),
		THURSDAY("thursday", 4),
		FRIDAY("friday", 5),
		SATURDAY("saturday", 6);


        private String stringValue;
        private int intValue;
        
        private Day(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public int toInt(){return intValue;}
    }
	
	public enum Status {
        ACTIVE("active", 0),
        RESERVED("reserved", 1);

        private String stringValue;
        private int intValue;
        
        private Status(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public int toInt(){return intValue;}
    }
	
	public String toString() {
		return  this.getId() + "\n" +
				this.getDay() + "\n" + 
				this.getStart_time() + "\n" +
				this.getEnd_time() + "\n" +
				this.getDoctor().getId() + "\n" +
				this.getDate();
	}
	
}
