package com.innovator.hencodeproject.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.innovator.hencodeproject.R;
import com.innovator.hencodeproject.Utils;

/**
 * 头像
 */
public class AvatarView extends View {

    //头像宽度
    private static final float WIDTH = Utils.dp2px(300);

    private static final float PADDING = Utils.dp2px(25);

    //边框大小
    private static final float EDGE_PADDING = Utils.dp2px(10);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    Bitmap bitmap;

    Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    RectF savedarea = new RectF();

    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        bitmap = getAvatar((int) WIDTH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        savedarea.set(PADDING,PADDING,PADDING + WIDTH,PADDING + WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.parseColor("#4387cd"));
        //底层作为边框层
        canvas.drawOval(savedarea,paint);
        paint.setColor(Color.parseColor("#000000"));

        //离屏缓冲
        int saved = canvas.saveLayer(savedarea,paint);
        //画椭圆，这个圆就是我们要展现的内容的范围
        canvas.drawOval(PADDING+EDGE_PADDING,PADDING+EDGE_PADDING,
                PADDING + WIDTH - EDGE_PADDING,PADDING + WIDTH - EDGE_PADDING,paint);
        //设置图形叠加的模式
        paint.setXfermode(xfermode);
        canvas.drawBitmap(bitmap,PADDING,PADDING, paint);

        //恢复Xfermode
        paint.setXfermode(null);
        canvas.restoreToCount(saved);
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
