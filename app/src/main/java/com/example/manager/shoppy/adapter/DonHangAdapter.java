package com.example.manager.shoppy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.shoppy.Interface.ItemClickListener;
import com.example.manager.shoppy.model.DonHang;
import com.example.manager.shoppy.R;
import com.example.manager.shoppy.model.EventBus.DonHangEvent;

import org.greenrobot.eventbus.EventBus;

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
        holder.trangthai.setText(tinhTrangDon(donHang.getTrangthai()));
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
        //STEP 46
        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                //ktra event onLong cho chac
                if(isLongClick){
                    //gui toan bo don hang qua
                    //cần EventBus : DonHangEvent=> post đơn hàng qua màn hình XemDon để perform
                    EventBus.getDefault().postSticky(new DonHangEvent(donHang));

                }
            }
        });
    }

    private String tinhTrangDon(int status){
        String result="";
        switch (status){
            case 0:
                result = "Đơn hàng đang được xử lí";
                break;
            case 1:
                result = "Đơn hàng đã chấp nhận và đang bàn giao cho đơn vị vận chuyển";
                break;
            case 2:
                result = "Đơn hàng đã giao cho đơn vị vận chuyển";
                break;
            case 3:
                result = "Thành công";
                break;
            case 4:
                result = "Đơn hàng đã hủy";
                break;
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return listDonHang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView tvDonHang,trangthai;
        RecyclerView recyclerViewChiTiet;
        ItemClickListener listener;//STEP 46
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDonHang = itemView.findViewById(R.id.idDonHang);
            trangthai = itemView.findViewById(R.id.trangThai);
            recyclerViewChiTiet = itemView.findViewById(R.id.recycleview_ChiTiet);
            itemView.setOnLongClickListener(this);
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onClick(view,getAdapterPosition(),true);
            return false;
        }
    }
}
