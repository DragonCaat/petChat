package com.pet.bean;

/**
 * Created by dragon on 2018/7/3.
 * 其他实体
 */

public class OtherInfoEntity {

    /**
     * user_id : 48
     * user_name : 耶耶耶
     * user_icon : /uploads/icon/20180429/49066a4ed2c95244a08e4ad9a7512d10.png
     * user_desc : null
     * background_img : null
     * pet_food : 99999
     * follow_count : 0
     * fans_count : 0
     * user_gender : 1
     * upvoteCount : 0
     * follow_status : 0
     */

    private int user_id;
    private String user_name;
    private String user_icon;
    private Object user_desc;
    private Object background_img;
    private int pet_food;
    private int follow_count;
    private int fans_count;
    private int user_gender;
    private String upvoteCount;
    private int follow_status;

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

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public Object getUser_desc() {
        return user_desc;
    }

    public void setUser_desc(Object user_desc) {
        this.user_desc = user_desc;
    }

    public Object getBackground_img() {
        return background_img;
    }

    public void setBackground_img(Object background_img) {
        this.background_img = background_img;
    }

    public int getPet_food() {
        return pet_food;
    }

    public void setPet_food(int pet_food) {
        this.pet_food = pet_food;
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

    public int getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public String getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(String upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public int getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(int follow_status) {
        this.follow_status = follow_status;
    }
}
