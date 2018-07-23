package com.pet.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dragon on 2018/6/21.
 * 动态的实体
 */

public class DynamicEntity {


    /**
     * user_id : 71
     * user_name : 大发明家
     * user_icon : /uploads/icon/20180710/8b1855c6e05b7a9f07a70490b34ff217.jpg
     * post_id : 103
     * content : 生活不止眼前的苟且还有诗和远方
     * post_latitude : 22.534168861082
     * post_longitude : 113.95532576997
     * user_address : 粤海街道金蝶大厦A座金蝶大厦
     * send_time : 2018-07-10 09:34:13
     * upvoteCount : 0
     * img_urls : ["/uploads/snapshot_img/20180710/9a33c9070dc6be483843ffdcf5d57763.jpg","/uploads/snapshot_img/20180710/842ed8c5994988c18abe1329547a4d73.jpg","/uploads/snapshot_img/20180710/b94e6c2f6b9428fcf3ce82bcd3cdbefe.jpg","/uploads/snapshot_img/20180710/68ac473e08a97b3f094b000efdfa07b5.jpg"]
     * fst_cmmt : {"user_id":51,"cmmt_content":"是是","user_name":"haha"}
     * cmmt_count : 1
     * upvote : 0
     */

    private int user_id;
    private String user_name;
    private String user_icon;
    private int post_id;
    private String content;
    private double post_latitude;
    private double post_longitude;
    private String user_address;
    private String send_time;
    private int upvoteCount;
    private FstCmmtBean fst_cmmt;
    private int cmmt_count;
    private int upvote;
    private List<String> img_urls;
    /**
     * dynamic_id : 124
     * post_latitude : 22.531714949418
     * post_longitude : 113.95286216104
     */

    private int dynamic_id;
    @SerializedName("post_latitude")
    private double post_latitudeX;
    @SerializedName("post_longitude")
    private double post_longitudeX;


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

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getPost_latitude() {
        return post_latitude;
    }

    public void setPost_latitude(double post_latitude) {
        this.post_latitude = post_latitude;
    }

    public double getPost_longitude() {
        return post_longitude;
    }

    public void setPost_longitude(double post_longitude) {
        this.post_longitude = post_longitude;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
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

    public FstCmmtBean getFst_cmmt() {
        return fst_cmmt;
    }

    public void setFst_cmmt(FstCmmtBean fst_cmmt) {
        this.fst_cmmt = fst_cmmt;
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

    public int getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(int dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public double getPost_latitudeX() {
        return post_latitudeX;
    }

    public void setPost_latitudeX(double post_latitudeX) {
        this.post_latitudeX = post_latitudeX;
    }

    public double getPost_longitudeX() {
        return post_longitudeX;
    }

    public void setPost_longitudeX(double post_longitudeX) {
        this.post_longitudeX = post_longitudeX;
    }

    public static class FstCmmtBean {
        /**
         * user_id : 51
         * cmmt_content : 是是
         * user_name : haha
         */

        private int user_id;
        private String cmmt_content;
        private String user_name;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getCmmt_content() {
            return cmmt_content;
        }

        public void setCmmt_content(String cmmt_content) {
            this.cmmt_content = cmmt_content;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }
}
