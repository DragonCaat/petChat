package com.pet.bean;

/**
 * Created by dragon on 2018/6/20.
 */

public class RcEntity {


    /**
     * user_id : 71
     * type : 1
     * rc_token : hjzi27h0Gmq3RM7sY4+QKqSGiZvImHCM2TVZuiktWwykf3YPN3WoHelfC1jW9Ec7RFADtmP8AVfXKRNF2VPdUg==
     */

    private String user_id;
    private int type;
    private String rc_token;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRc_token() {
        return rc_token;
    }

    public void setRc_token(String rc_token) {
        this.rc_token = rc_token;
    }
}
