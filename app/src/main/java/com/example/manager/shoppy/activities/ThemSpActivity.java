package com.example.manager.shoppy.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.manager.shoppy.R;
import com.example.manager.shoppy.databinding.ActivityThemSpBinding;
import com.example.manager.shoppy.model.MessageModel;
import com.example.manager.shoppy.model.SanPhamMoi;
import com.example.manager.shoppy.retrofit.ApiBanHang;
import com.example.manager.shoppy.retrofit.RetrofitClient;
import com.example.manager.shoppy.ultils.Ultils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import soup.neumorphism.NeumorphCardView;

public class ThemSpActivity extends AppCompatActivity {
    Spinner spinner;
    int loai = 0;
    ActivityThemSpBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String mediaPath;
    SanPhamMoi sanPhamSua;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemSpBinding.inflate(getLayoutInflater());
        apiBanHang= RetrofitClient.getInstance(Ultils.BASE_URL).create(ApiBanHang.class);
        setContentView(binding.getRoot());
        initView();
        initData();
        Intent intent = getIntent();
        sanPhamSua = (SanPhamMoi) intent.getSerializableExtra("sua");
        //ktra khi nào sửa, thêm
        if(sanPhamSua == null){
            //them moi
        }else {
            flag = true;
            binding.btnThem.setText("Sửa sản phẩm");
            //Show data
            binding.textInputMota.setText(sanPhamSua.getMota());
            binding.textInputGia.setText(sanPhamSua.getGiasp()+"");
            binding.textInputTenSp.setText(sanPhamSua.getTensanpham());
            binding.textInputHinhAnh.setText(sanPhamSua.getHinhanh());
            binding.spinnerLoai.setSelection(sanPhamSua.getLoai());
        }

    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn loại");
        stringList.add("Loại 1");
        stringList.add("Loại 2");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loai = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == false){
                    themSanPham();
                }else {
                    suaSanPham();
                }

            }
        });
        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dùng thư viện lấy hình ảnh
                ImagePicker.with(ThemSpActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void suaSanPham() {
        String str_tensp = binding.textInputTenSp.getText().toString().trim();
        String str_giasp = binding.textInputGia.getText().toString().trim();
        String str_mota = binding.textInputMota.getText().toString().trim();
        String str_hinhanh = binding.textInputHinhAnh.getText().toString().trim();
        if(TextUtils.isEmpty(str_tensp)||TextUtils.isEmpty(str_giasp)||TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_mota)||loai==0){
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đủ thông tin",Toast.LENGTH_LONG).show();
        }else {
            compositeDisposable.add(apiBanHang.updateSp(str_tensp,str_giasp,str_hinhanh,str_mota,loai,sanPhamSua.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(messageModel -> {
                                if(messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();

                                }
                            },throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    //Chọn được hình sẽ trả về hàm này
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d("log", "onActivityResult: "+mediaPath);
    }

    private void themSanPham() {
        String str_tensp = binding.textInputTenSp.getText().toString().trim();
        String str_giasp = binding.textInputGia.getText().toString().trim();
        String str_mota = binding.textInputMota.getText().toString().trim();
        String str_hinhanh = binding.textInputHinhAnh.getText().toString().trim();
        if(TextUtils.isEmpty(str_tensp)||TextUtils.isEmpty(str_giasp)||TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_mota)||loai==0){
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đủ thông tin",Toast.LENGTH_LONG).show();
        }else {
            compositeDisposable.add(apiBanHang.insertSp(str_tensp,str_giasp,str_hinhanh,str_mota,(loai))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(messageModel -> {
                        if(messageModel.isSuccess()){
                            Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    },throwable -> {
                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    ));
        }
    }
    //Hàm lấy đường dẫn thật
    private String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if(cursor == null){
            result = uri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }


    // Uploading Image/Video
    private void uploadMultipleFiles() {
        Uri uri = Uri.parse(mediaPath);

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(getPath(uri));
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        Call<MessageModel> call = apiBanHang.uploadFile(fileToUpload1);
        call.enqueue(new Callback< MessageModel >() {
            @Override
            public void onResponse(Call < MessageModel > call, Response< MessageModel > response) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
//                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.textInputHinhAnh.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
            }
            @Override
            public void onFailure(Call < MessageModel > call, Throwable t) {
                Log.d("log",t.getMessage());
            }
        });
    }



    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}