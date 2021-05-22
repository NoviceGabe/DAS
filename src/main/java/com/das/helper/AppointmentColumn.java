package com.das.helper;

public class AppointmentColumn extends Column{

    public AppointmentColumn id(){
        column = "id";
        return this;
    }
    public AppointmentColumn id(String name){
        column = name;
        return this;
    }
    public AppointmentColumn status(){
        column = "status";
        return this;
    }
    public AppointmentColumn prescription(){
        column = "prescription";
        return this;
        }
    public AppointmentColumn patient_id(){
        column = "patient_id";
        return this;
        }
    
   
    public AppointmentColumn patient_id(String name){
        column = name;
        return this;
        }
    public AppointmentColumn doctor_id(){
        column = "doctor_id";
        return this;
    }
    public AppointmentColumn doctor_id(String name){
        column = name;
        return this;
        }
    public AppointmentColumn sched_id(){
        column = "sched_id";
        return this;
    }
    public AppointmentColumn sched_id(String name){
        column = name;
        return this;
        }
    public AppointmentColumn date_created(){
        column = "date_created";
        return this;
    }
}
