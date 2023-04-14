package com.example.shoppy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.shoppy.activities.model.SanPhamMoi;
import com.example.shoppy.Interface.ItemClickListener;
import com.example.shoppy.R;
import com.example.shoppy.activities.ChiTietActivity;
import com.example.shoppy.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> array;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.txtTenSp.setText(sanPhamMoi.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"Đ");
        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imgItemSp);
        //STEP 17:
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    //click
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet",sanPhamMoi);//STEP 17
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTenSp,txtGia;
        ImageView imgItemSp;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSp = itemView.findViewById(R.id.txtTenSp);
            txtGia = itemView.findViewById(R.id.txtGia);
            imgItemSp = itemView.findViewById(R.id.imgItemSp);
            itemView.setOnClickListener(this);//STEP 17:

        }
        //STEP 17:
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        //STEP 17:
        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
}
