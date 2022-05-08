package com.reintrinh.quanlytruyenhinh_nhom10.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;



import com.google.android.material.button.MaterialButton;
import com.reintrinh.quanlytruyenhinh_nhom10.Constant.Constants;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongTinPhatSong;
import com.reintrinh.quanlytruyenhinh_nhom10.model.User;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;
import com.reintrinh.quanlytruyenhinh_nhom10.util.PreferenceManager;
import com.reintrinh.quanlytruyenhinh_nhom10.widget.CustomToast;

public class LoginActivity extends AppCompatActivity {
    private TextView txtForgotPass;
    private MaterialButton buttonSignIn;
    private EditText inputEmail, inputPassword;
    private PreferenceManager preferenceManager;
    private QuanLyTruyenHinhHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new QuanLyTruyenHinhHelper(this);
        khoiTaoDatabase();
        setControl();
        setEvent();

        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setEvent() {
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    User user = dbHelper.checkUserExist(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim());
                    if (user != null) {
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, user.getId() + "");
                        preferenceManager.putString(Constants.KEY_NAME, user.getFirstname() + " " + user.getLastname());
                        preferenceManager.putString(Constants.KEY_EMAIL, user.getEmail());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        CustomToast.makeCustomToast(LoginActivity.this,
                                R.drawable.ic_person, "Đăng nhập thất bại!").show();
                    }
                }
            }
        });
        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        dbHelper = QuanLyTruyenHinhHelper.getInstance(this);
        preferenceManager = new PreferenceManager(getApplicationContext());
        buttonSignIn = findViewById(R.id.buttonSignIn);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        txtForgotPass = findViewById(R.id.textForgotPassword);
    }

    private boolean check() {
        if (inputEmail.getText().toString().trim().isEmpty()) {
            CustomToast.makeCustomToast(this, R.drawable.ic_mail_outline, "Enter email").show();
            return false;
        } else if (inputPassword.getText().toString().trim().isEmpty()) {
            CustomToast.makeCustomToast(this, R.drawable.ic_lock, "Enter password").show();
            return false;
        } else {
            return true;
        }
    }

    public void signUp(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

//    public void forgotPassword(View view) {
//        if(inputEmail.getText().toString().isEmpty()){
//            Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_LONG).show();
//            return;
//        }
//        Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
//        intent.putExtra("gmail",inputEmail.getText().toString());
//        Log.d("gmail",inputEmail.getText().toString());
//        startActivity(intent);
//    }

    private void khoiTaoDatabase() {
        //Tạo bảng
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS User (FirstName VARCHAR(100),LastName VARCHAR(100),Email VARCHAR(100),Password VARCHAR(100), HinhAnh BLOB)");
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS TheLoai (MaTL VARCHAR(5), TenTL VARCHAR(100), HinhAnh BLOB)");
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS ChuongTrinh (MaCT VARCHAR(5),TenCT VARCHAR,MaTL VARCHAR(5), HinhAnh BLOB)");
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS BienTapVien(MaBTV VARCHAR(5), HoTen VARCHAR,NgaySinh VARCHAR,SDT VARCHAR, HinhAnh BLOB)");
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS ThongTinPhatSong(MaPS VARCHAR(5),MaCT VARCHAR(5),MaBTV VARCHAR(5),NgayPS VARCHAR, ThoiLuong INTEGER, HinhAnh BLOB)");

        //user
        User user1 = new User( "Quan", "Truong" ,"quansonvu2408@gmail.com","123456", getByteArrayFromImageResource(R.drawable.btv) );
        User user2 = new User( "Phuc", "Pham" ,"phuphuc123@gmail.com","123456", getByteArrayFromImageResource(R.drawable.btv) );
        User user3 = new User( "Trinh", "Nguyen" ,"reintrinh123@gmail.com","123456", getByteArrayFromImageResource(R.drawable.btv) );
        User user4 = new User( "Mo", "Ha" ,"moha123@gmail.com","123456", getByteArrayFromImageResource(R.drawable.btv) );

        if(!dbHelper.hasData("User")) {
            dbHelper.themUser(user1);
            dbHelper.themUser(user2);
            dbHelper.themUser(user3);
            dbHelper.themUser(user4);
        }

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
        ChuongTrinh chuongTrinh1 = new ChuongTrinh("CT1", "Bí mật đêm khuya", "TL1", getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
        ChuongTrinh chuongTrinh2 = new ChuongTrinh("CT2", "Thách thức danh hài", "TL1", getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
        ChuongTrinh chuongTrinh3 = new ChuongTrinh("CT3", "Gặp nhau để cười", "TL2", getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
        ChuongTrinh chuongTrinh4 = new ChuongTrinh("CT4", "Ai là triệu phú", "TL2", getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
        ChuongTrinh chuongTrinh5 = new ChuongTrinh("CT5", "Khúc vọng xưa", "TL3", getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));

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
        BienTapVien bienTapVien7 = new BienTapVien("BTV6", "Trung Hiền", "22/02/2000", "0935856530", getByteArrayFromImageResource(R.drawable.btv));
        BienTapVien bienTapVien8 = new BienTapVien("BTV8", "Nguyễn Võ Duy Long", "22/02/2000", "0935856530", getByteArrayFromImageResource(R.drawable.btv));

        if(!dbHelper.hasData("BienTapVien")) {
            dbHelper.themBienTapVien(bienTapVien1);
            dbHelper.themBienTapVien(bienTapVien2);
            dbHelper.themBienTapVien(bienTapVien3);
            dbHelper.themBienTapVien(bienTapVien4);
            dbHelper.themBienTapVien(bienTapVien5);
            dbHelper.themBienTapVien(bienTapVien6);
            dbHelper.themBienTapVien(bienTapVien7);
            dbHelper.themBienTapVien(bienTapVien8);
        }

        if (!dbHelper.hasData("ThongTinPhatSong")) {
            ThongTinPhatSong thongTinPhatSong1 =
                    new ThongTinPhatSong("01", chuongTrinh1, bienTapVien1, "14/02/2022", 20,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong2 =
                    new ThongTinPhatSong("02", chuongTrinh1, bienTapVien2, "15/02/2022", 20,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong3 =
                    new ThongTinPhatSong("03", chuongTrinh1, bienTapVien3, "21/02/2022", 20,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong4 =
                    new ThongTinPhatSong("04", chuongTrinh1, bienTapVien4, "22/02/2022", 20,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong5 =
                    new ThongTinPhatSong("05", chuongTrinh2, bienTapVien1, "14/02/2022", 45,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong6 =
                    new ThongTinPhatSong("06", chuongTrinh2, bienTapVien3, "21/02/2022", 45,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong7 =
                    new ThongTinPhatSong("07", chuongTrinh2, bienTapVien2, "28/02/2022", 45,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong8 =
                    new ThongTinPhatSong("08", chuongTrinh3, bienTapVien5, "01/03/2022", 30,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong9 =
                    new ThongTinPhatSong("09", chuongTrinh3, bienTapVien6, "08/03/2022", 30,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
            ThongTinPhatSong thongTinPhatSong10 =
                    new ThongTinPhatSong("10", chuongTrinh5, bienTapVien7, "10/03/2022", 25,
                            getByteArrayFromImageResource(R.drawable.avatar_chuongtrinh));
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
    private byte[] getByteArrayFromImageResource(int resourceId) {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(resourceId);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        return ImageUtil.getByteArrayFromBitmap(bitmap);
    }
}
