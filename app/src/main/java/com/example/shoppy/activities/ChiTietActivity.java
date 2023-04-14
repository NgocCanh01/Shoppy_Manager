package com.example.shoppy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shoppy.R;
import com.example.shoppy.model.SanPhamMoi;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tvTenSp, tvGiaSp,tvMoTaChiTiet;
    Button btnThemGioHang;
    ImageView imgChiTiet;
    Spinner spinner;
    Toolbar toolbarChiTiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        actionToolbar();
        initData();
    }

    private void initData() {
        SanPhamMoi sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tvTenSp.setText(sanPhamMoi.getTensanpham());
        tvMoTaChiTiet.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imgChiTiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvGiaSp.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"Đ");
        Integer[] so = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9,10};
        ArrayAdapter<Integer> adapterSpin = new ArrayAdapter<>(this, com.facebook.R.layout.support_simple_spinner_dropdown_item,so);
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
}