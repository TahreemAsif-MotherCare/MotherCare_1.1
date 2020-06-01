package com.example.mothercare.Views.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Doctor;
import com.example.mothercare.Models.UserLocation;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourDoctor extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private FirebaseUtil firebaseUtil;
    private ImageView profilePic, openLocationInGMap, call, sendEmail, chatWithDoctor;
    private CoordinatorLayout yourDoctorLayout;
    private TextView doctorName, specialization, qualification, education, worksat, contactNumber, email, address;
    private RatingBar ratingBar;
    private ImageView leaveDoctor;
    private Doctor doctor;
    private Bitmap bitmap;
    private String doctorID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        doctorID = intent.getStringExtra("DoctorID");
        init();
    }

    private void init() {
        profilePic = findViewById(R.id.doctorProfilePicture);
        yourDoctorLayout = findViewById(R.id.yourDoctorLayout);
        doctorName = findViewById(R.id.addedDoctorName);
        ratingBar = findViewById(R.id.ratingbar);
        specialization = findViewById(R.id.specializationDP);
        qualification = findViewById(R.id.qualificationDP);
        education = findViewById(R.id.educationDP);
        worksat = findViewById(R.id.worksAtTextViewDP);
        contactNumber = findViewById(R.id.contactNumber);
        email = findViewById(R.id.email);
        chatWithDoctor = findViewById(R.id.chatWithDoctor);
        address = findViewById(R.id.doctorAddress);
        openLocationInGMap = findViewById(R.id.openInGoogleMaps);
        call = findViewById(R.id.call);
        sendEmail = findViewById(R.id.sendEmail);

        firebaseUtil = new FirebaseUtil(YourDoctor.this);
        firebaseUtil.setFirebaseResponse(this);
        leaveDoctor = findViewById(R.id.leaveDoctorButton);

        leaveDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(YourDoctor.this)
                        .setTitle("Leave Doctor")
                        .setMessage("Are you sure you want to leave the doctor?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                showRatingDialog();
                            }
                        })

                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        openLocationInGMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", doctor.getUserLocation().latitude, doctor.getUserLocation().longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + doctor.phoneNumber));
                startActivity(callIntent);
            }
        });
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{doctor.email});
                emailIntent.setType("text/plain");
                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(emailIntent);
            }
        });
        chatWithDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourDoctor.this, PatientChatActivity.class);
                intent.putExtra("DoctorID", doctor.doctorID);
                startActivity(intent);
            }
        });
        getDoctorInfo();
    }


    public void getDoctorInfo() {
        showHideProgress(true, "");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Doctors").child(doctorID);
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        doctor = dataSnapshot.getValue(Doctor.class);
                        doctorName.setText(doctor.username);
                        specialization.setText(doctor.experience);
                        education.setText(doctor.institute);
                        qualification.setText(doctor.qualification);
                        worksat.setText(doctor.worksat);
                        contactNumber.setText(doctor.phoneNumber);
                        email.setText(doctor.email);
                        address.setText(getDoctorAddress(doctor.getUserLocation()));
                        ratingBar.setRating(doctor.rating);
                        firebaseUtil.getProfilePicture("Doctor", doctor.doctorID);
                    } else {
                        showHideProgress(false, "");
                        showToast("Unexpected Error!");
                        onBackPressed();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            showHideProgress(false, "");
            showAlertDialog("Error", e.getLocalizedMessage());
        }
    }

    private void showRatingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(YourDoctor.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_rating_dialog, null);
        dialogBuilder.setView(dialogView);

        CircleImageView profilePic = (CircleImageView) dialogView.findViewById(R.id.circleIVRating);
        RatingBar rateDoctor = (RatingBar) dialogView.findViewById(R.id.rateDoctor);
        TextView doctorName = dialogView.findViewById(R.id.doctorNameRating);

        profilePic.setImageBitmap(bitmap);
        doctorName.setText(doctor.username);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        rateDoctor.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int averageRating = (int) Math.ceil((doctor.rating + rating) / 2);
                showHideProgress(true, "Please Wait");
                firebaseUtil.removeDoctor(Float.valueOf(averageRating), YourDoctor.this);

            }
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_your_doctor;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case ProfilePicture: {
                showHideProgress(false, "");
                bitmap = (Bitmap) o;
                profilePic.setImageBitmap(bitmap);
                yourDoctorLayout.setVisibility(View.VISIBLE);
                break;
            }
            case DoctorRemoved: {
                showHideProgress(false, "");
                onBackPressed();
            }
        }
    }

    public String getDoctorAddress(UserLocation userLocation) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1);

            return addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
