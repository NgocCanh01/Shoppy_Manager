package com.example.manager.shoppy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.shoppy.R;
import com.example.manager.shoppy.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessagesList;
    private String sendId;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;

    public ChatAdapter(Context context, List<ChatMessage> chatMessagesList, String sendId) {
        this.context = context;
        this.chatMessagesList = chatMessagesList;
        this.sendId = sendId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEND) {
            view = LayoutInflater.from(context).inflate(R.layout.item_send_mess,parent,false);
            return new SendMessViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.item_received_mess,parent,false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_SEND){
            ((SendMessViewHolder) holder).tvMessSend.setText(chatMessagesList.get(position).mess);
            ((SendMessViewHolder) holder).tvTimeSend.setText(chatMessagesList.get(position).datetime);
        }else {
            ((ReceivedViewHolder) holder).tvMessReceived.setText(chatMessagesList.get(position).mess);
            ((ReceivedViewHolder) holder).tvTimeReceived.setText(chatMessagesList.get(position).datetime);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessagesList.size();
    }

    //1 send, 2 receive
    //so sanh id user vois id truyen vao TRUNG=>

    @Override
    public int getItemViewType(int position) {
        if(chatMessagesList.get(position).sendid.equals(sendId)){
            return TYPE_SEND;
        }else {
            return TYPE_RECEIVE;
        }
    }

    class SendMessViewHolder extends RecyclerView.ViewHolder{
        TextView tvMessSend, tvTimeSend;
        public SendMessViewHolder(@NonNull View itemView){
            super(itemView);
            tvMessSend = itemView.findViewById(R.id.tvMessSend);
            tvTimeSend = itemView.findViewById(R.id.tvTimeSend);
        }
    }

    class ReceivedViewHolder extends RecyclerView.ViewHolder{
        TextView tvMessReceived, tvTimeReceived;
        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessReceived = itemView.findViewById(R.id.tvMessReceived);
            tvTimeReceived = itemView.findViewById(R.id.tvTimeReceived);
        }
    }
}
