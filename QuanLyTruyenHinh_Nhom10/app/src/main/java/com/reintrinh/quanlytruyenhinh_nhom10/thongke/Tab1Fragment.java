package com.reintrinh.quanlytruyenhinh_nhom10.thongke;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.ThongKeAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongKe;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment {

    QuanLyTruyenHinhHelper db;
    View view;
    BarChart chartTheLoai;

    ListView lvThongKeTL;
    ArrayList<ThongKe> data = new ArrayList<>();;
    ThongKeAdapter thongKeAdapter;

    public Tab1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab1, container, false);

        setControl();
        getData();

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        BarEntry barEntry;
        for (int i = 0; i < data.size(); i++) {
            barEntry = new BarEntry(i, data.get(i).getValue());
            barEntries.add(barEntry);
        }

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            labels.add(data.get(i).getLabel());
        }

        XAxis xAxis = chartTheLoai.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setAxisMinimum(0f);
        //xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Thể loại");

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        chartTheLoai.setData(new BarData(barDataSet));
        chartTheLoai.animateY(1000);
        chartTheLoai.getDescription().setText("Top 5 thời lượng phát sóng theo thể loại");
        chartTheLoai.getDescription().setTextColor(Color.BLUE);

        thongKeAdapter = new ThongKeAdapter(getContext(), R.layout.list_layout_thongke, data);
        lvThongKeTL.setAdapter(thongKeAdapter);

        return view;
    }

    public void getData() {
        db = new QuanLyTruyenHinhHelper(getContext());
        data.clear();
        String sql = "SELECT tl.TenTL, SUM(tt.ThoiLuong) ThoiLuongPhatSong\n" +
                    "FROM ThongTinPhatSong tt INNER JOIN ChuongTrinh ct INNER JOIN TheLoai tl\n" +
                    "ON tt.MaCT = ct.MaCT and ct.MaTL = tl.MaTL\n" +
                    "GROUP BY tl.TenTL";
        Cursor cursor = db.getData(sql);

        ThongKe tk;
        while (cursor.moveToNext()) {
            tk = new ThongKe();
            tk.setLabel(cursor.getString(0));
            tk.setValue(cursor.getInt(1));
            data.add(tk);
        }
    }

    private void setControl() {
        lvThongKeTL = view.findViewById(R.id.lvThongKeTL);
        chartTheLoai = view.findViewById(R.id.chartTheLoai);
    }
}