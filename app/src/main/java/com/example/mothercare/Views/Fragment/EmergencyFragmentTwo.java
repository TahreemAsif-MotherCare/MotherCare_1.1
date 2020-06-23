package com.example.mothercare.Views.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.EmergencyContactsAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.EmergencyContact;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Views.Activities.EmergencyActivity;

import java.util.ArrayList;

public class EmergencyFragmentTwo extends Fragment implements FirebaseUtil.FirebaseResponse {
    private RecyclerView recyclerView;
    private EmergencyContactsAdapter adapter;
    private ArrayList<EmergencyContact> emergencyContacts = new ArrayList<>();
    private FirebaseUtil firebaseUtil;

    public static EmergencyFragmentTwo newInstance() {
        return new EmergencyFragmentTwo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_two, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.emergencyContactsRV);
//        prepareEmergencyContacts();
        firebaseUtil = new FirebaseUtil(getActivity());
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getAllEmergencyServices();
        ((EmergencyActivity)getActivity()).showHideProgress(true , "Please Wait");
    }

    private void prepareEmergencyContacts() {
        EmergencyContact emergencyContact = new EmergencyContact("Edhi Service", "115");
        emergencyContacts.add(emergencyContact);
        emergencyContact = new EmergencyContact("Rescue 1122", "1122");
        emergencyContacts.add(emergencyContact);
        emergencyContact = new EmergencyContact("Chippa Service", "021111111134");
        emergencyContacts.add(emergencyContact);
        emergencyContact = new EmergencyContact("Al-Rehman Ambulance Service", "03015214267");
        emergencyContacts.add(emergencyContact);
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case getAllEmergencyContacts: {
                emergencyContacts = (ArrayList<EmergencyContact>) o;
                ((EmergencyActivity)getActivity()).showHideProgress(false , "Please Wait");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new EmergencyContactsAdapter(emergencyContacts, getActivity());
                recyclerView.setAdapter(adapter);
                break;
            }
            case Error: {
                break;
            }
        }
    }
}
