package com.pet.bean;

/**
 * Created by dragon on 2018/6/20.
 * 注册成功后返回的数据实体
 */

public class RegisterEntity {
    /**
     * user_id : 62
     * user_name : 联萌星人17675674521
     * user_icon : /static/photo/profilephoto.png
     * access_token : xm6wgAzn4G42Jsh7MQ8LDPFOH93iK3fa
     * rc_token : vvl1+luTXhRhwFg+8Bn2gOYxiOy88VvrbAj/xPiHZr/GYUsO2DatU//RiQNjCwZ/FjqUw4oAjqjjr/PQTALtYw==
     */

    private String user_id;
    private String user_name;
    private String user_icon;
    private String access_token;
    private String rc_token;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRc_token() {
        return rc_token;
    }

    public void setRc_token(String rc_token) {
        this.rc_token = rc_token;
    }
}
