package com.reintrinh.quanlytruyenhinh_nhom10.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.ThongTinPhatSongActivity;
import com.reintrinh.quanlytruyenhinh_nhom10.fragment.ChuongTrinhFragment;
import com.reintrinh.quanlytruyenhinh_nhom10.listener.ChuongTrinhListener;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ChuongTrinh;

import java.util.ArrayList;
import java.util.List;

public class ChuongTrinhAdapter extends RecyclerView.Adapter<ChuongTrinhViewHolder>{
    ChuongTrinhFragment context;
    List<ChuongTrinh> list;
    List<ChuongTrinh> listSearch = new ArrayList<>();;
    ChuongTrinhListener listener;

    public ChuongTrinhAdapter(ChuongTrinhFragment context, List<ChuongTrinh> list) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        listSearch.addAll(list);
    }

    public ChuongTrinhAdapter(ChuongTrinhFragment context, List<ChuongTrinh> list, ChuongTrinhListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        listSearch.addAll(list);
    }

    @NonNull
    @Override
    public ChuongTrinhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChuongTrinhViewHolder(LayoutInflater.from(context.getContext()).inflate(R.layout.list_layout_chuongtrinh, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChuongTrinhViewHolder holder, int position) {
        ChuongTrinh chuongTrinh = list.get(position);
        holder.txtName.setText("Chương trình " + chuongTrinh.tenCT);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getContext(), ThongTinPhatSongActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(context.getString(R.string.key_program), chuongTrinh);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                context.dialogUpdate( String.valueOf(list.get(holder.getAdapterPosition()).maCT),
                        String.valueOf(list.get(holder.getAdapterPosition()).tenCT),
                        String.valueOf(list.get(holder.getAdapterPosition()).maTL)   );
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

class ChuongTrinhViewHolder extends RecyclerView.ViewHolder {

    TextView txtName, txtMa;
    CardView cardView;

    public ChuongTrinhViewHolder(@NonNull View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txtName);

        cardView = itemView.findViewById(R.id.cardView);
    }
}
