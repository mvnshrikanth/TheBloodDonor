package com.android.mvnshrikanth.theblooddonor.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.ChatMessage;
import com.android.mvnshrikanth.theblooddonor.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mvnsh on 12/22/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> chatMessageList;
    private String mUid;

    public ChatMessageAdapter(String mUid) {
        this.mUid = mUid;
    }

    public void prepareChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:
                ((MyViewHolderLeft) holder).textViewMessageLeft.setText(chatMessageList.get(position).getMessageText());
                ((MyViewHolderLeft) holder).textViewDateTimeLeft.setText(Utils.getTimeForDisplay(chatMessageList.get(position).getMessageDate()));
                ((MyViewHolderLeft) holder).textViewNameLeft.setText(chatMessageList.get(position).getChatUserName());
                break;
            case 1:
                ((MyViewHolderRight) holder).textViewMessageRight.setText(chatMessageList.get(position).getMessageText());
                ((MyViewHolderRight) holder).textViewDateTimeRight.setText(Utils.getTimeForDisplay(chatMessageList.get(position).getMessageDate()));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (mUid.equals(chatMessageList.get(position).getChatUserId())) {
            viewType = 1;
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case 0:
                View viewLeft = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
                viewHolder = new MyViewHolderLeft(viewLeft);
                break;
            case 1:
                View viewRight = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
                viewHolder = new MyViewHolderRight(viewRight);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (null == chatMessageList) return 0;
        return chatMessageList.size();
    }

    public class MyViewHolderLeft extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_message_name_left)
        TextView textViewNameLeft;
        @BindView(R.id.textView_message_left)
        TextView textViewMessageLeft;
        @BindView(R.id.textView_date_time_left)
        TextView textViewDateTimeLeft;
        @BindView(R.id.image_message_profile)
        ImageView imageViewMessageProfile;

        public MyViewHolderLeft(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MyViewHolderRight extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_message_right)
        TextView textViewMessageRight;
        @BindView(R.id.textView_date_time_right)
        TextView textViewDateTimeRight;

        public MyViewHolderRight(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
