package com.pet.bean;

import java.util.List;

/**
 * Created by dragon on 2018/6/21.
 * 获取附近的人
 */

public class RangeEntity {


    /**
     * _id : {"$oid":"5b1defa6c388f90644005267"}
     * user_id : 35
     * loc : {"type":"Point","coordinates":[113.95254340278,22.532240125868]}
     * pet_info : {"pet_icon":"/uploads/pet_icon/20180105/7eeae9744771dedf9f23bfeeacec2aba.png","pet_kind":"阿比西尼亚猫","pet_type":1,"share_pet_agree_or_not":1,"appointment_or_not":1}
     */

    private IdBean _id;
    private String user_id;
    private LocBean loc;
    private PetInfoBean pet_info;

    public IdBean get_id() {
        return _id;
    }

    public void set_id(IdBean _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public LocBean getLoc() {
        return loc;
    }

    public void setLoc(LocBean loc) {
        this.loc = loc;
    }

    public PetInfoBean getPet_info() {
        return pet_info;
    }

    public void setPet_info(PetInfoBean pet_info) {
        this.pet_info = pet_info;
    }

    public static class IdBean {
        /**
         * $oid : 5b1defa6c388f90644005267
         */

        private String $oid;

        public String get$oid() {
            return $oid;
        }

        public void set$oid(String $oid) {
            this.$oid = $oid;
        }
    }

    public static class LocBean {
        /**
         * type : Point
         * coordinates : [113.95254340278,22.532240125868]
         */

        private String type;
        private List<Double> coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class PetInfoBean {
        /**
         * pet_icon : /uploads/pet_icon/20180105/7eeae9744771dedf9f23bfeeacec2aba.png
         * pet_kind : 阿比西尼亚猫
         * pet_type : 1
         * share_pet_agree_or_not : 1
         * appointment_or_not : 1
         */

        private String pet_icon;
        private String pet_kind;
        private int pet_type;
        private int share_pet_agree_or_not;
        private int appointment_or_not;

        public String getPet_icon() {
            return pet_icon;
        }

        public void setPet_icon(String pet_icon) {
            this.pet_icon = pet_icon;
        }

        public String getPet_kind() {
            return pet_kind;
        }

        public void setPet_kind(String pet_kind) {
            this.pet_kind = pet_kind;
        }

        public int getPet_type() {
            return pet_type;
        }

        public void setPet_type(int pet_type) {
            this.pet_type = pet_type;
        }

        public int getShare_pet_agree_or_not() {
            return share_pet_agree_or_not;
        }

        public void setShare_pet_agree_or_not(int share_pet_agree_or_not) {
            this.share_pet_agree_or_not = share_pet_agree_or_not;
        }

        public int getAppointment_or_not() {
            return appointment_or_not;
        }

        public void setAppointment_or_not(int appointment_or_not) {
            this.appointment_or_not = appointment_or_not;
        }
    }
}
