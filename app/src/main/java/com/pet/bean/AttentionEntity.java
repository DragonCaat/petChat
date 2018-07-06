package com.pet.bean;

import java.util.List;

/**
 * Created by dragon on 2018/6/27.
 * 个人动态实体
 */

public class AttentionEntity {


    /**
     * dynamic_id : 71
     * content : ddd
     * send_time : 2018-06-23 20:39:01
     * imgs : []
     */

    private int dynamic_id;
    private String content;
    private String send_time;
    private List<String> imgs;

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

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public List<?> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
