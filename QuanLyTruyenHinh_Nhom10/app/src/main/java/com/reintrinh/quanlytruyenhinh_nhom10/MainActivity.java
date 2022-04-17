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
import com.reintrinh.quanlytruyenhinh_nhom10.model.User;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setControl();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        setEvent();
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