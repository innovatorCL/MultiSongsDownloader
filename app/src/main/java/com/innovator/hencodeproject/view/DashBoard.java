package com.innovator.hencodeproject.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.innovator.hencodeproject.Utils;

/**
 * 刻度盘
 */
public class DashBoard extends View{

    private static final int ANGLE = 120;
    //半径
    private static final float RADIUS = Utils.dp2px(150);
    //指针长度
    private static final float LENGTH = Utils.dp2px(130);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path dash = new Path();
    PathDashPathEffect effect;

    public DashBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Utils.dp2px(2));
        dash.addRect(0,0,Utils.dp2px(2),Utils.dp2px(10), Path.Direction.CW); //顺时针
        Path arc = new Path();
        arc.addArc(getWidth() / 2 - RADIUS,getHeight() / 2 - RADIUS,getWidth() / 2 +RADIUS,
                getHeight() / 2 + RADIUS,90 + ANGLE / 2,360 - ANGLE);
        PathMeasure pathMeasure = new PathMeasure(arc,false);   //用来计算弧线周长
        effect = new PathDashPathEffect(dash,(pathMeasure.getLength() - Utils.dp2px(2)) / 20,0, PathDashPathEffect.Style.ROTATE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画弧线
        canvas.drawArc(getWidth() / 2 - RADIUS,getHeight() / 2 - RADIUS,getWidth() / 2 +RADIUS,
                getHeight() / 2 + RADIUS,90 + ANGLE / 2,360 - ANGLE,false,paint);

        //画刻度
        paint.setPathEffect(effect);
        canvas.drawArc(getWidth() / 2 - RADIUS,getHeight() / 2 - RADIUS,getWidth() / 2 +RADIUS,
                getHeight() / 2 + RADIUS,90 + ANGLE / 2,360 - ANGLE,false,paint);
        paint.setPathEffect(null);

        //画指针
        int currentAngle = getAngleFromMark(5);  //指针和 x 轴正向的夹角
        canvas.drawLine(getWidth() /2 ,getHeight() /2,
                (float) Math.cos(Math.toRadians(currentAngle)) * LENGTH + getWidth() /2, //相当于移动坐标系，相对距离
                (float) Math.sin(Math.toRadians(currentAngle)) * LENGTH + getHeight() /2,paint);
    }


    /**
     * 获取 @param mark 指向的角度（相对于 x 轴正向）
     * @param mark
     * @return
     */
    private int getAngleFromMark(int mark){
        return (int)(90 + (float)ANGLE / 2 + (360 - (float)ANGLE) / 20 * mark);
    }
}
