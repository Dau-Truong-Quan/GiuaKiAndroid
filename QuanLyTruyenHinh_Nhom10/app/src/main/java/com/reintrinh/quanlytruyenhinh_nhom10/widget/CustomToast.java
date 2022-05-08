package com.reintrinh.quanlytruyenhinh_nhom10.widget;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reintrinh.quanlytruyenhinh_nhom10.R;

public class CustomToast {

    public static Toast makeCustomToast(Activity activity, int image, String text) {
        Toast toast = new Toast(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_custom_toast, activity.findViewById(R.id.layout_custom_toast));

        ImageView imgImage = view.findViewById(R.id.img_image);
        TextView tvText = view.findViewById(R.id.tv_text);

        imgImage.setImageResource(image);
        tvText.setText(text);

        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

}
