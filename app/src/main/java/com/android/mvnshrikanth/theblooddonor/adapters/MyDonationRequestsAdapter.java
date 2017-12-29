package com.android.mvnshrikanth.theblooddonor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.android.mvnshrikanth.theblooddonor.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mvnsh on 10/31/2017.
 */

public class MyDonationRequestsAdapter extends RecyclerView.Adapter<MyDonationRequestsAdapter.MyViewHolder> {
    private static final String LOG_TAG = MyDonationRequestsAdapter.class.getSimpleName();
    private List<DonationRequest> myDonationRequestList;
    private MyDonationRequestAdapterOnClickListener mClickHandler;
    private String mUid;
    private String mUserName;
    private Context context;

    public MyDonationRequestsAdapter(MyDonationRequestAdapterOnClickListener mClickHandler, String mUid, String mUserName, Context context) {
        this.mClickHandler = mClickHandler;
        this.mUid = mUid;
        this.mUserName = mUserName;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_donation_requests, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textViewDonatedBloodGroup.setText(myDonationRequestList.get(position).getRequestedBloodType());
        if (myDonationRequestList.get(position).getDonorName() == null) {
            holder.textViewDonorName.setText("N/A");
        } else {
            holder.textViewDonorName.setText(myDonationRequestList.get(position).getDonorName());
        }
        if (myDonationRequestList.get(position).getDonatedDate() == null) {
            Glide.with(context)
                    .load(context.getDrawable(R.drawable.inprogress))
                    .into(holder.imageViewStatus);
        } else {
            Glide.with(context)
                    .load(context.getDrawable(R.drawable.complete))
                    .into(holder.imageViewStatus);
        }

        holder.textViewRequestedDate.setText(Utils.getDateForDisplay(myDonationRequestList.get(position).getRequestedDate()));
        holder.textViewResponse.setText("2");
    }

    public void prepareMyDonationRequestList(List<DonationRequest> myDonationRequestList) {
        this.myDonationRequestList = myDonationRequestList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == myDonationRequestList) return 0;
        return myDonationRequestList.size();
    }

    public interface MyDonationRequestAdapterOnClickListener {
        void onClick(String donationRequestKey, String mUid, String mUserName);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageView_status)
        ImageView imageViewStatus;
        @BindView(R.id.textView_donated_by)
        TextView textViewDonorName;
        @BindView(R.id.textView_requested_date)
        TextView textViewRequestedDate;
        @BindView(R.id.textView_donated_blood_group)
        TextView textViewDonatedBloodGroup;
        @BindView(R.id.textView_responses)
        TextView textViewResponse;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DonationRequest donationRequest = myDonationRequestList.get(getAdapterPosition());
            mClickHandler.onClick(donationRequest.getDonationRequestKey(), mUid, mUserName);
        }
    }
}
