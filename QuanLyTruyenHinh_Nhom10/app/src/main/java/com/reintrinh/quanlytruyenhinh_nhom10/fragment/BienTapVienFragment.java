package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;

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

        lvBienTapVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openUpdateDialog(data.get(i));
            }
        });

        lvBienTapVien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                openDeleteDialog(data.get(i));
                return true;
            }
        });

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

    private void openDeleteDialog(BienTapVien btv) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_xacnhan_xoa);

        Button btnXoa = dialog.findViewById(R.id.btnXacNhanXoa);
        Button btnHuy = dialog.findViewById(R.id.btnHuyXoa);

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.queryData("DELETE FROM BienTapVien WHERE MaBTV='" + btv.getMaBTV() + "'");
                dialog.dismiss();
                hienThi();
                bienTapVienAdapter.notifyDataSetChanged();
                bienTapVienAdapter.updateDataSearch();
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

    private void openUpdateDialog(BienTapVien btv) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chinhsua_bientapvien);

        EditText editMaBTV = dialog.findViewById(R.id.editMaBTV);
        EditText editHoTen = dialog.findViewById(R.id.editHoTenBTV);
        EditText editNgaySinh = dialog.findViewById(R.id.editNgaySinhBTV);
        EditText editSdt = dialog.findViewById(R.id.editSdtBTV);

        ImageView ivDatePicker = dialog.findViewById(R.id.ivDatePicker);
        Button btnLuu = dialog.findViewById(R.id.btnLuu);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        // set các giá trị
        editMaBTV.setText(btv.getMaBTV());
        editHoTen.setText(btv.getHoTen());
        editNgaySinh.setText(btv.getNgaySinh());
        editSdt.setText(btv.getSdt());

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
                                String value = String.format("%d/%d/%d", dayOfMonth, (month + 1), year);
                                editNgaySinh.setText(value);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maBTV = String.valueOf(editMaBTV.getText());
                String tenBTVMoi = String.valueOf(editHoTen.getText());
                String ngaySinhBTVMoi = String.valueOf(editNgaySinh.getText());
                String sdtBTVMoi = String.valueOf(editSdt.getText());

                if (TextUtils.isEmpty(tenBTVMoi) || TextUtils.isEmpty(sdtBTVMoi) || TextUtils.isEmpty(ngaySinhBTVMoi)) {
                    Toast.makeText(getContext(), "Nội dung cần thêm chưa được nhập", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.queryData(String.format("UPDATE BienTapVien SET HoTen = '%s', NgaySinh = '%s', Sdt = '%s' WHERE MaBTV = '%s'", tenBTVMoi, ngaySinhBTVMoi, sdtBTVMoi, maBTV));
                dialog.dismiss();
                hienThi();
                bienTapVienAdapter.notifyDataSetChanged();
                bienTapVienAdapter.updateDataSearch();
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

    private void openInsertDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thembientapvien);

        EditText editMaBTV = dialog.findViewById(R.id.addMaBTV);
        EditText editHoTen = dialog.findViewById(R.id.addHoTenBTV);
        EditText editNgaySinh = dialog.findViewById(R.id.addNgaySinhBTV);
        EditText editSdt = dialog.findViewById(R.id.addSdtBTV);

        ImageView ivDatePicker = dialog.findViewById(R.id.ivDatePicker);
        Button btnThem = dialog.findViewById(R.id.btnThem);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

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
                                String value = String.format("%d/%d/%d", dayOfMonth, (month + 1), year);
                                editNgaySinh.setText(value);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maBTVMoi = String.valueOf(editMaBTV.getText());
                String tenBTVMoi = String.valueOf(editHoTen.getText());
                String ngaySinhBTVMoi = String.valueOf(editNgaySinh.getText());
                String sdtBTVMoi = String.valueOf(editSdt.getText());

                if (TextUtils.isEmpty(maBTVMoi) || TextUtils.isEmpty(tenBTVMoi) || TextUtils.isEmpty(sdtBTVMoi) || TextUtils.isEmpty(ngaySinhBTVMoi)) {
                    Toast.makeText(getContext(), "Nội dung cần thêm chưa được nhập", Toast.LENGTH_SHORT).show();
                    return;
                }

                //kiem tra trung
                Cursor dataBTV = db.getData("SELECT * FROM BienTapVien");

                ArrayList<BienTapVien> arrayBTV = new ArrayList<>();
                BienTapVien btvTam;
                while (dataBTV.moveToNext()) {
                    btvTam = new BienTapVien(dataBTV.getString(0), dataBTV.getString(1), dataBTV.getString(2), dataBTV.getString(3));
                    arrayBTV.add(btvTam);
                }

                for (int i = 0; i < arrayBTV.size(); i++) {
                    if(arrayBTV.get(i).getMaBTV().equalsIgnoreCase(maBTVMoi)){
                        Toast.makeText(getContext(), "Mã biên tập viên đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                db.queryData("INSERT INTO BienTapVien VALUES ('" + maBTVMoi + "','" + tenBTVMoi + "','" + ngaySinhBTVMoi + "', '" + sdtBTVMoi + "')");
                dialog.dismiss();
                hienThi();
                bienTapVienAdapter.notifyDataSetChanged();
                bienTapVienAdapter.updateDataSearch();
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