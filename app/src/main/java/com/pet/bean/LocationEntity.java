package com.pet.bean;

import com.amap.api.maps.model.LatLng;

/**
 * Created by dragon on 2018/7/9.
 * 首页地图的实体
 */

public class LocationEntity {

    private String url;

    private LatLng latLng;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
