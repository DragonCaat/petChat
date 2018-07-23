package com.pet.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.adapter.AttentionAdapter;
import com.pet.bean.AttentionEntity;
import com.pet.bean.ChangeInfoEntity;
import com.pet.bean.Const;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.FitStateUI;
import com.pet.utils.PreferencesUtils;
import com.pet.view.DialogGetPicture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * 完善用户信息界面
 */
public class CompileUserDataActivity extends AppCompatActivity {
    public static final int RESULT_CODE = 0;//用户头像相册
    public static final int REQUEST_CODE_CAMERA = 1;// 背景相机
    public static final int REQUEST_CAMERA = 3;// 头像相机
    public static final int PHOTO_GALLERY = 2; // 背景相册
    public static final String IMAGE_UNSPECIFIED = "image/*";//随意图片类型

    @BindView(R.id.ll_girl)
    LinearLayout llGirl;
    @BindView(R.id.iv_girl)
    ImageView ivGirl;
    @BindView(R.id.ll_boy)
    LinearLayout llBoy;
    @BindView(R.id.iv_boy)
    ImageView ivBoy;

    @BindView(R.id.ll_have_pet)
    LinearLayout llHavePet;
    @BindView(R.id.iv_have_pet)
    ImageView ivHavePet;
    @BindView(R.id.ll_no_pet)
    LinearLayout llNoPet;
    @BindView(R.id.iv_no_pet)
    ImageView ivNoPet;

    @BindView(R.id.ll_dog)
    LinearLayout llDog;
    @BindView(R.id.iv_dog)
    ImageView ivDog;
    @BindView(R.id.ll_cat)
    LinearLayout llCat;
    @BindView(R.id.iv_cat)
    ImageView ivCat;
    @BindView(R.id.ll_other)
    LinearLayout llOther;
    @BindView(R.id.iv_other)
    ImageView ivOther;

    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_sign)
    EditText mEtSign;
    @BindView(R.id.circle_head)
    CircleImageView circleHead;
    @BindView(R.id.et_age)
    EditText mEtAge;

    private Context mContext;

    private String nick = "";
    private String sign = "";
    private String head = "";
    private String age = "0";
    private String bg = "";

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";
    private int gender = 0;//0:男 1：女
    private int userType = 0;//0：吸宠，1：铲屎官

    private String pet_icon = "";//宠物头像路径
    private String img_url = "";//宠物真实图片
    Map<String, RequestBody> bodyUserIcon;
    Map<String, RequestBody> bodyUserBg;

    private ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);

        FitStateUI.changeStatusBarTextColor(this, true);
        FitStateUI.setImmersionStateMode(this);
        mContext = this;
        Intent intent = getIntent();
        nick = intent.getStringExtra("name");
        sign = intent.getStringExtra("sign");
        head = intent.getStringExtra("head");
        bg = intent.getStringExtra("bg");
        setContentView(R.layout.activity_compile_user_data);
        ButterKnife.bind(this);
        setUserData();

        initListener();
    }

    //设置用户信息
    @SuppressLint("SetTextI18n")
    private void setUserData() {
        Glide.with(this).load(Const.PIC_URL + bg)
                .placeholder(R.mipmap.take_photo_loading)
                .dontAnimate().into(mIvBg);
        mEtName.setText("" + nick);
        mEtName.setSelection(nick.length());//将光标移至文字末尾

        mEtSign.setText("" + sign);
        mEtSign.setSelection(sign.length());//将光标移至文字末尾
        Glide.with(this).load(Const.PIC_URL + head)
                .dontAnimate()
                .placeholder(R.mipmap.default_head)
                .into(circleHead);
    }


    /**
     * 初始化互斥按钮的监听
     */
    private void initListener() {
        llBoy.setOnClickListener(sexListener);
        llGirl.setOnClickListener(sexListener);

        llHavePet.setOnClickListener(identiftyListener);
        llNoPet.setOnClickListener(identiftyListener);

        llDog.setOnClickListener(animalListener);
        llCat.setOnClickListener(animalListener);
        llOther.setOnClickListener(animalListener);
    }

    //选择性别的监听
    private View.OnClickListener sexListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ll_boy) {
                gender = 0;
                ivBoy.setImageResource(R.drawable.circle_press);
            } else {
                ivBoy.setImageResource(R.drawable.circle);
            }

            if (view.getId() == R.id.ll_girl) {
                gender = 1;
                ivGirl.setImageResource(R.drawable.circle_press);
            } else {
                ivGirl.setImageResource(R.drawable.circle);
            }
        }
    };


    //选择身份的监听
    private View.OnClickListener identiftyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ll_have_pet) {
                userType = 1;
                ivHavePet.setImageResource(R.drawable.circle_press);
            } else {
                ivHavePet.setImageResource(R.drawable.circle);
            }

            if (view.getId() == R.id.ll_no_pet) {
                userType = 0;
                ivNoPet.setImageResource(R.drawable.circle_press);
            } else {
                ivNoPet.setImageResource(R.drawable.circle);
            }
        }
    };

    //选择喜欢宠物的监听
    private View.OnClickListener animalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ll_dog) {
                ivDog.setImageResource(R.drawable.circle_press);
            } else {
                ivDog.setImageResource(R.drawable.circle);
            }

            if (view.getId() == R.id.ll_cat) {
                ivCat.setImageResource(R.drawable.circle_press);
            } else {
                ivCat.setImageResource(R.drawable.circle);
            }
            if (view.getId() == R.id.ll_other) {
                ivOther.setImageResource(R.drawable.circle_press);
            } else {
                ivOther.setImageResource(R.drawable.circle);
            }
        }
    };


    @OnClick({R.id.circle_head, R.id.iv_back, R.id.iv_bg, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circle_head:
                //1：修改头像
                showSeletPic(1);
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_bg:
                showSeletPic(2);
                break;

            //提交修改数据
            case R.id.btn_commit:
                checkData();
                break;

            default:

                break;
        }
    }

    //检查并获取用户修改的数据
    private void checkData() {
        nick = mEtName.getText().toString().trim();
        sign = mEtSign.getText().toString().trim();
        age = mEtAge.getText().toString().trim();
        if (TextUtils.isEmpty(age))
            age = "24";
        commitData();
    }

    //展示选择图片的dialog
    private void showSeletPic(final int code) {
        DialogGetPicture dialogGetPicture = new DialogGetPicture(this) {
            @Override
            public void amble() {
                if (code == 1)//头像
                    selectPictureFromAlbum(CompileUserDataActivity.this, RESULT_CODE);
                else //背景
                    selectPictureFromAlbum(CompileUserDataActivity.this, PHOTO_GALLERY);
            }

            @Override
            public void photo() {
                if (code == 1)//头像
                    photograph(CompileUserDataActivity.this, REQUEST_CAMERA);
                else //背景
                    photograph(CompileUserDataActivity.this, REQUEST_CODE_CAMERA);
            }
        };
        dialogGetPicture.show();
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
    public static void selectPictureFromAlbum(Activity activity, int code) {
        // 调用系统的相冊
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);

        activity.startActivityForResult(intent, code);
    }

    //修改用户数据
    private void commitData() {
        showProgressDialog();
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("user_name", nick);
        params.put("user_desc", sign);
        params.put("user_gender", gender);
        params.put("user_age", age);
        params.put("user_type", userType);

        retrofit2.Call<ResultEntity> call = api.changeUserInfo(params, bodyUserIcon, bodyUserBg);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(retrofit2.Call<ResultEntity> call,
                                   Response<ResultEntity> response) {
                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 获取成功
                    hideProgressDialog();
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();

                    ChangeInfoEntity changeInfoEntity = JSON.parseObject(result.getData().toString(),ChangeInfoEntity.class);

                   //更新融云头像
                    UserInfo userInfo = new UserInfo(""+user_id, changeInfoEntity.getUser_name(), Uri.parse(Const.PIC_URL + changeInfoEntity.getSmall_path()));
                    RongIM.getInstance().refreshUserInfoCache(userInfo);

                    finish();
                } else {
                    hideProgressDialog();
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //头像相册返回数据
            case RESULT_CODE:
                if (data != null) {
                    Uri uri = data.getData();
                    Glide.with(mContext).load(uri).into(circleHead);
                    //用户头像真实路径
                    pet_icon = getRealFilePath(mContext, uri);
                    File file = new File(pet_icon);
                    bodyUserIcon = filesToRequestBodyParts(file);
                }
                break;
            //头像相机返回数据
            case REQUEST_CAMERA:
               if (data != null) {
                    // 获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
                    Bundle bundle1 = data.getExtras();
                    if (bundle1 == null)
                        return;
                    Bitmap bitmap1 = (Bitmap) bundle1.get("data");
                    if (bitmap1 == null)
                        return;
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
                            bodyUserIcon = filesToRequestBodyParts(file);
                            Glide.with(mContext).load(uri).into(circleHead);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "取消拍照", Toast.LENGTH_SHORT).show();
                }
                break;

            //背景相机真实图片
            case REQUEST_CODE_CAMERA:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    // 获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
                    if (bundle == null)
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
                            bodyUserBg = filesToRequestBodyPart(file);
                            Glide.with(mContext).load(uri).into(mIvBg);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "取消拍照", Toast.LENGTH_SHORT).show();
                }
                break;

            case PHOTO_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    Glide.with(mContext).load(uri).into(mIvBg);
                    //用户头像真实路径
                    img_url = getRealFilePath(mContext, uri);
                    File file = new File(img_url);
                    bodyUserBg = filesToRequestBodyPart(file);
                }
                break;

            default:
                break;
        }
    }

    //头像
    private Map<String, RequestBody> filesToRequestBodyParts(File file) {
        Map<String, RequestBody> parts = new HashMap<>();
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), file);
        parts.put("user_icon\"; filename=\"" + file.getName(), requestBody);

        return parts;
    }

    //真实图片
    private Map<String, RequestBody> filesToRequestBodyPart(File file) {
        Map<String, RequestBody> parts = new HashMap<>();
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), file);
        parts.put("background_img\"; filename=\"" + file.getName(), requestBody);

        return parts;
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


    //展示加载对话框
    private void showProgressDialog() {
        proDialog = android.app.ProgressDialog.show(this, "", "正在提交...");
        proDialog.setCanceledOnTouchOutside(true);
    }

    private void hideProgressDialog() {
        if (proDialog != null)
            proDialog.dismiss();
    }
}
