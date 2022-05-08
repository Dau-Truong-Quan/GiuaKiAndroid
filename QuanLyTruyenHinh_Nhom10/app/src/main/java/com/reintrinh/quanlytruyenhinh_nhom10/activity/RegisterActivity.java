package com.reintrinh.quanlytruyenhinh_nhom10.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.User;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;
import com.reintrinh.quanlytruyenhinh_nhom10.widget.CustomToast;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends AppCompatActivity {
    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword;
    private ImageView imgUserAvatar;
    private Button btnSelectImage;
    private Button buttonSignUp;
    private QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;

    public static final int REQUEST_CODE_SELECT_IMAGE = 1;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imgUserAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                CustomToast.makeCustomToast(this, R.drawable.ic_error, e.getMessage()).show();
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void setEvent() {
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ImagePicker.with(RegisterActivity.this)
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(REQUEST_CODE_SELECT_IMAGE);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        CustomToast.makeCustomToast(RegisterActivity.this, R.drawable.ic_error, "Permission Denied\n" + deniedPermissions.toString()).show();
                        //Toast.makeText(RegisterActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidSignUpDetails()) {
                    User user = new User(
                            inputFirstName.getText().toString().trim(),
                            inputLastName.getText().toString().trim(),
                            inputEmail.getText().toString().trim(),
                            inputPassword.getText().toString().trim(),
                            ImageUtil.getByteArrayFromBitmap(((BitmapDrawable)imgUserAvatar.getDrawable()).getBitmap())
                    );
                    int i = quanLyTruyenHinhHelper.themUser(user);
                    if (i == 1) {
                        showToast("Email đã tồn tại!");
                    } else if (i == 0) {
                        sendMail(inputEmail.getText().toString().trim());
                        CustomToast.makeCustomToast(RegisterActivity.this, R.drawable.ic_check, "Đăng ký thành công! Đã gửi thông báo tới gmail của bạn").show();
                        //showToast("Đăng ký thành công! Đã gửi thông báo tới gmail của bạn");
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
        imgUserAvatar = findViewById(R.id.img_user_avatar);
        btnSelectImage = findViewById(R.id.btn_select_image);
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
        CustomToast.makeCustomToast(RegisterActivity.this, R.drawable.ic_error, message).show();
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
