package com.pet.bean;

import java.util.List;

/**
 * Created by dragon on 2018/6/25.
 * 评论的实体
 */

public class AnswerEntity{

    /**
     * user_id : 48
     * user_name : 页页
     * user_icon : /uploads/icon/20180703/2e518c8f3225be83ce87412f31c4b47e.png
     * post_id : 89
     * content : 浪迹天涯
     * post_latitude : null
     * post_longitude : null
     * user_address : null
     * send_time : 2018-07-04 13:40:22
     * upvoteCount : 1
     * img_urls : ["/uploads/snapshot_img/20180704/5f989e8b9907a6dc55ef0519a5bfb3ff.png"]
     * cmmt_count : 1
     * upvote : 0
     * cmmt : [{"cmmt_id":178,"cmmt_content":"哈哈哈","create_time":"2018-07-10 09:59:20","user_id":68,"user_name":"达西sang","user_icon":"/uploads/small_user_icon/278f78d334d54bd998243d4681bf2fa8.png","cmmt_upvoteCount":2,"cmmt_count":1,"super_user_id":"","super_user_name":"","cmmt_upvote":0}]
     */

    private int user_id;
    private String user_name;
    private String user_icon;
    private int post_id;
    private String content;
    private Object post_latitude;
    private Object post_longitude;
    private Object user_address;
    private String send_time;
    private int upvoteCount;
    private int cmmt_count;
    private int upvote;
    private List<String> img_urls;
    private List<CmmtBean> cmmt;

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

    public List<CmmtBean> getCmmt() {
        return cmmt;
    }

    public void setCmmt(List<CmmtBean> cmmt) {
        this.cmmt = cmmt;
    }

    public static class CmmtBean {
        /**
         * cmmt_id : 178
         * cmmt_content : 哈哈哈
         * create_time : 2018-07-10 09:59:20
         * user_id : 68
         * user_name : 达西sang
         * user_icon : /uploads/small_user_icon/278f78d334d54bd998243d4681bf2fa8.png
         * cmmt_upvoteCount : 2
         * cmmt_count : 1
         * super_user_id :
         * super_user_name :
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
        private String super_user_id;
        private String super_user_name;
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

        public String getSuper_user_id() {
            return super_user_id;
        }

        public void setSuper_user_id(String super_user_id) {
            this.super_user_id = super_user_id;
        }

        public String getSuper_user_name() {
            return super_user_name;
        }

        public void setSuper_user_name(String super_user_name) {
            this.super_user_name = super_user_name;
        }

        public int getCmmt_upvote() {
            return cmmt_upvote;
        }

        public void setCmmt_upvote(int cmmt_upvote) {
            this.cmmt_upvote = cmmt_upvote;
        }
    }
}
