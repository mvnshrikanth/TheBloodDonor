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

public class BloodDataAdapter extends RecyclerView.Adapter<BloodDataAdapter.MyViewHolder> {
    private static final String LOG_TAG = BloodDataAdapter.class.getSimpleName();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blood_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }


    //TODO Change the item count in the adapter.
    @Override
    public int getItemCount() {
        return 1;
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
