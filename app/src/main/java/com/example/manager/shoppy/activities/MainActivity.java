package com.example.manager.shoppy.activities;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.manager.shoppy.R;
import com.example.manager.shoppy.retrofit.ApiBanHang;
import com.example.manager.shoppy.retrofit.RetrofitClient;
import com.example.manager.shoppy.adapter.LoaiSpAdapter;
import com.example.manager.shoppy.adapter.SanPhamMoiAdapter;
import com.example.manager.shoppy.model.LoaiSp;
import com.example.manager.shoppy.model.SanPhamMoi;
import com.example.manager.shoppy.model.User;
import com.example.manager.shoppy.ultils.Ultils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
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
        STEP 35: TẠO GIỎ HÀNG CÓ CHECK BOX
        STEP 46: UPDATE TÌNH TRẠNG ĐƠN HÀNG APP MANAGER
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
        Paper.init(this);
        if(Paper.book().read("user")!=null){
            User user = Paper.book().read("user");
            Ultils.user_current = user;
        }
        getToken();

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

    private void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if(!TextUtils.isEmpty(s)){
                            compositeDisposable.add(apiBanHang.updateToken(Ultils.user_current.getId(),s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {

                                            },
                                            throwable -> {
                                                Log.d("log", throwable.getMessage());
                                            }
                                    ));
                        }
                    }
                });
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
                    case 3:
                        Intent manhinh = new Intent(getApplicationContext(),MobileActivity.class);
                        manhinh.putExtra("loai",3);
                        startActivity(manhinh);
                        break;
                    case 4:
                        Intent harddrive = new Intent(getApplicationContext(),MobileActivity.class);
                        harddrive.putExtra("loai",4);
                        startActivity(harddrive);
                        break;
                    case 5:
                        Intent ram = new Intent(getApplicationContext(),MobileActivity.class);
                        ram.putExtra("loai",5);
                        startActivity(ram);
                        break;
                    case 6:
                        Intent vga = new Intent(getApplicationContext(),MobileActivity.class);
                        vga.putExtra("loai",6);
                        startActivity(vga);
                        break;
                    case 7:
                        Intent mainboard = new Intent(getApplicationContext(),MobileActivity.class);
                        mainboard.putExtra("loai",7);
                        startActivity(mainboard);
                        break;
                    case 8:
                        Intent cpu = new Intent(getApplicationContext(),MobileActivity.class);
                        cpu.putExtra("loai",8);
                        startActivity(cpu);
                        break;
                    case 9:
                        Intent keyboard = new Intent(getApplicationContext(),MobileActivity.class);
                        keyboard.putExtra("loai",9);
                        startActivity(keyboard);
                        break;
                    case 10:
                        Intent mouse = new Intent(getApplicationContext(),MobileActivity.class);
                        mouse.putExtra("loai",10);
                        startActivity(mouse);
                        break;
                    case 12:
                        Intent donmua = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donmua);
                        break;
                    case 14:
                        Intent quanli = new Intent(getApplicationContext(), QuanLiActivity.class);
                        startActivity(quanli);
                        break;
                    case 15:
                        Intent chat = new Intent(getApplicationContext(), UserActivity.class);
                        startActivity(chat);
                        break;
                    case 16:
                        Intent thongke = new Intent(getApplicationContext(), ThongKeActivity.class);
                        startActivity(thongke);
                        break;
                    case 17:
                        //xoa key user
                        Paper.book().delete("user");
                        //signout
                        FirebaseAuth.getInstance().signOut();
                        Intent dangnhap = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(dangnhap);
                        finish();
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
                                //STEP 36: dang suat
                                mangLoaiSp.add(new LoaiSp("Quản lí","https://kiemthecaomienphi.files.wordpress.com/2023/05/manage.png?fbclid=IwAR2BKVrLn2vhGD0WDL-3w-BM6JFr47RnCBkdRzzWjX93dTHCRIeGI4jDJgw"));
                                mangLoaiSp.add(new LoaiSp("Chat","https://kiemthecaomienphi.files.wordpress.com/2023/05/chat.png?fbclid=IwAR2qbM28btOk8PViChB2Kyv2kh9x3U9i09YVAwF4fCyULRkadAC0VVlU27Q"));
                                mangLoaiSp.add(new LoaiSp("Thống kê","https://kiemthecaomienphi.files.wordpress.com/2023/05/analysis.png?fbclid=IwAR2OK1a84o_H8WwO5CvyPA0Q3z1kQwthGXjToRvXkovJUiRuJugauuUv2t0"));
                                mangLoaiSp.add(new LoaiSp("Đăng xuất","https://kiemthecaomienphi.files.wordpress.com/2023/05/14_log_out.png?fbclid=IwAR18cxKVjEfYjJ-4LOjc9pcH01C7JYCBDrO9N_ypT0d42A1_ad91Zxscdr8"));
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

        //62
        if(Paper.book().read("so_spgiohang")!=null){
            Ultils.mangGioHang = Paper.book().read("so_spgiohang");
        }
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