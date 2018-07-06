package com.pet.bean;

/**
 * Created by dragon on 2018/6/20.
 * 登陆返回的数据
 */

public class LoginEntity {
    /**
     * access_token : T1BE5iV8AOru35kjI4XJ81Z6RMGlfw92
     * login_status : 1
     * user_id : 60
     * continue_day : 0
     * pet_food : 0
     */

    private String access_token;
    private int login_status;
    private int user_id;
    private int continue_day;
    private String pet_food;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getLogin_status() {
        return login_status;
    }

    public void setLogin_status(int login_status) {
        this.login_status = login_status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getContinue_day() {
        return continue_day;
    }

    public void setContinue_day(int continue_day) {
        this.continue_day = continue_day;
    }

    public String getPet_food() {
        return pet_food;
    }

    public void setPet_food(String pet_food) {
        this.pet_food = pet_food;
    }
}
