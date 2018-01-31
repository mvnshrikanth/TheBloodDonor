package com.android.mvnshrikanth.theblooddonor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.android.mvnshrikanth.theblooddonor.utilities.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationRequestAdapter
        extends RecyclerView.Adapter<DonationRequestAdapter.MyViewHolder> {

    private List<DonationRequest> donationRequestList;
    private DonationRequestAdapterOnClickListener mClickHandler;
    private String mUid;
    private String mUserName;
    private int lastPosition = -1;
    private Context context;

    public DonationRequestAdapter(DonationRequestAdapterOnClickListener mClickHandler, String mUid, String mUserName, Context context) {
        this.mClickHandler = mClickHandler;
        this.mUid = mUid;
        this.mUserName = mUserName;
        this.context = context;
    }

    @Override
    public DonationRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation_request, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonationRequestAdapter.MyViewHolder holder, int position) {
        holder.textViewDonationLocation.setText((donationRequestList.get(position).getRequesterCity() + ", " + donationRequestList.get(position).getRequesterState()));
        holder.textViewRequestDate.setText(Utils.getDateAndTimeForDisplay(donationRequestList.get(position).getRequestedDate()));
        holder.textViewRequestedBloodGroup.setText(donationRequestList.get(position).getRequestedBloodType());
        holder.textViewRequesterName.setText(donationRequestList.get(position).getRequesterName());

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void prepareDonationRequest(List<DonationRequest> donationRequestList) {
        this.donationRequestList = donationRequestList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == donationRequestList) return 0;
        return donationRequestList.size();
    }

    public interface DonationRequestAdapterOnClickListener {
        void onClick(DonationRequest donationRequest, String donationRequesterUid, String mUid, String mUserName);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textView_requested_by)
        TextView textViewRequesterName;
        @BindView(R.id.textView_location)
        TextView textViewDonationLocation;
        @BindView(R.id.textView_requested_blood_group)
        TextView textViewRequestedBloodGroup;
        @BindView(R.id.textView_requested_date)
        TextView textViewRequestDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DonationRequest donationRequest = donationRequestList.get(getAdapterPosition());
            mClickHandler.onClick(donationRequest, donationRequest.getRequesterUidKey(), mUid, mUserName);
        }
    }
}
