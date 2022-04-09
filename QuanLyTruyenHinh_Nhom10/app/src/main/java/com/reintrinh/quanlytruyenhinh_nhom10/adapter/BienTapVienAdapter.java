package com.reintrinh.quanlytruyenhinh_nhom10.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;

import java.util.ArrayList;
import java.util.Random;

public class BienTapVienAdapter extends ArrayAdapter<BienTapVien> {

    Context context;
    int resource;
    ArrayList<BienTapVien> data;
    ArrayList<BienTapVien> dataSearch = new ArrayList<>();

    public BienTapVienAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BienTapVien> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        dataSearch.addAll(data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public int random() {
        int min = 0;
        int max = 255;
        Random generator = new Random();
        return generator.nextInt((max - min) + 1) + min;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvHoTen = convertView.findViewById(R.id.tvHoTen);
        TextView tvSDT = convertView.findViewById(R.id.tvSDT);
        ImageView ivIcon = convertView.findViewById(R.id.ivIcon);

        BienTapVien btv = data.get(position);
        tvHoTen.setText(btv.getHoTen());
        tvSDT.setText(btv.getSdt());

        ivIcon.setBackgroundColor(Color.rgb(random(), random(), random()));

        return convertView;
    }

    public void filter(String text)
    {
        data.clear();
        text = text.trim().toLowerCase();

        if(text.length() == 0)
            data.addAll(dataSearch);
        else {
            for (BienTapVien btv : dataSearch) {
                if (btv.getHoTen().toLowerCase().contains(text)) {
                    data.add(btv);
                    Toast.makeText(context, btv.getHoTen(), Toast.LENGTH_LONG).show();
                }
            }
        }
        notifyDataSetChanged();
    }
}
