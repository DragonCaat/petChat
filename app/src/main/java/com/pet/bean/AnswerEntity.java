package com.pet.bean;

/**
 * Created by dragon on 2018/6/25.
 * 评论的实体
 */

public class AnswerEntity {
    /**
     * cmmt_id : 5
     * cmmt_content : 微信
     * create_time : 2017-12-18 17:21:40
     * user_id : 35
     * user_name : Tix
     * user_icon : /uploads/small_user_icon/5df4405e441f005bf819cb3ffde0ca85.png
     * cmmt_upvoteCount : 2
     * cmmt_count : 2
     * super_user_id : null
     * super_user_name : null
     * cmmt_upvote : 0
     */

    private int cmmt_id;
    private String cmmt_content;
    private String create_time;
    private int user_id;
    private String user_name;
    private String user_icon;
    private int cmmt_upvoteCount;
    private int cmmt_count;
    private Object super_user_id;
    private Object super_user_name;
    private int cmmt_upvote;

    public int getCmmt_id() {
        return cmmt_id;
    }

    public void setCmmt_id(int cmmt_id) {
        this.cmmt_id = cmmt_id;
    }

    public String getCmmt_content() {
        return cmmt_content;
    }

    public void setCmmt_content(String cmmt_content) {
        this.cmmt_content = cmmt_content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

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

    public int getCmmt_upvoteCount() {
        return cmmt_upvoteCount;
    }

    public void setCmmt_upvoteCount(int cmmt_upvoteCount) {
        this.cmmt_upvoteCount = cmmt_upvoteCount;
    }

    public int getCmmt_count() {
        return cmmt_count;
    }

    public void setCmmt_count(int cmmt_count) {
        this.cmmt_count = cmmt_count;
    }

    public Object getSuper_user_id() {
        return super_user_id;
    }

    public void setSuper_user_id(Object super_user_id) {
        this.super_user_id = super_user_id;
    }

    public Object getSuper_user_name() {
        return super_user_name;
    }

    public void setSuper_user_name(Object super_user_name) {
        this.super_user_name = super_user_name;
    }

    public int getCmmt_upvote() {
        return cmmt_upvote;
    }

    public void setCmmt_upvote(int cmmt_upvote) {
        this.cmmt_upvote = cmmt_upvote;
    }
}
