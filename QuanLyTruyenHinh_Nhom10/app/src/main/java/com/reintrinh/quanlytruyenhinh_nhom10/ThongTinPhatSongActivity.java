package com.reintrinh.quanlytruyenhinh_nhom10;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.ThongTinPhatSongAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongTinPhatSong;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ThongTinPhatSongActivity extends AppCompatActivity {

    private CoordinatorLayout rootView;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private FloatingActionButton fabAdd;
    private ImageView imgHinhAnhChuongTrinh;
    private TextView tvTenTheLoai;
    private TextView tvSoLuongPhatSong;
    private RecyclerView rcvThongTinPhatSong;
    private ThongTinPhatSongAdapter thongTinPhatSongAdapter;
    private ChuongTrinh chuongTrinh;
    private List<ThongTinPhatSong> thongTinPhatSongList;
    private QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;

    private boolean isExpanded = false;
    public static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private ImageView imgChonHinhAnhPS;
//    private ActivityResultLauncher<Intent> startForProfileImageResult = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//        @Override
//        public void onActivityResult(ActivityResult result) {
//            int resultCode = result.getResultCode();
//            Intent intent = result.getData();
//            if (resultCode == RESULT_OK) {
//                if (intent == null) {
//                    return;
//                }
//                Uri uri = intent.getData();
//                imgHinhAnhPhatSong.setImageURI(uri);
//            }
//            else if (resultCode == ImagePicker.RESULT_ERROR) {
//                Toast.makeText(ThongTinPhatSongActivity.this, ImagePicker.getError(intent), Toast.LENGTH_LONG).show();
//            }
//        }
//    })

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_phat_song);

        quanLyTruyenHinhHelper = new QuanLyTruyenHinhHelper(this);
        Bundle bundle = getIntent().getExtras();
        chuongTrinh = (ChuongTrinh) bundle.getSerializable(getString(R.string.key_program));

        setControl();
        initToolbar();
        initToolbarAnimation();
        initRecyclerView();
        loadData();
        setEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imgChonHinhAnhPS.setImageURI(uri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setControl() {
        rootView = findViewById(R.id.root_view);
        appBarLayout = findViewById(R.id.appbar_layout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        toolbar = findViewById(R.id.toolbar);
        fabAdd = findViewById(R.id.fab_add);
        imgHinhAnhChuongTrinh = findViewById(R.id.img_hinh_anh_chuong_trinh);
        tvTenTheLoai = findViewById(R.id.tv_ten_the_loai);
        tvSoLuongPhatSong = findViewById(R.id.tv_so_luong_phat_song);
        rcvThongTinPhatSong = findViewById(R.id.rcv_thong_tin_phat_song);

        TheLoai theLoai = quanLyTruyenHinhHelper.getTheLoaiByMaTheLoai(chuongTrinh.getMaTL());
        tvTenTheLoai.setText(theLoai.getTenTL());
//        byte[] hinhAnhChuongTrinh = chuongTrinh.getHinhAnh();
//        imgHinhAnhChuongTrinh.setIma
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(chuongTrinh.getTenCT());
        }
    }

    private void initToolbarAnimation() {
        collapsingToolbarLayout.setTitle(chuongTrinh.getTenCT());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avata_chuongtrinh);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                int myColor = palette.getVibrantColor(getResources().getColor(R.color.color_toolbar));
                collapsingToolbarLayout.setContentScrimColor(myColor);
                collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.black_trans));
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200) {
                    isExpanded = false;
                } else {
                    isExpanded = true;
                }
                invalidateOptionsMenu();
            }
        });
    }

    private void initRecyclerView() {
        rcvThongTinPhatSong.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvThongTinPhatSong.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvThongTinPhatSong.addItemDecoration(itemDecoration);

        thongTinPhatSongList = quanLyTruyenHinhHelper.getListThongTinPhatSongByChuongTrinh(chuongTrinh);
        thongTinPhatSongAdapter = new ThongTinPhatSongAdapter(thongTinPhatSongList, new ThongTinPhatSongAdapter.IClickItemListener() {
            @Override
            public void onClickEditItem(ThongTinPhatSong thongTinPhatSong) {
                showDialogSua(thongTinPhatSong);
            }

            @Override
            public void onClickDeleteItem(ThongTinPhatSong thongTinPhatSong) {
                showDialogXoa(thongTinPhatSong);
            }
        });
        rcvThongTinPhatSong.setAdapter(thongTinPhatSongAdapter);
    }

    private void loadData() {
        thongTinPhatSongList = quanLyTruyenHinhHelper.getListThongTinPhatSongByChuongTrinh(chuongTrinh);
        if (thongTinPhatSongList != null) {
            tvSoLuongPhatSong.setText(String.valueOf(thongTinPhatSongList.size()));
        }
        else {
            tvSoLuongPhatSong.setText("0");
        }

        thongTinPhatSongAdapter.setData(thongTinPhatSongList);
    }


    private void setEvent() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogThem();
            }
        });
    }

    private void showDialogThem() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_thongtinphatsong);
        dialog.setCancelable(false);

        TextView tvTenChuongTrinh = dialog.findViewById(R.id.tv_ten_chuong_trinh);
        EditText edtMaPhatSong = dialog.findViewById(R.id.edt_ma_phat_song);
        Spinner spinnerBienTapVien = dialog.findViewById(R.id.spinner_bien_tap_vien);
        TextView tvNgayPhatSong = dialog.findViewById(R.id.tv_ngay_phat_song);
        ImageView imgCalendar = dialog.findViewById(R.id.img_ngay_phat_song);
        EditText edtThoiLuong = dialog.findViewById(R.id.edt_thoi_luong);
        imgChonHinhAnhPS = dialog.findViewById(R.id.img_chon_hinh_anh_phat_song);
        Button btnChonHinhAnh = dialog.findViewById(R.id.btnChonHinhAnh);
        Button btnThem = dialog.findViewById(R.id.btnThem);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        tvTenChuongTrinh.setText(chuongTrinh.getTenCT());
        List<BienTapVien> bienTapVienList = quanLyTruyenHinhHelper.getAllBienTapVien();
        ArrayAdapter<BienTapVien> bienTapVienArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bienTapVienList)
        {
            @Nullable
            @Override
            public Object getItem(int position) {
                return bienTapVienList.get(position).getHoTen();
            }
        };
        spinnerBienTapVien.setAdapter(bienTapVienArrayAdapter);
        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentYear = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ThongTinPhatSongActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        calendar.set(year, month, dayOfMonth);
                        tvNgayPhatSong.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, currentYear, currentMonth, currentDay);

                datePickerDialog.show();
            }
        });
        btnChonHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ImagePicker.with(ThongTinPhatSongActivity.this)
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(REQUEST_CODE_SELECT_IMAGE);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(ThongTinPhatSongActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maPhatSong = edtMaPhatSong.getText().toString().trim().toUpperCase();
                if (maPhatSong.isEmpty()) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Bạn chưa nhập mã phát sóng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (quanLyTruyenHinhHelper.kiemTraMaPSTrung(maPhatSong)) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Mã phát sóng này đã tồn tại! Vui lòng nhập mã khác!", Toast.LENGTH_SHORT).show();
                    return;
                }

                BienTapVien bienTapVien = bienTapVienList.get(spinnerBienTapVien.getSelectedItemPosition());
                if (bienTapVien == null) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Bạn chưa chọn biên tập viên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ngayPhatSong = tvNgayPhatSong.getText().toString().trim();
                if (ngayPhatSong.isEmpty()) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Bạn chưa chọn ngày phát sóng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int thoiLuong;
                try {
                    thoiLuong = Integer.parseInt(edtThoiLuong.getText().toString().trim());
                    if (thoiLuong <= 0) {
                        Toast.makeText(ThongTinPhatSongActivity.this, "Thời lượng phải là số nguyên dương!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception ex) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Thời lượng phải là số nguyên dương!", Toast.LENGTH_SHORT).show();
                    return;
                }

                BitmapDrawable bitmapDrawable = (BitmapDrawable)imgChonHinhAnhPS.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] hinhAnh = ImageUtil.getByteArrayFromBitmap(bitmap);

                ThongTinPhatSong thongTinPhatSong = new ThongTinPhatSong(maPhatSong, chuongTrinh, bienTapVien, ngayPhatSong, thoiLuong, hinhAnh);
                try {
                    quanLyTruyenHinhHelper.themThongTinPhatSong(thongTinPhatSong);
                    Snackbar snackbar = Snackbar.make(rootView, "Đã thêm thông tin phát sóng mới!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("OK", null);
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
                catch (Exception ex) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Xảy ra lỗi khi thêm thông tin phát sóng mới! Vui lòng thử lại\n" +
                            ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
                loadData();
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

    private void showDialogSua(ThongTinPhatSong thongTinPhatSong) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua_thongtinphatsong);
        dialog.setCancelable(false);

        TextView tvTenChuongTrinh = dialog.findViewById(R.id.tv_ten_chuong_trinh);
        EditText edtMaPhatSong = dialog.findViewById(R.id.edt_ma_phat_song);
        Spinner spinnerBienTapVien = dialog.findViewById(R.id.spinner_bien_tap_vien);
        TextView tvNgayPhatSong = dialog.findViewById(R.id.tv_ngay_phat_song);
        ImageView imgCalendar = dialog.findViewById(R.id.img_ngay_phat_song);
        EditText edtThoiLuong = dialog.findViewById(R.id.edt_thoi_luong);
        imgChonHinhAnhPS = dialog.findViewById(R.id.img_chon_hinh_anh_phat_song);
        Button btnChonHinhAnh = dialog.findViewById(R.id.btnChonHinhAnh);
        Button btnSua = dialog.findViewById(R.id.btnSua);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        tvTenChuongTrinh.setText(chuongTrinh.getTenCT());
        edtMaPhatSong.setEnabled(false);
        edtMaPhatSong.setText(thongTinPhatSong.getMaPhatSong());
        List<BienTapVien> bienTapVienList = quanLyTruyenHinhHelper.getAllBienTapVien();
        ArrayAdapter<BienTapVien> bienTapVienArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bienTapVienList)
        {
            @Nullable
            @Override
            public Object getItem(int position) {
                return bienTapVienList.get(position).getHoTen();
            }
        };
        spinnerBienTapVien.setAdapter(bienTapVienArrayAdapter);
        int btvPosition = 0;
        for (int i = 0; i < bienTapVienList.size(); i++) {
            BienTapVien bienTapVien = bienTapVienList.get(i);
            if (bienTapVien.getMaBTV().equals(thongTinPhatSong.getBienTapVien().getMaBTV())) {
                btvPosition = i;
                break;
            }
        }
        spinnerBienTapVien.setSelection(btvPosition);
        tvNgayPhatSong.setText(thongTinPhatSong.getNgayPhatSong());
        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentYear = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ThongTinPhatSongActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        calendar.set(year, month, dayOfMonth);
                        tvNgayPhatSong.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
            }
        });
        edtThoiLuong.setText(String.valueOf(thongTinPhatSong.getThoiLuong()));
        byte[] hinhAnh = thongTinPhatSong.getHinhAnh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        imgChonHinhAnhPS.setImageBitmap(bitmap);
        btnChonHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ImagePicker.with(ThongTinPhatSongActivity.this)
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(REQUEST_CODE_SELECT_IMAGE);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(ThongTinPhatSongActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maPhatSong = edtMaPhatSong.getText().toString().trim();

                BienTapVien bienTapVien = bienTapVienList.get(spinnerBienTapVien.getSelectedItemPosition());
                if (bienTapVien == null) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Bạn chưa chọn biên tập viên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ngayPhatSong = tvNgayPhatSong.getText().toString().trim();
                if (ngayPhatSong.isEmpty()) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Bạn chưa chọn ngày phát sóng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int thoiLuong;
                try {
                    thoiLuong = Integer.parseInt(edtThoiLuong.getText().toString().trim());
                    if (thoiLuong <= 0) {
                        Toast.makeText(ThongTinPhatSongActivity.this, "Thời lượng phải là số nguyên dương!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception ex) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Thời lượng phải là số nguyên dương!", Toast.LENGTH_SHORT).show();
                    return;
                }

                BitmapDrawable bitmapDrawable = (BitmapDrawable)imgChonHinhAnhPS.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] hinhAnh = ImageUtil.getByteArrayFromBitmap(bitmap);

                ThongTinPhatSong ttps = new ThongTinPhatSong(maPhatSong, chuongTrinh, bienTapVien, ngayPhatSong, thoiLuong, hinhAnh);
                try {
                    quanLyTruyenHinhHelper.suaThongTinPhatSong(ttps);
                    Snackbar snackbar = Snackbar.make(rootView, "Đã cập nhật thông tin phát sóng!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Hoàn tác", view1 -> {
                        quanLyTruyenHinhHelper.suaThongTinPhatSong(thongTinPhatSong);
                        Toast.makeText(ThongTinPhatSongActivity.this, "Đã hoàn tác!", Toast.LENGTH_SHORT).show();
                        loadData();
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
                catch (Exception ex) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Xảy ra lỗi khi thêm cập nhật tin phát sóng! Vui lòng thử lại\n" +
                            ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
                loadData();
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

    private void showDialogXoa(ThongTinPhatSong thongTinPhatSong) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa thông tin phát sóng này không?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            quanLyTruyenHinhHelper.xoaThongTinPhatSong(thongTinPhatSong.getMaPhatSong());
                            loadData();
                            Snackbar snackbar = Snackbar.make(rootView, "Đã xóa thông tin phát sóng!", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Hoàn tác", view -> {
                                quanLyTruyenHinhHelper.themThongTinPhatSong(thongTinPhatSong);
                                Toast.makeText(ThongTinPhatSongActivity.this, "Đã hoàn tác!", Toast.LENGTH_SHORT).show();
                                loadData();
                            });
                            snackbar.setActionTextColor(Color.YELLOW);
                            snackbar.show();
                        }
                        catch (Exception ex) {
                            Toast.makeText(ThongTinPhatSongActivity.this, "Xảy ra lỗi khi thêm cập nhật tin phát sóng! Vui lòng thử lại\n" +
                                    ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}