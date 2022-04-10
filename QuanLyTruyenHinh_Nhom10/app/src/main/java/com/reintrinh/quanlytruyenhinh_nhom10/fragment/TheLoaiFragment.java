package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.TheLoaiAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiFragment extends Fragment {

    RecyclerView recycleView;
    QuanLyTruyenHinhHelper dbHelper;

    Button buttonThem;
    TheLoaiAdapter theLoaiAdapter;
    TextView txtTongTheLoai;

    List<TheLoai> arrayTheLoai = new ArrayList<>();

    SearchView searchView;
    View mView;

    public TheLoaiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_the_loai, container, false);

        setControl();

        //Load data
        dbHelper = new QuanLyTruyenHinhHelper(getContext());

        actionGetData();

        setEvent();

        return mView;
    }

    private void setEvent() {
        buttonThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInsert();
            }
        });
    }

    private void setControl() {
        buttonThem = mView.findViewById(R.id.buttonThem);
        txtTongTheLoai = mView.findViewById(R.id.txtTongTheLoai);
        recycleView = mView.findViewById(R.id.recycleView);
        searchView = mView.findViewById(R.id.searchView);
    }

    private void dialogInsert() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themtheloai);

        EditText editmaTL = (EditText) dialog.findViewById(R.id.addmaTL);
        EditText editTenTL = (EditText) dialog.findViewById(R.id.addTenTL);
        Button btnThem = (Button) dialog.findViewById(R.id.btnThem);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);

        // Sự kiện thêm thể loại
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maTheLoaiMoi = String.valueOf(editmaTL.getText());
                String tenTheLoaiMoi = String.valueOf(editTenTL.getText());
                if (TextUtils.isEmpty(String.valueOf(editTenTL.getText())) || TextUtils.isEmpty(tenTheLoaiMoi)) {
                    Toast.makeText(getContext(), "Nội dung cần thêm chưa được nhập", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Kiểm tra trùng
                Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai");
                ArrayList<TheLoai> arrayTheLoai = new ArrayList<TheLoai>();
                TheLoai theLoaiTam;
                while (dataTheLoai.moveToNext()) {
                    theLoaiTam = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1));
                    arrayTheLoai.add(theLoaiTam);
                }

                for (int i = 0; i < arrayTheLoai.size(); i++) {
                    if(arrayTheLoai.get(i).getMaTl().equalsIgnoreCase(maTheLoaiMoi)){
                        Toast.makeText(getContext(), "Mã thể loại đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                dbHelper.queryData("INSERT INTO TheLoai VALUES ('" + maTheLoaiMoi + "','" + tenTheLoaiMoi + "')");
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
        arrayTheLoai.clear();

        Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai");

        TheLoai theLoai;
        while (dataTheLoai.moveToNext()) {
            theLoai = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1));
            arrayTheLoai.add(theLoai);
        }

        txtTongTheLoai.setText("Tổng số thể loại: " + arrayTheLoai.size());

        theLoaiAdapter = new TheLoaiAdapter(this, arrayTheLoai);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycleView.setAdapter(theLoaiAdapter);
    }

    public void dialogUpdate(String maTL, String TenTL) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chinhsua_theloai);

        EditText editmaTL = (EditText) dialog.findViewById(R.id.editmaTL);
        editmaTL.setEnabled(false);
        EditText editTenTL = (EditText) dialog.findViewById(R.id.editTenTL);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btnXacNhan);
        Button btnXoa = (Button) dialog.findViewById(R.id.btnXoa);

        //set dữ liệu
        editmaTL.setText(maTL);
        editTenTL.setText(TenTL);

        //Bắt sự kiện nút bấm
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tenTL = String.valueOf(editTenTL.getText());
                if (TextUtils.isEmpty(tenTL)) {
                    Toast.makeText(getContext(), "Nội dung cần sửa chưa được nhập", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                dbHelper.queryData("UPDATE TheLoai SET TenTL='" + tenTL + "' WHERE MaTl='" + maTL + "'");
                dialog.dismiss();
                actionGetData();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.queryData("DELETE FROM TheLoai WHERE MaTL='" + maTL + "'");
                dialog.dismiss();
                actionGetData();
            }
        });

        dialog.show();
    }
}