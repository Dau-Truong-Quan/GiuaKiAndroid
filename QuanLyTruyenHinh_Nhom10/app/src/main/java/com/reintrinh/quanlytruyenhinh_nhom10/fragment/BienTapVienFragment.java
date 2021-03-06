package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.BienTapVienAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;
import com.reintrinh.quanlytruyenhinh_nhom10.widget.CustomToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BienTapVienFragment extends Fragment {

    LinearLayout mainContent;
    View view;
    QuanLyTruyenHinhHelper db;

    ListView lvBienTapVien;
    ArrayList<BienTapVien> data = new ArrayList<>();
    BienTapVienAdapter bienTapVienAdapter;

    SearchView svBTV;
    Button btnThemBTV;
    TextView tvSoLuongBTV;
    ImageView ivChonHinhAnh;

    public static final int REQUEST_CODE_SELECT_IMAGE = 1;

    public BienTapVienFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bien_tap_vien, container, false);

        setControl();
        setEvent();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            ivChonHinhAnh.setImageURI(uri);
        }
    }

    private void setEvent() {
        db = new QuanLyTruyenHinhHelper(getContext());
        hienThi();
        bienTapVienAdapter = new BienTapVienAdapter(getContext(), R.layout.list_layout_bientapvien, data);
        lvBienTapVien.setAdapter(bienTapVienAdapter);

        lvBienTapVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openUpdateDialog(data.get(i));
            }
        });

        svBTV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                bienTapVienAdapter.filter(s);
                return true;
            }
        });

        btnThemBTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInsertDialog();
            }
        });
    }

    private void openDeleteDialog(BienTapVien btv, Dialog dialog) {
        new AlertDialog.Builder(getContext())
                .setTitle("X??c nh???n")
                .setMessage("B???n c?? ch???c mu???n x??a bi??n t???p vi??n n??y kh??ng?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            db.xoaBienTapVien(btv.getMaBTV());

                            dialog.dismiss();
                            hienThi();
                            bienTapVienAdapter.notifyDataSetChanged();
                            bienTapVienAdapter.updateDataSearch();

                            Snackbar snackbar = Snackbar.make(mainContent, "???? x??a bi??n t???p vi??n!", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ho??n t??c", view -> {
                                db.themBienTapVien(btv);
                                CustomToast.makeCustomToast(getActivity(), R.drawable.ic_check, "???? ho??n t??c!").show();
                                hienThi();
                                bienTapVienAdapter.notifyDataSetChanged();
                                bienTapVienAdapter.updateDataSearch();
                            });
                            snackbar.setActionTextColor(Color.CYAN);
                            snackbar.show();
                        }
                        catch (Exception ex) {
                            CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error,
                                    "X???y ra l???i khi th??m bi??n t???p vi??n! Vui l??ng th??? l???i\n" + ex.getMessage()).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openUpdateDialog(BienTapVien btv) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chinhsua_bientapvien);

        EditText editMaBTV = dialog.findViewById(R.id.editMaBTV);
        EditText editHoTen = dialog.findViewById(R.id.editHoTenBTV);
        EditText editNgaySinh = dialog.findViewById(R.id.editNgaySinhBTV);
        EditText editSdt = dialog.findViewById(R.id.editSdtBTV);

        ImageView ivDatePicker = dialog.findViewById(R.id.ivDatePicker);
        Button btnChonHinhAnh = dialog.findViewById(R.id.btnChonHinhAnhEditBTV);
        Button btnLuu = dialog.findViewById(R.id.btnLuu);
        Button btnXoa = dialog.findViewById(R.id.btnXoa);

        ivChonHinhAnh = dialog.findViewById(R.id.editHinhAnhBTV);

        // set c??c gi?? tr???
        editMaBTV.setText(btv.getMaBTV());
        editHoTen.setText(btv.getHoTen());
        editNgaySinh.setText(btv.getNgaySinh());
        editSdt.setText(btv.getSdt());
        ivChonHinhAnh.setImageBitmap(ImageUtil.getBitmapFromByteArray(btv.getHinhAnh()));

        ivDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                String value = String.format("%d/%d/%d", dayOfMonth, (month + 1), year);
                                editNgaySinh.setText(value);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        btnChonHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maBTV = String.valueOf(editMaBTV.getText());
                String tenBTVMoi = String.valueOf(editHoTen.getText());
                String ngaySinhBTVMoi = String.valueOf(editNgaySinh.getText());
                String sdtBTVMoi = String.valueOf(editSdt.getText());

                if (TextUtils.isEmpty(tenBTVMoi) || TextUtils.isEmpty(sdtBTVMoi) || TextUtils.isEmpty(ngaySinhBTVMoi)) {
                    CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "N???i dung c???n th??m ch??a ???????c nh???p").show();
                    return;
                }

                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivChonHinhAnh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] hinhAnh = ImageUtil.getByteArrayFromBitmap(bitmap);

                BienTapVien editBTV = new BienTapVien(maBTV, tenBTVMoi, ngaySinhBTVMoi, sdtBTVMoi, hinhAnh);

                db.suaBienTapVien(editBTV);

                CustomToast.makeCustomToast(getActivity(), R.drawable.ic_check, "C???p nh???t bi??n t???p vi??n th??nh c??ng").show();
                //Toast.makeText(getContext(), "C???p nh???t bi??n t???p vi??n th??nh c??ng", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                hienThi();
                bienTapVienAdapter.notifyDataSetChanged();
                bienTapVienAdapter.updateDataSearch();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = db.getData(String.format("SELECT * FROM ThongTinPhatSong WHERE MaBTV = '%s'", btv.getMaBTV()));
                if(cursor != null && cursor.moveToNext()) {
                    CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "Bi??n t???p vi??n n??y ???? c?? l???ch ph??t s??ng").show();
                    //Toast.makeText(getContext(), "Bi??n t???p vi??n n??y ???? c?? l???ch ph??t s??ng", Toast.LENGTH_LONG).show();
                    return;
                }
                openDeleteDialog(btv, dialog);
            }
        });

        dialog.show();
    }

    private void openInsertDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thembientapvien);

        EditText editMaBTV = dialog.findViewById(R.id.addMaBTV);
        EditText editHoTen = dialog.findViewById(R.id.addHoTenBTV);
        EditText editNgaySinh = dialog.findViewById(R.id.addNgaySinhBTV);
        EditText editSdt = dialog.findViewById(R.id.addSdtBTV);

        ImageView ivDatePicker = dialog.findViewById(R.id.ivDatePicker);
        Button btnChonHinhAnh = dialog.findViewById(R.id.btnChonHinhAnhAddBTV);
        Button btnThem = dialog.findViewById(R.id.btnThem);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        ivChonHinhAnh = dialog.findViewById(R.id.addHinhAnhBTV);

        ivDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                calendar.set(year, month, dayOfMonth);
                                editNgaySinh.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        btnChonHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maBTVMoi = String.valueOf(editMaBTV.getText()).toUpperCase();
                String tenBTVMoi = String.valueOf(editHoTen.getText());
                String ngaySinhBTVMoi = String.valueOf(editNgaySinh.getText());
                String sdtBTVMoi = String.valueOf(editSdt.getText());

                if (TextUtils.isEmpty(maBTVMoi) || TextUtils.isEmpty(tenBTVMoi) || TextUtils.isEmpty(sdtBTVMoi) || TextUtils.isEmpty(ngaySinhBTVMoi)) {
                    CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "N???i dung c???n th??m ch??a ???????c nh???p").show();
                    //Toast.makeText(getContext(), "N???i dung c???n th??m ch??a ???????c nh???p", Toast.LENGTH_SHORT).show();
                    return;
                }

                //kiem tra ma trung
                if(db.getBienTapVienByMaBTV(maBTVMoi) != null) {
                    CustomToast.makeCustomToast(getActivity(), R.drawable.ic_error, "M?? bi??n t???p vi??n ???? t???n t???i").show();
                    //Toast.makeText(getContext(), "M?? bi??n t???p vi??n ???? t???n t???i", Toast.LENGTH_SHORT).show();
                    return;
                }

                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivChonHinhAnh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] hinhAnh = ImageUtil.getByteArrayFromBitmap(bitmap);

                BienTapVien newBTV = new BienTapVien(maBTVMoi, tenBTVMoi, ngaySinhBTVMoi, sdtBTVMoi, hinhAnh);

                db.themBienTapVien(newBTV);
                CustomToast.makeCustomToast(getActivity(), R.drawable.ic_check, "Th??m bi??n t???p vi??n m???i th??nh c??ng").show();
                //Toast.makeText(getContext(), "Th??m bi??n t???p vi??n m???i th??nh c??ng", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                hienThi();
                bienTapVienAdapter.notifyDataSetChanged();
                bienTapVienAdapter.updateDataSearch();
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

    private void checkPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ImagePicker.with(BienTapVienFragment.this)
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

    private void hienThi() {
        data.clear();
        data.addAll(db.getAllBienTapVien());
        tvSoLuongBTV.setText(String.valueOf(data.size()));
        //bienTapVienAdapter.notifyDataSetChanged();
    }

    private void setControl() {
        lvBienTapVien = view.findViewById(R.id.lvBienTapVien);
        btnThemBTV = view.findViewById(R.id.btnThemBTV);
        tvSoLuongBTV = view.findViewById(R.id.tvSoLuongBTV);
        svBTV = view.findViewById(R.id.svBTV);
        mainContent = view.findViewById(R.id.main_btv);
    }
}