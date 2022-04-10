package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.BienTapVienAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;

import java.util.ArrayList;
import java.util.Calendar;

public class BienTapVienFragment extends Fragment {

    View view;
    QuanLyTruyenHinhHelper db;

    ListView lvBienTapVien;
    ArrayList<BienTapVien> data = new ArrayList<>();
    BienTapVienAdapter bienTapVienAdapter;

    SearchView svBTV;
    Button btnThemBTV;
    TextView tvSoLuongBTV;

    public BienTapVienFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bien_tap_vien, container, false);

        setControl();
        setEvent();

        return view;
    }

    private void setEvent() {
        db = new QuanLyTruyenHinhHelper(getContext());
        hienThi();
        bienTapVienAdapter = new BienTapVienAdapter(getContext(), R.layout.list_layout_bientapvien, data);
        lvBienTapVien.setAdapter(bienTapVienAdapter);



        svBTV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                bienTapVienAdapter.filter(s);
                return true;
            }
        });

        btnThemBTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInsertDialog();
            }
        });
    }

    private void openInsertDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thembientapvien);

        EditText editMaBTV = dialog.findViewById(R.id.addMaBTV);
        EditText editHoTen = dialog.findViewById(R.id.addHoTenBTV);
        EditText editSdt = dialog.findViewById(R.id.addSdtBTV);
        ImageView ivDatePicker = dialog.findViewById(R.id.ivDatePicker);
        Button btnThem = (Button) dialog.findViewById(R.id.btnThem);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);

        ivDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                Toast.makeText(getContext(), String.format("%d/%d/%d", dayOfMonth, (month + 1), year), Toast.LENGTH_LONG).show();
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Thêm", Toast.LENGTH_LONG).show();
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

    private void hienThi() {
        data.clear();
        String sql = "select * from BienTapVien";
        Cursor cursor = db.getData(sql);

        BienTapVien btv;
        while (cursor.moveToNext()) {
            btv = new BienTapVien();
            btv.setMaBTV(cursor.getString(0));
            btv.setHoTen(cursor.getString(1));
            btv.setNgaySinh(cursor.getString(2));
            btv.setSdt(cursor.getString(3));
            data.add(btv);
        }

        tvSoLuongBTV.setText(String.valueOf(data.size()));

        //bienTapVienAdapter.notifyDataSetChanged();
    }


    private void setControl() {
        lvBienTapVien = view.findViewById(R.id.lvBienTapVien);
        btnThemBTV = view.findViewById(R.id.btnThemBTV);
        tvSoLuongBTV = view.findViewById(R.id.tvSoLuongBTV);
        svBTV = view.findViewById(R.id.svBTV);
    }
}