package com.example.shoppy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoppy.R;
import com.example.shoppy.model.Item;

import java.util.List;

public class ChiTietAdapter extends RecyclerView.Adapter<ChiTietAdapter.MyViewHolder> {
    Context context;
    List<Item>itemList;

    public ChiTietAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.tvTenSp.setText(item.getTensanpham()+"");
        holder.tvSoLuong.setText("Số lượng: "+item.getSoluong()+"");
        Glide.with(context).load(item.getHinhanh()).into(holder.imgChiTiet);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgChiTiet;
        TextView tvTenSp,tvSoLuong;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChiTiet = itemView.findViewById(R.id.item_imgChiTiet);
            tvTenSp = itemView.findViewById(R.id.item_tenSpChiTiet);
            tvSoLuong = itemView.findViewById(R.id.item_soLuongChiTiet);
        }
    }
}
