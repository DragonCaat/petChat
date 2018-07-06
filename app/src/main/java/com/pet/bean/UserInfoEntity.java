package com.pet.bean;

/**
 * Created by dragon on 2018/6/22.
 * 用户个人信息的实体
 */

public class UserInfoEntity {
    /**
     * user_id : 71
     * user_name : 联萌星人17622384521
     * user_gender : 2
     * user_age : 0
     * user_desc : 刚来到联萌星球，快点写点什么吧！
     * user_type : 0
     * follow_count : 8
     * fans_count : 0
     * pet_type : 0
     * fosterage_or_not : 0
     * user_icon : /static/photo/profilephoto.png
     * small_user_icon : /static/photo/small_profilephoto.png
     * background_img : /static/photo/profilephoto.png
     * share_pet_agree_or_not : 1
     * appointment_or_not : 1
     */

    private int user_id;
    private String user_name;
    private int user_gender;
    private int user_age;
    private String user_desc;
    private int user_type;
    private int follow_count;
    private int fans_count;
    private int pet_type;
    private int fosterage_or_not;
    private String user_icon;
    private String small_user_icon;
    private String background_img;
    private int share_pet_agree_or_not;
    private int appointment_or_not;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public String getUser_desc() {
        return user_desc;
    }

    public void setUser_desc(String user_desc) {
        this.user_desc = user_desc;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getPet_type() {
        return pet_type;
    }

    public void setPet_type(int pet_type) {
        this.pet_type = pet_type;
    }

    public int getFosterage_or_not() {
        return fosterage_or_not;
    }

    public void setFosterage_or_not(int fosterage_or_not) {
        this.fosterage_or_not = fosterage_or_not;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getSmall_user_icon() {
        return small_user_icon;
    }

    public void setSmall_user_icon(String small_user_icon) {
        this.small_user_icon = small_user_icon;
    }

    public String getBackground_img() {
        return background_img;
    }

    public void setBackground_img(String background_img) {
        this.background_img = background_img;
    }

    public int getShare_pet_agree_or_not() {
        return share_pet_agree_or_not;
    }

    public void setShare_pet_agree_or_not(int share_pet_agree_or_not) {
        this.share_pet_agree_or_not = share_pet_agree_or_not;
    }

    public int getAppointment_or_not() {
        return appointment_or_not;
    }

    public void setAppointment_or_not(int appointment_or_not) {
        this.appointment_or_not = appointment_or_not;
    }
}
