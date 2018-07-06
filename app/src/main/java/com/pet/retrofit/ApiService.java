package com.pet.retrofit;

import com.pet.bean.ResultEntity;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

/**
 * Created by dragon on 2018/6/13.
 * 网络请求的接口
 */

public interface ApiService {
    //用户登陆
    @POST("UserRegister/login")
    Call<ResultEntity> login(@QueryMap Map<String, String> params);

    //用户注册
    @POST("UserRegister/register")
    Call<ResultEntity> register(@QueryMap Map<String, String> params);

    //获取融云的token
    @POST("controller/IM/IM/get_IM_token")
    Call<ResultEntity> getIMToken(@QueryMap Map<String, Object> params);

    //获取范围内的用户/
    @POST("controller/distance/Distance/GetRange")
    Call<ResultEntity> GetRange(@QueryMap Map<String, Object> params);

    //随手拍主页数据
    @POST("controller/snapshot/Snapshot/get_snapshot_img")
    Call<ResultEntity> GetSnapshot(@QueryMap Map<String, Object> params);

    //个人关注
    @POST("controller/dynamic/Dynamic/follow_user_dynamic")
    Call<ResultEntity> GetDynamic(@QueryMap Map<String, Object> params);

    //个人信息
    @POST("controller/userInfo/UserInfo/show_userinfo")
    Call<ResultEntity> GetUserinfo(@QueryMap Map<String, Object> params);

    //发布动态
    @Multipart
    @POST("controller/dynamic/Dynamic/create_dynamic")
    Call<ResultEntity> CreateDynamic(@QueryMap Map<String, Object> params,   @PartMap Map<String, RequestBody> parts);

    //随手拍热点话题
    @POST("controller/snapshot/Snapshot/get_hot_post")
    Call<ResultEntity> GetHotPast(@QueryMap Map<String, Object> params);

    //单独显示话题
    @POST("controller/snapshot/show_snapshot_post")
    Call<ResultEntity> ShowSnapshot(@QueryMap Map<String, Object> params);

    //显示随手拍评论
    @POST("controller/snapshot/Snapshot/show_post_cmmt")
    Call<ResultEntity> ShowPostCmmt(@QueryMap Map<String, Object> params);

    //我的动态
    @POST("controller/dynamic/Dynamic/show_user_dynamic_paging")
    Call<ResultEntity> ShowUserDynamic(@QueryMap Map<String, Object> params);

    //我的萌宠
    @POST("controller/pet/PetInfo/show_user_petinfo")
    Call<ResultEntity> ShowUserPetinfo(@QueryMap Map<String, Object> params);

    //附近的人
    @POST("controller/distance/Distance/nearby_users")
    Call<ResultEntity> nearbyUsers(@QueryMap Map<String, Object> params);

    //展示其他人数据
    @POST("controller/dynamic/Dynamic/show_homepage")
    Call<ResultEntity> showHomePage(@QueryMap Map<String, Object> params);

    //添加宠物
    @Multipart
    @POST("controller/pet/PetInfo/add_pet")
    Call<ResultEntity> addPet(@QueryMap Map<String, Object> params,@PartMap Map<String, RequestBody> part,@PartMap Map<String, RequestBody> part1);

    //用户宠物删除
    @POST("controller/pet/PetInfo/delete_pet")
    Call<ResultEntity> deletePet(@QueryMap Map<String, Object> params);

    //用户宠物管理
    @POST("controller/pet/PetInfo/pet_manage")
    Call<ResultEntity> PetManage(@QueryMap Map<String, Object> params);

    //意见反馈
    @POST("controller/Setup/Setup/addFeedback")
    Call<ResultEntity> addFeedback(@QueryMap Map<String, Object> params);
}
