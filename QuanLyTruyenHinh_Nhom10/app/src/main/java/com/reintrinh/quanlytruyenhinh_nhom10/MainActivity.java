package com.reintrinh.quanlytruyenhinh_nhom10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.BienTapVienFragment;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.ViewPagerAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongTinPhatSong;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    QuanLyTruyenHinhHelper dbHelper;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new QuanLyTruyenHinhHelper(this);
        khoiTaoDatabase();
        setControl();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        setEvent();
    }

    private void khoiTaoDatabase() {
        //Tạo bảng
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS TheLoai (MaTL VARCHAR(5), TenTL VARCHAR(100), HinhAnh BLOB)");
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS ChuongTrinh (MaCT VARCHAR(5),TenCT VARCHAR,MaTL VARCHAR(5), HinhAnh BLOB)");
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS BienTapVien(MaBTV VARCHAR(5), HoTen VARCHAR,NgaySinh VARCHAR,SDT VARCHAR, HinhAnh BLOB)");
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS ThongTinPhatSong(MaPS VARCHAR(5),MaCT VARCHAR(5),MaBTV VARCHAR(5),NgayPS VARCHAR, ThoiLuong INTEGER, HinhAnh BLOB)");

        // Thể loại
        TheLoai theLoai1 = new TheLoai("TL1", "Âm nhạc", getByteArrayFromImageResource(R.drawable.theloai));
        TheLoai theLoai2 = new TheLoai("TL2", "Hài kịch", getByteArrayFromImageResource(R.drawable.theloai));
        TheLoai theLoai3 = new TheLoai("TL3", "Thời sự", getByteArrayFromImageResource(R.drawable.theloai));
        TheLoai theLoai4 = new TheLoai("TL4", "Hoạt hình", getByteArrayFromImageResource(R.drawable.theloai));
        TheLoai theLoai5 = new TheLoai("TL5", "Phim truyện", getByteArrayFromImageResource(R.drawable.theloai));

        if(!dbHelper.hasData("TheLoai")) {
            dbHelper.themTheLoai(theLoai1);
            dbHelper.themTheLoai(theLoai2);
            dbHelper.themTheLoai(theLoai3);
            dbHelper.themTheLoai(theLoai4);
            dbHelper.themTheLoai(theLoai5);
        }

        // Chương trình
        ChuongTrinh chuongTrinh1 = new ChuongTrinh("CT1", "Bí mật đêm khuya", "TL1", getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
        ChuongTrinh chuongTrinh2 = new ChuongTrinh("CT2", "Thách thức danh hài", "TL1", getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
        ChuongTrinh chuongTrinh3 = new ChuongTrinh("CT3", "Gặp nhau để cười", "TL2", getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
        ChuongTrinh chuongTrinh4 = new ChuongTrinh("CT4", "Ai là triệu phú", "TL2", getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
        ChuongTrinh chuongTrinh5 = new ChuongTrinh("CT5", "Khúc vọng xưa", "TL3", getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));

        if(!dbHelper.hasData("ChuongTrinh")) {
            dbHelper.themChuongTrinh(chuongTrinh1);
            dbHelper.themChuongTrinh(chuongTrinh2);
            dbHelper.themChuongTrinh(chuongTrinh3);
            dbHelper.themChuongTrinh(chuongTrinh4);
            dbHelper.themChuongTrinh(chuongTrinh5);
        }

        // Biên tập viên
        BienTapVien bienTapVien1 = new BienTapVien("BTV1", "Đậu Trường Quân", "22/02/2000", "0935856530", getByteArrayFromImageResource(R.drawable.btv));
        BienTapVien bienTapVien2 = new BienTapVien("BTV2", "Nguyễn Ngọc Phương Trinh", "22/02/2000", "0935856530", getByteArrayFromImageResource(R.drawable.btv));
        BienTapVien bienTapVien3 = new BienTapVien("BTV3", "Phạm Đức Phú Phúc", "07/05/2000", "0935856530", getByteArrayFromImageResource(R.drawable.btv));
        BienTapVien bienTapVien4 = new BienTapVien("BTV4", "Hà Thị Mơ", "22/02/2000", "0935856530", getByteArrayFromImageResource(R.drawable.btv));
        BienTapVien bienTapVien5 = new BienTapVien("BTV5", "Trung Đỗ Nguyên", "22/02/2000", "0935856530", getByteArrayFromImageResource(R.drawable.btv));
        BienTapVien bienTapVien6 = new BienTapVien("BTV7", "Nguyễn Hữu Nhân", "22/02/2000", "0935856530", getByteArrayFromImageResource(R.drawable.btv));
        BienTapVien bienTapVien7 = new BienTapVien("BTV6", "Trần Ngọc Sang", "22/02/2000", "0935856530", getByteArrayFromImageResource(R.drawable.btv));

        if(!dbHelper.hasData("BienTapVien")) {
            dbHelper.themBienTapVien(bienTapVien1);
            dbHelper.themBienTapVien(bienTapVien2);
            dbHelper.themBienTapVien(bienTapVien3);
            dbHelper.themBienTapVien(bienTapVien4);
            dbHelper.themBienTapVien(bienTapVien5);
            dbHelper.themBienTapVien(bienTapVien6);
            dbHelper.themBienTapVien(bienTapVien7);
        }
        // Thông tin phát sóng
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('01','CT1', 'BTV1', '22/02/2000', '1')");
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('02','CT1', 'BTV1', '22/02/2000', '2')");
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('03','CT1', 'BTV1', '22/02/2000', '3')");
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('04','CT2', 'BTV2', '22/02/2000', '4')");
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('05','CT2', 'BTV2', '22/02/2000', '5')");
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('06','CT2', 'BTV2', '22/02/2000', '6')");
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('07','CT3', 'BTV2', '22/02/2000', '7')");
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('08','CT3', 'BTV2', '22/02/2000', '8')");
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('09','CT5', 'BTV3', '22/02/2000', '9')");
//        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('10','CT5', 'BTV3', '22/02/2000', '10')");
        if (!dbHelper.hasData("ThongTinPhatSong")) {
            ThongTinPhatSong thongTinPhatSong1 =
                    new ThongTinPhatSong("01", chuongTrinh1, bienTapVien1, "14/02/2022", 20,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong2 =
                    new ThongTinPhatSong("02", chuongTrinh1, bienTapVien2, "15/02/2022", 20,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong3 =
                    new ThongTinPhatSong("03", chuongTrinh1, bienTapVien3, "21/02/2022", 20,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong4 =
                    new ThongTinPhatSong("04", chuongTrinh1, bienTapVien4, "22/02/2022", 20,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong5 =
                    new ThongTinPhatSong("05", chuongTrinh2, bienTapVien1, "14/02/2022", 45,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong6 =
                    new ThongTinPhatSong("06", chuongTrinh2, bienTapVien3, "21/02/2022", 45,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong7 =
                    new ThongTinPhatSong("07", chuongTrinh2, bienTapVien2, "28/02/2022", 45,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong8 =
                    new ThongTinPhatSong("08", chuongTrinh3, bienTapVien5, "01/03/2022", 30,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong9 =
                    new ThongTinPhatSong("09", chuongTrinh3, bienTapVien6, "08/03/2022", 30,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong10 =
                    new ThongTinPhatSong("10", chuongTrinh5, bienTapVien7, "10/03/2022", 25,
                            getByteArrayFromImageResource(R.drawable.avata_chuongtrinh));
            dbHelper.themThongTinPhatSong(thongTinPhatSong1);
            dbHelper.themThongTinPhatSong(thongTinPhatSong2);
            dbHelper.themThongTinPhatSong(thongTinPhatSong3);
            dbHelper.themThongTinPhatSong(thongTinPhatSong4);
            dbHelper.themThongTinPhatSong(thongTinPhatSong5);
            dbHelper.themThongTinPhatSong(thongTinPhatSong6);
            dbHelper.themThongTinPhatSong(thongTinPhatSong7);
            dbHelper.themThongTinPhatSong(thongTinPhatSong8);
            dbHelper.themThongTinPhatSong(thongTinPhatSong9);
            dbHelper.themThongTinPhatSong(thongTinPhatSong10);
        }
    }

    private void setEvent() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_thongke).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_chuongtrinh).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_theloai).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_bientapvien).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_thongke:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_chuongtrinh:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_theloai:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.menu_bientapvien:
                        viewPager.setCurrentItem(3);
                        break;
                }

                return true;
            }
        });
    }

    private void setControl() {
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private byte[] getByteArrayFromImageResource(int resourceId) {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(resourceId);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        return ImageUtil.getByteArrayFromBitmap(bitmap);
    }

}