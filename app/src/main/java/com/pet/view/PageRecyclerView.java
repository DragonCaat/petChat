package com.pet.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.FillFosterActivity;
import com.pet.activity.ShowUserActivity;
import com.pet.activity.WalkDogInfoActivity;
import com.pet.adapter.PetsIconAdapter;
import com.pet.bean.MapPetInfo;

import java.util.List;

import cn.smssdk.gui.util.Const;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dragon on 2018/6/29.
 * 带指示器的recycleView
 */

public class PageRecyclerView extends RecyclerView {
    private Context mContext = null;

    private PageAdapter myAdapter = null;

    private int shortestDistance; // 超过此距离的滑动才有效
    private float slideDistance = 0; // 滑动的距离
    private float scrollX = 0; // X轴当前的位置

    private int spanRow = 1; // 行数
    private int spanColumn = 2; // 每页列数
    private int totalPage = 0; // 总页数
    private int currentPage = 1; // 当前页

    private int pageMargin = 0; // 页间距

    private PageIndicatorView mIndicatorView = null; // 指示器布局

    private int realPosition = 0; // 真正的位置（从上到下从左到右的排列方式变换成从左到右从上到下的排列方式后的位置）

    /* * 0: 停止滚动且手指移开; 1: 开始滚动; 2: 手指做了抛的动作（手指离开屏幕前，用力滑了一下） */
    private int scrollState = 0; // 滚动状态

    public PageRecyclerView(Context context) {
        this(context, null);
    }

    public PageRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        defaultInit(context);
    }

    // 默认初始化
    private void defaultInit(Context context) {
        this.mContext = context;
        setLayoutManager(new LinearLayoutManager(
                mContext, LinearLayoutManager.HORIZONTAL, false));
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * 设置行数和每页列数 * * @param spanRow 行数，<=0表示使用默认的行数 * @param spanColumn 每页列数，<=0表示使用默认每页列数
     */
    public void setPageSize(int spanRow, int spanColumn) {
        this.spanRow = spanRow <= 0 ? this.spanRow : spanRow;
        this.spanColumn = spanColumn <= 0 ? this.spanColumn : spanColumn;
        setLayoutManager(new LinearLayoutManager(
                mContext, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * 设置页间距 * * @param pageMargin 间距(px)
     */
    public void setPageMargin(int pageMargin) {
        this.pageMargin = pageMargin;
    }

    /**
     * 设置指示器 * * @param indicatorView 指示器布局
     */
    public void setIndicator(PageIndicatorView indicatorView) {
        this.mIndicatorView = indicatorView;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        shortestDistance = getMeasuredWidth() / 3;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        this.myAdapter = (PageAdapter) adapter;
        update();
    }

    // 更新页码指示器和相关数据
    private void update() {
        // 计算总页数
        int temp = ((int) Math.ceil(myAdapter.pet_info.size() / (double) (spanRow * spanColumn)));
        if (temp != totalPage) {
            mIndicatorView.initIndicator(temp);
            // 页码减少且当前页为最后一页
            if (temp < totalPage && currentPage == totalPage) {
                currentPage = temp;
                // 执行滚动
                smoothScrollBy(-getWidth(), 0);
            }
            mIndicatorView.setSelectedPage(currentPage - 1);
            totalPage = temp;
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case 2:
                scrollState = 2;
                break;
            case 1:
                scrollState = 1;
                break;
            case 0:
                if (slideDistance == 0) {
                    break;
                }
                scrollState = 0;
                if (slideDistance < 0) { // 上页
                    currentPage = (int) Math.ceil(scrollX / getWidth());
                    if (currentPage * getWidth() - scrollX < shortestDistance) {
                        currentPage += 1;
                    }
                } else { // 下页
                    currentPage = (int) Math.ceil(scrollX / getWidth()) + 1;
                    if (currentPage <= totalPage) {
                        if (scrollX - (currentPage - 2) * getWidth() < shortestDistance) {
                            // 如果这一页滑出距离不足，则定位到前一页
                            currentPage -= 1;
                        }
                    } else {
                        currentPage = totalPage;
                    }
                }
                // 执行自动滚动
                smoothScrollBy((int) ((currentPage - 1) * getWidth() - scrollX), 0);
                // 修改指示器选中项
                mIndicatorView.setSelectedPage(currentPage - 1);
                slideDistance = 0;
                break;
        }
        super.onScrollStateChanged(state);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        scrollX += dx;
        if (scrollState == 1) {
            slideDistance += dx;
        }

        super.onScrolled(dx, dy);
    }

    /**
     * 数据适配器
     */
    public class PageAdapter extends RecyclerView.Adapter<PageAdapter.ViewHolder> {
        private List<MapPetInfo.PetInfoBean> pet_info;
        private int itemCount = 0;
        private Context mContext;

        private double distance;
        int fans_count;

        /**
         * 实例化适配器 * * @param data * @param callBack
         */
        public PageAdapter(List<MapPetInfo.PetInfoBean> pet_info, Context mContext, double distance) {
            this.pet_info = pet_info;
            itemCount = pet_info.size(); //+ spanRow * spanColumn;
            this.mContext = mContext;
            this.distance = distance;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView gv_text;
            TextView tvFoster;
            CircleImageView cvHead;
            ImageView ivPetHead;
            TextView tvBirthday;
            TextView tvKind;
            TextView tvFansCount;

            LinearLayout gridView;

            TextView tvDistance;

            //            CircleImageView head1;
//            CircleImageView head2;
//            CircleImageView head3;
//            CircleImageView head4;
//            CircleImageView head5;
            ImageView ivLove1;
            ImageView ivLove2;
            ImageView ivLove3;
            ImageView ivLove4;
            ImageView ivLove5;

            Button btnWalkTogether;

            public ViewHolder(View itemView) {
                super(itemView);
                gv_text = itemView.findViewById(R.id.tv);
                tvFoster = itemView.findViewById(R.id.tv_foster);
                cvHead = itemView.findViewById(R.id.cv_user_head);
                ivPetHead = itemView.findViewById(R.id.iv_pet_img);
                tvBirthday = itemView.findViewById(R.id.tv_birthday);
                tvKind = itemView.findViewById(R.id.tv_pet_kind);

                tvFansCount = itemView.findViewById(R.id.tv_fans_count);
                gridView = itemView.findViewById(R.id.gv_pets);

                tvDistance = itemView.findViewById(R.id.tv_distance);

                ivLove1 = itemView.findViewById(R.id.iv_love1);
                ivLove2 = itemView.findViewById(R.id.iv_love2);
                ivLove3 = itemView.findViewById(R.id.iv_love3);
                ivLove4 = itemView.findViewById(R.id.iv_love4);
                ivLove5 = itemView.findViewById(R.id.iv_love5);

//                head1 = itemView.findViewById(R.id.head1);
//                head2 = itemView.findViewById(R.id.head2);
//                head3 = itemView.findViewById(R.id.head3);
//                head4 = itemView.findViewById(R.id.head4);
//                head5 = itemView.findViewById(R.id.head5);

                btnWalkTogether = itemView.findViewById(R.id.btn_together);
            }
        }

        @Override
        public PageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_show_user_item, parent, false);
            return new ViewHolder(view);

        }

        @SuppressLint({"SetTextI18n", "RtlHardcoded"})
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            countRealPosition(position);
            final MapPetInfo.PetInfoBean petInfoBean = pet_info.get(position);

            holder.gv_text.setText(petInfoBean.getPet_name());
            holder.tvBirthday.setText(petInfoBean.getPet_brith());
            holder.tvKind.setText(petInfoBean.getPet_kind());
            holder.tvFansCount.setText("被约次数:  " + petInfoBean.getAppointment_count());
            holder.tvDistance.setText("距离您 " + distance + " km");

            //用户头像
            Glide.with(mContext)
                    .load(com.pet.bean.Const.PIC_URL + petInfoBean.getUser_icon())
                    .dontAnimate()
                    .into(holder.cvHead);
            holder.cvHead.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转展示用户信息的页面
                    Intent intent = new Intent(mContext, ShowUserActivity.class);
                    intent.putExtra("id", petInfoBean.getUser_id());
                    mContext.startActivity(intent);
                }
            });

            holder.btnWalkTogether.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转展示遛狗信息填写的页面
                    Intent intent = new Intent(mContext, WalkDogInfoActivity.class);
                    intent.putExtra("id", petInfoBean.getUser_id());
                    intent.putExtra("head", petInfoBean.getPet_icon());
                    intent.putExtra("pet_id", petInfoBean.getPet_id());
                    mContext.startActivity(intent);
                }
            });


            Glide.with(mContext)
                    .load(com.pet.bean.Const.PIC_URL + petInfoBean.getPet_icon())
                    .placeholder(R.mipmap.default_head)
                    .dontAnimate()
                    .into(holder.ivPetHead);
            //寄养
            holder.tvFoster.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, FillFosterActivity.class);
                    mContext.startActivity(intent);
                }
            });


            List<MapPetInfo.PetInfoBean.UserIconsBean> user_icons = petInfoBean.getUser_icons();
            if (user_icons.size() > 0) {
                CircleImageView imageView;
                for (int i = 0; i < user_icons.size(); i++) {
                    imageView = new CircleImageView(mContext);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    param.leftMargin = 10;
                    //必须要加上这句，setMargin才会起作用，而且此句还必须在setMargins下面
                    param.gravity = Gravity.LEFT;
                    imageView.setLayoutParams(param);
                    Glide.with(mContext)
                            .load(com.pet.bean.Const.PIC_URL + user_icons.get(i).getUser_icon())
                            .dontAnimate()
                            .fitCenter()
                            .into(imageView);

                    holder.gridView.addView(imageView, 60, 60);
                }
                //表示很无语的界面
                fans_count = petInfoBean.getFans_count();
                if (fans_count < 1) {
                    showLove(holder.ivLove1, false);
                    showLove(holder.ivLove2, false);
                    showLove(holder.ivLove3, false);
                    showLove(holder.ivLove4, false);
                    showLove(holder.ivLove5, false);
                } else if (fans_count < 10 && fans_count > 0) {
                    showLove(holder.ivLove1, true);
                    showLove(holder.ivLove2, false);
                    showLove(holder.ivLove3, false);
                    showLove(holder.ivLove4, false);
                    showLove(holder.ivLove5, false);
                } else if (fans_count < 30 && fans_count > 9) {
                    showLove(holder.ivLove1, true);
                    showLove(holder.ivLove2, true);
                    showLove(holder.ivLove3, false);
                    showLove(holder.ivLove4, false);
                    showLove(holder.ivLove5, false);
                } else if (fans_count < 70 && fans_count > 29) {
                    showLove(holder.ivLove1, true);
                    showLove(holder.ivLove2, true);
                    showLove(holder.ivLove3, true);
                    showLove(holder.ivLove4, false);
                    showLove(holder.ivLove5, false);
                } else if (fans_count < 100 && fans_count > 69) {
                    showLove(holder.ivLove1, true);
                    showLove(holder.ivLove2, true);
                    showLove(holder.ivLove3, true);
                    showLove(holder.ivLove4, true);
                    showLove(holder.ivLove5, false);
                } else {
                    showLove(holder.ivLove1, true);
                    showLove(holder.ivLove2, true);
                    showLove(holder.ivLove3, true);
                    showLove(holder.ivLove4, true);
                    showLove(holder.ivLove5, true);
                }

//                if (user_icons.size() == 1) {
//                    holder.head1.setVisibility(VISIBLE);
//                    Glide.with(mContext)
//                            .load(com.pet.bean.Const.PIC_URL + user_icons.get(0).getUser_icon())
//                            .dontAnimate()
//                            .into(holder.head1);
//                }else if (user_icons.size() == 2){
//                    holder.head1.setVisibility(VISIBLE);
//                    holder.head2.setVisibility(VISIBLE);
//                    Glide.with(mContext)
//                            .load(com.pet.bean.Const.PIC_URL + user_icons.get(0).getUser_icon())
//                            .dontAnimate()
//                            .into(holder.head1);
//                    Glide.with(mContext)
//                            .load(com.pet.bean.Const.PIC_URL + user_icons.get(1).getUser_icon())
//                            .dontAnimate()
//                            .into(holder.head2);
//                }
            }
        }

        private void showLove(ImageView imageView, boolean status) {
            if (status)
                imageView.setImageResource(R.mipmap.love_full);
            else
                imageView.setImageResource(R.mipmap.love);
        }


        @Override
        public int getItemCount() {
            return itemCount;
        }

        private void countRealPosition(int position) {
            // 为了使Item从左到右从上到下排列，需要position的值
            realPosition = position;
        }

        /**
         * 删除Item * * @param position 位置
         */
        public void remove(int position) {
            if (position < pet_info.size()) {
                // 删除数据
                pet_info.remove(position);
                itemCount--;
                // 删除Item
                notifyItemRemoved(position);
                // 更新界面上发生改变的Item
                notifyItemRangeChanged((currentPage - 1) * spanRow * spanColumn, currentPage * spanRow * spanColumn);
                // 更新页码指示器
                update();
            }
        }
    }
}
