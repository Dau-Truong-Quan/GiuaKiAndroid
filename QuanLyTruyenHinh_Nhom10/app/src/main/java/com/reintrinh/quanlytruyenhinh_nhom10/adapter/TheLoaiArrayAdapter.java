package com.reintrinh.quanlytruyenhinh_nhom10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;

import java.util.List;

public class TheLoaiArrayAdapter extends ArrayAdapter<TheLoai> {
    Context context;
    int resource;
    List<TheLoai> data;

    public TheLoaiArrayAdapter(@NonNull Context context, int resource, @NonNull List<TheLoai> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.data = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tenTheLoai = convertView.findViewById(R.id.txtName);
        ImageView ivIcon = convertView.findViewById(R.id.img_the_loai);

        TheLoai btv = data.get(position);
        tenTheLoai.setText(btv.getTenTL());

        byte[] hinhAnh = btv.getHinhAnh();
        ivIcon.setImageBitmap(ImageUtil.getBitmapFromByteArray(hinhAnh));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tenTheLoai = convertView.findViewById(R.id.txtName);
        ImageView ivIcon = convertView.findViewById(R.id.img_the_loai);

        TheLoai btv = data.get(position);
        tenTheLoai.setText(btv.getTenTL());

        byte[] hinhAnh = btv.getHinhAnh();
        ivIcon.setImageBitmap(ImageUtil.getBitmapFromByteArray(hinhAnh));

        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
