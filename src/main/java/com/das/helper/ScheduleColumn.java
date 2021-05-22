package com.das.helper;

public class ScheduleColumn extends Column{
	
	 public ScheduleColumn id(){
	        column = "id";
	        return this;
	 }
	 
	 public ScheduleColumn id(String name){
	        column = name;
	        return this;
	 }
	 
	 public ScheduleColumn day(){
	        column = "day";
	        return this;
	 }
	 
	 public ScheduleColumn start_time(){
	        column = "start_time";
	        return this;
	 }
	 
	 public ScheduleColumn end_time(){
	        column = "end_time";
	        return this;
	 }
	 
	 public ScheduleColumn doctor_id(){
	        column = "doctor_id";
	        return this;
	 }
	 
	 public ScheduleColumn status(){
	        column = "status";
	        return this;
	 } 
	 
	 public ScheduleColumn date_created(){
	        column = "date_created";
	        return this;
	 }
}
