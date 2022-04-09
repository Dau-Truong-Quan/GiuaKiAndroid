package com.reintrinh.quanlytruyenhinh_nhom10.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongTinPhatSong;

import java.util.List;

public class ThongTinPhatSongAdapter extends RecyclerView.Adapter<ThongTinPhatSongAdapter.ThongTinPhatSongViewHolder> {

    private List<ThongTinPhatSong> thongTinPhatSongList;

    @NonNull
    @Override
    public ThongTinPhatSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thong_tin_phat_song, parent, false);
        return new ThongTinPhatSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongTinPhatSongViewHolder holder, int position) {
        ThongTinPhatSong thongTinPhatSong = thongTinPhatSongList.get(position);
        if (thongTinPhatSong == null) {
            return;
        }
        holder.tvNgayPS.setText(thongTinPhatSong.getNgayPhatSong().toString());
//        holder.tvTenBTV.setText(thongTinPhatSong.get);
    }

    @Override
    public int getItemCount() {
        if (thongTinPhatSongList != null) {
            return thongTinPhatSongList.size();
        }
        return 0;
    }

    public class ThongTinPhatSongViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgThongTinPS;
        private TextView tvNgayPS, tvTenBTV, tvThoiLuong;
        private ImageView imgSua, imgXoa;

        public ThongTinPhatSongViewHolder(@NonNull View itemView) {
            super(itemView);

            imgThongTinPS = itemView.findViewById(R.id.img_thong_tin_phat_song);
            tvNgayPS = itemView.findViewById(R.id.tv_ngay_phat_song);
            tvTenBTV = itemView.findViewById(R.id.tv_bien_tap_vien);
            tvThoiLuong = itemView.findViewById(R.id.tv_thoi_luong);
            imgSua = itemView.findViewById(R.id.img_sua);
            imgXoa = itemView.findViewById(R.id.img_xoa);
        }
    }
}
