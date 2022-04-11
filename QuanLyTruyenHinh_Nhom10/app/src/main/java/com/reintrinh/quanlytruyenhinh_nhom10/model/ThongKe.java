package com.reintrinh.quanlytruyenhinh_nhom10.model;

public class ThongKe {
    private String label;
    private int value;

    public ThongKe() {
    }

    public ThongKe(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
