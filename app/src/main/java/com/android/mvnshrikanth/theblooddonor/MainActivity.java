package com.android.mvnshrikanth.theblooddonor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.spinner_sex)
    Spinner spinner_sex;
    @BindView(R.id.spinner_blood_type)
    Spinner spinner_blood_type;
    @BindView(R.id.cardView_view_only)
    CardView cardView_view_only;
    @BindView(R.id.cardView_editable)
    CardView cardView_editable;
    @BindView(R.id.linearLayout_scrollView_container)
    LinearLayout linearLayout_scrollView_container;
    @BindView(R.id.button_save)
    Button button_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> adapterSex =
                ArrayAdapter.createFromResource(this, R.array.sex_array, R.layout.support_simple_spinner_dropdown_item);
        adapterSex.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_sex.setAdapter(adapterSex);

        ArrayAdapter<CharSequence> adapterBloodType =
                ArrayAdapter.createFromResource(this, R.array.blood_type_array, R.layout.support_simple_spinner_dropdown_item);
        adapterBloodType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_blood_type.setAdapter(adapterBloodType);

        cardView_view_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(linearLayout_scrollView_container);
                cardView_editable.setVisibility(View.VISIBLE);
                cardView_view_only.setVisibility(View.GONE);
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(linearLayout_scrollView_container);
                cardView_editable.setVisibility(View.GONE);
                cardView_view_only.setVisibility(View.VISIBLE);
            }
        });
    }
}
