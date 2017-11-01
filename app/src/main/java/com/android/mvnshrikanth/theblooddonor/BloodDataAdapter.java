package com.android.mvnshrikanth.theblooddonor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
