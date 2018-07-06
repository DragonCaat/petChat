package com.pet.bean;

/**
 * Created by dragon on 2018/7/2.
 * 附近的人实体
 */

public class NearByEntity {


    /**
     * user_info : {"user_id":35,"user_name":"Tix","user_icon":"/uploads/icon/20180131/5df4405e441f005bf819cb3ffde0ca85.png","user_desc":"养猪小能手","appointment_or_not":1}
     * distances : 100
     */

    private UserInfoBean user_info;
    private int distances;

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public int getDistances() {
        return distances;
    }

    public void setDistances(int distances) {
        this.distances = distances;
    }

    public static class UserInfoBean {
        /**
         * user_id : 35
         * user_name : Tix
         * user_icon : /uploads/icon/20180131/5df4405e441f005bf819cb3ffde0ca85.png
         * user_desc : 养猪小能手
         * appointment_or_not : 1
         */

        private int user_id;
        private String user_name;
        private String user_icon;
        private String user_desc;
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

        public String getUser_icon() {
            return user_icon;
        }

        public void setUser_icon(String user_icon) {
            this.user_icon = user_icon;
        }

        public String getUser_desc() {
            return user_desc;
        }

        public void setUser_desc(String user_desc) {
            this.user_desc = user_desc;
        }

        public int getAppointment_or_not() {
            return appointment_or_not;
        }

        public void setAppointment_or_not(int appointment_or_not) {
            this.appointment_or_not = appointment_or_not;
        }
    }
}
