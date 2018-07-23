package com.pet.retrofit;

import com.pet.bean.ResultEntity;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
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

    //忘记密码
    @POST("UserRegister/forget_psw")
    Call<ResultEntity> forgetPwd(@QueryMap Map<String, String> params);

    //检验验证码
    @POST("controller/SMS/SMS/check_sms")
    Call<ResultEntity> checkSms(@QueryMap Map<String, String> params);


    //获取融云的token
    @POST("controller/IM/IM/get_IM_token")
    Call<ResultEntity> getIMToken(@QueryMap Map<String, Object> params);

    //获取范围内的用户
    @POST("controller/distance/Distance/GetRange")
    Call<ResultEntity> GetRange(@QueryMap Map<String, Object> params);

    //地图上点击获取信息
    @POST("controller/pet/PetInfo/get_petInfo")
    Call<ResultEntity> getPetInfo(@QueryMap Map<String, Object> params);

    //随手拍主页数据
    @POST("controller/snapshot/Snapshot/get_snapshot_img")
    Call<ResultEntity> GetSnapshot(@QueryMap Map<String, Object> params);

    //个人关注
    @POST("controller/dynamic/Dynamic/follow_user_dynamic")
    Call<ResultEntity> GetDynamic(@QueryMap Map<String, Object> params);

    //个人信息
    @POST("controller/userInfo/UserInfo/show_userinfo")
    Call<ResultEntity> GetUserinfo(@QueryMap Map<String, Object> params);

    //修改用户信息
    @Multipart
    @POST("controller/userInfo/UserInfo/change_userinfo")
    Call<ResultEntity> changeUserInfo(@QueryMap Map<String, Object> params, @PartMap Map<String, RequestBody> part, @PartMap Map<String, RequestBody> part1);

    //发布动态
    @Multipart
    @POST("controller/dynamic/Dynamic/create_dynamic")
    Call<ResultEntity> CreateDynamic(@QueryMap Map<String, Object> params, @PartMap Map<String, RequestBody> parts);

    //发布随手拍
    @Multipart
    @POST("controller/snapshot/Snapshot/snapshot_posts")
    Call<ResultEntity> CreatesNapshot(@QueryMap Map<String, Object> params, @PartMap Map<String, RequestBody> parts);

    //随手拍热点话题
    @POST("controller/snapshot/Snapshot/get_hot_post")
    Call<ResultEntity> GetHotPast(@QueryMap Map<String, Object> params);

    //单独显示话题
    @POST("controller/snapshot/Snapshot/snapshot_post_details")
    Call<ResultEntity> ShowSnapshot(@QueryMap Map<String, Object> params);

    //显示随手拍评论
    @POST("controller/snapshot/Snapshot/show_post_cmmt")
    Call<ResultEntity> ShowPostCmmt(@QueryMap Map<String, Object> params);

    //发表随手拍评论
    @POST("controller/snapshot/Snapshot/posts_cmmt")
    Call<ResultEntity> posts_cmmt(@QueryMap Map<String, Object> params);

    //动态评论
    @POST("controller/dynamic/Dynamic/dynamic_cmmt")
    Call<ResultEntity> dynamic_cmmt(@QueryMap Map<String, Object> params);

    //随手拍评论点赞
    @POST("controller/snapshot/Snapshot/cmmt_upvote")
    Call<ResultEntity> cmmt_upvote(@QueryMap Map<String, Object> params, @Body Map<String, Object> params1);

    //随手拍点赞
    @POST("controller/snapshot/Snapshot/posts_upvote")
    Call<ResultEntity> posts_upvotet(@QueryMap Map<String, Object> params, @Body Map<String, Object> params1);

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
    Call<ResultEntity> addPet(@QueryMap Map<String, Object> params, @PartMap Map<String, RequestBody> part, @PartMap Map<String, RequestBody> part1);

    //用户宠物删除
    @POST("controller/pet/PetInfo/delete_pet")
    Call<ResultEntity> deletePet(@QueryMap Map<String, Object> params);

    //用户宠物管理
    @POST("controller/pet/PetInfo/pet_manage")
    Call<ResultEntity> PetManage(@QueryMap Map<String, Object> params);

    //意见反馈
    @POST("controller/Setup/Setup/addFeedback")
    Call<ResultEntity> addFeedback(@QueryMap Map<String, Object> params);

    //发送验证码接口
    @POST("controller/SMS/SMS/send_sms")
    Call<ResultEntity> sendSms(@QueryMap Map<String, Object> params);

    //用户关注和取消关注
    @POST("controller/dynamic/Dynamic/user_follow")
    Call<ResultEntity> userFollow(@QueryMap Map<String, Object> params);

    //约宠信息填写
    @POST("controller/pet/Appointment/appointment_pet")
    Call<ResultEntity> appointmentPet(@QueryMap Map<String, Object> params);

    //根据融云id获取用户信息
    @POST("controller/IM/IM/get_user_info")
    Call<ResultEntity> get_user_info(@QueryMap Map<String, Object> params);

    //向服务器发送位置信息
    @POST("controller/distance/Distance/insert_map")
    Call<ResultEntity> insert_map(@QueryMap Map<String, Object> params);

    //喂宠物
    @POST("controller/pet/Feed/pet_hungry_level")
    Call<ResultEntity> pet_hungry_level(@QueryMap Map<String, Object> params);

    //获取用户的宠粮数
    @POST("controller/pet/Feed/get_pet_food")
    Call<ResultEntity> get_pet_food(@QueryMap Map<String, Object> params);

    //获取消息管理中需要的数据
    @POST("controller/pushMessage/PushMessage/show_message")
    Call<ResultEntity> show_message(@QueryMap Map<String, Object> params);

    //显示约宠信息
    @POST("controller/pet/Appointment/show_appointment")
    Call<ResultEntity> show_appointment(@QueryMap Map<String, Object> params);

    //拒绝或同意约宠
    @POST("controller/pet/Appointment/agree_appointment_pet")
    Call<ResultEntity> agree_appointment_pet(@QueryMap Map<String, Object> params);

    //获取宠粮和粮票数
    @POST("controller/pet/Feed/GetFoodandCoupon")
    Call<ResultEntity> GetFoodandCoupon(@QueryMap Map<String, Object> params);

    //宠粮兑换粮票
    @POST("controller/Coupon/Coupon/exchangeCashCoupon")
    Call<ResultEntity> exchangeCashCoupon(@QueryMap Map<String, Object> params);

    //打赏
    @POST("controller/pet/Feed/user_reward")
    Call<ResultEntity> user_reward(@QueryMap Map<String, Object> params);

    //动态详情
    @POST("controller/dynamic/Dynamic/dynamic_post_details")
    Call<ResultEntity> dynamic_post_details(@QueryMap Map<String, Object> params);

    //删除动态
    @POST("controller/dynamic/Dynamic/delete_user_dynamic")
    Call<ResultEntity> delete_user_dynamic(@QueryMap Map<String, Object> params);


    //发送极光registerId
    @POST("UserRegister/get_RegistrationId")
    Call<ResultEntity> get_RegistrationId(@QueryMap Map<String, Object> params);

    //获取附近的商家数据
    @POST("controller/Store/Store/GetDistanceInfos")
    Call<ResultEntity> GetDistanceInfos(@QueryMap Map<String, Object> params);

    //是否在地图上显示宠物信息
    @POST("controller/pet/PetInfo/appoint_agree_or_not")
    Call<ResultEntity> appoint_agree_or_not(@QueryMap Map<String, Object> params);

    //是否打开约宠开关
    @POST("controller/pet/Appointment/appointment_or_not")
    Call<ResultEntity> appointment_or_not(@QueryMap Map<String, Object> params);
}
