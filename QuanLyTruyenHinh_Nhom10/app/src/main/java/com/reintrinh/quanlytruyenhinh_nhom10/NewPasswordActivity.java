package com.reintrinh.quanlytruyenhinh_nhom10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;

public class NewPasswordActivity extends Activity {
    EditText editTextNewPassword;
    Button buttonConfirmNewPassword;
    String gmail="";
    QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        quanLyTruyenHinhHelper =  QuanLyTruyenHinhHelper.getInstance(this);
        gmail=getIntent().getStringExtra("gmail");
        setControl();
        setEvent();
    }

    private void setEvent() {
        buttonConfirmNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(editTextNewPassword.getText())||TextUtils.isEmpty(editTextNewPassword.getText())) {
                    Toast.makeText(NewPasswordActivity.this, "Nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                quanLyTruyenHinhHelper.capNhatMatKhauUser(String.valueOf(editTextNewPassword.getText().toString().trim()),gmail);
                Toast.makeText(NewPasswordActivity.this,"thay đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        editTextNewPassword=findViewById(R.id.editTextNewPassword);
        buttonConfirmNewPassword=findViewById(R.id.buttonConfirmNewPassword);
    }
}
