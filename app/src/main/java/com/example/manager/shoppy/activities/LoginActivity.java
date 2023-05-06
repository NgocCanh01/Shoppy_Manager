package com.example.manager.shoppy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manager.shoppy.retrofit.ApiBanHang;
import com.example.manager.shoppy.retrofit.RetrofitClient;
import com.example.manager.shoppy.R;
import com.example.manager.shoppy.ultils.Ultils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    TextView createNewAccount, forgotPassword;
    EditText inputEmail, inputPassword, inputSdt;
    Button btnLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    String email, password, mobile;
    ImageView btnFacebook, btnGoogle;
    CompositeDisposable compositeDisposable;
    ApiBanHang apiBanHang;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quenMatKhau = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(quenMatKhau);
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, DangKiActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perforLogin();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginGoogle = new Intent(LoginActivity.this, GoogleSignInActivity.class);
                startActivity(loginGoogle);
            }
        });
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginFacebook = new Intent(LoginActivity.this, FacebookAuthActivity.class);
                startActivity(loginFacebook);
            }
        });
    }

    private void initView() {
        createNewAccount = findViewById(R.id.createNewAccount);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //THÊM
        Paper.init(this);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.forgotPassword);//

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        btnFacebook = findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);

        apiBanHang = RetrofitClient.getInstance(Ultils.BASE_URL).create(ApiBanHang.class);
        compositeDisposable = new CompositeDisposable();
//        thêm read data
        if(Paper.book().read("email")!=null&&Paper.book().read("password")!=null){
            inputEmail.setText(Paper.book().read("email"));
            inputPassword.setText(Paper.book().read("password"));
            if(Paper.book().read("isLogin")!=null){
                boolean flag = Paper.book().read("isLogin");
                if(flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    },1000);
                }
            }
        }
    }

    private void perforLogin() {

//        mobile= Paper.book().read("sdt");
//        mobile=EmailSingleton.getInstance().getMobile();

        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
//        EmailSingleton.getInstance().setEmail(email);
//        mobile = inputSdt.getText().toString();
//        Paper.book().read("sdt",mobile);
//        EmailSingleton.getInstance().setMobile(mobile);

//        if (!email.matches(emailPattern)) {
//            inputEmail.setError("Enter Connext Email");
//        } else if (password.isEmpty() || password.length() < 6) {
//            inputPassword.setError("Enter Proper Password");
//        } else {
//

//
//            progressDialog.setMessage("Please wait while Login .....");
//            progressDialog.setTitle("Registration");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
//
//            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        progressDialog.dismiss();
//                        sendUserToNextActivity();
//                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//
//                    }else {
//
//                        progressDialog.dismiss();
//                        Toast.makeText(LoginActivity.this,"" + task.getException(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//
//
//        }
        String str_email = inputEmail.getText().toString().trim();
        String str_pass = inputPassword.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Pass", Toast.LENGTH_LONG).show();
        } else {
            Paper.book().write("email",str_email);
            Paper.book().write("password",str_pass);

            if(user!=null){
                //user da co dang nhap Firebase
                dangNhap(str_email,str_pass);
            }else{
                //user da signout
                firebaseAuth.signInWithEmailAndPassword(str_email,str_pass)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    dangNhap(str_email,str_pass);
                                }
                            }
                        });
            }


        }
    }

    private void dangNhap(String str_email,String str_pass){
        compositeDisposable.add(apiBanHang.dangNhap(str_email,str_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userModel -> {
                            if(userModel.isSuccess()){
                                Ultils.user_current = userModel.getResult().get(0);//do cái result là 1 cái list => lấy ptu first
                                //Lưu lại thông tin người dùng
                                Paper.book().write("user",userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

//    private void sendUserToNextActivity() {
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        //Sửa gửi email qua màn hình thanh toán
////        Intent sendEmail = new Intent(LoginActivity.this,ThanhToanActivity.class);
////        Bundle bundle = new Bundle();
////        bundle.putString(Ultils.KEY_GET_EMAIL,email);
////        sendEmail.putExtras(bundle);
////        startActivity(sendEmail);
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Ultils.user_current.getEmail() != null && Ultils.user_current.getPass() != null) {
            inputEmail.setText(Ultils.user_current.getEmail());
            inputPassword.setText(Ultils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
