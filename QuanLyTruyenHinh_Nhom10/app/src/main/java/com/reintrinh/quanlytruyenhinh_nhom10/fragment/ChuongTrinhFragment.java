package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.reintrinh.quanlytruyenhinh_nhom10.util.PreferenceManager;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.ChuongTrinhAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.TheLoaiArrayAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;
import com.reintrinh.quanlytruyenhinh_nhom10.widget.CustomToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChuongTrinhFragment extends Fragment {
    private PreferenceManager preferenceManager;
    LinearLayout mainContent;
    RecyclerView recycleView;
    Spinner spinnerTheLoai;
    QuanLyTruyenHinhHelper dbHelper;


    Button buttonThem;
    ChuongTrinhAdapter chuongTrinhAdapter;
    TextView txtTongChuongTrinh;

    List<ChuongTrinh> arrayChuongTrinh = new ArrayList<>();
    TheLoai theLoai;

    SearchView searchView;
    ImageView ivChonHinhAnh;
    View view;

    public static final int REQUEST_CODE_SELECT_IMAGE = 1;

    public ChuongTrinhFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chuong_trinh, container, false);
        preferenceManager = new PreferenceManager(getContext());
        setControl();

        ArrayList<TheLoai> arrayTheLoai= new ArrayList<TheLoai>();
        ArrayList<String> arrayTenTheLoai = new ArrayList<String>();
        TheLoai theLoai1 = new TheLoai("All", "T???t c???", getByteArrayFromImageResource(R.drawable.theloai));
//        arrayTenTheLoai.add("");
        arrayTheLoai.add(theLoai1);
        //Load data
        dbHelper = new QuanLyTruyenHinhHelper(getContext());

        Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai");

        TheLoai theLoai;
        while (dataTheLoai.moveToNext()) {
            theLoai = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1), dataTheLoai.getBlob(2));
            arrayTheLoai.add(theLoai);
            arrayTenTheLoai.add(theLoai.getTenTL());
        }

        TheLoaiArrayAdapter arrayAdapter = new TheLoaiArrayAdapter(getContext(),R.layout.list_layout_theloai_select, arrayTheLoai);
        spinnerTheLoai.setAdapter(arrayAdapter);

        /*Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle.getSerializable(getString(R.string.key_category)) == null) {
            theLoai = null;
        } else {
            theLoai = (TheLoai) bundle.getSerializable(getString(R.string.key_category));
        }
        if (theLoai == null) {
            spinnerTheLoai.setSelection(0);
        } else {
            spinnerTheLoai.setSelection(arrayTenTheLoai.indexOf(theLoai.getTenTL()));
        }*/

        actionGetData();

        setEvent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<TheLoai> arrayTheLoai= new ArrayList<TheLoai>();
        ArrayList<String> arrayTenTheLoai = new ArrayList<String>();
        TheLoai theLoai1 = new TheLoai("All", "T???t c???", getByteArrayFromImageResource(R.drawable.theloai));
//        arrayTenTheLoai.add("");
        arrayTheLoai.add(theLoai1);
        //Load data
        dbHelper = new QuanLyTruyenHinhHelper(getContext());

        Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai");

        TheLoai theLoai;
        while (dataTheLoai.moveToNext()) {
            theLoai = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1), dataTheLoai.getBlob(2));
            arrayTheLoai.add(theLoai);
            arrayTenTheLoai.add(theLoai.getTenTL());
        }

        TheLoaiArrayAdapter arrayAdapter = new TheLoaiArrayAdapter(getContext(),R.layout.list_layout_theloai_select, arrayTheLoai);
        spinnerTheLoai.setAdapter(arrayAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                ivChonHinhAnh.setImageBitmap(bitmap);
            } catch (IOException e) {
                CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, e.getMessage()).show();
                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void setEvent() {
        spinnerTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actionGetData();
                chuongTrinhAdapter.updateListSearch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                chuongTrinhAdapter.filter(s);
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
        buttonThem = view.findViewById(R.id.buttonThem);
        spinnerTheLoai = view.findViewById(R.id.spinnerTheLoai);
        txtTongChuongTrinh = view.findViewById(R.id.txtTongChuongTrinh);
        recycleView = view.findViewById(R.id.recycleView);
        searchView = view.findViewById(R.id.svChuongTrinh);
        mainContent = view.findViewById(R.id.main_chuongtrinh);

    }

    private void dialogInsert() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themchuongtrinh);

        EditText editmaCT = (EditText) dialog.findViewById(R.id.addmaCT);
        EditText editTenCT = (EditText) dialog.findViewById(R.id.addTenCT);
        Button btnChonHinhAnh = dialog.findViewById(R.id.btnChonHinhAnhAddCT);
        Button btnThem = (Button) dialog.findViewById(R.id.btnThem);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);

        ivChonHinhAnh = dialog.findViewById(R.id.addHinhAnhCT);

        Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai");
        ArrayList<TheLoai> arrayTheLoaiTam = new ArrayList<TheLoai>();
        // th??m d??? li???u v??o spin
        Spinner spinnerTheLoaiTam = dialog.findViewById(R.id.spinnerMaTheLoai);
        ArrayList<String> arrayTenTheLoaiTam = new ArrayList<String>();
        TheLoai theLoaiEdit;
        while (dataTheLoai.moveToNext()) {
            theLoaiEdit = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1), dataTheLoai.getBlob(2));
            arrayTheLoaiTam.add(theLoaiEdit);
            arrayTenTheLoaiTam.add(theLoaiEdit.getTenTL());
        }
        TheLoaiArrayAdapter arrayAdapterTam = new TheLoaiArrayAdapter(getContext(), R.layout.list_layout_theloai_select, arrayTheLoaiTam) {};
        spinnerTheLoaiTam.setAdapter(arrayAdapterTam);

        btnChonHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        // s??? ki???n th??m ch????ng tr??nh
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maChuongTrinhMoi = String.valueOf(editmaCT.getText()).toUpperCase();
                String tenChuongTrinhMoi = String.valueOf(editTenCT.getText());
                String maTheLoai = String.valueOf(arrayTheLoaiTam.get(spinnerTheLoaiTam.getSelectedItemPosition()).getMaTL());
                if (TextUtils.isEmpty(String.valueOf(editTenCT.getText())) || TextUtils.isEmpty(tenChuongTrinhMoi) || TextUtils.isEmpty(maTheLoai)) {
                    CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "N???i dung c???n th??m ch??a ???????c nh???p").show();
                    //Toast.makeText(getContext(), "N???i dung c???n th??m ch??a ???????c nh???p", Toast.LENGTH_SHORT).show();

                    return;
                }

                //kiem tra trung
                if(dbHelper.getChuongTrinhByMaCT(maChuongTrinhMoi) != null) {
                    CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "M?? ch????ng tr??nh ???? t???n t???i").show();
                    //Toast.makeText(getContext(), "M?? ch????ng tr??nh ???? t???n t???i", Toast.LENGTH_SHORT).show();
                    return;
                }

                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivChonHinhAnh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] hinhAnh = ImageUtil.getByteArrayFromBitmap(bitmap);

                ChuongTrinh newCT = new ChuongTrinh(maChuongTrinhMoi, tenChuongTrinhMoi, maTheLoai, hinhAnh);

                dbHelper.themChuongTrinh(newCT);
                CustomToast.makeCustomToast(getActivity(), R.drawable.ic_check, "Th??m ch????ng tr??nh m???i th??nh c??ng").show();
                //Toast.makeText(getContext(), "Th??m ch????ng tr??nh m???i th??nh c??ng", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                actionGetData();
                chuongTrinhAdapter.updateListSearch();
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

    public void actionGetData() {
        arrayChuongTrinh.clear();
        TheLoai tl = (TheLoai) spinnerTheLoai.getSelectedItem();
        if (tl.getMaTL().equals("All")) {

            Cursor dataChuongTrinh = dbHelper.getData("SELECT * FROM ChuongTrinh ORDER BY MaCT");


            ChuongTrinh chuongTrinh;
            while (dataChuongTrinh.moveToNext()) {
                chuongTrinh = new ChuongTrinh(dataChuongTrinh.getString(0), dataChuongTrinh.getString(1), dataChuongTrinh.getString(2), dataChuongTrinh.getBlob(3));
                arrayChuongTrinh.add(chuongTrinh);
            }

            txtTongChuongTrinh.setText("T???ng s??? ch????ng tr??nh: " + arrayChuongTrinh.size());
        } else {
//            Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai WHERE MaTL='" + tl.getMaTL() + "'");
//            String ma = "";
//
//            while (dataTheLoai.moveToNext()) {
//                ma = dataTheLoai.getString(0);
//                theLoai = new TheLoai(ma, dataTheLoai.getString(1), dataTheLoai.getBlob(2));
//            }
            Cursor dataChuongTrinh = dbHelper.getData("SELECT * FROM ChuongTrinh WHERE MaTL = '" + tl.getMaTL() + "'");

            ChuongTrinh chuongTrinh;
            while (dataChuongTrinh.moveToNext()) {
                chuongTrinh = new ChuongTrinh(dataChuongTrinh.getString(0), dataChuongTrinh.getString(1), dataChuongTrinh.getString(2), dataChuongTrinh.getBlob(3));
                arrayChuongTrinh.add(chuongTrinh);
            }
            txtTongChuongTrinh.setText("T???ng s??? ch????ng tr??nh: " + arrayChuongTrinh.size());
        }

        chuongTrinhAdapter = new ChuongTrinhAdapter(this, arrayChuongTrinh);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycleView.setAdapter(chuongTrinhAdapter);
    }

    public void dialogUpdate(ChuongTrinh ct) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chinhsua_chuongtrinh);

        EditText editmaCT = (EditText) dialog.findViewById(R.id.editmaCT);
        editmaCT.setEnabled(false);
        EditText editTenCT = (EditText) dialog.findViewById(R.id.editTenCT);
        Button btnChonHinhAnh = dialog.findViewById(R.id.btnChonHinhAnhEditCT);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btnXacNhan);
        Button btnXoa = (Button) dialog.findViewById(R.id.btnXoa);

        ivChonHinhAnh = dialog.findViewById(R.id.editHinhAnhCT);
        ivChonHinhAnh.setImageBitmap(ImageUtil.getBitmapFromByteArray(ct.getHinhAnh()));

        // th??m d??? li???u v??o spin
        Spinner spinnerTheLoaiTam = dialog.findViewById(R.id.spinnerMaTheLoaiChinhSua);
        Cursor dataTheLoai = dbHelper.getData("SELECT * FROM TheLoai");
        ArrayList<TheLoai> arrayTheLoaiTam = new ArrayList<TheLoai>();
        ArrayList<String> arrayTenTheLoaiTam = new ArrayList<String>();
        TheLoai theLoaiEdit;
        while (dataTheLoai.moveToNext()) {
            theLoaiEdit = new TheLoai(dataTheLoai.getString(0), dataTheLoai.getString(1), dataTheLoai.getBlob(2));
            arrayTheLoaiTam.add(theLoaiEdit);
            arrayTenTheLoaiTam.add(theLoaiEdit.getTenTL());
        }
        TheLoaiArrayAdapter arrayAdapterTam = new TheLoaiArrayAdapter(getContext(), R.layout.list_layout_theloai_select, arrayTheLoaiTam);
        spinnerTheLoaiTam.setAdapter(arrayAdapterTam);

        int tlPosition = 0;
        for (int i = 0; i < arrayTheLoaiTam.size(); i++) {
            TheLoai theLoai = arrayTheLoaiTam.get(i);
            if (theLoai.getMaTL().equals(ct.getMaTL())) {
                tlPosition = i;
                break;
            }
        }

        //set du lieu
        editmaCT.setText(ct.getMaCT());
        editTenCT.setText(ct.getTenCT());
        spinnerTheLoaiTam.setSelection(tlPosition);

        btnChonHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        //bat su kien nut bam
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tenCT = String.valueOf(editTenCT.getText());
                String tenTheLoai = String.valueOf(spinnerTheLoaiTam.getSelectedItem().toString());
                String maTheLoai = String.valueOf(arrayTheLoaiTam.get(spinnerTheLoaiTam.getSelectedItemPosition()).getMaTL());
                if (TextUtils.isEmpty(tenCT) || TextUtils.isEmpty(tenTheLoai)) {
                    CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "N???i dung c???n s???a ch??a ???????c nh???p").show();
                    //Toast.makeText(getContext(), "N???i dung c???n s???a ch??a ???????c nh???p", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivChonHinhAnh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] hinhAnh = ImageUtil.getByteArrayFromBitmap(bitmap);

                ChuongTrinh editCT = new ChuongTrinh(ct.getMaCT(), tenCT, maTheLoai, hinhAnh);

                dbHelper.suaChuongTrinh(editCT);
                CustomToast.makeCustomToast(getActivity(), R.drawable.ic_check, "C???p nh???t ch????ng tr??nh th??nh c??ng").show();
                //Toast.makeText(getContext(), "C???p nh???t ch????ng tr??nh th??nh c??ng", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                actionGetData();
                chuongTrinhAdapter.updateListSearch();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dbHelper.getData(String.format("SELECT * FROM ThongTinPhatSong WHERE MaCT = '%s'", ct.getMaCT()));
                if(cursor != null && cursor.moveToNext()) {
                    CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "Ch????ng tr??nh n??y ???? c?? l???ch ph??t s??ng").show();
                    //Toast.makeText(getContext(), "Ch????ng tr??nh n??y ???? c?? l???ch ph??t s??ng", Toast.LENGTH_LONG).show();
                    return;
                }
                openDeleteDialog(ct, dialog);
            }
        });

        dialog.show();
    }

    private void openDeleteDialog(ChuongTrinh ct, Dialog dialog) {
        new AlertDialog.Builder(getContext())
                .setTitle("X??c nh???n")
                .setMessage("B???n c?? ch???c mu???n x??a ch????ng tr??nh n??y kh??ng?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            dbHelper.xoaChuongTrinh(ct.getMaCT());
                            dialog.dismiss();
                            actionGetData();
                            chuongTrinhAdapter.updateListSearch();
                            Snackbar snackbar = Snackbar.make(mainContent, "???? x??a ch????ng tr??nh!", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ho??n t??c", view -> {
                                dbHelper.themChuongTrinh(ct);
                                CustomToast.makeCustomToast(getActivity(), R.drawable.ic_check, "???? ho??n t??c!").show();
                                //Toast.makeText(getContext(), "???? ho??n t??c!", Toast.LENGTH_SHORT).show();
                                actionGetData();
                                chuongTrinhAdapter.updateListSearch();
                            });
                            snackbar.setActionTextColor(Color.CYAN);
                            snackbar.show();
                        }
                        catch (Exception ex) {
                            CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "X???y ra l???i khi th??m ch????ng tr??nh! Vui l??ng th??? l???i\n" + ex.getMessage()).show();
                            //Toast.makeText(getContext(), "X???y ra l???i khi th??m ch????ng tr??nh! Vui l??ng th??? l???i\n" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void checkPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ImagePicker.with(ChuongTrinhFragment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(REQUEST_CODE_SELECT_IMAGE);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "Permission Denied\n" + deniedPermissions.toString()).show();
                //Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
    private byte[] getByteArrayFromImageResource(int resourceId) {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(resourceId);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        return ImageUtil.getByteArrayFromBitmap(bitmap);
    }


}