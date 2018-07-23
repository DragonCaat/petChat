package com.pet.utils;

import com.amap.api.maps.model.LatLng;

/**
 * Created by dragon on 2018/6/21.
 * 返回经纬度和图片的URL
 */

public interface ClusterItem {
    /**
     * 返回聚合元素的地理位置
     * @return
     */
    LatLng getPosition();
    String getUserType();

    String getUserId();

    String getUrl();


}
