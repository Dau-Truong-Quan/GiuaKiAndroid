package com.reintrinh.quanlytruyenhinh_nhom10.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.reintrinh.quanlytruyenhinh_nhom10.Constant.Constants;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.ViewPagerAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.listener.OnSaveClickListener;
import com.reintrinh.quanlytruyenhinh_nhom10.model.User;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;
import com.reintrinh.quanlytruyenhinh_nhom10.util.PreferenceManager;
import com.reintrinh.quanlytruyenhinh_nhom10.widget.CustomToast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_THONG_KE = 1;
    private static final int FRAGMENT_CHUONG_TRINH = 2;
    private static final int FRAGMENT_THE_LOAI = 3;
    private static final int FRAGMENT_BIEN_TAP_VIEN = 4;
    private static final int FRAGMENT_TAI_KHOAN = 5;
    private int currentFragment = FRAGMENT_THONG_KE;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;

    private ImageView imgUser;
    private TextView txtName, txtEmail;

    private PreferenceManager preferenceManager;
    private QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();
        setEvent();
        loadUserInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_doi_mat_khau:
                changeUserPassword();
                return true;
            case R.id.action_dang_xuat:
                signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUserInfo() {
        String email = preferenceManager.getString(Constants.KEY_EMAIL);
        User user = quanLyTruyenHinhHelper.getUserByEmail(email);
        if (user == null) return;
        imgUser.setImageBitmap(ImageUtil.getBitmapFromByteArray(user.getImg()));
        txtName.setText(user.getFirstname() + " " + user.getLastname());
        txtEmail.setText(user.getEmail());
    }

    private void signOut() {
        Toast.makeText(this, "Đăng xuất...", Toast.LENGTH_LONG).show();
        preferenceManager.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setOnSaveClickListener(new OnSaveClickListener() {
            @Override
            public void onClick() {
                loadUserInfo();
            }
        });
        viewPager.setAdapter(viewPagerAdapter);

        imgUser = navigationView.getHeaderView(0).findViewById(R.id.img_user_avatar);
        txtName = navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);

        preferenceManager = new PreferenceManager(this);
        quanLyTruyenHinhHelper = QuanLyTruyenHinhHelper.getInstance(this);
    }

    private void setEvent() {
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_thong_ke).setChecked(true);

        bottomNavigationView.getMenu().findItem(R.id.menu_thongke).setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_thongke:
                        openThongKeFragment();
                        break;
                    case R.id.menu_chuongtrinh:
                        openChuongTrinhFragment();
                        break;
                    case R.id.menu_theloai:
                        openTheLoaiFragment();
                        break;
                    case R.id.menu_bientapvien:
                        openBienTapVienFragment();
                        break;
                    case R.id.menu_taikhoan:
                        openTaiKhoanFragment();
                        break;
                }
                return true;
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        currentFragment = FRAGMENT_THONG_KE;
                        navigationView.getMenu().findItem(R.id.nav_thong_ke).setChecked(true);
                        bottomNavigationView.getMenu().findItem(R.id.menu_thongke).setChecked(true);
                        break;
                    case 1:
                        currentFragment = FRAGMENT_CHUONG_TRINH;
                        navigationView.getMenu().findItem(R.id.nav_chuong_trinh).setChecked(true);
                        bottomNavigationView.getMenu().findItem(R.id.menu_chuongtrinh).setChecked(true);
                        break;
                    case 2:
                        currentFragment = FRAGMENT_THE_LOAI;
                        navigationView.getMenu().findItem(R.id.nav_the_loai).setChecked(true);
                        bottomNavigationView.getMenu().findItem(R.id.menu_theloai).setChecked(true);
                        break;
                    case 3:
                        currentFragment = FRAGMENT_BIEN_TAP_VIEN;
                        navigationView.getMenu().findItem(R.id.nav_bien_tap_vien).setChecked(true);
                        bottomNavigationView.getMenu().findItem(R.id.menu_bientapvien).setChecked(true);
                        break;
                    case 4:
                        currentFragment = FRAGMENT_TAI_KHOAN;
                        navigationView.getMenu().findItem(R.id.nav_tai_khoan).setChecked(true);
                        bottomNavigationView.getMenu().findItem(R.id.menu_taikhoan).setChecked(true);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_thong_ke:
                openThongKeFragment();
                break;
            case R.id.nav_chuong_trinh:
                openChuongTrinhFragment();
                break;
            case R.id.nav_the_loai:
                openTheLoaiFragment();
                break;
            case R.id.nav_bien_tap_vien:
                openBienTapVienFragment();
                break;
            case R.id.nav_tai_khoan:
                openTaiKhoanFragment();
                break;
            case R.id.nav_doi_mat_khau:
                changeUserPassword();
                break;
            case R.id.nav_dang_xuat:
                signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeUserPassword() {
        Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    public void openThongKeFragment() {
        if (currentFragment != FRAGMENT_THONG_KE) {
            viewPager.setCurrentItem(0);
            currentFragment = FRAGMENT_THONG_KE;
        }
    }

    public void openChuongTrinhFragment() {
        if (currentFragment != FRAGMENT_CHUONG_TRINH) {
            viewPager.setCurrentItem(1);
            currentFragment = FRAGMENT_CHUONG_TRINH;
        }
    }

    public void openTheLoaiFragment() {
        if (currentFragment != FRAGMENT_THE_LOAI) {
            viewPager.setCurrentItem(2);
            currentFragment = FRAGMENT_THE_LOAI;
        }
    }

    public void openBienTapVienFragment() {
        if (currentFragment != FRAGMENT_BIEN_TAP_VIEN) {
            viewPager.setCurrentItem(3);
            currentFragment = FRAGMENT_BIEN_TAP_VIEN;
        }
    }

    public void openTaiKhoanFragment() {
        if (currentFragment != FRAGMENT_TAI_KHOAN) {
            viewPager.setCurrentItem(4);
            currentFragment = FRAGMENT_TAI_KHOAN;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}