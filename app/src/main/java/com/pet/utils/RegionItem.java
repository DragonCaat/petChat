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

    private String userId;

    public String getUserId(){
        return userId;
    }

    public String getUserType() {
        return userType;
    }
    public String getUrl() {
        return url;
    }

    public RegionItem(LatLng latLng, String userType,String url,String userId) {
        this.mLatLng=latLng;
        this.userType = userType;
        this.url = url;
        this.userId = userId;
    }

    @Override
    public LatLng getPosition() {
        // TODO Auto-generated method stub
        return mLatLng;
    }

}
