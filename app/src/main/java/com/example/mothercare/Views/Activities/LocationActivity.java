package com.example.mothercare.Views.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Doctor;
import com.example.mothercare.Models.UserLocation;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class LocationActivity extends BaseActivity implements OnMapReadyCallback, FirebaseUtil.FirebaseResponse {
    private GoogleMap mMap;
    private FirebaseUtil firebaseUtil;
    private String doctorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        getSupportActionBar().hide();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_location;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UserLocation userLocation = getLocation();
        LatLng latLng = new LatLng(userLocation.latitude, userLocation.longitude);
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(20000)
                .strokeWidth(2f)
                .strokeColor(Color.RED));
        showHideProgress(true, "Please Wait");
        firebaseUtil.getDoctors();
    }

    private void addMarkers(ArrayList<Doctor> doctorsArrayList) {
        for (Doctor doctor : doctorsArrayList) {
            UserLocation userLocation = getLocation();
            if (distance(userLocation.latitude, userLocation.longitude, doctor.getUserLocation().latitude, doctor.getUserLocation().longitude) < 20) {
                LatLng loc = new LatLng(doctor.getUserLocation().latitude, doctor.getUserLocation().longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title(doctor.username).snippet(doctor.doctorID));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10.0f));
                marker.showInfoWindow();
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(LocationActivity.this, DoctorProfileActivity.class);
                    intent.putExtra("doctorID", marker.getSnippet());
                    startActivity(intent);
                    return false;
                }
            });
        }
        showHideProgress(false, "");
    }

    @Override
    public void firebaseResponse(Object response, FirebaseResponses firebaseResponses) {
        showHideProgress(false, "");
        switch (firebaseResponses) {
            case Doctors: {
                ArrayList<Doctor> doctorArrayList = (ArrayList<Doctor>) response;
                if (!doctorArrayList.isEmpty()) {
                    addMarkers(doctorArrayList);
                }
            }
            case Error: {
            }
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
