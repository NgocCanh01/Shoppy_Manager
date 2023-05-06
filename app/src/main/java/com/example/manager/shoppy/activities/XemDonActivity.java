package com.example.manager.shoppy.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.manager.shoppy.model.DonHang;
import com.example.manager.shoppy.model.EventBus.DonHangEvent;
import com.example.manager.shoppy.retrofit.ApiBanHang;
import com.example.manager.shoppy.retrofit.RetrofitClient;
import com.example.manager.shoppy.R;
import com.example.manager.shoppy.adapter.DonHangAdapter;
import com.example.manager.shoppy.ultils.Ultils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView rcDonHang;
    Toolbar toolbar;
    DonHang donHang;//STEP 46
    int trangThai;//STEP 46
    AlertDialog dialog;//STEP 46

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolbar();
        getOrder();

    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(0)//Ultils.user_current.getId() = 0 để lấy tất cả đơn hàng
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
//                            Toast.makeText(getApplicationContext(), donHangModel.getResult().get(0).getItem().get(1).getTensp(), Toast.LENGTH_SHORT).show();
                            //laays thang dau tien
                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(),donHangModel.getResult());
                            rcDonHang.setAdapter(adapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không lấy được tên hàng", Toast.LENGTH_SHORT).show();

                        }
                ));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Ultils.BASE_URL).create(ApiBanHang.class);
        rcDonHang = findViewById(R.id.recycle_XemDon);
        toolbar = findViewById(R.id.toolbarXemDon);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcDonHang.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void showCostumDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_donhang,null);
        Spinner spinner = view.findViewById(R.id.spinner_dialog);
        AppCompatButton btn_dong_y = view.findViewById(R.id.btn_dongy_dialog);
        List<String> list = new ArrayList<>();
        list.add("Đơn hàng đang được xử lí");
        list.add("Đơn hàng đã chấp nhận và đang bàn giao cho đơn vị vận chuyển");
        list.add("Đơn hàng đã giao cho đơn vị vận chuyển");
        list.add("Thành công");
        list.add("Đơn hàng đã hủy");
        //Đổ data từ list string này cho spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(adapter);
        //Hiển thị cái trạng thái hiện tại của đơn hàng
        spinner.setSelection(donHang.getTrangthai());
        //Bắt event cho spinner khi nó thay đổi
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trangThai = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //bấm btn_dong_y update trên database
        btn_dong_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capNhatDonHang();
            }
        });

        //show thoong bao
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void capNhatDonHang() {
        //gửi id, trangThai lên database
        compositeDisposable.add(apiBanHang.updateOrder(donHang.getId(),trangThai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageModel -> {
                    //sau khi update trang thai chay lai ham
                    getOrder();
                    dialog.dismiss();//chon xong dong lai
                },
                        throwable -> {

                        }));
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventDonHang(DonHangEvent event){
        if(event != null){
            donHang = event.getDonHang();//lay don hang
            //tạo cho user dialog(xml)
            showCostumDialog();
        }
    }

    //Đăng kí và hủy đăng kí EventBus
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}