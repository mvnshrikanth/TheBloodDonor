package com.android.mvnshrikanth.theblooddonor.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mvnsh on 12/12/2017.
 */

public class DonationRequestAdapter
        extends RecyclerView.Adapter<DonationRequestAdapter.MyViewHolder> {

    private List<DonationRequest> donationRequestList;
    private DonationRequestAdapterOnClickListener mClickHandler;

    public DonationRequestAdapter() {
    }

    @Override
    public DonationRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation_request, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonationRequestAdapter.MyViewHolder holder, int position) {
        holder.textViewDonationLocation.setText((donationRequestList.get(position).getRequesterCity() + ", " + donationRequestList.get(position).getRequesterZip()));
        holder.textViewRequestDate.setText(donationRequestList.get(position).getRequestedDate());
        holder.textViewRequestedBloodGroup.setText(donationRequestList.get(position).getRequestedBloodType());
        holder.textViewRequesterName.setText(donationRequestList.get(position).getRequesterName());
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
        void onClick(DonationRequest donationRequest);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textView_requester_name)
        TextView textViewRequesterName;
        @BindView(R.id.textView_donation_location)
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
            mClickHandler.onClick(donationRequest);
        }
    }
}
