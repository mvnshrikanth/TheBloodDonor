package com.android.mvnshrikanth.theblooddonor.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mvnshrikanth.theblooddonor.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mvnsh on 10/31/2017.
 */

public class MyDonationRequestsAdapter extends RecyclerView.Adapter<MyDonationRequestsAdapter.MyViewHolder> {
    private static final String LOG_TAG = MyDonationRequestsAdapter.class.getSimpleName();

    //TODO create a my donation list and supply it to the adapter.

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_donation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_donor_name)
        TextView textViewDonorName;
        @BindView(R.id.textView_donated_location)
        TextView textViewDonatedLocation;
        @BindView(R.id.textView_donated_blood_group)
        TextView textViewDonatedBloodGroup;
        @BindView(R.id.textView__donated_date)
        TextView textViewDonatedDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
