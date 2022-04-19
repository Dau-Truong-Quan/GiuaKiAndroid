package com.reintrinh.quanlytruyenhinh_nhom10.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.reintrinh.quanlytruyenhinh_nhom10.listener.OnSaveClickListener;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private OnSaveClickListener onSaveClickListener;

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
            case 4:
                TaiKhoanFragment taiKhoanFragment = new TaiKhoanFragment();
                taiKhoanFragment.setOnSaveClickListener(onSaveClickListener);
                return taiKhoanFragment;
            default:
                return new ThongKeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void setOnSaveClickListener(OnSaveClickListener onSaveClickListener) {
        this.onSaveClickListener = onSaveClickListener;
    }
}
