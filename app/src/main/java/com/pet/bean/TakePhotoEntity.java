package com.pet.bean;

/**
 * Created by dragon on 2018/6/21.
 */

public class TakePhotoEntity {


    /**
     * circle_id : 2
     * circle_title : 路上的宠
     * circle_subtitle : 在路上
     * circle_img : https://cloudpetor.com/api/public/uploads/snapshot_img/small/lushang.jpg
     * circle_desc : 分享你我的快乐，与”它“在路上
     * circle_big_img : https://cloudpetor.com/api/public/uploads/snapshot_img/big/lushang.jpg
     * count : 4
     */

    private int circle_id;
    private String circle_title;
    private String circle_subtitle;
    private String circle_img;
    private String circle_desc;
    private String circle_big_img;
    private int count;

    public int getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(int circle_id) {
        this.circle_id = circle_id;
    }

    public String getCircle_title() {
        return circle_title;
    }

    public void setCircle_title(String circle_title) {
        this.circle_title = circle_title;
    }

    public String getCircle_subtitle() {
        return circle_subtitle;
    }

    public void setCircle_subtitle(String circle_subtitle) {
        this.circle_subtitle = circle_subtitle;
    }

    public String getCircle_img() {
        return circle_img;
    }

    public void setCircle_img(String circle_img) {
        this.circle_img = circle_img;
    }

    public String getCircle_desc() {
        return circle_desc;
    }

    public void setCircle_desc(String circle_desc) {
        this.circle_desc = circle_desc;
    }

    public String getCircle_big_img() {
        return circle_big_img;
    }

    public void setCircle_big_img(String circle_big_img) {
        this.circle_big_img = circle_big_img;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
