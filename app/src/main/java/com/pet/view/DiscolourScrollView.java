package com.pet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by dragon on 2018/5/24.
 */

public class DiscolourScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener;

    public DiscolourScrollView(Context context) {
        super(context);
    }

    public DiscolourScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscolourScrollView(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs, defStyle);
    }


    //接口回调
    public interface ScrollViewListener {

        void onScrollChanged(DiscolourScrollView scrollView, int x, int y, int oldx, int oldy);

    }
    
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}
