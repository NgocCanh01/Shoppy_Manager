package com.example.manager.shoppy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manager.shoppy.retrofit.ApiBanHang;
import com.example.manager.shoppy.retrofit.RetrofitClient;
import com.example.manager.shoppy.R;
import com.example.manager.shoppy.adapter.DienThoaiAdapter;
import com.example.manager.shoppy.model.SanPhamMoi;
import com.example.manager.shoppy.ultils.Ultils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbarSearch;
    RecyclerView recyclerViewSearch;
    EditText edtSearch;
    DienThoaiAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        actionToolbar();
    }

    private void initView() {
        sanPhamMoiList = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Ultils.BASE_URL).create(ApiBanHang.class);

        toolbarSearch = findViewById(R.id.toolbarSearch);
        recyclerViewSearch = findViewById(R.id.recycleviewSearch);
        edtSearch = findViewById(R.id.edtSearch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(layoutManager);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    sanPhamMoiList.clear();
                    adapterDt = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
                    recyclerViewSearch.setAdapter(adapterDt);
                } else {
                    getDataSearch(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getDataSearch(String s) {
        sanPhamMoiList.clear();
        compositeDisposable.add(apiBanHang.search(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                sanPhamMoiList = sanPhamMoiModel.getResult();
                                adapterDt = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
                                recyclerViewSearch.setAdapter(adapterDt);
                            } else {
                                Toast.makeText(getApplicationContext(), sanPhamMoiModel.getMessage(), Toast.LENGTH_LONG).show();
                                sanPhamMoiList.clear();
                                adapterDt.notifyDataSetChanged();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}