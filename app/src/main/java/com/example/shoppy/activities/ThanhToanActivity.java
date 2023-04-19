package com.example.shoppy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppy.R;
import com.example.shoppy.ultils.EmailSingleton;
import com.example.shoppy.ultils.Ultils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbarThanhToan;
    TextView tvGiaTien, tvSdt, tvEmail;
    EditText edtDiaChi;
    AppCompatButton btnDatHang;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        initControl();
    }

    private void initControl() {
        setSupportActionBar(toolbarThanhToan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThanhToan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Lấy data từ bên GiỏHàng
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long tongTien = getIntent().getLongExtra("tongtien",0);
        tvGiaTien.setText(decimalFormat.format(tongTien));
//        Intent getEmail = getIntent();
//        Bundle bundle = getEmail.getExtras();
//        if(bundle!=null){
//            tvEmail.setText(getEmail.getStringExtra(Ultils.KEY_GET_EMAIL));
//        }
//        tvEmail.setText(mUser);
        String email = EmailSingleton.getInstance().getEmail();
        tvEmail.setText(email);


        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strDiaChi = edtDiaChi.getText().toString().trim();
                if(TextUtils.isEmpty(strDiaChi)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ",Toast.LENGTH_LONG).show();
                }else {
                    //Nếu nhập địa chỉ ta sẽ post data(Giỏ hàng) lên server lưu database thành chuỗi json
                    //Dùng thư viện Gson
                    Log.d("TAG", new Gson().toJson(Ultils.mangGioHang));
                }
            }
        });
    }

    private void initView() {
        toolbarThanhToan = findViewById(R.id.toolbarThanhToan);
        tvGiaTien = findViewById(R.id.tvGiaTien);
        tvSdt = findViewById(R.id.tvSdt);
        tvEmail = findViewById(R.id.tvEmail);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnDatHang = findViewById(R.id.btnDatHang);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }
}