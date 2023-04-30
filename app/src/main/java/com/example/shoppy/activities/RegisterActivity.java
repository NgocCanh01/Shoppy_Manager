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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {

        TextView gaveAccount;
        EditText inputEmail, inputPassword, inputConfirmPassword, inputSdt;
        Button btnRegister;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        ProgressDialog progressDialog;
        FirebaseAuth mAuth;
        FirebaseUser mUser;
        String email,password,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //THÊM
        Paper.init(this);

        gaveAccount = findViewById(R.id.gaveAccount);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        inputSdt = findViewById(R.id.inputSodienthoai);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        gaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        //thêm sửa sdt
//        if(Paper.book().read("sdt")!=null){
//            inputSdt.setText(Paper.book().read("sdt"));
//        }



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PerforAuth();

            }
        });
    }

    private void PerforAuth() {
        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
        mobile = inputSdt.getText().toString();
        EmailSingleton.getInstance().setEmail(email);
        EmailSingleton.getInstance().setMobile(mobile);
        String confirmPassword = inputConfirmPassword.getText().toString();
        Paper.book().write("sdt",mobile);
//        Paper.book().write("email",email);

        if(!email.matches(emailPattern)){
            inputEmail.setError("Enter Connext Email");
        } else  if(password.isEmpty() || password.length()<6)
        {
            inputPassword.setError("Enter Proper Password");
        } else if( !password.equals(confirmPassword))
        {
            inputConfirmPassword.setError("Password not match both fields");
        }else{
            progressDialog.setMessage("Please wait while Registration .....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

        private void sendUserToNextActivity() {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
}