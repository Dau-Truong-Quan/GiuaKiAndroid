package com.reintrinh.quanlytruyenhinh_nhom10.thongke;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

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

public class Tab2Fragment extends Fragment {

    QuanLyTruyenHinhHelper db;
    View view;
    BarChart chartChuongTrinh;

    ListView lvThongKeCT;
    ArrayList<ThongKe> data = new ArrayList<>();;
    ThongKeAdapter thongKeAdapter;

    public Tab2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab2, container, false);

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

        XAxis xAxis = chartChuongTrinh.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setAxisMinimum(0f);
        //xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Thể loại");

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        chartChuongTrinh.setData(new BarData(barDataSet));
        chartChuongTrinh.animateY(1000);
        chartChuongTrinh.getDescription().setText("Top 5 thời lượng phát sóng theo chương trình");
        chartChuongTrinh.getDescription().setTextColor(Color.BLUE);

        thongKeAdapter = new ThongKeAdapter(getContext(), R.layout.list_layout_thongke, data);
        lvThongKeCT.setAdapter(thongKeAdapter);

        return view;
    }

    public void getData() {
        db = new QuanLyTruyenHinhHelper(getContext());
        data.clear();
        String sql = "SELECT ct.TenCT, SUM(tt.ThoiLuong) ThoiLuongPhatSong\n" +
                    "FROM ThongTinPhatSong tt INNER JOIN ChuongTrinh ct\n" +
                    "ON tt.MaCT = ct.MaCT\n" +
                    "GROUP BY ct.TenCT";
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
        lvThongKeCT = view.findViewById(R.id.lvThongKeCT);
        chartChuongTrinh = view.findViewById(R.id.chartChuongTrinh);
    }
}