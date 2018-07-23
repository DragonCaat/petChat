package com.pet.ninelayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.pet.R;
import com.pet.view.RatioImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 一般项目就实现NineGridLayout类即可，如果没有特殊需求，不要改动NineGridLayout类
 */
public class NineGridTestLayout extends NineGridLayout {

    private Context context;
    private int itemPosition;
    private OnItemPictureClickListener listener;

    public NineGridTestLayout(Context context) {
        this(context,null);
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    protected void displayImage(int position, RatioImageView imageView, String url) {
        if(context!=null){
            Picasso.with(context).load(url)
                    .placeholder(R.mipmap.take_photo_loading)
                    //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//不缓存
                    .resize(dip2px(250),dip2px(250))
                    .centerCrop()
                    .config(Bitmap.Config.RGB_565)//对于不透明的图片可以使用RGB_565来优化内存
                    .into(imageView);
            imageView.setTag(Utils.getNameByPosition(itemPosition,position));
            imageView.setTransitionName(Utils.getNameByPosition(itemPosition,position));
        }
    }

    @Override
    protected void onClickImage(int imageIndex, String url, List<String> urlList, ImageView imageView) {
        listener.onItemPictureClick(itemPosition,imageIndex,url,urlList,imageView);
    }


    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public void setListener(OnItemPictureClickListener listener) {
        this.listener = listener;
    }


    public  int dip2px( float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
