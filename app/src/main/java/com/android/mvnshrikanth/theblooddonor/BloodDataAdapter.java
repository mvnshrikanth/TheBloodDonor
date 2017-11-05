package com.android.mvnshrikanth.theblooddonor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mvnsh on 10/31/2017.
 */

public class BloodDataAdapter extends RecyclerView.Adapter<BloodDataAdapter.MyViewHolder> {
    private static final String LOG_TAG = BloodDataAdapter.class.getSimpleName();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_donor_donee_name)
        TextView textView_donor_donee_name;
        @BindView(R.id.textView_location)
        TextView textView_location;
        @BindView(R.id.textView_blood_group)
        TextView textView_blood_group;
        @BindView(R.id.textView_date)
        TextView textView_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
