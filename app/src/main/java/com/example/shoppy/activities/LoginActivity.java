package com.example.shoppy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppy.R;
import com.example.shoppy.ultils.EmailSingleton;
import com.example.shoppy.ultils.Ultils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    TextView createNewAccount,forgotPassword;
    EditText inputEmail, inputPassword;
    Button btnLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createNewAccount = findViewById(R.id.createNewAccount);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //THÊM
        Paper.init(this);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.forgotPassword);//

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //thêm
        if(Paper.book().read("email")!=null&&Paper.book().read("password")!=null){
            inputEmail.setText(Paper.book().read("email"));
            inputPassword.setText(Paper.book().read("password"));
        }

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quenMatKhau = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(quenMatKhau);
            }
        });


        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perforLogin();
            }
        });
    }

    private void perforLogin() {
        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
        EmailSingleton.getInstance().setEmail(email);

        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter Connext Email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Proper Password");
        } else {

            Paper.book().write("email",email);
            Paper.book().write("password",password);

            progressDialog.setMessage("Please wait while Login .....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    }else {

                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }
         }

    private void sendUserToNextActivity() {



        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Sửa gửi email qua màn hình thanh toán
//        Intent sendEmail = new Intent(LoginActivity.this,ThanhToanActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(Ultils.KEY_GET_EMAIL,email);
//        sendEmail.putExtras(bundle);
//        startActivity(sendEmail);

    }
}
