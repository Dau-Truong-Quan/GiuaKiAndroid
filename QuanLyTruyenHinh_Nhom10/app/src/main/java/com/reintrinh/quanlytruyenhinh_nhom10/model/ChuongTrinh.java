package com.reintrinh.quanlytruyenhinh_nhom10.model;

import java.io.Serializable;

public class ChuongTrinh implements Serializable {
    private String maCT;
    private String tenCT;
    private String maTL;
    private byte[] hinhAnh;

    public ChuongTrinh() {
    }

    public ChuongTrinh(String maCT, String tenCT, String maTL, byte[] hinhAnh) {
        this.maCT = maCT;
        this.tenCT = tenCT;
        this.maTL = maTL;
        this.hinhAnh = hinhAnh;
    }

    public String getMaCT() {
        return maCT;
    }

    public void setMaCT(String maCT) {
        this.maCT = maCT;
    }

    public String getTenCT() {
        return tenCT;
    }

    public void setTenCT(String tenCT) {
        this.tenCT = tenCT;
    }

    public String getMaTL() {
        return maTL;
    }

    public void setMaTL(String maTL) {
        this.maTL = maTL;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
