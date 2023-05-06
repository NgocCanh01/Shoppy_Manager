package com.example.manager.shoppy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manager.shoppy.Interface.ItemClickListener;
import com.example.manager.shoppy.activities.ChiTietActivity;
import com.example.manager.shoppy.model.SanPhamMoi;
//import com.example.shoppy.activities.Interface.ItemClickListener;
//import com.example.shoppy.activities.model.SanPhamMoi;
import com.example.manager.shoppy.R;

import java.text.DecimalFormat;
import java.util.List;

public class DienThoaiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi>array;
    private static final int VIEW_TYPE_DATA = 0;//STEP 13
    private static final int VIEW_TYPE_LOADING = 1;//STEP 13

    public DienThoaiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dienthoai,parent,false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }

    }

    //STEP 13
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPhamMoi sanPham = array.get(position);
            myViewHolder.tensp.setText(sanPham.getTensanpham().trim());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.giasp.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPham.getGiasp()))+"Đ");
            myViewHolder.mota.setText(sanPham.getMota());
//            myViewHolder.idSp.setText(sanPham.getId()+"");//STEP 14
            Glide.with(context).load(sanPham.getHinhanh()).into(myViewHolder.hinhAnh);
            //STEP 14
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        //click
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet",sanPham);//STEP 17
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    //STEP 13
    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING :VIEW_TYPE_DATA;
        //nếu mảng get vị trí null => xuất ra  LOADING ngược => DATA
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    //STEP 13:
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar_dt);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp,giasp,mota,idSp;
        ImageView hinhAnh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.tvItemDt_Ten);
            giasp = itemView.findViewById(R.id.tvItemDt_Gia);
            mota = itemView.findViewById(R.id.tvItemDt_Mota);
            hinhAnh = itemView.findViewById(R.id.image_itemDt);
//            idSp = itemView.findViewById(R.id.tvItemDt_idSp);//STEP 14
            itemView.setOnClickListener(this);//STEP 14
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;//STEP 14
        }

        @Override
        public void onClick(View view) {//STEP 14
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
}
