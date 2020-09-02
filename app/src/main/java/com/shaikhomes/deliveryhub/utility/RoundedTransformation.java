package com.shaikhomes.deliveryhub.utility;


import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;


public class RoundedTransformation implements com.squareup.picasso.Transformation{

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        //paint.setStyle(Paint.Style.STROKE);
        /*paint.setColor(Color.parseColor("#00bbff"));
        paint.setStrokeWidth(0.2f);*/

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);


        //border code
       /* paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor());
        paint.setStrokeWidth(borderwidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderwidth / 2, paint);*/

        squaredBitmap.recycle();



        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
