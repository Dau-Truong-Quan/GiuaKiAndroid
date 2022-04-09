package com.reintrinh.quanlytruyenhinh_nhom10.model;

import java.io.Serializable;

public class TheLoai implements Serializable {
    private String maTl,tenTL;

    public String getMaTl() {
        return maTl;
    }

    public void setMaTl(String maTl) {
        this.maTl = maTl;
    }

    public String getTenTL() {
        return tenTL;
    }

    public void setTenTL(String tenTL) {
        this.tenTL = tenTL;
    }

    public TheLoai() {
    }

    public TheLoai(String maTl, String tenTL) {
        this.maTl = maTl;
        this.tenTL = tenTL;
    }
}
