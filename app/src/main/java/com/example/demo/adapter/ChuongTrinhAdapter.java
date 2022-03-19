package com.example.demo.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.listener.ChuongTrinhListener;
import com.example.demo.model.ChuongTrinh;

import java.util.List;

public class ChuongTrinhAdapter extends RecyclerView.Adapter<ChuongTrinhViewHolder>{
    MainActivity context;
    List<ChuongTrinh> list;
    ChuongTrinhListener listener;

    public ChuongTrinhAdapter(MainActivity context, List<ChuongTrinh> list) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public ChuongTrinhAdapter(MainActivity context, List<ChuongTrinh> list, ChuongTrinhListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChuongTrinhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChuongTrinhViewHolder(LayoutInflater.from(context).inflate(R.layout.list_layout_chuongtrinh, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChuongTrinhViewHolder holder, int position) {
        holder.txtName.setText("Chương trình " + list.get(position).tenCT);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.dialogUpdate( String.valueOf(list.get(holder.getAdapterPosition()).maCT),
                        String.valueOf(list.get(holder.getAdapterPosition()).tenCT),
                        String.valueOf(list.get(holder.getAdapterPosition()).maTL)   );

            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                    return false;
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
