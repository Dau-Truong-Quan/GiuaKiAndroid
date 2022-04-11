package com.reintrinh.quanlytruyenhinh_nhom10.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.reintrinh.quanlytruyenhinh_nhom10.R;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    public static byte[] getByteArrayFromImageResource(Context context, int resourceId) {
        Resources res = context.getResources();
        Drawable drawable = res.getDrawable(resourceId);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
