package com.reintrinh.quanlytruyenhinh_nhom10.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.reintrinh.quanlytruyenhinh_nhom10.model.BienTapVien;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongTinPhatSong;
import com.reintrinh.quanlytruyenhinh_nhom10.model.User;

import java.util.ArrayList;
import java.util.List;

public class QuanLyTruyenHinhHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "QuanLyTruyenHinh.sqlite";

    private static final int DB_VERSION = 2;

    private static QuanLyTruyenHinhHelper sInstance;

    public QuanLyTruyenHinhHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public static synchronized QuanLyTruyenHinhHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new QuanLyTruyenHinhHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    //Truy van khong tra ket qua
    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //Truy van tra ket qua
    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // ---------------------- Chung -------------------------
    public boolean hasData(String tableName) {
        String sql = "SELECT * from %s";
        Cursor cursor = getData(String.format(sql, tableName));
        if (cursor == null) {
            return false;
        }

        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    // ---------------------- Thể loại -------------------------

    public TheLoai getTheLoaiByMaTheLoai(String maTL) {
        Cursor cursor = getData(String.format("SELECT * FROM TheLoai WHERE maTL = '%s'", maTL));
        if (cursor == null) {
            return null;
        }

        if (cursor.moveToNext()) {
            return new TheLoai(cursor.getString(0), cursor.getString(1), cursor.getBlob(2));
        }
        return null;
    }

    public void themTheLoai(TheLoai theLoai) {
        if (theLoai == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO TheLoai VALUES (?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, theLoai.getMaTL());
        statement.bindString(2, theLoai.getTenTL());
        statement.bindBlob(3, theLoai.getHinhAnh());
        statement.executeInsert();
    }

    public void suaTheLoai(TheLoai theLoai) {
        if (theLoai == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE TheLoai SET TenTL = ?, HinhAnh = ? WHERE MaTL = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, theLoai.getTenTL());
        statement.bindBlob(2, theLoai.getHinhAnh());
        statement.bindString(3, theLoai.getMaTL());
        statement.execute();
    }

    public void xoaTheLoai(String maTL) {
        queryData(String.format("DELETE FROM TheLoai WHERE MaTL = '%s'", maTL));
    }

    public List<TheLoai> getAllTheLoai() {
        Cursor cursor = getData("SELECT * FROM TheLoai ORDER BY MaTL");
        if (cursor == null) {
            return null;
        }

        List<TheLoai> list = new ArrayList<>();
        TheLoai theLoai;
        while (cursor.moveToNext()) {
            theLoai = new TheLoai(cursor.getString(0), cursor.getString(1), cursor.getBlob(2));
            list.add(theLoai);
        }
        return list;
    }

    // ---------------------- Chương trình -------------------------

    public ChuongTrinh getChuongTrinhByMaCT(String maCT) {
        Cursor cursor = getData(String.format("SELECT * FROM ChuongTrinh WHERE MaCT = '%s'", maCT));
        if (cursor == null) {
            return null;
        }

        if (cursor.moveToNext()) {
            return new ChuongTrinh(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3));
        }
        return null;
    }

    public void themChuongTrinh(ChuongTrinh chuongTrinh) {
        if (chuongTrinh == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ChuongTrinh VALUES (?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, chuongTrinh.getMaCT());
        statement.bindString(2, chuongTrinh.getTenCT());
        statement.bindString(3, chuongTrinh.getMaTL());
        statement.bindBlob(4, chuongTrinh.getHinhAnh());
        statement.executeInsert();
    }

    public void suaChuongTrinh(ChuongTrinh chuongTrinh) {
        if (chuongTrinh == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE ChuongTrinh SET TenCT = ?, MaTL = ?, HinhAnh = ? WHERE MaCT = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, chuongTrinh.getTenCT());
        statement.bindString(2, chuongTrinh.getMaTL());
        statement.bindBlob(3, chuongTrinh.getHinhAnh());
        statement.bindString(4, chuongTrinh.getMaCT());
        statement.execute();
    }

    public void xoaChuongTrinh(String maCT) {
        queryData(String.format("DELETE FROM ChuongTrinh WHERE MaCT = '%s'", maCT));
    }

    // ---------------------- Biên tập viên -------------------------

    public BienTapVien getBienTapVienByMaBTV(String maBTV) {
        Cursor cursor = getData(String.format("SELECT * FROM BienTapVien WHERE MaBTV = '%s'", maBTV));
        if (cursor == null) {
            return null;
        }

        if (cursor.moveToNext()) {
            return new BienTapVien(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(4));
        }
        return null;
    }

    public List<BienTapVien> getAllBienTapVien() {
        Cursor cursor = getData("SELECT * FROM BienTapVien ORDER BY MaBTV");
        if (cursor == null) {
            return null;
        }

        List<BienTapVien> list = new ArrayList<>();
        BienTapVien bienTapVien;
        while (cursor.moveToNext()) {
            bienTapVien = new BienTapVien(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(4));
            list.add(bienTapVien);
        }
        return list;
    }

    public void themBienTapVien(BienTapVien bienTapVien) {
        if (bienTapVien == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO BienTapVien VALUES (?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, bienTapVien.getMaBTV());
        statement.bindString(2, bienTapVien.getHoTen());
        statement.bindString(3, bienTapVien.getNgaySinh());
        statement.bindString(4, bienTapVien.getSdt());
        statement.bindBlob(5, bienTapVien.getHinhAnh());
        statement.executeInsert();
    }

    public void suaBienTapVien(BienTapVien bienTapVien) {
        if (bienTapVien == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE BienTapVien SET HoTen = ?, NgaySinh = ?, Sdt = ?, HinhAnh = ? WHERE MaBTV = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, bienTapVien.getHoTen());
        statement.bindString(2, bienTapVien.getNgaySinh());
        statement.bindString(3, bienTapVien.getSdt());
        statement.bindBlob(4, bienTapVien.getHinhAnh());
        statement.bindString(5, bienTapVien.getMaBTV());
        statement.execute();
    }

    public void xoaBienTapVien(String maBTV) {
        queryData(String.format("DELETE FROM BienTapVien WHERE MaBTV = '%s'", maBTV));
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
        Cursor cursor = getData(String.format("SELECT * FROM ThongTinPhatSong WHERE MaCT = '%s' ORDER BY MaPS", chuongTrinh.getMaCT()));
        if (cursor == null) {
            return null;
        }

        List<ThongTinPhatSong> list = new ArrayList<>();
        ThongTinPhatSong thongTinPhatSong;
        while (cursor.moveToNext()) {
            thongTinPhatSong = new ThongTinPhatSong(cursor.getString(0), chuongTrinh, getBienTapVienByMaBTV(cursor.getString(2)),
                    cursor.getString(3), cursor.getInt(4), cursor.getBlob(5));
            list.add(thongTinPhatSong);
        }
        return list;
    }

    public void themThongTinPhatSong(ThongTinPhatSong thongTinPhatSong) {
        if (thongTinPhatSong == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ThongTinPhatSong VALUES (?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, thongTinPhatSong.getMaPhatSong());
        statement.bindString(2, thongTinPhatSong.getChuongTrinh().getMaCT());
        statement.bindString(3, thongTinPhatSong.getBienTapVien().getMaBTV());
        statement.bindString(4, thongTinPhatSong.getNgayPhatSong());
        statement.bindLong(5, thongTinPhatSong.getThoiLuong());
        statement.bindBlob(6, thongTinPhatSong.getHinhAnh());
        statement.executeInsert();
    }

    public void suaThongTinPhatSong(ThongTinPhatSong thongTinPhatSong) {
        if (thongTinPhatSong == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE ThongTinPhatSong SET MaCT = ?, MaBTV = ?, NgayPS = ?, ThoiLuong = ?, HinhAnh = ? WHERE MaPS = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, thongTinPhatSong.getChuongTrinh().getMaCT());
        statement.bindString(2, thongTinPhatSong.getBienTapVien().getMaBTV());
        statement.bindString(3, thongTinPhatSong.getNgayPhatSong());
        statement.bindLong(4, thongTinPhatSong.getThoiLuong());
        statement.bindBlob(5, thongTinPhatSong.getHinhAnh());
        statement.bindString(6, thongTinPhatSong.getMaPhatSong());
        statement.execute();
    }

    public void xoaThongTinPhatSong(String maPS) {
        queryData(String.format("DELETE FROM ThongTinPhatSong WHERE MaPS = '%s'", maPS));
    }

    // ---------------------- User -------------------------

    public User checkUserExist(String username, String password) {
        User user = null;
        SQLiteDatabase db = getReadableDatabase();

        String selection = "WHERE EMAIL='" + username + "' and PASSWORD = '" + password + "'";

        Cursor cursor = db.rawQuery("SELECT * FROM User " + selection, null);
        int count = cursor.getCount();
        while (cursor.moveToNext()) {
            user = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getBlob(4)
            );
        }

        cursor.close();
        close();

        if (count > 0) {
            return user;
        } else {
            return null;
        }
    }

    // user
    public void capNhatMatKhauUser(String editTextNewPassword, String gmail) {
        queryData(String.format("UPDATE User SET PASSWORD='" + editTextNewPassword + "' WHERE EMAIL='" + gmail + "'"));
    }

    public User getUserByEmail(String email) {
        Cursor cursor = getData(String.format("SELECT * FROM User WHERE Email = '%s'", email));
        if (cursor == null) {
            return null;
        }

        if (cursor.moveToNext()) {
            return new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(4));
        }
        return null;
    }

    public int themUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM User " + "WHERE EMAIL='" + user.getEmail() + "'", null);
        int count = cursor.getCount();

        if (count > 1) {
            return 1;
        }

        if (user == null) {
            return -1;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO User VALUES (?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, user.getFirstname());
        statement.bindString(2, user.getLastname());
        statement.bindString(3, user.getEmail());
        statement.bindString(4, user.getPassword());
        statement.bindBlob(5, user.getImg());
        statement.executeInsert();

        return 0;
    }

    public void suaThongTinUser(User user) {
        if (user == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE User SET FirstName = ?, LastName = ?, HinhAnh = ? WHERE Email = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, user.getFirstname());
        statement.bindString(2, user.getLastname());
        statement.bindBlob(3, user.getImg());
        statement.bindString(4, user.getEmail());
        statement.execute();
    }
}
