package com.reintrinh.quanlytruyenhinh_nhom10.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.reintrinh.quanlytruyenhinh_nhom10.ChuongTrinhActivity;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.ThongTinPhatSongActivity;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.ChuongTrinhFragment;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.TheLoaiFragment;
import com.reintrinh.quanlytruyenhinh_nhom10.listener.ChuongTrinhListener;
import com.reintrinh.quanlytruyenhinh_nhom10.listener.TheLoaiListener;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;
import com.reintrinh.quanlytruyenhinh_nhom10.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiAdapter extends RecyclerView.Adapter<TheLoaiViewHolder>{
    TheLoaiFragment context;
    List<TheLoai> list;
    List<TheLoai> listSearch = new ArrayList<>();;
    TheLoaiListener listener;

    public TheLoaiAdapter(TheLoaiFragment context, List<TheLoai> list) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        listSearch.addAll(list);
    }

    public TheLoaiAdapter(TheLoaiFragment context, List<TheLoai> list, TheLoaiListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        listSearch.addAll(list);
    }

    @NonNull
    @Override
    public TheLoaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TheLoaiViewHolder(LayoutInflater.from(context.getContext()).inflate(R.layout.list_layout_theloai, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TheLoaiViewHolder holder, int position) {
        holder.txtName.setText("Thể loại " + list.get(position).getTenTL());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getContext(), ChuongTrinhActivity.class);
                context.startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                context.dialogUpdate( String.valueOf(list.get(holder.getAdapterPosition()).getMaTl()),
                        String.valueOf(list.get(holder.getAdapterPosition()).getTenTL()));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

class TheLoaiViewHolder extends RecyclerView.ViewHolder {

    TextView txtName, txtMa;
    CardView cardView;

    public TheLoaiViewHolder(@NonNull View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txtName);

        cardView = itemView.findViewById(R.id.cardView);
    }
}
