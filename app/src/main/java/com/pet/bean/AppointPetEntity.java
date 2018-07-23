package com.pet.bean;

/**
 * Created by dragon on 2018/7/13.
 * 约宠信息实体
 */

public class AppointPetEntity {

    /**
     * user_name : haha
     * user_icon : /uploads/icon/20180607/c90094f9036d7d5b06d0a64eb0488748.png
     * pet_icon : /uploads/pet_icon/20180705/7ea3c2302ef70186e4d2e1db53889512.jpg
     * user_id : 51
     * appointment_id : 85
     * appointment_status : 2
     * number_of_people : 1
     * appointment_time : 2018-07-13 16:06:00
     * appointment_location : 高新南七道1号 粤美特大厦
     */

    private String user_name;
    private String user_icon;
    private String pet_icon;
    private int user_id;
    private int appointment_id;
    private int appointment_status;
    private int number_of_people;
    private String appointment_time;
    private String appointment_location;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getPet_icon() {
        return pet_icon;
    }

    public void setPet_icon(String pet_icon) {
        this.pet_icon = pet_icon;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public int getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(int appointment_status) {
        this.appointment_status = appointment_status;
    }

    public int getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(int number_of_people) {
        this.number_of_people = number_of_people;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getAppointment_location() {
        return appointment_location;
    }

    public void setAppointment_location(String appointment_location) {
        this.appointment_location = appointment_location;
    }
}
