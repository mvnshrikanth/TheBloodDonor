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
 * Created by mvnsh on 12/18/2017.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private List<ChatMessage> chatMessageList;
    private ChatListAdapterEventListener mClickHandler;
    private String mUid;
    private String mUserName;
    private String donationRequestKey;

    public ChatListAdapter(ChatListAdapterEventListener mClickHandler, String donationRequestKey, String mUid, String mUserName) {
        this.mClickHandler = mClickHandler;
        this.donationRequestKey = donationRequestKey;
        this.mUid = mUid;
        this.mUserName = mUserName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_chats_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textViewChatUserName.setText(chatMessageList.get(position).getChatUserName());
        holder.textViewLastChatMessage.setText(chatMessageList.get(position).getMessageText());
        holder.textViewLastTime.setText(chatMessageList.get(position).getMessageDate()); // TODO Convert the date to today yesterday and time in hours if needed in Utils class.
    }

    public void prepareChatListAdapter(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == chatMessageList) return 0;
        return chatMessageList.size();
    }

    public interface ChatListAdapterEventListener {
        void onClick(String donationRequestKey, String mUid, String mUserName, String chatIdKey);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textView_chat_user_name)
        TextView textViewChatUserName;
        @BindView(R.id.textView_last_chat_message)
        TextView textViewLastChatMessage;
        @BindView(R.id.textView_time)
        TextView textViewLastTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ChatMessage chatMessage = chatMessageList.get(getAdapterPosition());
            mClickHandler.onClick(donationRequestKey, mUid, mUserName, chatMessage.getChatId());
        }
    }
}
