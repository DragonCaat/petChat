package com.pet.bean;

/**
 * Created by dragon on 2018/6/27.
 * 萌宠实体
 */

public class CutePetEntity {
    /**
     * pet_id : 70
     * user_id : 71
     * pet_name : 福建猫
     * pet_gender : 1
     * pet_brith : 2018-06-27
     * pet_kind : 埃及猫
     * pet_status : 它正在睡觉
     * pet_icon : /uploads/pet_icon/20180627/08d374e90f35165ce979f6c67487f90c.png
     * fans_count : 1
     * pet_hungry_level : 100
     */

    private int pet_id;
    private int user_id;
    private String pet_name;
    private int pet_gender;
    private String pet_brith;
    private String pet_kind;
    private String pet_status;
    private String pet_icon;
    private int fans_count;
    private int pet_hungry_level;

    private int permisstion_to_access;

    public int getPermisstion_to_access() {
        return permisstion_to_access;
    }

    public void setPermisstion_to_access(int permisstion_to_access) {
        this.permisstion_to_access = permisstion_to_access;
    }

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

    public int getPet_gender() {
        return pet_gender;
    }

    public void setPet_gender(int pet_gender) {
        this.pet_gender = pet_gender;
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

    public String getPet_status() {
        return pet_status;
    }

    public void setPet_status(String pet_status) {
        this.pet_status = pet_status;
    }

    public String getPet_icon() {
        return pet_icon;
    }

    public void setPet_icon(String pet_icon) {
        this.pet_icon = pet_icon;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getPet_hungry_level() {
        return pet_hungry_level;
    }

    public void setPet_hungry_level(int pet_hungry_level) {
        this.pet_hungry_level = pet_hungry_level;
    }
}
