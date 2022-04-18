package com.reintrinh.quanlytruyenhinh_nhom10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends AppCompatActivity {
    EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword;
    Button buttonSignUp;
    QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setControl();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        quanLyTruyenHinhHelper = QuanLyTruyenHinhHelper.getInstance(this);
        setEvent();



    }

    private void setEvent() {


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidSignUpDetails()) {
                    User user = new User(
                            inputFirstName.getText().toString().trim(),
                            inputLastName.getText().toString().trim(),
                            inputEmail.getText().toString().trim(),
                            inputPassword.getText().toString().trim()
                    );
                    int i = quanLyTruyenHinhHelper.themUser(user);
                    if (i == 1) {
                        showToast("Email đã tồn tại!");
                    } else if (i == 0) {

                        sendMail(inputEmail.getText().toString().trim());
                        showToast("Đăng ký thành công! Đã gửi thông báo tới gmail của bạn");
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    private void sendMail(String email) {
        final String username="quansonvu2408@gmail.com";
        final String password="mkzufdlqrsegxzpt";
        String messageToSend=" Chào mừng bạn đến với Quản lý truyền hình nhóm 10";
        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session=Session.getInstance(properties,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(username,password);
                    }
                });
        try {
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
            message.setSubject("Đăng ký thành công!");
            message.setText(messageToSend);
            Transport.send(message);

        }catch (MessagingException e){
            throw  new RuntimeException(e);
        }

    }
    private void setControl() {
        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

    }

    public void signIn(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    private Boolean isValidSignUpDetails() {
        if (inputFirstName.getText().toString().trim().isEmpty()) {
            showToast("Enter first name");
            return false;
        } else if (inputLastName.getText().toString().trim().isEmpty()) {
            showToast("Enter last name");
            return false;
        } else if (inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString().trim()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!inputPassword.getText().toString().trim().equals(inputConfirmPassword.getText().toString().trim())) {
            showToast("Password & confirm password must be same");
            return false;
        } else return true;
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
