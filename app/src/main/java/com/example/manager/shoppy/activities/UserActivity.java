package com.example.manager.shoppy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.manager.shoppy.R;
import com.example.manager.shoppy.adapter.UserChatAdapter;
import com.example.manager.shoppy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    Toolbar toolbarUser;
    RecyclerView recyclerView;
    UserChatAdapter userChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        initToolbar();
        getUserFromFire();
    }

    private void getUserFromFire() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<User> userList = new ArrayList<>();
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                User user = new User();
                                user.setId(documentSnapshot.getLong("id").intValue());
                                user.setUsername(documentSnapshot.getString("username"));
                                userList.add(user);
                            }
                            if(userList.size()>0){
                                userChatAdapter = new UserChatAdapter(getApplicationContext(),userList);
                                recyclerView.setAdapter(userChatAdapter);
                            }
                        }
                    }
                });
    }

    private void initToolbar() {
        setSupportActionBar(toolbarUser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        toolbarUser = findViewById(R.id.toolbarUser);
        recyclerView = findViewById(R.id.recycleUserChat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}