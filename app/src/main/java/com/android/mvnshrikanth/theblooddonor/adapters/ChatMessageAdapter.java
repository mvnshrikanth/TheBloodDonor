package com.android.mvnshrikanth.theblooddonor.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.ChatMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mvnsh on 12/22/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> {

    List<ChatMessage> chatMessageList;

    public ChatMessageAdapter() {
    }

    public void prepareChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textViewMessage.setText(chatMessageList.get(position).getMessageText());
//        holder.textViewName.setText(chatMessageList.get(position).getChatUserName());
    }

    @Override
    public int getItemCount() {
        if (null == chatMessageList) return 0;
        return chatMessageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.messageTextView)
        TextView textViewMessage;
//        @BindView(R.id.nameTextView)
//        TextView textViewName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
