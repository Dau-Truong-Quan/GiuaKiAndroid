package com.reintrinh.quanlytruyenhinh_nhom10;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView txtEmailtoRecoverPassword;
    Button btnTiepTuc,btnThoat;
    String gmail="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        setControl();
        setEvent();
    }

    private void setControl() {
        txtEmailtoRecoverPassword = findViewById(R.id.txtEmail);
        btnTiepTuc = findViewById(R.id.btnNext);
        btnThoat = findViewById(R.id.buttonThoat);
    }

    private void setEvent() {
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtEmailtoRecoverPassword.getText().toString().isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Enter email", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(ForgotPasswordActivity.this, OTPActivity.class);
                intent.putExtra("gmail",txtEmailtoRecoverPassword.getText().toString());

                startActivity(intent);
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}
