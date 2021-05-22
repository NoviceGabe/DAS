package com.das.model;

import java.sql.Date;

public class AppointmentModel extends Model{
	private int id;
	private Status status;
	private String presc;
	private UserModel patient;
	private ScheduleModel schedule;
	private Date date;
	
	public AppointmentModel() {}
	
	public AppointmentModel(int id, Status status, String presc, UserModel patient, 
			ScheduleModel schedule, Date date) {
		super();
		this.id = id;
		this.status = status;
		this.presc = presc;
		this.patient = patient;
		this.schedule = schedule;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPresc() {
		return presc;
	}

	public void setPresc(String presc) {
		this.presc = presc;
	}

	public UserModel getPatient() {
		return patient;
	}

	public void setPatient(UserModel patient) {
		this.patient = patient;
	}

	public UserModel getDoctor() {
		return schedule.getDoctor();
	}

	public void setDoctor(UserModel doctor) {
		schedule.setDoctor(doctor);
	}

	public ScheduleModel getSchedule() {
		return schedule;
	}

	public void setSchedule(ScheduleModel schedule) {
		this.schedule = schedule;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public enum Status {
        BOOKED("booked", 0),
        CANCELLED("cancelled", 1),
		COMPLETED("completed", 2);


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
		return this.getId() + "\n" +
				this.getStatus().toString() + "\n" +
				this.getPresc() + "\n\n" +
				this.getPatient().toString() + "\n" +
				this.getSchedule().getDoctor().toString() + "\n" + 
				this.getSchedule().toString();
	}
}
