package com.pet.bean;

import java.util.List;

/**
 * Created by dragon on 2018/6/21.
 * 动态的实体
 */

public class DynamicEntity {

    /**
     * user_id : 50
     * user_name : Amo
     * user_icon : /uploads/icon/20180615/0c4495d53edf36941103e92d2093b701.png
     * dynamic_id : 45
     * content : 刘姥姥
     * post_latitude : null
     * post_longitude : null
     * user_address : null
     * send_time : 2018-06-01 14:49:41
     * img_urls : [{"img_url":"/uploads/user_dynamic_img/20180601/ca10d2ea51eb658224f40f7e5327d53f.png"}]
     * upvoteCount : 0
     * cmmt_count : 1
     * upvote : 0
     */

    private int user_id;
    private String user_name;
    private String user_icon;
    private int dynamic_id;
    private String content;
    private Object post_latitude;
    private Object post_longitude;
    private Object user_address;
    private String send_time;
    private int upvoteCount;
    private int cmmt_count;
    private int upvote;
    private List<String> img_urls;

    private String post_id;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
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

    public int getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(int dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getPost_latitude() {
        return post_latitude;
    }

    public void setPost_latitude(Object post_latitude) {
        this.post_latitude = post_latitude;
    }

    public Object getPost_longitude() {
        return post_longitude;
    }

    public void setPost_longitude(Object post_longitude) {
        this.post_longitude = post_longitude;
    }

    public Object getUser_address() {
        return user_address;
    }

    public void setUser_address(Object user_address) {
        this.user_address = user_address;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public int getCmmt_count() {
        return cmmt_count;
    }

    public void setCmmt_count(int cmmt_count) {
        this.cmmt_count = cmmt_count;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public List<String> getImg_urls() {
        return img_urls;
    }

    public void setImg_urls(List<String> img_urls) {
        this.img_urls = img_urls;
    }

//    public static class ImgUrlsBean {
//        /**
//         * img_url : /uploads/user_dynamic_img/20180601/ca10d2ea51eb658224f40f7e5327d53f.png
//         */
//
//        private String img_url;
//
//        public String getImg_url() {
//            return img_url;
//        }
//
//        public void setImg_url(String img_url) {
//            this.img_url = img_url;
//        }
//    }
}
