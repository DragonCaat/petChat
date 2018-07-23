package com.pet.utils;

import com.amap.api.maps.model.Marker;

import java.util.List;

/**
 * Created by dragon on 18/6/10.
 *
 */

public interface ClusterClickListener{
        /**
         * 点击聚合点的回调处理函数
         *
         * @param marker
         *            点击的聚合点
         * @param clusterItems
         *            聚合点所包含的元素
         */
        void onClick(Marker marker, List<ClusterItem> clusterItems);
}
