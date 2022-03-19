package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.adapter.ChuongTrinhAdapter;
import com.example.demo.listener.ChuongTrinhListener;
import com.example.demo.model.ChuongTrinh;
import com.example.demo.model.TheLoai;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycleView;
    Spinner spinnerTheLoai;
    TheLoaiHelper theLoaiHelper;
    FloatingActionButton floatingActionButton;
    ChuongTrinhAdapter chuongTrinhAdapter;
    TextView txtTongChuongTrinh;

    List<ChuongTrinh> arrayChuongTrinh = new ArrayList<>();
    TheLoai theLoai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        ArrayList<TheLoai> arrayTheLoai= new ArrayList<TheLoai>();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        spinnerTheLoai = (Spinner) findViewById(R.id.spinnerTheLoai);
        ArrayList<String> arrayTenTheLoai = new ArrayList<String>();
        txtTongChuongTrinh = findViewById(R.id.txtTongChuongTrinh);
        recycleView = findViewById(R.id.recycleView);
        arrayTenTheLoai.add("Tất cả");

        //Tao database
        theLoaiHelper = new TheLoaiHelper(this);

        //Tao bang
        theLoaiHelper.QueryData("CREATE TABLE IF NOT EXISTS TheLoai(MaTl VARCHAR(5),TenTL VARCHAR(100))");

        theLoaiHelper.QueryData("CREATE TABLE IF NOT EXISTS ChuongTrinh(MaCT VARCHAR(5),TenCT VARCHAR,MaTL VARCHAR(5))");
        theLoaiHelper.QueryData("CREATE TABLE IF NOT EXISTS ThongTinPhatSong(MaPS VARCHAR(5),MaCT VARCHAR(5),MaBTV VARCHAR(5),NgayPS VARCHAR, ThoiLuong INTEGER)");
        theLoaiHelper.QueryData("CREATE TABLE IF NOT EXISTS BienTapVien(MaBTV VARCHAR(5), TenBTV VARCHAR,NgaySinh VARCHAR,SDT VARCHAR)");

        //Them du lieu
        theLoaiHelper.QueryData("delete from TheLoai");
        theLoaiHelper.QueryData("INSERT INTO TheLoai VALUES ('TL1','Âm nhạc')");
        theLoaiHelper.QueryData("INSERT INTO TheLoai VALUES ('TL2','Hài kịch')");
        theLoaiHelper.QueryData("INSERT INTO TheLoai VALUES ('TL3','Thời sự')");

        //Them du lieu Chuong Trinh
        theLoaiHelper.QueryData("delete from ChuongTrinh");
        theLoaiHelper.QueryData("INSERT INTO ChuongTrinh VALUES ('CT1','Bí mật đêm khuya', 'TL1')");
        theLoaiHelper.QueryData("INSERT INTO ChuongTrinh VALUES ('CT2','Thách thức danh hài', 'TL1')");
        theLoaiHelper.QueryData("INSERT INTO ChuongTrinh VALUES ('CT3','Gặp nhau để cười', 'TL2')");
        theLoaiHelper.QueryData("INSERT INTO ChuongTrinh VALUES ('CT4','Ai là triệu phú', 'TL2')");
        theLoaiHelper.QueryData("INSERT INTO ChuongTrinh VALUES ('CT5','Khúc vọng xưa', 'TL3')");

        //Them du lieu chi tiet Thong Tin Phat Song
        theLoaiHelper.QueryData("delete from ThongTinPhatSong");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('1','CT1', 'BTV1', '22/02/2000', '1')");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('1','CT1', 'BTV1', '22/02/2000', '2')");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('1','CT1', 'BTV1', '22/02/2000', '3')");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('2','CT2', 'BTV2', '22/02/2000', '4')");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('3','CT2', 'BTV2', '22/02/2000', '5')");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('3','CT2', 'BTV2', '22/02/2000', '6')");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('4','CT3', 'BTV2', '22/02/2000', '7')");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('4','CT3', 'BTV2', '22/02/2000', '8')");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('5','CT5', 'BTV3', '22/02/2000', '9')");
        theLoaiHelper.QueryData("INSERT INTO ThongTinPhatSong VALUES ('5','CT5', 'BTV3', '22/02/2000', '10')");

        //Them du lieu Bien Tap Vien
        theLoaiHelper.QueryData("delete from BienTapVien");
        theLoaiHelper.QueryData("INSERT INTO BienTapVien VALUES ('BTV1','Đậu Trường Quân', '22/02/2000', '0935856530')");
        theLoaiHelper.QueryData("INSERT INTO BienTapVien VALUES ('BTV2','Nguyễn Ngọc Phương Trinh', '22/02/2000', '0935856530')");
        theLoaiHelper.QueryData("INSERT INTO BienTapVien VALUES ('BTV3','Trung Đỗ Nguyên', '22/02/2000', '0935856530')");
        theLoaiHelper.QueryData("INSERT INTO BienTapVien VALUES ('BTV4','Nguyễn Hữu Nhân', '22/02/2000', '0935856530')");
        theLoaiHelper.QueryData("INSERT INTO BienTapVien VALUES ('BTV5','Trần Ngọc Sang', '22/02/2000', '0935856530')");

        Cursor dataTheLoai = theLoaiHelper.GetData("SELECT * FROM TheLoai");

        TheLoai theLoai;
        while (dataTheLoai.moveToNext()) {
            theLoai = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1));
            arrayTheLoai.add(theLoai);
            arrayTenTheLoai.add(theLoai.getTenTL());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayTenTheLoai);
        spinnerTheLoai.setAdapter(arrayAdapter);
        actionGetData();
        spinnerTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actionGetData();
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInsert();
            }

            
        });



    }

    public void actionGetData() {
        arrayChuongTrinh.clear();
        String tl = spinnerTheLoai.getSelectedItem().toString();
        if (tl.equals("Tất cả")) {

            Cursor dataChuongTrinh = theLoaiHelper.GetData("SELECT * FROM ChuongTrinh");
            theLoai = new TheLoai("Tất cả", "Tất cả");

            ChuongTrinh chuongTrinh;
            while (dataChuongTrinh.moveToNext()) {
                chuongTrinh = new ChuongTrinh(dataChuongTrinh.getString(0), dataChuongTrinh.getString(1), dataChuongTrinh.getString(2));
                arrayChuongTrinh.add(chuongTrinh);
            }

            txtTongChuongTrinh.setText("Tổng số chương trình: " + arrayChuongTrinh.size());
        } else {
            Cursor dataKho = theLoaiHelper.GetData("SELECT * FROM TheLoai WHERE TenTL='" + tl + "'");
            String ma = "";

            while (dataKho.moveToNext()) {
                ma = dataKho.getString(0);
                theLoai = new TheLoai(ma, dataKho.getString(1));
            }
            Cursor dataChuongTrinh = theLoaiHelper.GetData("SELECT * FROM ChuongTrinh WHERE MaTL = '" + ma + "'");

            ChuongTrinh chuongTrinh;
            while (dataChuongTrinh.moveToNext()) {
                chuongTrinh = new ChuongTrinh(dataChuongTrinh.getString(0), dataChuongTrinh.getString(1), dataChuongTrinh.getString(2));
                arrayChuongTrinh.add(chuongTrinh);
            }
            txtTongChuongTrinh.setText("Tổng số chương trình: " + arrayChuongTrinh.size());
        }





        chuongTrinhAdapter = new ChuongTrinhAdapter(MainActivity.this, arrayChuongTrinh);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        recycleView.setAdapter(chuongTrinhAdapter);
    }

    private final ChuongTrinhListener chuongTrinhListener = new ChuongTrinhListener() {
        @Override
        public void onChuongTrinhClicked(String id, ChuongTrinh chuongTrinh) {

            Intent intent = new Intent(MainActivity.this, ThongTinPhatSongActivity.class);

            Bundle bundle = new Bundle();

            bundle.putString("id", id);
            bundle.putSerializable("ChuongTrinh", chuongTrinh);
            bundle.putSerializable("TheLoai", theLoai);

            intent.putExtra("data", bundle);

            startActivity(intent);
        }

    };

    private void dialogInsert() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themchuongtrinh);

        EditText editmaCT = (EditText) dialog.findViewById(R.id.editmaCT);
        EditText editTenCT = (EditText) dialog.findViewById(R.id.editTenCT);
        Button btnThem = (Button) dialog.findViewById(R.id.btnThem);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);

        // thêm dữ liệu vào spin
        Spinner spinnerTheLoaiTam =dialog.findViewById(R.id.spinnerMaTheLoai);
        Cursor dataTheLoai = theLoaiHelper.GetData("SELECT * FROM TheLoai");
        ArrayList<TheLoai> arrayTheLoaiTam = new ArrayList<TheLoai>();
        ArrayList<String> arrayTenTheLoaiTam = new ArrayList<String>();
        TheLoai theLoaiEdit;
        while (dataTheLoai.moveToNext()) {
            theLoaiEdit = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1));
            arrayTheLoaiTam.add(theLoaiEdit);
            arrayTenTheLoaiTam.add(theLoaiEdit.getTenTL());
        }
        ArrayAdapter arrayAdapterTam = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayTenTheLoaiTam);
        spinnerTheLoaiTam.setAdapter(arrayAdapterTam);

        // sự kiện thêm chương trình
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maChuongTrinhMoi =String.valueOf(editmaCT.getText());
                String tenChuongTrinhMoi = String.valueOf(editTenCT.getText());
                String maTheLoai = String.valueOf(arrayTheLoaiTam.get(spinnerTheLoaiTam.getSelectedItemPosition()).getMaTl());
                if (TextUtils.isEmpty(String.valueOf(editTenCT.getText())) || TextUtils.isEmpty(tenChuongTrinhMoi) || TextUtils.isEmpty(maTheLoai)) {
                    Toast.makeText(MainActivity.this, "Nội dung cần thêm chưa được nhập", Toast.LENGTH_SHORT).show();

                    return;
                }

                //kiem tra trung
                Cursor dataChuongTrinh = theLoaiHelper.GetData("SELECT * FROM ChuongTrinh");
                ArrayList<ChuongTrinh> arrayChuongTrinh = new ArrayList<ChuongTrinh>();
                ChuongTrinh chuongTrinhTam;
                while (dataChuongTrinh.moveToNext()) {
                    chuongTrinhTam = new ChuongTrinh(dataChuongTrinh.getString(0), dataChuongTrinh.getString(1),dataChuongTrinh.getString(2));
                    arrayChuongTrinh.add(chuongTrinhTam);
                }

                for (int i = 0; i < arrayChuongTrinh.size(); i++) {
                    if(maChuongTrinhMoi==arrayChuongTrinh.get(i).getMaCT()){
                        Toast.makeText(MainActivity.this, "Mã chương trình đã tồn tại bạn ei", Toast.LENGTH_SHORT).show();

                        return;
                    }
                }


                theLoaiHelper.QueryData("INSERT INTO ChuongTrinh VALUES ('" + maChuongTrinhMoi + "','" + tenChuongTrinhMoi + "', '" + maTheLoai + "')");
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




}