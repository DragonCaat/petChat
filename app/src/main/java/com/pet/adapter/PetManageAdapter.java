package com.pet.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.AddPetActivity;
import com.pet.bean.Const;
import com.pet.bean.CutePetEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/7/5.
 * 宠物管理适配器
 */

public class PetManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int ITEM_TYPE_MAIN = 0;
    private int ITEM_TYPE_BOTTOM = 1;

    private Context mContext;

    private List<CutePetEntity> list;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    public PetManageAdapter(Context activity, List<CutePetEntity> list) {
        this.mContext = activity;
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cvPetHead;
        TextView tvPetName;
        TextView tvPetBirthday;
        TextView tvPetKind;
        TextView tvPetGender;
        TextView tvPetStatue;
        ImageView ivStautue;

        LinearLayout llDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            cvPetHead = itemView.findViewById(R.id.cv_pet_head);
            tvPetName = itemView.findViewById(R.id.tv_pet_name);
            tvPetBirthday = itemView.findViewById(R.id.tv_pet_birthday);
            tvPetKind = itemView.findViewById(R.id.tv_pet_kind);
            tvPetGender = itemView.findViewById(R.id.tv_pet_gender);
            tvPetStatue = itemView.findViewById(R.id.tv_pet_statue);
            ivStautue = itemView.findViewById(R.id.iv_statues);

            llDelete = itemView.findViewById(R.id.ll_delete);
        }
    }

    //添加宠物按钮的布局
    static class BottomViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAddPet;

        public BottomViewHolder(View v) {
            super(v);
            ivAddPet = v.findViewById(R.id.iv_add);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_MAIN) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pet_manage_item, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pet_manager_add, parent, false);
            return new BottomViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final CutePetEntity entity = list.get(position);

            if (entity.getPet_gender() == 1)
                ((ViewHolder) holder).tvPetGender.setText("女");
            else
                ((ViewHolder) holder).tvPetGender.setText("男");

            ((ViewHolder) holder).tvPetKind.setText(entity.getPet_kind());
            ((ViewHolder) holder).tvPetBirthday.setText(entity.getPet_brith());
            ((ViewHolder) holder).tvPetName.setText(entity.getPet_name());
            ((ViewHolder) holder).tvPetStatue.setText(entity.getPet_status());

            //0：未认证 1：已认证
            if (entity.getPermisstion_to_access() == 0)
                ((ViewHolder) holder).ivStautue.setImageResource(R.mipmap.check_false);
            else
                ((ViewHolder) holder).ivStautue.setImageResource(R.mipmap.check_true);

            //宠物图片
            Glide.with(mContext)
                    .load(Const.PIC_URL + entity.getPet_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.take_photo_loading)
                    .into(((ViewHolder) holder).cvPetHead);
            //删除宠物
            ((ViewHolder) holder).llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showDialog(entity.getPet_id(), position);
                }
            });

            //添加宠物
        } else {
            ((BottomViewHolder) holder).ivAddPet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AddPetActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position < list.size())
            return ITEM_TYPE_MAIN;
        else
            return ITEM_TYPE_BOTTOM;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }


    private void showDialog(final int id, final int position) {
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //    设置Title的图标
        builder.setIcon(R.mipmap.check_false);
        //    设置Title的内容
        builder.setTitle("温馨提示");
        //    设置Content来显示一个信息
        builder.setMessage("确定删除吗？删除后不可恢复");
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePet(id,position);
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //    显示出该对话框
        builder.show();
    }

    //删除宠物
    private void deletePet(int id, final int position) {
        user_id = PreferencesUtils.getInt(mContext, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(mContext, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(mContext, Const.MOBILE_PHONE);
        final ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("pet_id", id);
        Call<ResultEntity> call = api.deletePet(params);
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
                    list.remove(position);
                    notifyDataSetChanged();
                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

}