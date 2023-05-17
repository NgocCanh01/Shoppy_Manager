package com.example.manager.shoppy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.shoppy.Interface.ItemClickListener;
import com.example.manager.shoppy.R;
import com.example.manager.shoppy.activities.ChatActivity;
import com.example.manager.shoppy.model.User;

import java.util.List;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.MyViewHolder> {
    Context context;
    List<User> userList;

    public UserChatAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_chat,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvIdUserChat.setText(user.getId()+" ");
        holder.tvUserNameChat.setText(user.getUsername());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("id",user.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvIdUserChat, tvUserNameChat;
        ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdUserChat = itemView.findViewById(R.id.tvIdUserChat);
            tvUserNameChat = itemView.findViewById(R.id.tvUserNameChat);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
}
