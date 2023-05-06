package com.example.manager.shoppy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.manager.shoppy.R;
import com.example.manager.shoppy.adapter.DienThoaiAdapter;
import com.example.manager.shoppy.model.SanPhamMoi;
import com.example.manager.shoppy.retrofit.ApiBanHang;
import com.example.manager.shoppy.retrofit.RetrofitClient;
import com.example.manager.shoppy.ultils.Ultils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MobileActivity extends AppCompatActivity {
    Toolbar toolbarDT;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    DienThoaiAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        apiBanHang = RetrofitClient.getInstance(Ultils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai", 1);
        anhXa();
        actionToolbar();
        getData(page);//STEP 13
        addEventLoad();//STEP 13
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size() - 1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                sanPhamMoiList.add(null);
                adapterDt.notifyItemInserted(sanPhamMoiList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remove null
                sanPhamMoiList.remove(sanPhamMoiList.size() - 1);
                adapterDt.notifyItemRemoved(sanPhamMoiList.size());
                page = page + 1;
                //Sau khi get data tại trang mới lại set về false để tiếp tục load
                getData(page);
                adapterDt.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                //STEP 13:
                                //Vì hàm này được tái sử dụng => Viết lại khi nào adapter bị null thì mới new
                                if (adapterDt == null) {
                                    sanPhamMoiList = sanPhamMoiModel.getResult();
                                    adapterDt = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
                                    recyclerView.setAdapter(adapterDt);
                                } else {
                                    //Sau khi có data sẽ duyệt qua dữ liệu mới lấy
                                    int viTri = sanPhamMoiList.size() - 1;
                                    int soLuongAdd = sanPhamMoiModel.getResult().size();
                                    for (int i = 0; i < soLuongAdd; i++) {
                                        sanPhamMoiList.add(sanPhamMoiModel.getResult().get(i));//add vào sanPhamMoiList
                                    }
                                    adapterDt.notifyItemRangeInserted(viTri, soLuongAdd);//Thông báo cho Adapter add vị trí nào, số lượng ? => để biết add thêm vào
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Hết dữ liệu rồi", Toast.LENGTH_LONG).show();
                                isLoading = true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối tới server", Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarDT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDT.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void anhXa() {
        toolbarDT = findViewById(R.id.toolbarDT);
        recyclerView = findViewById(R.id.recycleviewDT);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//STEP 13
        recyclerView.setLayoutManager(linearLayoutManager);//STEP 13
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();//STEP 12
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}