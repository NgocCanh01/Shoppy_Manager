package com.example.shoppy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shoppy.R;
import com.example.shoppy.adapter.GioHangAdapter;
import com.example.shoppy.model.EventBus.TinhTongEvent;
import com.example.shoppy.ultils.Ultils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {
    TextView gioHangTrong,tongTien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnMuaHang;
    GioHangAdapter adapter;
    long tongTienSp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tinhTongTien();
    }

    private void tinhTongTien() {
        //duyệt mảng giỏ hàng tính tổng tiền
        tongTienSp = 0;
        //sua STEP 35
        for(int i = 0; i<Ultils.mangMuaHang.size();i++){
            tongTienSp = tongTienSp+(Ultils.mangMuaHang.get(i).getGiaSp()*Ultils.mangMuaHang.get(i).getSoLuong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongTien.setText(decimalFormat.format(tongTienSp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Nếu giỏ hàng trống
        if(Ultils.mangGioHang.size()==0){
            gioHangTrong.setVisibility(View.VISIBLE);
        }else{
            //Gio hang k trong tao adapter
            adapter = new GioHangAdapter(getApplicationContext(),Ultils.mangGioHang);
            recyclerView.setAdapter(adapter);
        }
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ThanhToanActivity.class);
                intent.putExtra("tongtien",tongTienSp);//STEP 23(29)
                //STEP 35: MUA XONG ĐƠN HÀNG THÌ CLEAR NÓ ĐI
                Ultils.mangGioHang.clear();
                startActivity(intent);
            }
        });


    }

    private void initView() {
        gioHangTrong = findViewById(R.id.tvGioHangTrong);
        toolbar = findViewById(R.id.toolbarGioHang);
        recyclerView = findViewById(R.id.recycleGioHang);
        tongTien = findViewById(R.id.tvTongTien);
        btnMuaHang = findViewById(R.id.btnMuaHang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if(event!=null){
            tinhTongTien();
        }
    }
}