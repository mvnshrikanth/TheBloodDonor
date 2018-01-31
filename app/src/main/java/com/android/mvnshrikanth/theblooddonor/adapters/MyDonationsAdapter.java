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

public class MyDonationsAdapter extends RecyclerView.Adapter<MyDonationsAdapter.MyViewHolder> {

    private List<DonationRequest> myDonationRequestList;
    private int lastPosition = -1;
    private Context context;

    public MyDonationsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_donation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textViewDonatedBloodGroup.setText(myDonationRequestList.get(position).getRequestedBloodType());
        holder.textViewDonatedDate.setText(Utils.getDateAndTimeForDisplay(myDonationRequestList.get(position).getDonatedDate()));
        String location = myDonationRequestList.get(position).getRequesterCity() + "," + myDonationRequestList.get(position).getRequesterState();
        holder.textViewDonatedLocation.setText(location);
        holder.textViewRequesterName.setText(myDonationRequestList.get(position).getRequesterName());

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void prepareMyDonationList(List<DonationRequest> myDonationRequestList) {
        this.myDonationRequestList = myDonationRequestList;
    }

    @Override
    public int getItemCount() {
        if (null == myDonationRequestList) return 0;
        return myDonationRequestList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_donor_name)
        TextView textViewRequesterName;
        @BindView(R.id.textView_donated_location)
        TextView textViewDonatedLocation;
        @BindView(R.id.textView_requested_blood_group)
        TextView textViewDonatedBloodGroup;
        @BindView(R.id.textView_donated_date)
        TextView textViewDonatedDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
