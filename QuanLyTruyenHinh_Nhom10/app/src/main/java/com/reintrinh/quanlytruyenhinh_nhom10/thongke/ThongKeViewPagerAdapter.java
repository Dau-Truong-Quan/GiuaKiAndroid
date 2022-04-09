package com.reintrinh.quanlytruyenhinh_nhom10.thongke;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.reintrinh.quanlytruyenhinh_nhom10.fragment.BienTapVienFragment;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.ChuongTrinhFragment;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.TheLoaiFragment;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.ThongKeFragment;

public class ThongKeViewPagerAdapter extends FragmentStatePagerAdapter {

    public ThongKeViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Tab1Fragment();
            case 1:
                return new Tab2Fragment();
            default:
                return new Tab1Fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Thể loại";
            case 1:
                return "Chương trình";
            default:
                return "Thể loại";
        }
    }
}
