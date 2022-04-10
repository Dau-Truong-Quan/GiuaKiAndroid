package com.reintrinh.quanlytruyenhinh_nhom10.model;

public class BienTapVien {
    private String maBTV;
    private String hoTen;
    private String ngaySinh;
    private String sdt;

    public BienTapVien() {
    }

    public BienTapVien(String maBTV, String hoTen, String ngaySinh, String sdt) {
        this.maBTV = maBTV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
    }

    public String getMaBTV() {
        return maBTV;
    }

    public void setMaBTV(String maBTV) {
        this.maBTV = maBTV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
