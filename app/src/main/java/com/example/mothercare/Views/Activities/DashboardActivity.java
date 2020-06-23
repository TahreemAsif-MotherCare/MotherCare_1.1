package com.example.mothercare.Views.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Views.Fragment.DashboardFragment;
import com.example.mothercare.Views.Fragment.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private DashboardFragment dashboardFragment;
    private NotificationsFragment notificationsFragment;
    private YourDoctor yourFragment;
    private ImageView menuImageView, notifications;
    private BottomNavigationView bottomNavView;
    private Boolean isPatient;
    private FirebaseUtil firebaseUtil;
    private static final int GRANT_ALL_PERMISSIONS = 100;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        checkPermission();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.logout)
                .setDrawerLayout(drawer)
                .build();
        menuImageView = findViewById(R.id.menuDrawer);
        notifications = findViewById(R.id.notifications);
        bottomNavView = findViewById(R.id.bottom_navigation);
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.checkUserStatus();
        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.navigation_home) {
                    replaceFragment(R.id.dashboardFrameLayout, dashboardFragment);
                } else if (menuItem.getItemId() == R.id.pharmacy) {
                    Intent intent = new Intent(DashboardActivity.this, MedicinesActivity.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.emergency) {
                    Intent intent = new Intent(DashboardActivity.this, EmergencyActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardActivity.this.replaceFragment(R.id.dashboardFrameLayout, notificationsFragment);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.change_account_settings) {
                    if (isPatient) {
                        Intent intent = new Intent(DashboardActivity.this, ChangePatientAccountSettingsActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(DashboardActivity.this, ChangeDoctorAccountSettings.class);
                        startActivity(intent);
                    }
                } else if (itemId == R.id.pharmacy) {
//                    replaceFragment(R.id.dashboardFrameLayout, yourFragment);
                }
                return true;
            }
        });
        dashboardFragment = DashboardFragment.newInstance();
        notificationsFragment = NotificationsFragment.newInstance();
        replaceFragment(R.id.dashboardFrameLayout, dashboardFragment);


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_dashboard;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.dashboardFrameLayout);
        popFragmentsFromBackStack(currentFragment);
    }

    public void popFragmentsFromBackStack(Fragment fragment) {
        if (fragment.getClass().getName() == dashboardFragment.getClass().getName()) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void checkPermission() {
        ActivityCompat.requestPermissions(this, permissions, GRANT_ALL_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GRANT_ALL_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Permission granted");
            } else {
                showToast("Permission cannot be granted");
            }
        }
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case isPatient: {
                isPatient = true;
                break;
            }
            case isDoctor: {
                isPatient = false;
                break;
            }
        }
    }
}
