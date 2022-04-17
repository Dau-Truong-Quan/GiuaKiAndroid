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
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class ThongTinPhatSongAdapter extends RecyclerView.Adapter<ThongTinPhatSongAdapter.ThongTinPhatSongViewHolder> {

    public interface IClickItemListener {
        void onClickEditItem(ThongTinPhatSong thongTinPhatSong);
        void onClickDeleteItem(ThongTinPhatSong thongTinPhatSong);
    }

    private List<ThongTinPhatSong> thongTinPhatSongList;
    private List<ThongTinPhatSong> dataSearch;
    private IClickItemListener iClickItemListener;

    public ThongTinPhatSongAdapter(List<ThongTinPhatSong> thongTinPhatSongList, IClickItemListener iClickItemListener) {
        this.thongTinPhatSongList = thongTinPhatSongList;
        dataSearch = new ArrayList<>(thongTinPhatSongList);
        this.iClickItemListener = iClickItemListener;
        notifyDataSetChanged();
    }

    public void setData(List<ThongTinPhatSong> thongTinPhatSongList) {
        this.thongTinPhatSongList = thongTinPhatSongList;
        dataSearch = new ArrayList<>(thongTinPhatSongList);
        notifyDataSetChanged();
    }

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
        byte[] hinhAnh = thongTinPhatSong.getHinhAnh();
        holder.imgThongTinPS.setImageBitmap(ImageUtil.getBitmapFromByteArray(hinhAnh));
        holder.tvNgayPS.setText(thongTinPhatSong.getNgayPhatSong().toString());
        holder.tvTenBTV.setText(thongTinPhatSong.getBienTapVien().getHoTen());
        holder.tvThoiLuong.setText(thongTinPhatSong.getThoiLuong() + " phút");
        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickEditItem(thongTinPhatSong);
            }
        });
        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickDeleteItem(thongTinPhatSong);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (thongTinPhatSongList != null) {
            return thongTinPhatSongList.size();
        }
        return 0;
    }

    public static class ThongTinPhatSongViewHolder extends RecyclerView.ViewHolder {

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

    public void filter(String text)
    {
        thongTinPhatSongList.clear();
        text = text.trim().toLowerCase();

        if(text.length() == 0)
            thongTinPhatSongList.addAll(dataSearch);
        else {
            for (ThongTinPhatSong thongTinPhatSong : dataSearch) {
                if (thongTinPhatSong.getBienTapVien().getHoTen().toLowerCase().contains(text)
                    || thongTinPhatSong.getNgayPhatSong().contains(text)) {
                    thongTinPhatSongList.add(thongTinPhatSong);
                }
            }
        }
        notifyDataSetChanged();
    }
}
