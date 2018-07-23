package com.pet.bean;

import java.util.List;

/**
 * Created by dragon on 2018/7/7.
 * 点击地图上用户显示宠物信息
 */

public class MapPetInfo {

    /**
     * pet_info : [{"pet_id":79,"user_id":50,"pet_name":"柴柴","pet_brith":"2016-10-03","pet_kind":"柴犬","pet_gender":0,"pet_icon":"/uploads/pet_icon/20180703/99f7a0b9f35e94e73c87b1fcb38620f8.png","user_icon":"/uploads/icon/20180615/0c4495d53edf36941103e92d2093b701.png","fans_count":3,"fosterage_or_not":1,"appointment_count":1,"user_icons":[{"user_icon":"/uploads/icon/20180607/c90094f9036d7d5b06d0a64eb0488748.png"}]},{"pet_id":76,"user_id":50,"pet_name":"豆豆","pet_brith":"2017-05-03","pet_kind":"美国短毛猫","pet_gender":0,"pet_icon":"/uploads/pet_icon/20180703/28e1c0c2e15003368b6be56439f62031.png","user_icon":"/uploads/icon/20180615/0c4495d53edf36941103e92d2093b701.png","fans_count":3,"fosterage_or_not":1,"appointment_count":0,"user_icons":[]}]
     * distance : 1.42
     */

    private double distance;
    private List<PetInfoBean> pet_info;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<PetInfoBean> getPet_info() {
        return pet_info;
    }

    public void setPet_info(List<PetInfoBean> pet_info) {
        this.pet_info = pet_info;
    }

    public static class PetInfoBean {
        /**
         * pet_id : 79
         * user_id : 50
         * pet_name : 柴柴
         * pet_brith : 2016-10-03
         * pet_kind : 柴犬
         * pet_gender : 0
         * pet_icon : /uploads/pet_icon/20180703/99f7a0b9f35e94e73c87b1fcb38620f8.png
         * user_icon : /uploads/icon/20180615/0c4495d53edf36941103e92d2093b701.png
         * fans_count : 3
         * fosterage_or_not : 1
         * appointment_count : 1
         * user_icons : [{"user_icon":"/uploads/icon/20180607/c90094f9036d7d5b06d0a64eb0488748.png"}]
         */

        private int pet_id;
        private int user_id;
        private String pet_name;
        private String pet_brith;
        private String pet_kind;
        private int pet_gender;
        private String pet_icon;
        private String user_icon;
        private int fans_count;
        private int fosterage_or_not;
        private int appointment_count;
        private List<UserIconsBean> user_icons;

        public int getPet_id() {
            return pet_id;
        }

        public void setPet_id(int pet_id) {
            this.pet_id = pet_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getPet_name() {
            return pet_name;
        }

        public void setPet_name(String pet_name) {
            this.pet_name = pet_name;
        }

        public String getPet_brith() {
            return pet_brith;
        }

        public void setPet_brith(String pet_brith) {
            this.pet_brith = pet_brith;
        }

        public String getPet_kind() {
            return pet_kind;
        }

        public void setPet_kind(String pet_kind) {
            this.pet_kind = pet_kind;
        }

        public int getPet_gender() {
            return pet_gender;
        }

        public void setPet_gender(int pet_gender) {
            this.pet_gender = pet_gender;
        }

        public String getPet_icon() {
            return pet_icon;
        }

        public void setPet_icon(String pet_icon) {
            this.pet_icon = pet_icon;
        }

        public String getUser_icon() {
            return user_icon;
        }

        public void setUser_icon(String user_icon) {
            this.user_icon = user_icon;
        }

        public int getFans_count() {
            return fans_count;
        }

        public void setFans_count(int fans_count) {
            this.fans_count = fans_count;
        }

        public int getFosterage_or_not() {
            return fosterage_or_not;
        }

        public void setFosterage_or_not(int fosterage_or_not) {
            this.fosterage_or_not = fosterage_or_not;
        }

        public int getAppointment_count() {
            return appointment_count;
        }

        public void setAppointment_count(int appointment_count) {
            this.appointment_count = appointment_count;
        }

        public List<UserIconsBean> getUser_icons() {
            return user_icons;
        }

        public void setUser_icons(List<UserIconsBean> user_icons) {
            this.user_icons = user_icons;
        }

        public static class UserIconsBean {
            /**
             * user_icon : /uploads/icon/20180607/c90094f9036d7d5b06d0a64eb0488748.png
             */

            private String user_icon;

            public String getUser_icon() {
                return user_icon;
            }

            public void setUser_icon(String user_icon) {
                this.user_icon = user_icon;
            }
        }
    }
}
