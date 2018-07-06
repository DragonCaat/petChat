package com.pet.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.AnimationRotate;
import com.pet.utils.PreferencesUtils;
import com.pet.view.DialogGetPicture;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 添加宠物的页面
 */
public class AddPetActivity extends BaseActivity {

    public static final int RESULT_CODE = 0;//选择宠物种类
    public static final int REQUEST_CODE_CAMERA = 1;// 拍照
    public static final int REQUEST_CAMERA = 3;// 宠物真实图片拍照
    public static final int PHOTO_GALLERY = 2; // 相册
    public static final String IMAGE_UNSPECIFIED = "image/*";//随意图片类型
    @BindView(R.id.ll_girl)
    LinearLayout llGirl;
    @BindView(R.id.iv_girl)
    ImageView ivGirl;
    @BindView(R.id.ll_boy)
    LinearLayout llBoy;
    @BindView(R.id.iv_boy)
    ImageView ivBoy;
    @BindView(R.id.circle_head)
    CircleImageView cvHead;
    @BindView(R.id.tv_add_pet_kind)
    TextView mTvKindPet;
    @BindView(R.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.iv_add_pet_head)
    ImageView mIvAddHead;
    @BindView(R.id.et_name)
    EditText mEtName;

    private Context mContext;
    private PopupWindow popupWindow;

    private int user_id = 0;
    //private String content = "";
    private String acces_token = "";
    private String phone = "";

    //要上传的宠物数据
    private String petName = "";
    private String petKind = "";
    private String petBirthday = "";
    private int petGender = 0;//0:男 1：女

    private String pet_icon = "";//宠物头像路径
    private String img_url = "";//宠物真实图片
    Map<String, RequestBody> bodyPetIcon;
    Map<String, RequestBody> bodyPetImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);

        ButterKnife.bind(this);
        mContext = this;
        initListener();
    }

    private void initListener() {
        llBoy.setOnClickListener(sexListener);
        llGirl.setOnClickListener(sexListener);
    }


    @OnClick({R.id.iv_back, R.id.tv_add_pet_kind, R.id.circle_head, R.id.tv_birthday, R.id.iv_add_pet_head, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //选择宠物种类
            case R.id.tv_add_pet_kind:
                Intent intent = new Intent(mContext, PetSelectActivity.class);
                startActivityForResult(intent, RESULT_CODE);
                break;

            //选择头像
            case R.id.circle_head:
                new DialogGetPicture(this) {
                    @Override
                    public void amble() {
                        selectPictureFromAlbum(AddPetActivity.this);
                    }

                    @Override
                    public void photo() {
                        photograph(AddPetActivity.this, REQUEST_CODE_CAMERA);

                    }
                }.show();

                break;

            case R.id.tv_birthday:
                selectTime(mTvBirthday);
                break;
                //宠物真实图片
            case R.id.iv_add_pet_head:
                photograph(this, REQUEST_CAMERA);
                break;
            //提交申请
            case R.id.btn_commit:
                checkData();
                break;

            default:
                break;

        }
    }

    //检查用户输入信息
    private void checkData() {
        petName = mEtName.getText().toString().trim();
        petKind = mTvKindPet.getText().toString();
        petBirthday = mTvBirthday.getText().toString();
        if (TextUtils.isEmpty(petName) || TextUtils.isEmpty(petKind)
                || TextUtils.isEmpty(petBirthday)
                || TextUtils.isEmpty(pet_icon)
                || TextUtils.isEmpty(img_url)) {
            Toast.makeText(mContext, "请完善信息后再提交", Toast.LENGTH_SHORT).show();
        } else {
            //提交信息
            addPet();
        }
    }

    //时间选择器
    private void selectTime(final TextView textView) {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                textView.setText(time);
            }
        }, "2018-07-29 00:00", "2020-01-01 00:00");
        timeSelector.show();
    }

    /**
     * 拍照
     *
     * @param activity
     */
    public void photograph(Activity activity, int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, code);
    }

    /**
     * 从系统相冊中选取照片上传
     *
     * @param activity
     */
    public static void selectPictureFromAlbum(Activity activity) {
        // 调用系统的相冊
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);

        activity.startActivityForResult(intent, PHOTO_GALLERY);
    }


    //选择性别的监听
    private View.OnClickListener sexListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ll_boy) {
                ivBoy.setImageResource(R.drawable.circle_press);
                petGender = 0;
            } else {
                ivBoy.setImageResource(R.drawable.circle);
            }

            if (view.getId() == R.id.ll_girl) {
                ivGirl.setImageResource(R.drawable.circle_press);
                petGender = 1;
            } else {
                ivGirl.setImageResource(R.drawable.circle);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_CODE:
                if (data != null)
                    mTvKindPet.setText("" + data.getStringExtra("pet"));
                break;
            //相册返回数据
            case PHOTO_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    Glide.with(mContext).load(uri).into(cvHead);
                    //宠物头像真实路径
                    pet_icon = getRealFilePath(mContext, uri);
                    File file = new File(pet_icon);
                    bodyPetIcon = filesToRequestBodyParts(file);
                }
                break;
            //相册返回数据
            case REQUEST_CODE_CAMERA:
                if (data != null) {
                    // 获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
                    Bundle bundle1 = data.getExtras();
                    if (bundle1==null)
                        return;
                    Bitmap bitmap1 = (Bitmap) bundle1.get("data");

                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1, null, null));
                    // Log.i("hello", "onActivityResult: " + uri);
                    Cursor cursor1 = getContentResolver().query(uri, null, null, null, null);
                    if (cursor1 != null && cursor1.moveToFirst()) {
                        pet_icon = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        if (TextUtils.isEmpty(pet_icon))
                            Toast.makeText(mContext, "图像路径不存在", Toast.LENGTH_SHORT).show();
                        else {
                            // Log.i("hello", "onActivityResult: " + pet_icon);
                            File file = new File(pet_icon);
                            bodyPetIcon = filesToRequestBodyParts(file);
                            Glide.with(mContext).load(uri).into(cvHead);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "取消拍照", Toast.LENGTH_SHORT).show();
                }
                break;

            //真实图片
            case REQUEST_CAMERA:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    // 获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
                    if (bundle==null)
                        return;
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    if (bitmap == null)
                        return;
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));

                    Cursor cursor1 = getContentResolver().query(uri, null, null, null, null);
                    if (cursor1 != null && cursor1.moveToFirst()) {
                        img_url = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        if (TextUtils.isEmpty(img_url))
                            Toast.makeText(mContext, "图像路径不存在", Toast.LENGTH_SHORT).show();
                        else {
                            // Log.i("hello", "onActivityResult: " + pet_icon);
                            File file = new File(img_url);
                            bodyPetImage = filesToRequestBodyPart(file);
                            Glide.with(mContext).load(uri).into(mIvAddHead);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "取消拍照", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    //添加宠物
    private void addPet() {
        showLoadingDialog("正在上传...");
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);

        params.put("pet_name", petName);
        params.put("pet_brith", petBirthday);
        params.put("pet_kind", petKind);
        params.put("pet_gender", petGender);
        params.put("old_pet_id","");
        Call<ResultEntity> call = api.addPet(params, bodyPetIcon, bodyPetImage);
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
                    hideLoadingDialog();
                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    hideLoadingDialog();
                   // Log.i("hello", "onResponse: 失败");
                }

            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    //头像
    private Map<String, RequestBody> filesToRequestBodyParts(File file) {
        Map<String, RequestBody> parts = new HashMap<>();
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), file);
        parts.put("pet_icon\"; filename=\"" + file.getName(), requestBody);

        return parts;
    }

    //真实图片
    private Map<String, RequestBody> filesToRequestBodyPart(File file) {
        Map<String, RequestBody> parts = new HashMap<>();
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), file);
        parts.put("img_url\"; filename=\"" + file.getName(), requestBody);

        return parts;
    }

    //展示加载框
    private void showLoadingDialog(String s) {
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(this).inflate(R.layout.loading_item, null);
        popupWindow.setContentView(view);
        ImageView ivLoading = view.findViewById(R.id.iv_loading);
        AnimationRotate.rotatebolowImage(ivLoading);
        TextView tvDes = view.findViewById(R.id.tv_des);
        tvDes.setText(s);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    //隐藏
    private void hideLoadingDialog() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    private String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
