package com.pet.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dragon on 2018/7/12.
 * 显示宠物饭量百分比的圆环
 */

public class CircleNumberProgress extends View {

    /** 进度条画笔的宽度（dp） */
    private int paintProgressWidth = 3;

    /** 文字百分比的字体大小（sp） */
    private int paintTextSize = 10;

    /** 未完成进度条的颜色 */
    private int paintUndoneColor = 0x80ffffff;

    /** 已完成进度条的颜色 */
    private int paintDoneColor = 0xfffed943;

    /** 百分比文字的颜色 */
    private int paintTextColor = 0xffffffff;

    /** 设置进度条画笔的宽度(px) */
    private int paintProgressWidthPx;

    /** 文字画笔的尺寸(px) */
    private int paintTextSizePx;
    /** Context上下文环境 */
    private Context context;

    /** 调用者设置的进程 0 - 100 */
    private int progress;

    /** 画未完成进度圆弧的画笔 */
    private Paint paintUndone = new Paint();

    /** 画已经完成进度条的画笔 */
    private Paint paintDone = new Paint();

    /** 画文字的画笔 */
    private Paint paintText = new Paint();

    /** 包围进度条圆弧的矩形 */
    private RectF rectF = new RectF();

    /** 包围文字所在路径圆弧的矩形，比上一个矩形略小 */
    private RectF rectF2 = new RectF();

    /** 进度文字所在的路径 */
    private Path path = new Path();

    /** 文字所在路径圆弧的半径 */
    private int radiusText;

    /** 是否进行过了测量 */
    private boolean isMeasured = false;

    public CircleNumberProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // 构造器中初始化数据
        initData();
    }

    /** 初始化数据 */
    private void initData() {

        // 设置进度条画笔的宽度
        paintProgressWidthPx = Utils.dip2px(context, paintProgressWidth);

        // 设置文字画笔的尺寸(px)
        paintTextSizePx = Utils.sp2px(context, paintTextSize);

        // 未完成进度圆环的画笔的属性
        paintUndone.setColor(paintUndoneColor);
        paintUndone.setStrokeWidth(paintProgressWidthPx);
        paintUndone.setAntiAlias(true);
        paintUndone.setStyle(Paint.Style.STROKE);

        // 已经完成进度条的画笔的属性
        paintDone.setColor(paintDoneColor);
        paintDone.setStrokeWidth(paintProgressWidthPx);
        paintDone.setAntiAlias(true);
        paintDone.setStyle(Paint.Style.STROKE);

        // 文字的画笔的属性
        paintText.setColor(paintTextColor);
        paintText.setTextSize(paintTextSizePx);
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.STROKE);
        paintText.setTypeface(Typeface.DEFAULT_BOLD);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            getWidthAndHeight();
            isMeasured = true;
        }
    }

    /** 得到视图等的高度宽度尺寸数据 */
    private void getWidthAndHeight() {

        // 得到自定义视图的高度
        int viewHeight;

        // 得到自定义视图的宽度
        int viewWidth;

        // 得到自定义视图的X轴中心点
        int viewCenterX;

        // 得到自定义视图的Y轴中心点
        int viewCenterY;

        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        viewCenterX = viewWidth / 2;
        viewCenterY = viewHeight / 2;

        // 取本View长宽较小者的一半为整个圆环部分（包括圆环和文字）最外侧的半径
        int minLenth = viewHeight > viewWidth ? viewWidth / 2 : viewHeight / 2;

        // 比较文字高度和圆环宽度，如果文字高度较大，那么文字将突破圆环，否则，圆环会把文字包裹在内部
        Rect rect = new Rect();
        paintText.getTextBounds("100%", 0, "100%".length(), rect);
        int textHeight = rect.height();

        // 得到圆环的中间半径（外径和内径平均值）
        int radiusArc = minLenth - (paintProgressWidthPx > textHeight ? paintProgressWidthPx / 4 : textHeight / 4);
        rectF.left = viewCenterX - radiusArc;
        rectF.top = viewCenterY - radiusArc;
        rectF.right = viewCenterX + radiusArc;
        rectF.bottom = viewCenterY + radiusArc;

        // 文字所依赖路径圆弧的半径
        radiusText = radiusArc - textHeight / 2;
        rectF2.left = viewCenterX - radiusText;
        rectF2.top = viewCenterY - radiusText;
        rectF2.right = viewCenterX + radiusText;
        rectF2.bottom = viewCenterY + radiusText;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画未完成进度的圆环
        canvas.drawArc(rectF, 0, 360, false, paintUndone);

        // 画已经完成进度的圆弧 从-90度开始，即从圆环顶部开始
        canvas.drawArc(rectF, -90, progress / 100.0f * 360, false, paintDone);

        // 为文字所在路径添加一段圆弧轨迹，进度为0%-9%时应该最短，进度为10%-99%时应该边长，进度为100%时应该最长
        // 这样才能保证文字和圆弧的进度一致，不会出现超前或者滞后的情况

        // 要画的文字
        String text = progress + "%";

        // 存储字符所有字符所占宽度的数组
        float[] widths = new float[text.length()];

        // 得到所有字符所占的宽度
        paintText.getTextWidths(text, 0, text.length(), widths);

        // 所有字符所占宽度之和
        float textWidth = 0;
        for (float f : widths) {
            textWidth += f;
        }

        // 根据长度得到路径对应的扫过的角度
        // width = sweepAngle * 2 * π * R / 360 ; sweepAngle = width * 360 / 2 /
        // π / R
        float sweepAngle = (float) (textWidth * 360 / 2 / Math.PI / radiusText);

        // 添加路径
        path.addArc(rectF2, progress * 3.6f - 90.0f - sweepAngle / 2.0f, sweepAngle);

        // 绘制进度的文字
        canvas.drawTextOnPath(text, path, 0, 0, paintText);

        // 重置路径
        path.reset();
    }

    /**
     * @param progress 外部传进来的当前进度,强制重绘
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}