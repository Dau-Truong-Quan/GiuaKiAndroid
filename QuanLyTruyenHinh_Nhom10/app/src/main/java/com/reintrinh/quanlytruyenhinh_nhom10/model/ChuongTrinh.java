package com.reintrinh.quanlytruyenhinh_nhom10.model;

import java.io.Serializable;

public class ChuongTrinh implements Serializable {
    public String maCT;
    public String tenCT;
    public String maTL;

    public ChuongTrinh() {
    }

    public ChuongTrinh(String maCT, String tenCT, String maTL) {
        this.maCT = maCT;
        this.tenCT = tenCT;
        this.maTL = maTL;
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
}
