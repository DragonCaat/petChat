package com.pet.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pet.R;
import com.pet.activity.NearPeopleActivity;
import com.pet.adapter.MapPetsAdapter;
import com.pet.bean.Const;
import com.pet.bean.MapPetInfo;
import com.pet.bean.PetStoreEntity;
import com.pet.bean.RangeEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.ClusterClickListener;
import com.pet.utils.ClusterItem;
import com.pet.utils.ClusterOverlay;
import com.pet.utils.ClusterRender;
import com.pet.utils.PreferencesUtils;
import com.pet.utils.RegionItem;
import com.pet.view.NumberRollingView;
import com.pet.view.PageIndicatorView;
import com.pet.view.PageRecyclerView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by dragon on 2018/6/11.
 * 首页地图的fragment
 */

public class MapFragment extends SupportMapFragment implements AMap.OnMapLoadedListener,
        AMapLocationListener, LocationSource, SensorEventListener, ClusterRender, ClusterClickListener, PreferenceManager.OnActivityDestroyListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener {

    private int DOG_FLAG = 0;//遛狗的标示
    private int SAME_FLAG = 0;//同类宠物
    private int PET_STORE_FLAG = 0;//宠物商店显示

    private MapView mMapView;
    private AMap mAmap;
    // 声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    // 声明定位回调监听器
    public AMapLocationListener mLocationListener;

    private AMapLocation mMyLocationPoint;
    // 我的位置监听器
    private LocationSource.OnLocationChangedListener mLocationChangeListener = null;

    //初始化陀螺仪传感器，注册回调函数
    private SensorManager mSM;
    private Sensor mSensor;
    @BindView(R.id.iv_dog)
    ImageView mIvDog;
    @BindView(R.id.iv_same)
    ImageView mIvSame;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    @BindView(R.id.iv_pet_shop)
    ImageView ivPetShop;

    private View mRoot;

    //聚合半径
    private int clusterRadius = 50;
    private ClusterOverlay mClusterOverlay;

    private List<RangeEntity> rangeEntities;
    private List<ClusterItem> items;
    //纬度
    private double latitude = 0;
    //经度
    private double longitude = 0;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private GeocodeSearch geocodeSearch;

    //宠物商店集合
    private List<PetStoreEntity> petStoreEntityList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = mRoot.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        initMap();

        user_id = PreferencesUtils.getInt(getActivity(), Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(getActivity(), Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(getActivity(), Const.MOBILE_PHONE);

        ButterKnife.bind(this, mRoot);
        geocodeSearch = new GeocodeSearch(getActivity());
        geocodeSearch.setOnGeocodeSearchListener(this);
        return mRoot;
    }

    private void initMap() {
        if (mAmap == null) {
            mAmap = mMapView.getMap();
            initMyLocation();
            mAmap.setOnMapLoadedListener(this);

            MyLocationStyle myLocationStyle = new MyLocationStyle();
            // 自定义定位蓝点图标
            myLocationStyle.myLocationIcon(
                    BitmapDescriptorFactory.fromResource(R.drawable.location_icon));
            // 自定义精度范围的圆形边框颜色
            myLocationStyle.strokeColor(Color.argb(22, 22, 0, 0));
            // 自定义精度范围的圆形边框宽度
            myLocationStyle.strokeWidth(0);
            // 设置圆形的填充颜色
            myLocationStyle.radiusFillColor(Color.argb(100, 254, 217, 67));
            // 将自定义的 myLocationStyle 对象添加到地图上
            mAmap.setMyLocationStyle(myLocationStyle);
            //螺旋仪
            mSM = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
            mSensor = mSM.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            mSM.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);//注册回调函数

            //marker点击事件的监听
            mAmap.setOnMarkerClickListener(this);

        }
    }

    @OnClick({R.id.iv_dog, R.id.iv_health, R.id.iv_foster_care, R.id.iv_near, R.id.iv_same, R.id.iv_location, R.id.iv_fresh, R.id.iv_pet_shop})
    public void onClick(View view) {
        switch (view.getId()) {
            //自身定位
            case R.id.iv_location:
                //是否显示自己的位置
                initMyLocation();
                break;
            //遛狗
            case R.id.iv_dog:
                if (DOG_FLAG == 0) {
                    mIvDog.setImageResource(R.mipmap.walk_dog_press);//寻找附近的遛狗
                    DOG_FLAG = 1;
                } else {
                    DOG_FLAG = 0;
                    mIvDog.setImageResource(R.mipmap.walk_dog);//取消显示遛狗
                }
                break;
            //健康
            case R.id.iv_health:
                showHealthDialog();
                break;
            //寄养
            case R.id.iv_foster_care:

                break;
            //同类
            case R.id.iv_same:
                if (SAME_FLAG == 0) {
                    mIvSame.setImageResource(R.mipmap.same_press);//寻找附近的相同宠物的
                    SAME_FLAG = 1;
                } else {
                    mIvSame.setImageResource(R.mipmap.same);//取消显示相同宠物的
                    SAME_FLAG = 0;
                }
                break;

            //刷新附近的人
            case R.id.iv_fresh:
                llLoading.setVisibility(View.VISIBLE);
                sleepNear();

                break;

            //附近的人
            case R.id.iv_near:
                if (longitude ==0||latitude == 0){
                    Toast.makeText(getActivity(),"请先获取位置信息",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), NearPeopleActivity.class);
                    intent.putExtra("long", longitude);
                    intent.putExtra("lat", latitude);
                    startActivity(intent);
                }

                break;

            //宠物商店按钮
            case R.id.iv_pet_shop:
                if (PET_STORE_FLAG == 0) {
                    llLoading.setVisibility(View.VISIBLE);
                    sleepPetStore();
                } else {
                    clearMarkers();
                }

                break;

            default:

                break;
        }
    }

    //删除指定Marker
    private void clearMarkers() {
        //获取地图上所有Marker
        List<Marker> mapScreenMarkers = mAmap.getMapScreenMarkers();
        //动画
        ScaleAnimation alphaAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f);
        alphaAnimation.setDuration(500);
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker = mapScreenMarkers.get(i);
            if (marker.getObject() instanceof PetStoreEntity) {
                marker.setAnimation(alphaAnimation);
                marker.startAnimation();
                marker.remove();//移除当前Marker
            }
        }
        mMapView.invalidate();//刷新地图
        ivPetShop.setImageResource(R.drawable.pet_shop);
        PET_STORE_FLAG = 0;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void sleepNear() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mClusterOverlay != null)
                            mClusterOverlay.cleanAllMarkers();
                        getRange();
                    }
                });
            }
        }.start();
    }

    private void sleepPetStore() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GetDistanceInfos();
                    }
                });
            }
        }.start();
    }

    /**
     * 初始化定位服务
     */
    private void initLocation() {
        mLocationClient = new AMapLocationClient(getActivity());
        mLocationClient.setLocationListener(this);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();


    }

    /**
     * 初始化我的定位
     */
    private void initMyLocation() {
        mAmap.setLocationSource(this);
        mAmap.getUiSettings().setMyLocationButtonEnabled(true);
        mAmap.setMyLocationEnabled(true);
        mAmap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        mAmap.getUiSettings().setLogoPosition(
                AMapOptions.LOGO_POSITION_BOTTOM_LEFT);// logo位置
        mAmap.getUiSettings().setScaleControlsEnabled(true);// 标尺开关
        mAmap.getUiSettings().setCompassEnabled(false);// 指南针开关
        mAmap.getUiSettings().setZoomControlsEnabled(false);//缩放按钮
        mAmap.getUiSettings().setMyLocationButtonEnabled(false);//定位按钮


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
        deactivate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mLocationClient != null) {
            //mLocationClient.startLocation();
        }
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(14));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁资源
        if (mClusterOverlay != null)
            mClusterOverlay.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
        mMapView.onDestroy();
        //销毁资源
        if (mClusterOverlay != null)
            mClusterOverlay.onDestroy();
    }


    @Override
    public void deactivate() {
        mLocationChangeListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mLocationChangeListener = listener;
        if (mLocationClient == null) {
            initLocation();
        }
    }

    /**
     * 高德地图的定位信息
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                // 定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果,详见定位类型表
                latitude = amapLocation.getLatitude();// 获取纬度
                longitude = amapLocation.getLongitude();// 获取经度
//                amapLocation.getAccuracy();// 获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat(
//                        "yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//               df.format(date);// 定位时间
//                amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果
//                amapLocation.getCountry();// 国家信息
//                amapLocation.getProvince();// 省信息
//                amapLocation.getCity();// 城市信息
//                amapLocation.getDistrict();// 城区信息
//                amapLocation.getRoad();// 街道信息
//                amapLocation.getCityCode();// 城市编码
//                amapLocation.getAdCode();// 地区编码
                if (mMyLocationPoint == null) {

                    mMyLocationPoint = amapLocation;
                    mLocationChangeListener.onLocationChanged(mMyLocationPoint);
                }


            } else {
                // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            }
        }
    }

    /**
     * 获取附近的人
     */
    private void getRange() {
        if (longitude == 0 || latitude == 0) {
            Toast.makeText(getActivity(), "请先获取定位", Toast.LENGTH_SHORT).show();
            llLoading.setVisibility(View.INVISIBLE);
            return;
        }
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("distance", 50);
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);

        Call<ResultEntity> call = api.GetRange(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 获取成功
                    rangeEntities = JSON.parseArray(result.getData().toString(), RangeEntity.class);
                    if (rangeEntities != null && rangeEntities.size() > 0)
                        //addMarkers(rangeEntities);
                        initData(rangeEntities);
                    llLoading.setVisibility(View.GONE);

                } else {
                    llLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "暂无附近宠物信息", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> arg0, Throwable arg1) {

            }
        });
    }

    //初始化聚合数据
    private void initData(List<RangeEntity> rangeEntities) {
        items = new ArrayList<>();
        for (int i = 0; i < rangeEntities.size(); i++) {
            RangeEntity rangeEntity = rangeEntities.get(i);
            double lat;
            double lon;
            lat = rangeEntity.getLoc().getCoordinates().get(1);
            lon = rangeEntity.getLoc().getCoordinates().get(0);
            String petIcon = Const.PIC_URL + rangeEntity.getPet_info().getPet_icon();

            String userId = rangeEntity.getUser_id();

            LatLng latLng = new LatLng(lat, lon, false);
            RegionItem regionItem = new RegionItem(latLng,
                    "pet", petIcon, userId);
            items.add(regionItem);
        }
        //构造方法引入markers数据
        mClusterOverlay = new ClusterOverlay(mAmap, items,
                dp2px(getContext(), clusterRadius),
                getActivity());
        mClusterOverlay.setClusterRenderer(MapFragment.this);
        mClusterOverlay.setOnClusterClickListener(MapFragment.this);

    }


    @Override
    public void onMapLoaded() {
        //获取定位数据
        if (rangeEntities == null)
            getRange();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    //展示健康的dialog
    private void showHealthDialog() {
        final PopupWindow popupWindow = new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //将布局转化为view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.health_ask_dialog, null);

        NumberRollingView tvNum = view.findViewById(R.id.tv_num);
        NumberRollingView tvNum2 = view.findViewById(R.id.tv_num2);
        tvNum2.setContent("9686522");
        tvNum.setContent("9686522");

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = event.values[0];
            float bearing = mAmap.getCameraPosition().bearing;
            if (degree + bearing > 360)
                mAmap.setMyLocationRotateAngle(degree + bearing - 360);// 设置小蓝点旋转角度
            else
                mAmap.setMyLocationRotateAngle(degree + bearing);//
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public View getDrawAble(int clusterNum) {
        final View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_marker_item, null);
        // Bitmap bitmap = convertViewToBitmap(markerView);
        return markerView;

    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (ClusterItem clusterItem : clusterItems) {
//            builder.include(clusterItem.getPosition());
//        }
//        LatLngBounds latLngBounds = builder.build();
//        mAmap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0)
//        );
        if (clusterItems.size() == 1) {
            double latitude = clusterItems.get(0).getPosition().latitude;
            double longitude = clusterItems.get(0).getPosition().longitude;
            //被点击用户的id
            String userId = clusterItems.get(0).getUserId();
            llLoading.setVisibility(View.VISIBLE);
            sleep(latitude, longitude, userId);
        } else {
            //展示popWindow
            List<String> list = new ArrayList<>();
            for (int i = 0; i < clusterItems.size(); i++) {
                list.add(clusterItems.get(i).getUrl());
            }
            showMapPetsWindow(list, clusterItems);
        }
    }

    //点击多个聚合数据时候展示的POP window
    private void showMapPetsWindow(List<String> list, final List<ClusterItem> clusterItems) {
        View popView = getLayoutInflater().inflate(R.layout.layout_show_pets_item, null);
        ListView listView = popView.findViewById(R.id.lv_pets);

        final PopupWindow popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        MapPetsAdapter adapter = new MapPetsAdapter(list, getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                llLoading.setVisibility(View.VISIBLE);
                sleep(clusterItems.get(i).getPosition().latitude, clusterItems.get(i).getPosition().longitude, clusterItems.get(i).getUserId());

                getAddressByLatlng(clusterItems.get(i).getPosition());

                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        popupWindow.setWidth(width * 20 / 30);

        popupWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
        //popupWindow.showAsDropDown(marker);
    }

    //系统休眠500
    private void sleep(final double latitude, final double longitude, final String userId) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getPetInfo(latitude, longitude, userId);
                    }
                });
            }
        }.start();
    }


    //activity销毁时候调用
    @Override
    public void onActivityDestroy() {

    }

    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 20f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

    //搜索实现方法
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        String simpleAddress = formatAddress.substring(9);
        //String simpleAddress1 = formatAddress.substring(13);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }


    /**
     * func:自定义监听接口,用来marker的icon加载完毕后回调添加marker属性
     */
    public interface OnMarkerIconLoadListener {
        void markerIconLoadingFinished(View view);

    }

    /**
     * func:view转bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;

    }

    /**
     * 初始化popupWindow
     *
     * @param pet_info 宠物信息
     * @param distance 半径
     */
    private void initPopupWindow(List<MapPetInfo.PetInfoBean> pet_info, double distance) {
        View popView = getLayoutInflater().inflate(R.layout.popwindow_item, null);
        PageRecyclerView mRecyclerView = popView.findViewById(R.id.cusom_swipe_view);
        // 设置指示器
        mRecyclerView.setIndicator((PageIndicatorView) popView.findViewById(R.id.indicator));
        // 设置行数和列数
        mRecyclerView.setPageSize(1, 1);
        // 设置页间距
        mRecyclerView.setPageMargin(0);
        // 设置数据
        mRecyclerView.setAdapter(mRecyclerView.new PageAdapter(pet_info, getActivity(), distance));

        PopupWindow popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popWindow_animation);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        int height = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        popupWindow.setHeight(height * 10 / 20);

        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
    }

    //获取用户的宠物信息
    private void getPetInfo(double latitude_, double longitude_, String userId_) {
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("fP1Lat", latitude);
        params.put("fP1Lon", longitude);
        params.put("fP2Lat", latitude_);
        params.put("fP2Lon", longitude_);
        params.put("user_id", userId_);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);

        Call<ResultEntity> call = api.getPetInfo(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 获取成功
                    MapPetInfo petInfo = JSON.parseObject(result.getData().toString(), MapPetInfo.class);
                    if (petInfo != null) {
                        List<MapPetInfo.PetInfoBean> pet_info = petInfo.getPet_info();
                        double distance = petInfo.getDistance();
                        llLoading.setVisibility(View.GONE);
                        initPopupWindow(pet_info, distance);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResultEntity> arg0, Throwable arg1) {

            }
        });
    }

    /**
     * 获取附近的宠物店信息
     */
    private void GetDistanceInfos() {
        if (longitude == 0 || latitude == 0) {
            Toast.makeText(getActivity(), "请先获取定位", Toast.LENGTH_SHORT).show();
            llLoading.setVisibility(View.INVISIBLE);
            return;
        }
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        //params.put("distance", 50);
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);

        Call<ResultEntity> call = api.GetDistanceInfos(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 获取成功

                    petStoreEntityList = JSON.parseArray(result.getData().toString(), PetStoreEntity.class);

                    if (petStoreEntityList.size() > 0) {
                        addMarkers(petStoreEntityList);
                    } else {

                    }
                    //改变按钮状态
                    PET_STORE_FLAG = 1;
                    ivPetShop.setImageResource(R.drawable.pet_shop_press);
                    llLoading.setVisibility(View.GONE);

                } else {
                    llLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "暂无附近宠物店信息", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> arg0, Throwable arg1) {

            }
        });
    }


    private void addMarkers(List<PetStoreEntity> petStoreEntityList) {
        //在地图上添加其他的markers
        for (PetStoreEntity petStoreEntity : petStoreEntityList) {
            addMarker(petStoreEntity);
        }
    }

    /**
     * by moos on 2017/11/13
     * func:定制化marker的图标
     *
     * @return
     */
    private void customizeMarkerIcon(String url, final OnMarkerIconLoadListener listener) {
        final View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_pet_store_item, null);
        final CircleImageView icon = markerView.findViewById(R.id.pet_photo);
        Glide.with(this)
                .load(url)
                .asBitmap()
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        //待图片加载完毕后再设置bitmapDes
                        icon.setImageBitmap(bitmap);
                        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(markerView));
                        listener.markerIconLoadingFinished(markerView);
                    }
                });

        markerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * func:添加marker到地图上显示
     */
    BitmapDescriptor bitmapDescriptor;

    //往地图上添加markers
    private void addMarker(final PetStoreEntity petStoreEntity) {
        double lat;
        double lon;
        lat = Double.parseDouble(petStoreEntity.getLatitude());
        lon = Double.parseDouble(petStoreEntity.getLongitude());
        String url = Const.PIC_URL + petStoreEntity.getShop_avatar();

        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(lat, lon));
        //动画
        final ScaleAnimation alphaAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f);
        alphaAnimation.setDuration(500);

//        View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_pet_store_item, null);
//        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(markerView));
//        markerOptions.icon(bitmapDescriptor);
//        Marker marker = mAmap.addMarker(markerOptions);
//        marker.setAnimation(alphaAnimation);
//        marker.startAnimation();
//        //这行关键，标记Marker的类型宠物商店类型
//        marker.setObject(petStoreEntity);

        customizeMarkerIcon(url, new OnMarkerIconLoadListener() {
            @Override
            public void markerIconLoadingFinished(View view) {
                markerOptions.icon(bitmapDescriptor);
                Marker marker = mAmap.addMarker(markerOptions);
                marker.setAnimation(alphaAnimation);
                marker.startAnimation();
                //这行关键，标记Marker的类型宠物商店类型
                marker.setObject(petStoreEntity);


            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.i("hello", "onMarkerClick: 呵呵哒");

        for (int i = 0; i < petStoreEntityList.size(); i++) {
            if (marker.getObject() instanceof PetStoreEntity) {
                Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
}
