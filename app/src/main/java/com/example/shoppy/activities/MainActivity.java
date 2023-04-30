package com.example.shoppy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.shoppy.R;
import com.example.shoppy.adapter.LoaiSpAdapter;
import com.example.shoppy.adapter.SanPhamMoiAdapter;
import com.example.shoppy.model.LoaiSp;
import com.example.shoppy.model.SanPhamMoi;
import com.example.shoppy.retrofit.ApiBanHang;
import com.example.shoppy.retrofit.RetrofitClient;
import com.example.shoppy.ultils.Ultils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    /*
        STEP 1,2: GIAO DIỆN MÀN HÌNH CHÍNH, QUẢNG CÁO:
    - change:MainActiviti, main_xml
        STEP 3,4: TẠO ADAPTER CHO LISTVIEW LOAISP, KẾT NỐI SERVER
        STEP 5 - 8: TẠO ADAPTER CHO RECYCLEVIEW  & HIỂN THỊ DATA MÀN HÌNH CHÍNH
        STEP 9: BẮT EVENT CHO MENU CHUYỂN MÀN
        STEP 10 11 12: TẠO ADAPTER CHO RECYCLEVIEW MÀN HÌNH ĐT & ĐƯA DATA VÀO RECYCLE SCREEN ĐT
        STEP 13: TẠO LOADMORE CHO RECYCLE VIEW ĐIỆN THOẠI
        STEP 14: BẮT EVENT CHUYỂN MÀN CHO RECYCLE VIEW ĐIỆN THOẠI
        STEP 15: ĐỔ DATA CHO LAPTOP
        STEP 16: GIAO DIỆN CHI TIẾT SẢN PHẨM
        STEP 17: ĐẨY DATA VÀO MÀN HÌNH CHI TIẾT
        STEP 18: THÊM SP VÀO GIỎ HÀNG
        STEP 19 20 21: GIAO DIỆN MÀN HÌNH GIỎ HÀNG, ĐẨY DATA VÀO MÀN HÌNH GIỎ HÀNG, TĂNG GIẢM TỔNG TIỀN GIỎ HÀNG
        STEP 22: XÓA SẢN PHẨM GIỎ HÀNG VÀ MÀN HÌNH ĐĂNG KÍ: muốn lưu trữ thông tin mua khách hàng cần có đăng kí, đăng nhập
        STEP 23(28): GIAO DIỆN MÀN HÌNH ĐẶT HÀNG
        STEP 24: XỬ LÍ NÚT THANH TOÁN GIỎ HÀNG
     */
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewMain;
    NavigationView navigationView;
    ListView listViewMain;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangLoaiSp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
//    String email;
    ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Ultils.BASE_URL).create(ApiBanHang.class);
        anhXa();
        actionBar();
        //STEP 2:
        if (isConnected(this)) {
            //Toast.makeText(getApplicationContext(),"ok ket noi",Toast.LENGTH_LONG).show();
            actionViewFlipper();//add QC cho viewFlipp
            //Hàm kết nối file php lấy tên loại sp
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        } else {
            Toast.makeText(getApplicationContext(), "Không có internet, vui lòng kết nối!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent trangChu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangChu);
                        break;
                    case 1:
                        Intent dienThoai = new Intent(getApplicationContext(), MobileActivity.class);
                        dienThoai.putExtra("loai", 1);
                        startActivity(dienThoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), MobileActivity.class);
                        laptop.putExtra("loai", 2);//STEP 14
                        startActivity(laptop);
                        break;
                    case 5:
                        Intent donmua = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donmua);
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                recyclerViewMain.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được tới server", Toast.LENGTH_LONG).show();
                        }
                ));

    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()) {
//                                Toast.makeText(getApplicationContext(),loaiSpModel.getResult().get(0).getTensanpham(),Toast.LENGTH_LONG).show();
                                //STEP 4
                                //Thêm data cho list view
                                mangLoaiSp = loaiSpModel.getResult();//nối data từ loại sp model vào mangLoaiSp
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangLoaiSp);
                                listViewMain.setAdapter(loaiSpAdapter);
                            }
                        },throwable -> {//TỰ THÊM
                            Toast.makeText(getApplicationContext(), "Không lấy được sản phẩm", Toast.LENGTH_LONG).show();

                        }

                ));
    }

    private void actionViewFlipper() {
        List<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangQuangCao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangQuangCao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        //Dùng thư viện glider đưa hình ảnh vào imageView
        for (int i = 0; i < mangQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        //set Animation cho viewFlipp
        Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slideIn);
        viewFlipper.setOutAnimation(slideOut);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void anhXa() {
        toolbar = findViewById(R.id.tbMain);
        viewFlipper = findViewById(R.id.vfMain);
        recyclerViewMain = findViewById(R.id.rcSanPham);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewMain.setLayoutManager(layoutManager);
        recyclerViewMain.setHasFixedSize(true);
        listViewMain = findViewById(R.id.lvMain);
        navigationView = findViewById(R.id.nvMain);
        drawerLayout = findViewById(R.id.drawerLayoutMain);
        badge = findViewById(R.id.menu_soLuong);
        frameLayout = findViewById(R.id.frameGioHang);
        imgSearch = findViewById(R.id.imgSearch);



        //STEP 2:
        //Khởi tạo list
        mangLoaiSp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
//        Khởi tạo Adapter
//        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangLoaiSps);
//        lvMain.setAdapter(loaiSpAdapter);
        //STEP 18:
        if(Ultils.mangGioHang == null){
            Ultils.mangGioHang = new ArrayList<>();
        }else{
            //STEP 20 21
            int totalItem = 0;
            for(int i=0; i<Ultils.mangGioHang.size(); i++){
                totalItem = totalItem + Ultils.mangGioHang.get(i).getSoLuong();
            }
            badge.setText(String.valueOf(totalItem));
        }
//        Intent getEmail = getIntent();

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);

            }
        });
        //STEP 34:
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

    }
    //STEP 20 21:
    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for(int i=0; i<Ultils.mangGioHang.size(); i++){
            totalItem = totalItem + Ultils.mangGioHang.get(i).getSoLuong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    //STEP 5:
    //Tạo hàm kiểm tra kết nối internet
    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
}