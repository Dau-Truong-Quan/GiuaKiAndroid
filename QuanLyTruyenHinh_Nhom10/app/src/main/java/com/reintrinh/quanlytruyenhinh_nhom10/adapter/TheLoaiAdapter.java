package com.reintrinh.quanlytruyenhinh_nhom10.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.ChuongTrinhFragment;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.TheLoaiFragment;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;
import com.reintrinh.quanlytruyenhinh_nhom10.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiAdapter extends RecyclerView.Adapter<TheLoaiAdapter.TheLoaiViewHolder>{
    private TheLoaiFragment context;
    private List<TheLoai> list;
    private List<TheLoai> listSearch = new ArrayList<>();
    private IClickItemListener iClickItemListener;

    public interface IClickItemListener {
        void onClickEditItem(TheLoai theLoai);
        void onClickDeleteItem(TheLoai theLoai);
    }

    public TheLoaiAdapter(@NonNull TheLoaiFragment context, @NonNull List<TheLoai> list, IClickItemListener iClickItemListener) {
        this.context = context;
        this.list = list;
        this.iClickItemListener = iClickItemListener;
        this.listSearch.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(List<TheLoai> theLoaisList) {
        this.list = theLoaisList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TheLoaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TheLoaiViewHolder(LayoutInflater.from(context.getContext()).inflate(R.layout.list_layout_theloai, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull TheLoaiViewHolder holder, int position) {
        TheLoai theLoai = list.get(position);
        if (theLoai == null) {
            return;
        }
        holder.txtName.setText(theLoai.getTenTL());

        byte[] hinhAnh = theLoai.getHinhAnh();
        holder.imgTheLoai.setImageBitmap(ImageUtil.getBitmapFromByteArray(hinhAnh));

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getContext(), ChuongTrinhFragment.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("the_loai", theLoai);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });*/

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickEditItem(theLoai);
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickDeleteItem(theLoai);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public void filter(String text)
    {
        list.clear();
        text = text.trim().toLowerCase();

        if (text.length() == 0)
            list.addAll(listSearch);
        else {
            for (TheLoai tl : listSearch) {
                if (tl.getTenTL().toLowerCase().contains(text)) {
                    list.add(tl);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class TheLoaiViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgTheLoai;
        private TextView txtName;
        private ImageView imgSua, imgXoa;
        //private CardView cardView;

        public TheLoaiViewHolder(@NonNull View itemView) {
            super(itemView);

            imgTheLoai = itemView.findViewById(R.id.img_the_loai);
            txtName = itemView.findViewById(R.id.txtName);
            imgSua = itemView.findViewById(R.id.img_sua);
            imgXoa = itemView.findViewById(R.id.img_xoa);
            //cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public void updateDataSearch() {
        listSearch.clear();
        listSearch.addAll(list);
    }
}
