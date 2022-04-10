package com.reintrinh.quanlytruyenhinh_nhom10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.database.Cursor;
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
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.ViewPagerAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;

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
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS TheLoai (MaTl VARCHAR(5), TenTL VARCHAR(100))");

        dbHelper.queryData("CREATE TABLE IF NOT EXISTS ChuongTrinh (MaCT VARCHAR(5),TenCT VARCHAR,MaTL VARCHAR(5))");
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS ThongTinPhatSong(MaPS VARCHAR(5),MaCT VARCHAR(5),MaBTV VARCHAR(5),NgayPS VARCHAR, ThoiLuong INTEGER)");
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS BienTapVien(MaBTV VARCHAR(5), HoTen VARCHAR, NgaySinh VARCHAR, Sdt VARCHAR)");

        //Thêm dữ liệu thể loại
        dbHelper.queryData("delete from TheLoai");
        dbHelper.queryData("INSERT INTO TheLoai VALUES ('TL1','Âm nhạc')");
        dbHelper.queryData("INSERT INTO TheLoai VALUES ('TL2','Hài kịch')");
        dbHelper.queryData("INSERT INTO TheLoai VALUES ('TL3','Thời sự')");

        //Them du lieu Chuong Trinh
        dbHelper.queryData("delete from ChuongTrinh");
        dbHelper.queryData("INSERT INTO ChuongTrinh VALUES ('CT1','Bí mật đêm khuya', 'TL1')");
        dbHelper.queryData("INSERT INTO ChuongTrinh VALUES ('CT2','Thách thức danh hài', 'TL1')");
        dbHelper.queryData("INSERT INTO ChuongTrinh VALUES ('CT3','Gặp nhau để cười', 'TL2')");
        dbHelper.queryData("INSERT INTO ChuongTrinh VALUES ('CT4','Ai là triệu phú', 'TL2')");
        dbHelper.queryData("INSERT INTO ChuongTrinh VALUES ('CT5','Khúc vọng xưa', 'TL3')");

        //Them du lieu chi tiet Thong Tin Phat Song
        dbHelper.queryData("delete from ThongTinPhatSong");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('1','CT1', 'BTV1', '22/02/2000', '1')");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('1','CT1', 'BTV1', '22/02/2000', '2')");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('1','CT1', 'BTV1', '22/02/2000', '3')");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('2','CT2', 'BTV2', '22/02/2000', '4')");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('3','CT2', 'BTV2', '22/02/2000', '5')");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('3','CT2', 'BTV2', '22/02/2000', '6')");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('4','CT3', 'BTV2', '22/02/2000', '7')");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('4','CT3', 'BTV2', '22/02/2000', '8')");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('5','CT5', 'BTV3', '22/02/2000', '9')");
        dbHelper.queryData("INSERT INTO ThongTinPhatSong VALUES ('5','CT5', 'BTV3', '22/02/2000', '10')");

        //Them du lieu Bien Tap Vien
        dbHelper.queryData("delete from BienTapVien");
        dbHelper.queryData("INSERT INTO BienTapVien VALUES ('BTV1','Đậu Trường Quân', '22/02/2000', '0935856530')");
        dbHelper.queryData("INSERT INTO BienTapVien VALUES ('BTV2','Nguyễn Ngọc Phương Trinh', '22/02/2000', '0935856530')");
        dbHelper.queryData("INSERT INTO BienTapVien VALUES ('BTV3','Trung Đỗ Nguyên', '22/02/2000', '0935856530')");
        dbHelper.queryData("INSERT INTO BienTapVien VALUES ('BTV4','Nguyễn Hữu Nhân', '22/02/2000', '0935856530')");
        dbHelper.queryData("INSERT INTO BienTapVien VALUES ('BTV5','Trần Ngọc Sang', '22/02/2000', '0935856530')");
        
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

}