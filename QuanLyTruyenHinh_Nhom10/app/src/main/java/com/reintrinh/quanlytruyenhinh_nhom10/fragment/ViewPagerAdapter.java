package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ThongKeFragment();
            case 1:
                return new ChuongTrinhFragment();
            case 2:
                return new TheLoaiFragment();
            case 3:
                return new BienTapVienFragment();
            default:
                return new ThongKeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
