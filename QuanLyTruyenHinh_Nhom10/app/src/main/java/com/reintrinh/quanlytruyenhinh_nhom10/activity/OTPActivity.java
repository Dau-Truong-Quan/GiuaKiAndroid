package com.reintrinh.quanlytruyenhinh_nhom10.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.widget.CustomToast;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


import java.util.ArrayList;

import java.util.Random;

public class OTPActivity extends AppCompatActivity {
    private EditText editTextConfirmOTP;
    private Button buttonConfirm,btnThoat;
    public String randomOTP="";
    private String gmail="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        gmail=getIntent().getStringExtra("gmail");

        randomOTP=createOTP();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        sendOTP();
        setControl();
        setEvent();
    }

    private void sendOTP() {
        final String username="quansonvu2408@gmail.com";
        final String password="mkzufdlqrsegxzpt";
        String messageToSend="Mã OTP xác nhận :"+ randomOTP;
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
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(gmail));
            message.setSubject("Quản lý truyền hình nhóm 10");
            message.setText(messageToSend);
            Transport.send(message);
            CustomToast.makeCustomToast(this, R.drawable.ic_mail_outline, "Send Email successfully").show();
            //Toast.makeText(this,"Send Email successfully",Toast.LENGTH_LONG).show();
        }catch (MessagingException e){
            throw  new RuntimeException(e);
        }

    }

    private void setEvent() {
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextConfirmOTP.getText().toString().equals(randomOTP)){
                    Intent intent = new Intent(OTPActivity.this, NewPasswordActivity.class);
                    intent.putExtra("gmail",gmail);
                    startActivity(intent);

                }
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setControl() {
        editTextConfirmOTP=findViewById(R.id.editTextConfirmOTP);
        buttonConfirm=findViewById(R.id.buttonConfirm);
        btnThoat = findViewById(R.id.buttonThoatHiem);
    }

    public String createOTP(){
        ArrayList<String> around = new ArrayList<>();
        String[] arrCode = new String[4];
        for(int i= 0; i<=9; i++){
            Integer tam = new Integer(i);
            around.add(tam.toString());
        }
        Random rand = new Random();
        randomOTP = "";
        for(int i =0; i< 4; i++){
            int randomInt = rand.nextInt(9);
            arrCode[i] = around.get(randomInt);
            System.out.println(arrCode[i]);
            randomOTP+=arrCode[i];
        }
        return randomOTP;
    }
}
