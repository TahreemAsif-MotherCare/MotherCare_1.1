package com.example.mothercare.Views.Activities;

import android.os.Bundle;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.R;
import com.example.mothercare.Views.Fragment.EmergencyFragmentOne;

public class EmergencyActivity extends BaseActivity {
    EmergencyFragmentOne emergencyFragmentOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emergencyFragmentOne = new EmergencyFragmentOne(this);
        replaceFragment(R.id.container, emergencyFragmentOne);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_emergency;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
