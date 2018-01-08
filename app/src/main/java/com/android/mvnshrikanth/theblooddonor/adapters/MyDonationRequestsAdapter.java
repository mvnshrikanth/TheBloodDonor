package com.android.mvnshrikanth.theblooddonor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.android.mvnshrikanth.theblooddonor.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mvnsh on 10/31/2017.
 */

public class MyDonationRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case 0:
                View viewCompleted = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_donation_completed_requests, parent, false);
                viewHolder = new MyViewHolderCompleted(viewCompleted);
                break;
            case 1:
                View viewInProgress = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_donation_requests_in_progress, parent, false);
                viewHolder = new MyViewHolderInProgress(viewInProgress);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:
                ((MyViewHolderCompleted) holder).textViewDonatedBloodGroup.setText(myDonationRequestList.get(position).getRequestedBloodType());
                ((MyViewHolderCompleted) holder).textViewDonorName.setText(myDonationRequestList.get(position).getDonorName());
                ((MyViewHolderCompleted) holder).textViewDonatedDate.setText(Utils.getDateAndTimeForDisplay(myDonationRequestList.get(position).getDonatedDate()));
                break;
            case 1:
                ((MyViewHolderInProgress) holder).textViewDonorResponseCount.setText("2");
                ((MyViewHolderInProgress) holder).textViewRequestedBloodGroup.setText(myDonationRequestList.get(position).getRequestedBloodType());
                ((MyViewHolderInProgress) holder).textViewRequestedDate.setText(Utils.getDateAndTimeForDisplay(myDonationRequestList.get(position).getRequestedDate()));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (myDonationRequestList.get(position).getDonatedDate() == null) {
            viewType = 1;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        if (null == myDonationRequestList) return 0;
        return myDonationRequestList.size();
    }

    public void prepareMyDonationRequestList(List<DonationRequest> myDonationRequestList) {
        this.myDonationRequestList = myDonationRequestList;
        notifyDataSetChanged();
    }

    public interface MyDonationRequestAdapterOnClickListener {
        void onClick(DonationRequest donationRequest, String mUid, String mUserName);
    }

    public class MyViewHolderCompleted extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_donated_by_name)
        TextView textViewDonorName;
        @BindView(R.id.textView_donated_date)
        TextView textViewDonatedDate;
        @BindView(R.id.textView_donated_blood_group)
        TextView textViewDonatedBloodGroup;

        public MyViewHolderCompleted(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MyViewHolderInProgress extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textView_responses_count)
        TextView textViewDonorResponseCount;
        @BindView(R.id.textView_requested_on_date)
        TextView textViewRequestedDate;
        @BindView(R.id.textView_requested_blood_group)
        TextView textViewRequestedBloodGroup;

        public MyViewHolderInProgress(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DonationRequest donationRequest = myDonationRequestList.get(getAdapterPosition());
            mClickHandler.onClick(donationRequest, mUid, mUserName);
        }
    }

}
