package com.example.mothercare.Views.Activities;

import android.os.Bundle;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.R;
import com.example.mothercare.Views.Fragment.EmergencyFragmentOne;
import com.example.mothercare.Views.Fragment.EmergencyFragmentTwo;

public class EmergencyActivity extends BaseActivity {
    EmergencyFragmentTwo emergencyFragmentTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emergencyFragmentTwo = new EmergencyFragmentTwo();
        replaceFragment(R.id.container, emergencyFragmentTwo);
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
