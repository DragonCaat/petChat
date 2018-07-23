package com.pet.bean;

/**
 * Created by dragon on 2018/7/19.
 * 宠物商店实体类
 */

public class PetStoreEntity {


    /**
     * shop_id : 1
     * shop_name : 达达商店
     * longitude : 113.9530376519097
     * latitude : 22.53287490000000
     * shop_avatar : /uploads/store_avatar/default/1.jpg
     * distance : 109
     */

    private int shop_id;
    private String shop_name;
    private String longitude;
    private String latitude;
    private String shop_avatar;
    private int distance;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getShop_avatar() {
        return shop_avatar;
    }

    public void setShop_avatar(String shop_avatar) {
        this.shop_avatar = shop_avatar;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
