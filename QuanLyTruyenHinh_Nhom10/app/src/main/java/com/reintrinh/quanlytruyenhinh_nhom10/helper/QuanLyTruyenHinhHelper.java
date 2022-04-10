package com.reintrinh.quanlytruyenhinh_nhom10.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongTinPhatSong;

import java.util.ArrayList;
import java.util.List;

public class QuanLyTruyenHinhHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "QuanLyTruyenHinh.sqlite";

    private static final int DB_VERSION = 1;

    public QuanLyTruyenHinhHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Truy van khong tra ket qua
    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //Truy van tra ket qua
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // ---------------------- Thể loại -------------------------

    public TheLoai getTheLoaiByMaTheLoai(String maTL) {
        Cursor cursor = getData(String.format("SELECT * FROM TheLoai WHERE maTL = '%s'", maTL));
        if (cursor == null) {
            return null;
        }

        if (cursor.moveToNext()) {
            return new TheLoai(cursor.getString(0), cursor.getString(1));
        }
        return null;
    }

    // ---------------------- Biên tập viên -------------------------

    public BienTapVien getBienTapVienByMaBTV(String maBTV) {
        Cursor cursor = getData(String.format("SELECT * FROM BienTapVien WHERE MaBTV = '%s'", maBTV));
        if (cursor == null) {
            return null;
        }

        if (cursor.moveToNext()) {
            return new BienTapVien(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }
        return null;
    }

    public List<BienTapVien> getAllBienTapVien() {
        Cursor cursor = getData("SELECT * FROM BienTapVien");
        if (cursor == null) {
            return null;
        }

        List<BienTapVien> list = new ArrayList<>();
        BienTapVien bienTapVien;
        while (cursor.moveToNext()) {
            bienTapVien = new BienTapVien(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            list.add(bienTapVien);
        }
        return list;
    }

    // ---------------------- Thông tin phát sóng -------------------------

    public boolean kiemTraMaPSTrung(String maPS) {
        Cursor cursor = getData(String.format("SELECT * FROM ThongTinPhatSong WHERE MaPS = '%s'", maPS));
        if (cursor == null) {
            return false;
        }

        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public List<ThongTinPhatSong> getListThongTinPhatSongByChuongTrinh(ChuongTrinh chuongTrinh) {
        Cursor cursor = getData(String.format("SELECT * FROM ThongTinPhatSong WHERE MaCT = '%s'", chuongTrinh.getMaCT()));
        if (cursor == null) {
            return null;
        }

        List<ThongTinPhatSong> list = new ArrayList<>();
        ThongTinPhatSong thongTinPhatSong;
        while (cursor.moveToNext()) {
            thongTinPhatSong = new ThongTinPhatSong(cursor.getString(0), chuongTrinh, getBienTapVienByMaBTV(cursor.getString(2)),
                    cursor.getString(3), cursor.getInt(4));
            list.add(thongTinPhatSong);
        }
        return list;
    }

    public void themThongTinPhatSong(ThongTinPhatSong thongTinPhatSong) {
        if (thongTinPhatSong == null) {
            return;
        }
        queryData(String.format("INSERT INTO ThongTinPhatSong VALUES ('%s','%s', '%s', '%s', %d)",
                thongTinPhatSong.getMaPhatSong(), thongTinPhatSong.getChuongTrinh().getMaCT(), thongTinPhatSong.getBienTapVien().getMaBTV(),
                thongTinPhatSong.getNgayPhatSong(), thongTinPhatSong.getThoiLuong()));
    }
}
