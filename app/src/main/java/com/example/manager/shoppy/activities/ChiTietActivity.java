package com.example.manager.shoppy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manager.shoppy.R;
import com.example.manager.shoppy.model.GioHang;
import com.example.manager.shoppy.model.SanPhamMoi;
import com.example.manager.shoppy.ultils.Ultils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tvTenSp, tvGiaSp, tvMoTaChiTiet;
    Button btnThemGioHang;
    ImageView imgChiTiet;
    Spinner spinner;
    Toolbar toolbarChiTiet;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        actionToolbar();
        initData();
        initControl();//STEP 18:
    }

    private void initControl() {
        btnThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();//STEP 18:
            }
        });
    }

    private void themGioHang() {
        if (Ultils.mangGioHang.size() > 0) {
            boolean flag = false;
            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Ultils.mangGioHang.size(); i++) {
                if (Ultils.mangGioHang.get(i).getIdSp() == sanPhamMoi.getId()) {
                    Ultils.mangGioHang.get(i).setSoLuong(soLuong + Ultils.mangGioHang.get(i).getSoLuong());
                    long gia = Long.parseLong(sanPhamMoi.getGiasp()) * Ultils.mangGioHang.get(i).getSoLuong();
                    Ultils.mangGioHang.get(i).setGiaSp(gia);
                    flag = true;
                }
            }
            if(flag == false){
                long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soLuong;
                GioHang gioHang = new GioHang();
                gioHang.setGiaSp(gia);
                gioHang.setSoLuong(soLuong);
                gioHang.setIdSp(sanPhamMoi.getId());
                gioHang.setTenSp(sanPhamMoi.getTensanpham());
                gioHang.setHinhSp(sanPhamMoi.getHinhanh());
                Ultils.mangGioHang.add(gioHang);
            }


        } else {
            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soLuong;
            GioHang gioHang = new GioHang();
            gioHang.setGiaSp(gia);
            gioHang.setSoLuong(soLuong);
            gioHang.setIdSp(sanPhamMoi.getId());
            gioHang.setTenSp(sanPhamMoi.getTensanpham());
            gioHang.setHinhSp(sanPhamMoi.getHinhanh());
            Ultils.mangGioHang.add(gioHang);
        }
        //FIX ĐẾM SỐ LƯỢNG SẢN PHẨM(KHÔNG PHẢI LOẠI)
        int totalItem = 0;
        for(int i=0; i<Ultils.mangGioHang.size(); i++){
            totalItem = totalItem + Ultils.mangGioHang.get(i).getSoLuong();
        }
        badge.setText(String.valueOf(totalItem));
        //ĐOẠN NÀY CHỈ ĐẾM SỐ SP KHÁC NHAU THÊM VÀO
//        badge.setText(String.valueOf(Ultils.mangGioHang.get());

    }

    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tvTenSp.setText(sanPhamMoi.getTensanpham());
        tvMoTaChiTiet.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imgChiTiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvGiaSp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp())) + "Đ");
        Integer[] so = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapterSpin = new ArrayAdapter<>(this, com.facebook.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterSpin);
    }

    private void initView() {
        tvTenSp = findViewById(R.id.tvTenSp);
        tvGiaSp = findViewById(R.id.tvGiaSp);
        tvMoTaChiTiet = findViewById(R.id.tvMoTaChiTiet);
        btnThemGioHang = findViewById(R.id.btnThemGioHang);
        imgChiTiet = findViewById(R.id.imgChiTiet);
        spinner = findViewById(R.id.spinner);
        toolbarChiTiet = findViewById(R.id.toolbarChiTiet);
        //STEP 18:
        badge = findViewById(R.id.menu_soLuong);
        FrameLayout frameLayoutGioHang = findViewById(R.id.frameGioHang);
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang= new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if(Ultils.mangGioHang !=null){
            //FIX ĐẾM SỐ LƯỢNG SẢN PHẨM(KHÔNG PHẢI LOẠI)
            int totalItem = 0;
            for(int i=0; i<Ultils.mangGioHang.size(); i++){
                totalItem = totalItem + Ultils.mangGioHang.get(i).getSoLuong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarChiTiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChiTiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //STEP 22:
    @Override
    protected void onResume() {
        super.onResume();
        if(Ultils.mangGioHang !=null){
            //FIX ĐẾM SỐ LƯỢNG SẢN PHẨM(KHÔNG PHẢI LOẠI)
            int totalItem = 0;
            for(int i=0; i<Ultils.mangGioHang.size(); i++){
                totalItem = totalItem + Ultils.mangGioHang.get(i).getSoLuong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}