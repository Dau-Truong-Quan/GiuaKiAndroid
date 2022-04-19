package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.reintrinh.quanlytruyenhinh_nhom10.Constant.Constants;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.activity.ThongTinPhatSongActivity;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.listener.OnSaveClickListener;
import com.reintrinh.quanlytruyenhinh_nhom10.model.User;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;
import com.reintrinh.quanlytruyenhinh_nhom10.util.PreferenceManager;

import java.io.IOException;
import java.util.List;

public class TaiKhoanFragment extends Fragment {

    private View view;
    private EditText edtFirstName, edtLastName, edtEmail;
    private ImageView imgUserAvatar;
    private Button btnSelectImage, btnSave, btnCancel;

    private PreferenceManager preferenceManager;
    private QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private OnSaveClickListener onSaveClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tai_khoan, container, false);
        setControl();
        loadUserInfo();
        setEvent();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                imgUserAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void setControl() {
        edtFirstName = view.findViewById(R.id.edt_first_name);
        edtLastName = view.findViewById(R.id.edt_last_name);
        edtEmail = view.findViewById(R.id.edt_email);
        imgUserAvatar = view.findViewById(R.id.img_user_avatar);
        btnSelectImage = view.findViewById(R.id.btn_select_image);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);

        preferenceManager = new PreferenceManager(getContext());
        quanLyTruyenHinhHelper = QuanLyTruyenHinhHelper.getInstance(getContext());
    }

    private void loadUserInfo() {
        String email = preferenceManager.getString(Constants.KEY_EMAIL);
        User user = quanLyTruyenHinhHelper.getUserByEmail(email);
        if (user == null) return;
        edtFirstName.setText(user.getFirstname());
        edtLastName.setText(user.getLastname());
        edtEmail.setText(user.getEmail());
        imgUserAvatar.setImageBitmap(ImageUtil.getBitmapFromByteArray(user.getImg()));
    }

    private void setEvent() {
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ImagePicker.with(TaiKhoanFragment.this)
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(REQUEST_CODE_SELECT_IMAGE);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = edtFirstName.getText().toString().trim();
                if (firstName.isEmpty()) {
                    Toast.makeText(getContext(), "Bạn chưa nhập tên!", Toast.LENGTH_LONG).show();
                    return;
                }
                String lastName = edtLastName.getText().toString().trim();
                if (lastName.isEmpty()) {
                    Toast.makeText(getContext(), "Bạn chưa nhập họ!", Toast.LENGTH_LONG).show();
                    return;
                }
                String email = edtEmail.getText().toString().trim();
                BitmapDrawable bitmapDrawable = (BitmapDrawable)imgUserAvatar.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] hinhAnh = ImageUtil.getByteArrayFromBitmap(bitmap);
                User user = new User(firstName, lastName, email, null, hinhAnh);
                try {
                    quanLyTruyenHinhHelper.suaThongTinUser(user);
                    onSaveClickListener.onClick();
                    Toast.makeText(getContext(), "Đã cập nhật thông tin của bạn!", Toast.LENGTH_LONG).show();
                }
                catch (Exception ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUserInfo();
            }
        });
    }

    public void setOnSaveClickListener(OnSaveClickListener onSaveClickListener) {
        this.onSaveClickListener = onSaveClickListener;
    }
}