package com.pet.utils;

import com.amap.api.maps.model.LatLng;

/**
 * Created by dragon on 2018/6/21.
 *
 */

public class RegionItem implements ClusterItem {

    private LatLng mLatLng;
    private String userType;
    private String url;

    public String getUserType() {
        return userType;
    }
    public String getUrl() {
        return url;
    }

    public RegionItem(LatLng latLng, String userType,String url) {
        this.mLatLng=latLng;
        this.userType = userType;
        this.url = url;
    }

    @Override
    public LatLng getPosition() {
        // TODO Auto-generated method stub
        return mLatLng;
    }

}
