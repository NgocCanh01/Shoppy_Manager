package com.example.shoppy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppy.R;
import com.example.shoppy.model.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listDonHang;

    public DonHangAdapter(Context context, List<DonHang> listDonHang) {
        this.context = context;
        this.listDonHang = listDonHang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listDonHang.get(position);
        holder.tvDonHang.setText("Đơn hàng: "+donHang.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
          holder.recyclerViewChiTiet.getContext(),
          LinearLayoutManager.VERTICAL,
          false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        //adapter chitiet
        ChiTietAdapter chiTietAdapter = new ChiTietAdapter(context,donHang.getItem());
        holder.recyclerViewChiTiet.setLayoutManager(layoutManager);
        holder.recyclerViewChiTiet.setAdapter(chiTietAdapter);
        holder.recyclerViewChiTiet.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return listDonHang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDonHang;
        RecyclerView recyclerViewChiTiet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDonHang = itemView.findViewById(R.id.idDonHang);
            recyclerViewChiTiet = itemView.findViewById(R.id.recycleview_ChiTiet);
        }
    }
}
