package com.reintrinh.quanlytruyenhinh_nhom10.model;

import java.io.Serializable;

public class TheLoai implements Serializable {
    private String maTL,tenTL;
    private byte[] hinhAnh;

    public String getMaTL() {
        return maTL;
    }

    public void setMaTL(String maTL) {
        this.maTL = maTL;
    }

    public String getTenTL() {
        return tenTL;
    }

    public void setTenTL(String tenTL) {
        this.tenTL = tenTL;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public TheLoai() {
    }

    public TheLoai(String maTL, String tenTL, byte[] hinhAnh) {
        this.maTL = maTL;
        this.tenTL = tenTL;
        this.hinhAnh = hinhAnh;
    }
}
