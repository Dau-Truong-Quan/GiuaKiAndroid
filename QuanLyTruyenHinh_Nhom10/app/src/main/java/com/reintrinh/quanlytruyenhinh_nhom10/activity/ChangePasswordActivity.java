package com.reintrinh.quanlytruyenhinh_nhom10.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.reintrinh.quanlytruyenhinh_nhom10.Constant.Constants;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.User;
import com.reintrinh.quanlytruyenhinh_nhom10.util.PreferenceManager;
import com.reintrinh.quanlytruyenhinh_nhom10.widget.CustomToast;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtOldPass, edtNewPass, edtConfirmPass;
    private Button btnChangePass, btnExit;

    private PreferenceManager preferenceManager;
    private QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setControl();
        setEvent();
    }

    private void setControl() {
        edtOldPass = findViewById(R.id.edt_old_pass);
        edtNewPass = findViewById(R.id.edt_new_pass);
        edtConfirmPass = findViewById(R.id.edt_confirm_pass);
        btnChangePass = findViewById(R.id.btn_change_password);
        btnExit = findViewById(R.id.btn_exit);

        preferenceManager = new PreferenceManager(this);
        quanLyTruyenHinhHelper = QuanLyTruyenHinhHelper.getInstance(this);
    }

    private void setEvent() {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUserPassword();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void changeUserPassword() {
        String oldPass = edtOldPass.getText().toString().trim();
        if (oldPass.isEmpty()) {
            CustomToast.makeCustomToast(this, R.drawable.ic_key, "Bạn chưa nhập mật khẩu cũ!").show();
            return;
        }
        String email = preferenceManager.getString(Constants.KEY_EMAIL);
        User user = quanLyTruyenHinhHelper.checkUserExist(email, oldPass);
        if (user == null) {
            CustomToast.makeCustomToast(this, R.drawable.ic_key_off, "Mật khẩu cũ không chính xác!").show();
            return;
        }
        String newPass = edtNewPass.getText().toString().trim();
        if (newPass.isEmpty()) {
            CustomToast.makeCustomToast(this, R.drawable.ic_lock, "Bạn chưa nhập mật khẩu mới!").show();
            return;
        }
        String confirmPass = edtConfirmPass.getText().toString().trim();
        if (!confirmPass.equals(newPass)) {
            CustomToast.makeCustomToast(this, R.drawable.ic_lock_reset, "Mật khẩu mới và xác nhận mật khẩu mới phải giống nhau").show();
            return;
        }
        try {
            quanLyTruyenHinhHelper.capNhatMatKhauUser(newPass, email);
            CustomToast.makeCustomToast(this, R.drawable.ic_check, "Đã thay đổi mật khẩu của bạn!").show();
            finish();
        }
        catch (Exception ex) {
            CustomToast.makeCustomToast(this, R.drawable.ic_error, ex.getMessage()).show();
            return;
        }
    }
}