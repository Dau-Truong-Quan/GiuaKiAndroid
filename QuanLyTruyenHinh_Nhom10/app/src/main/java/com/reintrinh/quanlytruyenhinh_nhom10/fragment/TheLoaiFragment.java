package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.TheLoaiAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiFragment extends Fragment {

    private LinearLayout mainContent;
    private RecyclerView recycleView;

    private Button buttonThem;
    private TheLoaiAdapter theLoaiAdapter;
    private TextView txtTongTheLoai;

    private List<TheLoai> arrayTheLoai = new ArrayList<>();

    private SearchView searchView;
    private View mView;

    private QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;
    public static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private ImageView chonHinhAnhTheLoai;

    public TheLoaiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_the_loai, container, false);

        quanLyTruyenHinhHelper = new QuanLyTruyenHinhHelper(getContext());
        setControl();

        initRecyclerView();
        actionGetData();

        setEvent();

        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            chonHinhAnhTheLoai.setImageURI(uri);
        }
    }

    private void setEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                theLoaiAdapter.filter(s);
                return true;
            }
        });

        buttonThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInsert();
            }
        });
    }

    private void setControl() {
        buttonThem = mView.findViewById(R.id.buttonThem);
        txtTongTheLoai = mView.findViewById(R.id.txtTongTheLoai);
        recycleView = mView.findViewById(R.id.recycleView);
        searchView = mView.findViewById(R.id.searchView);
        mainContent = mView.findViewById(R.id.main_theloai);
    }

    private void initRecyclerView() {
        recycleView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycleView.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recycleView.addItemDecoration(itemDecoration);

        arrayTheLoai = quanLyTruyenHinhHelper.getAllTheLoai();
        theLoaiAdapter = new TheLoaiAdapter(this, arrayTheLoai, new TheLoaiAdapter.IClickItemListener() {
            @Override
            public void onClickEditItem(TheLoai theLoai) {
                dialogUpdate(theLoai);
            }

            @Override
            public void onClickDeleteItem(TheLoai theLoai) {
                Cursor cursor = quanLyTruyenHinhHelper.getData(String.format("SELECT * FROM ChuongTrinh WHERE MaTL = '%s'", theLoai.getMaTL()));
                if (cursor != null && cursor.moveToNext()) {
                    Toast.makeText(getContext(), "Thể loại này đã có chương trình", Toast.LENGTH_LONG).show();
                    return;
                }
                dialogDelete(theLoai);
            }
        });
        recycleView.setAdapter(theLoaiAdapter);
    }

    private void dialogInsert() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themtheloai);
        dialog.setCancelable(false);

        chonHinhAnhTheLoai = dialog.findViewById(R.id.img_chon_hinh_anh_the_loai);
        Button btnChonHinhAnh = dialog.findViewById(R.id.btnChonHinhAnh);
        EditText editmaTL = dialog.findViewById(R.id.addmaTL);
        EditText editTenTL = dialog.findViewById(R.id.addTenTL);
        Button btnThem = dialog.findViewById(R.id.btnThem);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        btnChonHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ImagePicker.with(TheLoaiFragment.this)
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

        // Sự kiện thêm thể loại
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maTheLoaiMoi = String.valueOf(editmaTL.getText());
                String tenTheLoaiMoi = String.valueOf(editTenTL.getText());
                if (TextUtils.isEmpty(String.valueOf(editTenTL.getText())) || TextUtils.isEmpty(tenTheLoaiMoi)) {
                    Toast.makeText(getContext(), "Nội dung cần thêm chưa được nhập", Toast.LENGTH_SHORT).show();
                    return;
                }

                BitmapDrawable bitmapDrawable = (BitmapDrawable)chonHinhAnhTheLoai.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] hinhAnh = ImageUtil.getByteArrayFromBitmap(bitmap);

                //Kiểm tra trùng
                Cursor dataTheLoai = quanLyTruyenHinhHelper.getData("SELECT * FROM TheLoai");
                ArrayList<TheLoai> arrayTheLoai = new ArrayList<TheLoai>();
                TheLoai theLoaiTam;
                while (dataTheLoai.moveToNext()) {
                    theLoaiTam = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1), dataTheLoai.getBlob(2));
                    arrayTheLoai.add(theLoaiTam);
                }

                for (int i = 0; i < arrayTheLoai.size(); i++) {
                    if(arrayTheLoai.get(i).getMaTL().equalsIgnoreCase(maTheLoaiMoi)){
                        Toast.makeText(getContext(), "Mã thể loại đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                for (int i = 0; i < arrayTheLoai.size(); i++) {
                    if(arrayTheLoai.get(i).getTenTL().equalsIgnoreCase(tenTheLoaiMoi)){
                        Toast.makeText(getContext(), "Tên thể loại đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                TheLoai theLoai = new TheLoai(maTheLoaiMoi, tenTheLoaiMoi, hinhAnh);
                quanLyTruyenHinhHelper.themTheLoai(theLoai);
                dialog.dismiss();
                actionGetData();
                theLoaiAdapter.updateDataSearch();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void actionGetData() {
        arrayTheLoai = quanLyTruyenHinhHelper.getAllTheLoai();
        if (arrayTheLoai != null) {
            txtTongTheLoai.setText(String.valueOf(arrayTheLoai.size()));
        } else {
            txtTongTheLoai.setText("0");
        }
        theLoaiAdapter.setData(arrayTheLoai);
    }

    public void dialogUpdate(TheLoai theLoai) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chinhsua_theloai);
        dialog.setCancelable(false);

        chonHinhAnhTheLoai = dialog.findViewById(R.id.img_chon_hinh_anh_the_loai);
        Button btnChonHinhAnh = dialog.findViewById(R.id.btnChonHinhAnh);
        EditText editmaTL = dialog.findViewById(R.id.editmaTL);
        editmaTL.setEnabled(false);
        EditText editTenTL = dialog.findViewById(R.id.editTenTL);
        Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        //set dữ liệu
        byte[] hinhAnh = theLoai.getHinhAnh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        chonHinhAnhTheLoai.setImageBitmap(bitmap);
        editmaTL.setText(theLoai.getMaTL());
        editTenTL.setText(theLoai.getTenTL());

        //Bắt sự kiện nút bấm
        btnChonHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ImagePicker.with(TheLoaiFragment.this)
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

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenTL = String.valueOf(editTenTL.getText());
                if (TextUtils.isEmpty(tenTL)) {
                    Toast.makeText(getContext(), "Nội dung cần sửa chưa được nhập", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                BitmapDrawable bitmapDrawable = (BitmapDrawable)chonHinhAnhTheLoai.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] hinhAnh = ImageUtil.getByteArrayFromBitmap(bitmap);

                //Kiểm tra trùng
                Cursor dataTheLoai = quanLyTruyenHinhHelper.getData("SELECT * FROM TheLoai");
                ArrayList<TheLoai> arrayTheLoai = new ArrayList<TheLoai>();
                TheLoai theLoaiTam;
                while (dataTheLoai.moveToNext()) {
                    theLoaiTam = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1), dataTheLoai.getBlob(2));
                    arrayTheLoai.add(theLoaiTam);
                }

                for (int i = 0; i < arrayTheLoai.size(); i++) {
                    if (arrayTheLoai.get(i).getTenTL().equalsIgnoreCase(theLoai.getTenTL())) {
                        break;
                    }
                    if (arrayTheLoai.get(i).getTenTL().equalsIgnoreCase(tenTL)){
                        Toast.makeText(getContext(), "Tên thể loại đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                TheLoai theLoai1 = new TheLoai(theLoai.getMaTL(), tenTL, hinhAnh);
                quanLyTruyenHinhHelper.suaTheLoai(theLoai1);
                dialog.dismiss();
                actionGetData();
                theLoaiAdapter.updateDataSearch();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void dialogDelete(TheLoai theLoai) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa thể loại này không?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            quanLyTruyenHinhHelper.xoaTheLoai(theLoai.getMaTL());
                            actionGetData();
                            theLoaiAdapter.updateDataSearch();
                            Snackbar snackbar = Snackbar.make(mainContent, "Đã xóa thể loại!", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Hoàn tác", view -> {
                                quanLyTruyenHinhHelper.themTheLoai(theLoai);
                                Toast.makeText(getContext(), "Đã hoàn tác!", Toast.LENGTH_SHORT).show();
                                actionGetData();
                                theLoaiAdapter.updateDataSearch();
                            });
                            snackbar.setActionTextColor(Color.CYAN);
                            snackbar.show();
                        }
                        catch (Exception ex) {
                            Toast.makeText(getContext(), "Xảy ra lỗi khi thêm cập nhật thể loại! Vui lòng thử lại\n" +
                                    ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}