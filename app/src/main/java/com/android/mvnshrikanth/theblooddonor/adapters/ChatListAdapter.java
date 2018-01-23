package com.android.mvnshrikanth.theblooddonor.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.ChatMessage;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.android.mvnshrikanth.theblooddonor.utilities.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.USER_PROFILE_PICTURES_STORAGE_PATH;

/**
 * Created by mvnsh on 12/18/2017.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private List<ChatMessage> chatMessageList;
    private ChatListAdapterEventListener mClickHandler;
    private String mUid;
    private String mUserName;
    private DonationRequest donationRequest;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Context context;

    public ChatListAdapter(ChatListAdapterEventListener mClickHandler, DonationRequest donationRequest, String mUid, String mUserName, Context context) {
        this.mClickHandler = mClickHandler;
        this.donationRequest = donationRequest;
        this.mUid = mUid;
        this.mUserName = mUserName;
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child(USER_PROFILE_PICTURES_STORAGE_PATH);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_chats_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.textViewChatUserName.setText(chatMessageList.get(position).getDonorName());
        holder.textViewLastChatMessage.setText(chatMessageList.get(position).getMessageText());
        holder.textViewLastTime.setText(Utils.getDateAndTimeForDisplay(chatMessageList.get(position).getMessageDate()));
        storageReference.child(chatMessageList.get(position).getDonorId()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .into(holder.imageViewMessageProfile);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Glide.with(context)
                                .load(context.getResources().getDrawable(R.drawable.ic_account_circle_black_24dp))
                                .into(holder.imageViewMessageProfile);
                    }
                });
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
        void onClick(DonationRequest donationRequest, String mUid, String mUserName, String chatIdKey);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textView_chat_user_name)
        TextView textViewChatUserName;
        @BindView(R.id.textView_last_chat_message)
        TextView textViewLastChatMessage;
        @BindView(R.id.textView_time)
        TextView textViewLastTime;
        @BindView(R.id.imageView_last_user_message)
        ImageView imageViewMessageProfile;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ChatMessage chatMessage = chatMessageList.get(getAdapterPosition());
            mClickHandler.onClick(donationRequest, mUid, mUserName, chatMessage.getChatId());
        }
    }
}
