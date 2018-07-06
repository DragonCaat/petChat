package com.pet.utils;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragon on 2018/6/21.
 *
 */

public class Cluster {
    private LatLng mLatLng;
    private List<ClusterItem> mClusterItems;
    private Marker mMarker;

    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Cluster(LatLng latLng) {

        mLatLng = latLng;
        mClusterItems = new ArrayList<>();
    }

    public Cluster(List<ClusterItem> clusterItems){
        mClusterItems = new ArrayList<>() ;
        mClusterItems = clusterItems;
    }

    public void addClusterItem(ClusterItem clusterItem) {
        mClusterItems.add(clusterItem);
    }

    public int getClusterCount() {
        return mClusterItems.size();
    }


    public LatLng getCenterLatLng() {
        return mLatLng;
    }

    public void setMarker(Marker marker) {
        mMarker = marker;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public List<ClusterItem> getClusterItems() {
        return mClusterItems;
    }
}
