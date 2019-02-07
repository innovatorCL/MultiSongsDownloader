package com.innovator.hencodeproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.innovator.hencodeproject.Utils;

/**
 * 环状图
 */
public class SportsView extends View {

    //环的宽度
    private static final float RING_WIDTH = Utils.dp2px(20);
    //半径
    private static final float RADIUS = Utils.dp2px(150);

    private static final int CIRCLE_COLOR = Color.parseColor("#90A4AE");
    private static final int HIGHTIGHT_COLOR = Color.parseColor("#FF4081");

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    Rect rect = new Rect();

    Paint.FontMetrics fontMetrics = new Paint.FontMetrics();

    {
        //字体大小
        paint.setTextSize(Utils.dp2px(100));
        //字体
        paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Quicksand-Regular.ttf"));
        //字体方向，横向居中、纵向居中
        paint.setTextAlign(Paint.Align.CENTER);
        //获取文字的 ascent 和 dscent
        paint.getFontMetrics(fontMetrics);

    }

    public SportsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制环
        paint.setStyle(Paint.Style.STROKE);  //描边
        paint.setColor(CIRCLE_COLOR);
        //环的宽度大小决定了看起来是环还是弧线
        paint.setStrokeWidth(RING_WIDTH);
        canvas.drawCircle(getWidth()/2,getHeight()/2,RADIUS,paint);

        //绘制进度条
        paint.setColor(HIGHTIGHT_COLOR);
        //封边是圆形
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(getWidth() /2 - RADIUS,getHeight() / 2 - RADIUS,
                getWidth() /2 + RADIUS,getHeight() /2 + RADIUS,
                -90,225,false,paint);

        //绘制文字
        paint.setStyle(Paint.Style.FILL); //填充
        paint.setTextAlign(Paint.Align.CENTER);

        //方法一：将文字的左上右下坐标映射到矩形中，为了找出 BaseLine，
//        paint.getTextBounds("abab",0,"abab".length(),rect);
//        int offset = (rect.top + rect .bottom) / 2;

        //方法二：获取文字的 ascent 和 dscent，从而获取偏移量
        float offset = (fontMetrics.ascent + fontMetrics .descent) / 2;
        //处理 y 轴上的偏移
        canvas.drawText("aaaa",getWidth()/2,getHeight()/2 - offset,paint);


        //绘制文字
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("aaaa",0,200,paint);
    }
}
