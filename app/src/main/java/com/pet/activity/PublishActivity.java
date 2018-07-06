package com.pet.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.GridViewShowPicAdapter;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.AnimationRotate;
import com.pet.utils.PreferencesUtils;
import com.pet.view.NoScrollGridView;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 发布的界面
 */
public class PublishActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 732;
    //用于存放图片的返回地址
    private ArrayList<String> mResults = new ArrayList<>();
    //压缩后的图片地址
    private ArrayList<String> mNewResults = new ArrayList<>();
    private Context mContext;
    private GridViewShowPicAdapter adapter;

    private int PUBLISH_FLAG = 0;//0:发动态，1：随手拍

    @BindView(R.id.gv_add)
    NoScrollGridView mGridView;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    //latitude longitude user_address content
    private int user_id = 0;
    private String content = "";
    private String acces_token = "";
    private String phone = "";
    private double latitude = 0;
    private double longitude = 0;
    private String user_address = "";

    private PopupWindow popupWindow;
    /**
     * path:存放图片目录路径
     */
    private String path = Environment.getExternalStorageDirectory().getPath() + "/lianmeng/";
    /**
     * saveCatalog:保存文件目录
     */
    private File saveCatalog;

    /**
     * saveFile:保存的文件
     */
    private File saveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //获取发布的flag
        PUBLISH_FLAG = intent.getIntExtra("flag", 0);
        setContentView(R.layout.activity_publish);
        mContext = this;
        ButterKnife.bind(this);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);

        adapter = new GridViewShowPicAdapter(mContext, mResults);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    openGallery();
                    adapter.notifyDataSetChanged();
                }

            }
        });

        if (PUBLISH_FLAG == 0)
            mTvTitle.setText("发动态");
        else
            mTvTitle.setText("发帖");
    }


    @OnClick({R.id.iv_back, R.id.ll_add_location, R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_location:
                Intent intent = new Intent(mContext, GetLocationActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_back:
                finish();
                break;
            //发布按钮
            case R.id.btn_publish:
                checkData();
                break;

            default:

                break;
        }
    }

    //检查用户的输入
    private void checkData() {
        content = mEtInput.getText().toString().trim();
        if (TextUtils.isEmpty(content))
            Toast.makeText(mContext, "写点什么吧", Toast.LENGTH_SHORT).show();
        else if (mResults==null||mResults.size()==0){
            Toast.makeText(mContext, "必须添加图片", Toast.LENGTH_SHORT).show();
        }else {
            //上传数据
            compressImages();
        }
    }

    /**
     * 打开选择图片框
     */
    private void openGallery() {
        // start multiple photos selector
        Intent intent = new Intent(PublishActivity.this, ImagesSelectorActivity.class);
        // max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
        // min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
        // show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
        // pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
        // start the selector
        startActivityForResult(intent, REQUEST_CODE);
    }


    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                if (mResults.size() > 0) {
                    adapter = new GridViewShowPicAdapter(this, mResults);
                    mGridView.setAdapter(adapter);

                } else {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //发布动态
    private void CreateDynamic(Map<String, RequestBody> partList) {
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("content", content);
        Call<ResultEntity> call = api.CreateDynamic(params, partList);
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
                    //Log.i("hello", "onResponse: 成功");
                    deleteFile(new File(path));
                    finish();
                } else {
                    hideLoadingDialog();
                    //Log.i("hello", "onResponse: 失败");
                }

            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    private Map<String, RequestBody> filesToRequestBodyParts(List<File> files) {
        Map<String, RequestBody> parts = new HashMap<>();
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"), file);
            parts.put("img[]\"; filename=\"" + file.getName(), requestBody);
        }
        return parts;
    }

    //压缩图片
    private void compressImages() {
        showLoadingDialog("正在上传...");
        createOrGetFilePath("", this);
        Luban.with(this)
                .load(mResults)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                //.putGear(Luban.THIRD_GEAR)
                .setTargetDir(path)                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        Log.i("hello", "onStart: ");
                    }

                    @Override
                    public void onSuccess(File file) {
                        mNewResults.add(path + file.getName());
                        Log.i("hello", "onSuccess: " + path + file.getName());
                        if (mResults.size() == mNewResults.size()) {
                            List<File> fileList = new ArrayList<>();
                            File file1;
                            for (int i = 0; i < mNewResults.size(); i++) {
                                file1 = new File(mNewResults.get(i));
                                fileList.add(file1);
                            }
                            Map<String, RequestBody> bodyMap = filesToRequestBodyParts(fileList);
                            CreateDynamic(bodyMap);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        //Log.i("hello", "onError: 失败" + e.toString());
                    }
                }).launch();    //启动压缩


    }

    /**
     * 创建临时文件夹
     */
    public String createOrGetFilePath(String fileName, Context mContext) {
        saveCatalog = new File(path);
        if (!saveCatalog.exists()) {
            saveCatalog.mkdirs();
        }
        saveFile = new File(saveCatalog, fileName);
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            Toast.makeText(mContext, "创建文件失败，请检查SD是否有足够空间", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return saveFile.getAbsolutePath();
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
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    //隐藏
    private void hideLoadingDialog() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    //删除临时文件
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            //file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

}
