package com.pet.utils;

import android.view.View;

/**
 * Created by dragon on 18/6/10.
 */

public interface ClusterRender {
    /**
     * 根据聚合点的元素数目返回渲染背景样式
     *
     * @param clusterNum
     * @return
     */
     View getDrawAble(int clusterNum);
}
