package com.example.manager.shoppy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manager.shoppy.Interface.IImageClickListener;
import com.example.manager.shoppy.model.EventBus.TinhTongEvent;
import com.example.manager.shoppy.model.GioHang;
import com.example.manager.shoppy.R;
import com.example.manager.shoppy.ultils.Ultils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.tv_item_giohang_tensp.setText(gioHang.getTenSp());
        holder.tv_item_giohang_soluong.setText(gioHang.getSoLuong() + " ");
        Glide.with(context).load(gioHang.getHinhSp()).into(holder.img_item_giohang);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tv_item_giohang_gia.setText(decimalFormat.format((gioHang.getGiaSp())));
        long gia = gioHang.getSoLuong() * gioHang.getGiaSp();
        holder.tv_item_giohang_giasp2.setText(decimalFormat.format(gia));

        //STEP 35:
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Ultils.mangMuaHang.add(gioHang);//sau khi được check
                    //Tinhs tien
                    EventBus.getDefault().postSticky(new TinhTongEvent());//gửi sự kiện qua TinhTongEvent.class
                }else {
                    //duyệt mảng mua hàng
                    for(int i = 0; i<Ultils.mangMuaHang.size(); i++){
                        if(Ultils.mangMuaHang.get(i).getIdSp()==gioHang.getIdSp()){
                            Ultils.mangMuaHang.remove(i);
                            //remove xong tinh lai tien
                            EventBus.getDefault().postSticky(new TinhTongEvent());//gửi sự kiện qua TinhTongEvent.class
                        }
                    }

                }
            }
        });

        //STEP 21:
        holder.setListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                Log.d("TAG", "onImageClick: " + pos + "..." + giatri);
                if (giatri == 1) {
                    if (gioHangList.get(pos).getSoLuong() > 1) {
                        int soLuongMoi = gioHangList.get(pos).getSoLuong() - 1;
                        gioHangList.get(pos).setSoLuong(soLuongMoi);
                        //STEP 22: chỉ update tiền khi cộng hoặc trừ(xóa)
                        holder.tv_item_giohang_soluong.setText(gioHangList.get(pos).getSoLuong() + " ");
                        long gia = gioHangList.get(pos).getSoLuong() * gioHangList.get(pos).getGiaSp();
                        holder.tv_item_giohang_giasp2.setText(decimalFormat.format(gia));
                        //DO HÀM TÍNH TỔNG TIỀN NẰM BÊN GioHang.Activity MÀ SỰ KIỆN CLICK CỘNG TRỪ NẰM BÊN GioHangAdapter
                        //=> C1:CHUYỂN HÀM TỔNG VỀ PUBLIC
                        //=> C2: DÙNG THƯ VIỆN EVENTBUS
                        EventBus.getDefault().postSticky(new TinhTongEvent());//gửi sự kiện qua TinhTongEvent.class
                    }else if(gioHangList.get(pos).getSoLuong()==1){//STEP 22
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo!");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Ultils.mangGioHang.remove(pos);
                                notifyDataSetChanged();//Update data
                                //Sau khi xóa tính tổng lại tiền
                                EventBus.getDefault().postSticky(new TinhTongEvent());//gửi sự kiện qua TinhTongEvent.class

                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else if (giatri == 2) {
                    if (gioHangList.get(pos).getSoLuong() < 11) {
                        int soLuongMoi = gioHangList.get(pos).getSoLuong() + 1;
                        gioHangList.get(pos).setSoLuong(soLuongMoi);
                    }
                    holder.tv_item_giohang_soluong.setText(gioHangList.get(pos).getSoLuong() + " ");
                    long gia = gioHangList.get(pos).getSoLuong() * gioHangList.get(pos).getGiaSp();
                    holder.tv_item_giohang_giasp2.setText(decimalFormat.format(gia));
                    //DO HÀM TÍNH TỔNG TIỀN NẰM BÊN GioHang.Activity MÀ SỰ KIỆN CLICK CỘNG TRỪ NẰM BÊN GioHangAdapter
                    //=> C1:CHUYỂN HÀM TỔNG VỀ PUBLIC
                    //=> C2: DÙNG THƯ VIỆN EVENTBUS
                    EventBus.getDefault().postSticky(new TinhTongEvent());//gửi sự kiện qua TinhTongEvent.class
                }

            }

            });
        }


        @Override
        public int getItemCount() {
            return gioHangList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView img_item_giohang, imgTru, imgCong;
            TextView tv_item_giohang_tensp, tv_item_giohang_gia, tv_item_giohang_soluong, tv_item_giohang_giasp2;
            IImageClickListener listener;
            //STEP 35
            CheckBox checkBox;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                img_item_giohang = itemView.findViewById(R.id.img_item_giohang);
                tv_item_giohang_tensp = itemView.findViewById(R.id.tv_item_giohang_tensp);
                tv_item_giohang_gia = itemView.findViewById(R.id.tv_item_giohang_gia);
                tv_item_giohang_soluong = itemView.findViewById(R.id.tv_item_giohang_soluong);
                tv_item_giohang_giasp2 = itemView.findViewById(R.id.tv_item_giohang_giasp2);
                imgTru = itemView.findViewById(R.id.img_item_giohang_tru);
                imgCong = itemView.findViewById(R.id.img_item_giohang_cong);
                checkBox = itemView.findViewById(R.id.checkbox_item_giohang);
                //event click
                imgCong.setOnClickListener(this);
                imgTru.setOnClickListener(this);
            }

            public void setListener(IImageClickListener listener) {
                this.listener = listener;
            }

            @Override
            public void onClick(View view) {
                if (view == imgTru) {
                    listener.onImageClick(view, getAdapterPosition(), 1);
                    //1 tru
                } else if (view == imgCong) {
                    listener.onImageClick(view, getAdapterPosition(), 2);
                    //2 cong
                }
            }
        }
    }

