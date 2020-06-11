package com.example.mothercare.Views.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.DashboardAdapter;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Dashboard;
import com.example.mothercare.Models.Doctor;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.Models.Pharmacist;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Views.Activities.DashboardActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardFragment extends Fragment implements FirebaseUtil.FirebaseResponse {
    public Patient patient;
    public Doctor doctor;
    CardView findDoctor;
    TextView username, tips, firstCardNumber, firstCardHeading, firstCardSubHeading, secondCardNumber, secondCardHeading, secondCardSubHeading,
            dateTime, greetings, welcome;
    CircleImageView profilePic;
    private RecyclerView dashboardRecyclerView;
    private ConstraintLayout cardviewConstarintLayout;
    private DashboardAdapter adapter;
    private ArrayList<Dashboard> dashboardItemList = new ArrayList<>();
    private ArrayList<Doctor> doctorsArrayList = new ArrayList<>();
    private ArrayList<String> doctors = new ArrayList<>();
    private FirebaseUtil firebaseUtil;
    private boolean isPatient = false;
    private View view, headerView;

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard2, container, false);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        firebaseUtil = new FirebaseUtil(getActivity());
        init(view);
        dashboardItemList.clear();
        firebaseUtil.setFirebaseResponse(this);
        ((DashboardActivity) getActivity()).showHideProgress(true, "");
        firebaseUtil.checkUserStatus();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void init(View view) {
        findDoctor = view.findViewById(R.id.findDoctorConstraintLayout);
        cardviewConstarintLayout = view.findViewById(R.id.cardviewConstarintLayout);
        dashboardRecyclerView = view.findViewById(R.id.dashboardRecyclerView);
        username = view.findViewById(R.id.userNameDashboard);
        greetings = view.findViewById(R.id.goodevening);
        welcome = view.findViewById(R.id.welcome);
        profilePic = view.findViewById(R.id.profilePictureDashboard);
        tips = view.findViewById(R.id.tips);
        firstCardHeading = view.findViewById(R.id.firstCardHeading);
        firstCardNumber = view.findViewById(R.id.firstCardNumber);
        firstCardSubHeading = view.findViewById(R.id.firstCardSubHeading);
        secondCardHeading = view.findViewById(R.id.secondCardHeading);
        secondCardNumber = view.findViewById(R.id.secondCardNumber);
        secondCardSubHeading = view.findViewById(R.id.secondCardSubHeading);
        dateTime = view.findViewById(R.id.dateTime);
        dateTime.setText(getCurrentDate());

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) findDoctor.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int DeviceTotalHeight = (metrics.heightPixels * 30) / 100;
        layoutParams.height = DeviceTotalHeight;
        findDoctor.setLayoutParams(layoutParams);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new DashboardAdapter(dashboardItemList, getActivity());
        dashboardRecyclerView.setLayoutManager(mLayoutManager);
        dashboardRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dashboardRecyclerView.setAdapter(adapter);
    }

    public String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a dd MMMM yyyy");
        dateFormat.setTimeZone(TimeZone
                .getTimeZone("GMT+05:00"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    private void prepareDashboardItemData(String userStatus) {
        if (userStatus.equals("Patient")) {
            Dashboard dashboardItem = new Dashboard("Find Doctors", R.drawable.find_doctor);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Your Doctor", R.drawable.ic_added_doctors);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("View Appointments", R.drawable.ic_appointments);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Medicine Reminders", R.drawable.ic_medication_schdule);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("View Reports", R.drawable.ic_report);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Analyze Symptoms", R.drawable.ic_report);
            dashboardItemList.add(dashboardItem);
            dashboardItem = new Dashboard("Order History", R.drawable.ic_report);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Chat Forum", R.drawable.ic_chat);
            dashboardItemList.add(dashboardItem);
            dashboardItem = new Dashboard("Awareness Information", R.drawable.ic_info);
            dashboardItemList.add(dashboardItem);
            adapter.notifyDataSetChanged();
        } else if (userStatus.equals("Doctor")) {
            Dashboard dashboardItem = new Dashboard("Registered Patients", R.drawable.ic_patient);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Requests", R.drawable.ic_added_doctors);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Scheduled Appointments", R.drawable.ic_appointments);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Appointment Requests", R.drawable.ic_appointments);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Patient Reports", R.drawable.ic_report);
            dashboardItemList.add(dashboardItem);
            adapter.notifyDataSetChanged();
        } else if (userStatus.equals("Pharmacist")) {

            Dashboard dashboardItem = new Dashboard("Medicines", R.drawable.ic_medicine);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Order Requests", R.drawable.ic_medicine);
            dashboardItemList.add(dashboardItem);

            dashboardItem = new Dashboard("Order History", R.drawable.ic_appointments);
            dashboardItemList.add(dashboardItem);

            adapter.notifyDataSetChanged();
        }

    }


    public void getDoctors() {
        ((DashboardActivity) getActivity()).showHideProgress(true, "Please wait while we get the data");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Doctors");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        doctor = postSnapshot.getValue(Doctor.class);
                        doctorsArrayList.add(doctor);
                    }
//                    prepareEditText();
                    ((DashboardActivity) getActivity()).showHideProgress(false, "");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            ((DashboardActivity) getActivity()).showHideProgress(false, "");
            ((DashboardActivity) getActivity()).showAlertDialog("Error", e.getLocalizedMessage());
        }
    }

    private void changeNavigationDrawerHeaderView(String username, String email) {
        TextView navUsername = headerView.findViewById(R.id.nameOnHeader);
        navUsername.setText(username);
        TextView navUserEmail = headerView.findViewById(R.id.emailOnHeader);
        navUserEmail.setText(email);
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case isPatient: {
                patient = ((DataSnapshot) o).getValue(Patient.class);
                username.setText(patient.username);
                changeNavigationDrawerHeaderView(patient.username, patient.email);
                prepareDashboardItemData("Patient");
                firebaseUtil.getMedicineRemindersCount(patient.patientID);
                firebaseUtil.getProfilePicture("Patient", patient.patientID);
                break;
            }
            case isDoctor: {
//                cardviewConstarintLayout.setBackground(getResources().getDrawable(R.drawable.doctor_gradient));
                cardviewConstarintLayout.setBackgroundColor(getResources().getColor(R.color.pink));
                username.setTextColor(Color.BLACK);
                dateTime.setTextColor(Color.BLACK);
                welcome.setTextColor(Color.BLACK);
                greetings.setTextColor(Color.BLACK);
                doctor = ((DataSnapshot) o).getValue(Doctor.class);
                username.setText(doctor.username);
                prepareDashboardItemData("Doctor");
                changeNavigationDrawerHeaderView(doctor.username, doctor.email);
                tips.setVisibility(View.GONE);
                firebaseUtil.getRequestsCount();
                firebaseUtil.getProfilePicture("Doctor", doctor.doctorID);
                break;
            }
            case isPharmacist: {
                cardviewConstarintLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tips.setVisibility(View.GONE);
                prepareDashboardItemData("Pharmacist");
                Pharmacist pharmacist = ((DataSnapshot) o).getValue(Pharmacist.class);
                changeNavigationDrawerHeaderView(pharmacist.username, pharmacist.email);
                firebaseUtil.getProfilePicture("Pharmacist", pharmacist.pharmacistID);
                break;
            }
            case MedicineRemindersCount: {
                firstCardNumber.setText(String.valueOf(o));
                firebaseUtil.getAppointmentsCount();
                break;
            }
            case AppointmentsCount: {
                secondCardNumber.setText(String.valueOf(o));
                break;
            }
            case RequestsCount: {
                firstCardNumber.setText(String.valueOf(o));
                firstCardHeading.setText("Request");
                firstCardSubHeading.setText("New Requests");
                firebaseUtil.getAppointmentsCount();
                break;
            }
            case ProfilePicture: {
                profilePic.setImageBitmap((Bitmap) o);
                ImageView imageView = headerView.findViewById(R.id.imageView);
                imageView.setImageBitmap((Bitmap) o);
                ((DashboardActivity) Objects.requireNonNull(getActivity())).showHideProgress(false, "");
                break;
            }
            case Error:
                break;
        }
    }
}
