package com.example.mothercare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mothercare.Models.UserLocation;

import java.util.Random;

import dmax.dialog.SpotsDialog;

public abstract class BaseActivity extends AppCompatActivity {

    public AlertDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
    }

    protected abstract int getLayoutResource();

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showHideProgress(final boolean show, String progressMessage) {
        if (progressMessage.isEmpty()) {
            progressMessage = "Please wait...";
        }

        if (show) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                return;
            }
            mProgressDialog = new SpotsDialog.Builder()
                    .setContext(this)
                    .setMessage("Please Wiat")
                    .setCancelable(false)
                    .build();
            mProgressDialog.show();
        } else {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            closeKeyboard();
//            Intent intent = new Intent();
//            intent.putExtra("openFragment", true);
//            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && this.getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public void showAlertDialog(String dialogTitle, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(dialogTitle);

        builder.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(dialogTitle);
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showTimePickerDialog(TimePickerDialog.OnTimeSetListener timeSetListener) {
        final Calendar c = Calendar.getInstance();
        TimePickerDialog mTimePicker = new TimePickerDialog(this, timeSetListener, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void replaceFragment(int containerViewID, Fragment fragment) {
        String homeFragmentTag = null;
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName()) == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() == 0) {
                homeFragmentTag = backStateName;
            }
            ft.replace(containerViewID, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public UserLocation getLocation() {
        turnGPSOn();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location locationGPS = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        @SuppressLint("MissingPermission") Location locationNetwork = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        UserLocation userLocation = new UserLocation();
        if (locationGPS != null) {
            userLocation.latitude = locationGPS.getLatitude();
            userLocation.longitude = locationGPS.getLongitude();
        } else if (locationNetwork != null) {
            userLocation.latitude = locationNetwork.getLatitude();
            userLocation.longitude = locationNetwork.getLongitude();
        }
        return userLocation;
    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) {
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    protected int generateNotificationID() {
        Random r = new Random();
        int low = 10;
        int high = 1000000000;
        return r.nextInt(high - low) + low;
    }

    public String generateRandomID() {
        String AlphaNumericString = "0123456789";
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i < 15; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}