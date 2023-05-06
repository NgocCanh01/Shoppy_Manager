package com.example.manager.shoppy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.manager.shoppy.R;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Paper.init(this);
        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch (Exception ex){

                }finally {
                    if(Paper.book().read("user")==null){
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent home = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(home);
                        finish();
                    }



                }
            }
        };
        thread.start();
    }
}