package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.reintrinh.quanlytruyenhinh_nhom10.R;

public class BienTapVienFragment extends Fragment {

    public BienTapVienFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bien_tap_vien, container, false);
    }
}