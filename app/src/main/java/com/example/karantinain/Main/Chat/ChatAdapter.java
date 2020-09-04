package com.example.karantinain.Main.Chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
    private ArrayList<MessageData> listChat;
    private Context context;
    private String idProfile, usernameProfile;

    public ChatAdapter(ArrayList<MessageData> list, String idProfile, String usernameProfile){
        this.listChat = list;
        this.idProfile = idProfile;
        this.usernameProfile = usernameProfile;
    }


    @Override
    public int getItemViewType(int position) {
        if(listChat.get(position).getFromUserId().equals(idProfile)){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_send, parent, false);
            return new ViewHolderSend(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_receive, parent, false);
            return new ViewHolderReceive(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageData chat = listChat.get(position);

        if(chat.getFromUserId().equals(idProfile)){
            String initialName = String.valueOf(usernameProfile.trim().charAt(0)).toUpperCase();
            ViewHolderSend holderUser = (ViewHolderSend) holder;
            holderUser.tvMessage.setText(chat.getMessage());
            holderUser.tvInitialName.setText(initialName);

        }else{
            ViewHolderReceive holderBot = (ViewHolderReceive) holder;
            holderBot.tvMessage.setText(chat.getMessage());

        }
    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }

    class ViewHolderSend extends RecyclerView.ViewHolder{
        TextView tvMessage, tvInitialName;

        public ViewHolderSend(@NonNull View itemView) {
            super(itemView);
            tvInitialName = itemView.findViewById(R.id.tvInitialName);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

    class ViewHolderReceive extends RecyclerView.ViewHolder{
        TextView tvMessage, tvInitialName;

        public ViewHolderReceive(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
