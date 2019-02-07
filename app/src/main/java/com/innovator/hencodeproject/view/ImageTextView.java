package com.innovator.hencodeproject.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.innovator.hencodeproject.R;
import com.innovator.hencodeproject.Utils;

public class ImageTextView extends View {

    //环的宽度
    private static final float RING_WIDTH = Utils.dp2px(20);
    //半径
    private static final float RADIUS = Utils.dp2px(150);

    private static final int CIRCLE_COLOR = Color.parseColor("#90A4AE");
    private static final int HIGHTIGHT_COLOR = Color.parseColor("#FF4081");

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;

    {
        //字体大小
        paint.setTextSize(Utils.dp2px(12));
        bitmap = getAvatar((int)Utils.dp2px(100));
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap,getWidth() - (int)Utils.dp2px(100),50,paint);

    }

    private Bitmap getAvatar(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //获取采样率
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.avatar_rengwuxian, options);
        options.inJustDecodeBounds = false;
        //设置位图的像素密度，即每英寸有多少个像素
        options.inDensity = options.outWidth;
        //设置绘制位图的屏幕密度,与inScale和inDesity一起使用,来对位图进行放缩.
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.avatar_rengwuxian, options);
    }
}
