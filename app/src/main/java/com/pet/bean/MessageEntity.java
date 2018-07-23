package com.pet.bean;

/**
 * Created by dragon on 2018/7/13.
 * 消息实体
 */

public class MessageEntity {

    /**
     * user_name : 古星瞳
     * user_icon : /uploads/icon/20180709/6efb4b5ef589073328a5f767b4f5ed63.png
     * module_type : 0
     * ID : 102
     * cmmt_id : 199
     * img_url : /uploads/snapshot_img/20180709/caadab37b2dadf73004a80ff39cd31d0.jpg
     * m_id : 940
     * user_id : 77
     * message_type : 2
     * create_time : 2018-07-12 12:03:15
     */

    private String user_name;
    private String user_icon;
    private int module_type;
    private int ID;
    private int cmmt_id;
    private String img_url;
    private int m_id;
    private int user_id;
    private int message_type;
    private String create_time;
    /**
     * mobilephone : 15999902006
     * appointment_id : 83
     * refuse_reason : null
     * is_agree : 1
     */

    private String mobilephone;
    private int appointment_id;
    private Object refuse_reason;
    private int is_agree;
    /**
     * content : 砸了
     */

    private String content;
    /**
     * food_count : 10
     */

    private int food_count;
    /**
     * pet_icon : /uploads/pet_icon/20180705/7ea3c2302ef70186e4d2e1db53889512.jpg
     * appointment_status : 0
     * number_of_people : 1
     * appointment_time : 2018-07-29 00:00:00
     * appointment_location : 粤海街道深圳湾科技生态园
     */

    private int appointment_status;


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

    public int getModule_type() {
        return module_type;
    }

    public void setModule_type(int module_type) {
        this.module_type = module_type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCmmt_id() {
        return cmmt_id;
    }

    public void setCmmt_id(int cmmt_id) {
        this.cmmt_id = cmmt_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public Object getRefuse_reason() {
        return refuse_reason;
    }

    public void setRefuse_reason(Object refuse_reason) {
        this.refuse_reason = refuse_reason;
    }

    public int getIs_agree() {
        return is_agree;
    }

    public void setIs_agree(int is_agree) {
        this.is_agree = is_agree;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFood_count() {
        return food_count;
    }

    public void setFood_count(int food_count) {
        this.food_count = food_count;
    }


    public int getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(int appointment_status) {
        this.appointment_status = appointment_status;
    }

}
