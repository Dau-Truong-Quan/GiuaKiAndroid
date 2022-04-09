package com.reintrinh.quanlytruyenhinh_nhom10.model;

import java.util.Date;

public class ThongTinPhatSong {

    private String maPhatSong;
    private ChuongTrinh chuongTrinh;
    private BienTapVien bienTapVien;
    private Date ngayPhatSong;
    private int thoiLuong;

    public ThongTinPhatSong() {
    }

    public ThongTinPhatSong(String maPhatSong, ChuongTrinh chuongTrinh, BienTapVien bienTapVien, Date ngayPhatSong, int thoiLuong) {
        this.maPhatSong = maPhatSong;
        this.chuongTrinh = chuongTrinh;
        this.bienTapVien = bienTapVien;
        this.ngayPhatSong = ngayPhatSong;
        this.thoiLuong = thoiLuong;
    }

    public String getMaPhatSong() {
        return maPhatSong;
    }

    public void setMaPhatSong(String maPhatSong) {
        this.maPhatSong = maPhatSong;
    }

    public ChuongTrinh getChuongTrinh() {
        return chuongTrinh;
    }

    public void setChuongTrinh(ChuongTrinh chuongTrinh) {
        this.chuongTrinh = chuongTrinh;
    }

    public BienTapVien getBienTapVien() {
        return bienTapVien;
    }

    public void setBienTapVien(BienTapVien bienTapVien) {
        this.bienTapVien = bienTapVien;
    }

    public Date getNgayPhatSong() {
        return ngayPhatSong;
    }

    public void setNgayPhatSong(Date ngayPhatSong) {
        this.ngayPhatSong = ngayPhatSong;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }
}
