package com.das.model;

import java.sql.Date;

public class UserModel extends Model{
	private int id;
	private String name;
	private int age;
	private String email;
	private String password;
	private String address;
	private String contact;
	private Role role;
	private String descp;
	private Date date;
	
	public UserModel() {}
	
	public UserModel(int id, String name, int age, String email, String password, String address, String contact, Role role,
			String descp, Date date) {
		super();
		this.id = id;
		this.age = age;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.contact = contact;
		this.role = role;
		this.descp = descp;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role roles) {
		this.role = roles;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String toString(){
		  return 
				  this.getId() + "\n" +
				  this.getName() + "\n" +
				  this.getRole() + "\n" +
				  this.getEmail() + "\n" +
				  this.getPassword() + "\n" +
				  this.getContact() + "\n" + 
				  this.getDescp() + "\n" +
				  this.getAddress() + "\n" +
				  this.getDate() + "\n";
		}



	public enum Role {
        PATIENT("patient", 0),
        DOCTOR("doctor", 1),
		ADMIN("admin", 2);


        private String stringValue;
        private int intValue;
        
        private Role(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public int toInt(){return intValue;}
    }
}
