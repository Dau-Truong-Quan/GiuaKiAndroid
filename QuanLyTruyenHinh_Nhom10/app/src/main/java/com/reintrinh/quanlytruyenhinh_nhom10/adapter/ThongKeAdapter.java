package com.reintrinh.quanlytruyenhinh_nhom10.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongKe;

import java.util.ArrayList;

public class ThongKeAdapter extends ArrayAdapter<ThongKe> {

    Context context;
    int resource;
    ArrayList<ThongKe> data;

    public ThongKeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ThongKe> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvLabel = convertView.findViewById(R.id.tvLabel);
        TextView tvValue = convertView.findViewById(R.id.tvValue);

        ThongKe thongKe = data.get(position);
        tvLabel.setText(thongKe.getLabel());
        tvValue.setText(String.valueOf(thongKe.getValue()));

        return convertView;
    }
}
