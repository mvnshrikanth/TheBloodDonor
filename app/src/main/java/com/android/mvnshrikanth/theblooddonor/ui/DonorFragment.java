package com.android.mvnshrikanth.theblooddonor.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.adapters.BloodDataAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DonorFragment extends Fragment {
    private static final String LOG_TAG = DonorFragment.class.getSimpleName();

    @BindView(R.id.recyclerView_blood_data)
    RecyclerView recyclerView;
    private Unbinder unbinder;

    public DonorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donor, container, false);
        unbinder = ButterKnife.bind(this, view);
        BloodDataAdapter bloodDataAdapter = new BloodDataAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(bloodDataAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
