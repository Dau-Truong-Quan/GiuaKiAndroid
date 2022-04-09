package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reintrinh.quanlytruyenhinh_nhom10.MainActivity;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.ChuongTrinhAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class ChuongTrinhFragment extends Fragment {

    RecyclerView recycleView;
    Spinner spinnerTheLoai;
    QuanLyTruyenHinhHelper dbHelper;

    Button buttonThem;
    ChuongTrinhAdapter chuongTrinhAdapter;
    TextView txtTongChuongTrinh;

    List<ChuongTrinh> arrayChuongTrinh = new ArrayList<>();
    TheLoai theLoai;

    SearchView searchView;
    View mView;

    public ChuongTrinhFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chuong_trinh, container, false);

        setControl();

        ArrayList<TheLoai> arrayTheLoai= new ArrayList<TheLoai>();
        ArrayList<String> arrayTenTheLoai = new ArrayList<String>();

        arrayTenTheLoai.add("Tất cả");

        //Load data
        dbHelper = new QuanLyTruyenHinhHelper(getContext());
        Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai");

        TheLoai theLoai;
        while (dataTheLoai.moveToNext()) {
            theLoai = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1));
            arrayTheLoai.add(theLoai);
            arrayTenTheLoai.add(theLoai.getTenTL());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrayTenTheLoai);
        spinnerTheLoai.setAdapter(arrayAdapter);

        actionGetData();

        setEvent();

        return mView;
    }

    private void setEvent() {
        spinnerTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actionGetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInsert();
            }

        });
    }

    private void setControl() {
        buttonThem = mView.findViewById(R.id.buttonThem);
        spinnerTheLoai = mView.findViewById(R.id.spinnerTheLoai);
        txtTongChuongTrinh = mView.findViewById(R.id.txtTongChuongTrinh);
        recycleView = mView.findViewById(R.id.recycleView);
        searchView = mView.findViewById(R.id.searchView);
    }

    private void dialogInsert() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themchuongtrinh);

        EditText editmaCT = (EditText) dialog.findViewById(R.id.addmaCT);
        EditText editTenCT = (EditText) dialog.findViewById(R.id.addTenCT);
        Button btnThem = (Button) dialog.findViewById(R.id.btnThem);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);

        // thêm dữ liệu vào spin
        Spinner spinnerTheLoaiTam = dialog.findViewById(R.id.spinnerMaTheLoai);
        Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai");
        ArrayList<TheLoai> arrayTheLoaiTam = new ArrayList<TheLoai>();
        ArrayList<String> arrayTenTheLoaiTam = new ArrayList<String>();
        TheLoai theLoaiEdit;
        while (dataTheLoai.moveToNext()) {
            theLoaiEdit = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1));
            arrayTheLoaiTam.add(theLoaiEdit);
            arrayTenTheLoaiTam.add(theLoaiEdit.getTenTL());
        }
        ArrayAdapter arrayAdapterTam = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrayTenTheLoaiTam);
        spinnerTheLoaiTam.setAdapter(arrayAdapterTam);

        // sự kiện thêm chương trình
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maChuongTrinhMoi = String.valueOf(editmaCT.getText());
                String tenChuongTrinhMoi = String.valueOf(editTenCT.getText());
                String maTheLoai = String.valueOf(arrayTheLoaiTam.get(spinnerTheLoaiTam.getSelectedItemPosition()).getMaTl());
                if (TextUtils.isEmpty(String.valueOf(editTenCT.getText())) || TextUtils.isEmpty(tenChuongTrinhMoi) || TextUtils.isEmpty(maTheLoai)) {
                    Toast.makeText(getContext(), "Nội dung cần thêm chưa được nhập", Toast.LENGTH_SHORT).show();

                    return;
                }

                //kiem tra trung
                Cursor dataChuongTrinh = dbHelper.getData("SELECT * FROM ChuongTrinh");
                ArrayList<ChuongTrinh> arrayChuongTrinh = new ArrayList<ChuongTrinh>();
                ChuongTrinh chuongTrinhTam;
                while (dataChuongTrinh.moveToNext()) {
                    chuongTrinhTam = new ChuongTrinh(dataChuongTrinh.getString(0), dataChuongTrinh.getString(1),dataChuongTrinh.getString(2));
                    arrayChuongTrinh.add(chuongTrinhTam);
                }

                for (int i = 0; i < arrayChuongTrinh.size(); i++) {
                    if(arrayChuongTrinh.get(i).getMaCT().equalsIgnoreCase(maChuongTrinhMoi)){
                        Toast.makeText(getContext(), "Mã chương trình đã tồn tại", Toast.LENGTH_SHORT).show();

                        return;
                    }
                }

                dbHelper.queryData("INSERT INTO ChuongTrinh VALUES ('" + maChuongTrinhMoi + "','" + tenChuongTrinhMoi + "', '" + maTheLoai + "')");
                dialog.dismiss();
                actionGetData();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void actionGetData() {
        arrayChuongTrinh.clear();
        String tl = spinnerTheLoai.getSelectedItem().toString();
        if (tl.equals("Tất cả")) {

            Cursor dataChuongTrinh = dbHelper.getData("SELECT * FROM ChuongTrinh");
            theLoai = new TheLoai("Tất cả", "Tất cả");

            ChuongTrinh chuongTrinh;
            while (dataChuongTrinh.moveToNext()) {
                chuongTrinh = new ChuongTrinh(dataChuongTrinh.getString(0), dataChuongTrinh.getString(1), dataChuongTrinh.getString(2));
                arrayChuongTrinh.add(chuongTrinh);
            }

            txtTongChuongTrinh.setText("Tổng số chương trình: " + arrayChuongTrinh.size());
        } else {
            Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai WHERE TenTL='" + tl + "'");
            String ma = "";

            while (dataTheLoai.moveToNext()) {
                ma = dataTheLoai.getString(0);
                theLoai = new TheLoai(ma, dataTheLoai.getString(1));
            }
            Cursor dataChuongTrinh = dbHelper.getData("SELECT * FROM ChuongTrinh WHERE MaTL = '" + ma + "'");

            ChuongTrinh chuongTrinh;
            while (dataChuongTrinh.moveToNext()) {
                chuongTrinh = new ChuongTrinh(dataChuongTrinh.getString(0), dataChuongTrinh.getString(1), dataChuongTrinh.getString(2));
                arrayChuongTrinh.add(chuongTrinh);
            }
            txtTongChuongTrinh.setText("Tổng số chương trình: " + arrayChuongTrinh.size());
        }

        chuongTrinhAdapter = new ChuongTrinhAdapter(this, arrayChuongTrinh);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycleView.setAdapter(chuongTrinhAdapter);
    }

    public void dialogUpdate(String maCT, String TenCT, String MaTL) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chinhsua_chuongtrinh);

        EditText editmaCT = (EditText) dialog.findViewById(R.id.editmaCT);
        editmaCT.setEnabled(false);
        EditText editTenCT = (EditText) dialog.findViewById(R.id.editTenCT);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btnXacNhan);
        Button btnXoa = (Button) dialog.findViewById(R.id.btnXoa);

        // thêm dữ liệu vào spin
        Spinner spinnerTheLoaiTam = dialog.findViewById(R.id.spinnerMaTheLoaiChinhSua);
        Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai");
        ArrayList<TheLoai> arrayTheLoaiTam = new ArrayList<TheLoai>();
        ArrayList<String> arrayTenTheLoaiTam = new ArrayList<String>();
        TheLoai theLoaiEdit;
        while (dataTheLoai.moveToNext()) {
            theLoaiEdit = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1));
            arrayTheLoaiTam.add(theLoaiEdit);
            arrayTenTheLoaiTam.add(theLoaiEdit.getTenTL());
        }
        ArrayAdapter arrayAdapterTam = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrayTenTheLoaiTam);
        spinnerTheLoaiTam.setAdapter(arrayAdapterTam);

        //set du lieu
        editmaCT.setText(maCT);
        editTenCT.setText(TenCT);
        spinnerTheLoaiTam.setSelection(arrayAdapterTam.getPosition(MaTL));

        //bat su kien nut bam
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tenCT = String.valueOf(editTenCT.getText());
                String tenTheLoai = String.valueOf(spinnerTheLoaiTam.getSelectedItem().toString());
                String maTheLoai = String.valueOf(arrayTheLoaiTam.get(spinnerTheLoaiTam.getSelectedItemPosition()).getMaTl());
                if (TextUtils.isEmpty(tenCT) || TextUtils.isEmpty(tenTheLoai)) {
                    Toast.makeText(getContext(), "Nội dung cần sửa chưa được nhập", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                dbHelper.queryData("UPDATE ChuongTrinh SET TenCT='" + tenCT + "',MaTl='" + maTheLoai + "' WHERE MaCT='" + maCT + "'");
                dialog.dismiss();
                actionGetData();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.queryData("DELETE FROM ChuongTrinh WHERE MaCT='" + maCT + "'");
                dialog.dismiss();
                actionGetData();
            }
        });

        dialog.show();
    }
}