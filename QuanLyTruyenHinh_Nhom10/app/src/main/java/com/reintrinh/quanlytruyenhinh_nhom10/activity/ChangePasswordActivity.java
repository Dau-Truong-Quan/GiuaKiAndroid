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

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtOldPass, edtNewPass, edtConfirmPass;
    private Button btnChangePass, btnExit;

    private PreferenceManager preferenceManager;
    private QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;
    private User user;

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
            Toast.makeText(this, "Bạn chưa nhập mật khẩu cũ!", Toast.LENGTH_LONG).show();
            return;
        }
        String email = preferenceManager.getString(Constants.KEY_EMAIL);
        User user = quanLyTruyenHinhHelper.checkUserExist(email, oldPass);
        if (user == null) {
            Toast.makeText(this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_LONG).show();
            return;
        }
        String newPass = edtNewPass.getText().toString().trim();
        if (newPass.isEmpty()) {
            Toast.makeText(this, "Bạn chưa nhập mật khẩu mới!", Toast.LENGTH_LONG).show();
            return;
        }
        String confirmPass = edtConfirmPass.getText().toString().trim();
        if (!confirmPass.equals(newPass)) {
            Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu mới phải giống nhau!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            quanLyTruyenHinhHelper.capNhatMatKhauUser(newPass, email);
            Toast.makeText(this, "Đã thay đổi mật khẩu của bạn!", Toast.LENGTH_LONG).show();
            finish();
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }
}