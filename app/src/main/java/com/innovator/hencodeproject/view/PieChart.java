package com.innovator.hencodeproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.innovator.hencodeproject.Utils;

/**
 * 饼图
 */
public class PieChart extends View {

    private static final int ANGLE = 120;
    //半径
    private static final float RADIUS = Utils.dp2px(150);
    //指针长度
    private static final float LENGTH = Utils.dp2px(20);
    //拉出来的扇形 index
    private static final int PULLED_OUT_INDEX  = 2;


    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    RectF bounds = new RectF();

    //每个扇形占的角度
    int[] angles = {60,100,120,80};
    int[] colors = {Color.parseColor("#2979FF"),Color.parseColor("#C2185B"),
            Color.parseColor("#009688"),Color.parseColor("#FF8F00")};

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * onLayout 的回调
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //外接矩形的范围
        bounds.set((int)(getWidth() /2 - RADIUS),(int)(getHeight() /2 - RADIUS),(int)(getWidth() /2 + RADIUS),
                (int)(getHeight() / 2 + RADIUS));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int currentAngle = 0;
        for(int i = 0;i<angles.length;i++){
            paint.setColor(colors[i]);
            canvas.save();
            if( i == PULLED_OUT_INDEX){
                //将画布偏移一下
                canvas.translate((float) Math.cos(Math.toRadians(currentAngle + angles[i] /2 )) * LENGTH ,
                        (float) Math.sin(Math.toRadians(currentAngle + angles[i] /2)) * LENGTH);
            }

            //画扇形
            canvas.drawArc(bounds,currentAngle, angles[i],true,paint);
            canvas.restore();
            //移到下一个扇形的起始弧度
            currentAngle += angles[i];
        }

    }
}
