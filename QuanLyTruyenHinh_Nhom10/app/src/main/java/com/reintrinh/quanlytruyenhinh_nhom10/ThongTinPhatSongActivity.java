package com.reintrinh.quanlytruyenhinh_nhom10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.ThongTinPhatSongAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongTinPhatSong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ThongTinPhatSongActivity extends AppCompatActivity {

    private CoordinatorLayout rootView;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private FloatingActionButton fabAdd;
    private TextView tvTenTheLoai;
    private TextView tvSoLuongPhatSong;
    private RecyclerView rcvThongTinPhatSong;
    private ThongTinPhatSongAdapter thongTinPhatSongAdapter;
    private ChuongTrinh chuongTrinh;
    private List<ThongTinPhatSong> thongTinPhatSongList;
    private QuanLyTruyenHinhHelper quanLyTruyenHinhHelper;


    private boolean isExpanded = false;

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
        tvTenTheLoai = findViewById(R.id.tv_ten_the_loai);
        tvSoLuongPhatSong = findViewById(R.id.tv_so_luong_phat_song);
        rcvThongTinPhatSong = findViewById(R.id.rcv_thong_tin_phat_song);

        TheLoai theLoai = quanLyTruyenHinhHelper.getTheLoaiByMaTheLoai(chuongTrinh.getMaTL());
        tvTenTheLoai.setText(theLoai.getTenTL());
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
        ImageView imgNgayPhatSong = dialog.findViewById(R.id.img_ngay_phat_song);
        EditText edtThoiLuong = dialog.findViewById(R.id.edt_thoi_luong);
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

        imgNgayPhatSong.setOnClickListener(new View.OnClickListener() {
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

                ThongTinPhatSong thongTinPhatSong = new ThongTinPhatSong(maPhatSong, chuongTrinh, bienTapVien, ngayPhatSong, thoiLuong);
                try {
                    quanLyTruyenHinhHelper.themThongTinPhatSong(thongTinPhatSong);
                    Snackbar snackbar = Snackbar.make(rootView, "Đã thêm thông tin phát sóng mới!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("OK", null);
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
                catch (Exception ex) {
                    Toast.makeText(ThongTinPhatSongActivity.this, "Thời lượng phải là số nguyên dương!", Toast.LENGTH_SHORT).show();
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

    }

    private void showDialogXoa(ThongTinPhatSong thongTinPhatSong) {

    }
}