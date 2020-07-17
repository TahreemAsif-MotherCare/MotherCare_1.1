package com.example.mothercare.Views.Activities.webCrawler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mothercare.R;

public class LabourAndBirthFragment extends Fragment {
    public static LabourAndBirthFragment newInstance() {
        LabourAndBirthFragment fragment = new LabourAndBirthFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_labour_and_birth, container, false);
        return view;
    }
}
