package com.example.mothercare.Views.Activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.Models.Doctor;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.Models.Pharmacist;
import com.example.mothercare.Models.UserLocation;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.storage.FirebaseStorage;
import com.nex3z.togglebuttongroup.button.CircularToggle;
import com.nex3z.togglebuttongroup.button.OnCheckedChangeListener;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SignupActivity extends BaseActivity {
    Button signUp;
    Spinner userRole;
    TextView loginText;
    LinearLayout doctorRegistrationForm, patientRegistrationForm, pharmacyRegForm;
    private static Uri selectedImage;
    private static Bitmap bitmap;
    private static final int GET_FROM_GALLERY = 101;
    private CircularToggle mon, tue, wed, thu, fri, sat, sun;
    private boolean monChecked, tueChecked, wedChecked, thuChecked, friChecked, satChecked, sunChecked;
    private TextView mondayStartTimeTV, mondayEndTimeTV, tuesdayStartTimeTV, tuesdayEndTimeTV, wedStartTimeTV, wedEndTimeTV, thuStartTimeTV,
            thuEndTimeTV, friStartTimeTV, friEndTimeTV, satStartTimeTV, satEndTimeTV, sunStartTimeTV, sunEndTimeTV;
    private String mondayStartTime, mondayEndTime, tuesdayStartTime, tuesdayEndTime, wedStartTime, wedEndTime, thuStartTime, thuEndTime, friStartTime,
            friEndTime, satStartTime, satEndTime, sunStartTime, sunEndTime;
    EditText name, email, password, confirmPassword, phoneNumber, address, qualification, specialization, institute, worksAt, month, trimester, week, charges;
    ImageView profileImageView, scheduleImageView, locationImageView, pharmacyLoc;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private int PLACE_PICKER_REQUEST = 1;
    UserLocation location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        userRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (userRole.getSelectedItem().equals("Doctor")) {
                    doctorRegistrationForm.setVisibility(View.VISIBLE);
                    patientRegistrationForm.setVisibility(View.GONE);
                } else if (userRole.getSelectedItem().equals("Patient")) {
                    patientRegistrationForm.setVisibility(View.VISIBLE);
                    doctorRegistrationForm.setVisibility(View.GONE);
                } else if (userRole.getSelectedItem().equals("Pharmacy")) {
                    patientRegistrationForm.setVisibility(View.GONE);
                    doctorRegistrationForm.setVisibility(View.GONE);
                    pharmacyRegForm.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        scheduleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                final View dialogView = getLayoutInflater().inflate(R.layout.doctor_schedule_dialog, null);
                builder.setView(dialogView);
                final AlertDialog dialog;
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performScheduleValidation(dialogView);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                initDialogViews(dialogView);
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUserInfo()) {
                    FirebaseUtil firebaseUtil = new FirebaseUtil(SignupActivity.this);
                    if (userRole.getSelectedItem().equals("Patient")) {
                        firebaseUtil.signUpPatient(getPatientData(), password.getText().toString());
                        showHideProgress(true, "Please Wait");
                    } else if (userRole.getSelectedItem().equals("Doctor")) {
                        firebaseUtil.signUpDoctor(getDoctorData(), password.getText().toString());
                        showHideProgress(true, "Please Wait");
                    } else if (userRole.getSelectedItem().equals("Pharmacy")) {
                        firebaseUtil.signUpPharmacist(getPharmacistData(), password.getText().toString());
                        showHideProgress(true, "Please Wait");
                    }
                }
            }
        });

        locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LocationPickerActivity.class);
                startActivityForResult(i, PLACE_PICKER_REQUEST);
            }
        });
        pharmacyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LocationPickerActivity.class);
                startActivityForResult(i, PLACE_PICKER_REQUEST);
            }
        });
    }

    private void init() {
        signUp = findViewById(R.id.nextButton);
        doctorRegistrationForm = findViewById(R.id.doctorsRegistrationInformation);
        patientRegistrationForm = findViewById(R.id.patientRegistrationInformation);
        pharmacyRegForm = findViewById(R.id.pharmacyRegistrationInformation);
        name = findViewById(R.id.userName);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        qualification = findViewById(R.id.qualification);
        specialization = findViewById(R.id.specialization);
        loginText = findViewById(R.id.loginText);
        userRole = findViewById(R.id.userRoles);
        institute = findViewById(R.id.institute);
        worksAt = findViewById(R.id.worksAt);
        profileImageView = findViewById(R.id.profilePictureImageView);
        scheduleImageView = findViewById(R.id.scheduleImageView);
        locationImageView = findViewById(R.id.locationImageView);
        pharmacyLoc = findViewById(R.id.locationImageView_pharmacy);
        trimester = findViewById(R.id.trimester);
        month = findViewById(R.id.pregnancyMonth);
        week = findViewById(R.id.week);
        charges = findViewById(R.id.charges);

        mondayStartTime = tuesdayStartTime = wedStartTime = thuStartTime = friStartTime = satStartTime = sunStartTime = "Start Time";
        mondayEndTime = tuesdayEndTime = wedEndTime = thuEndTime = friEndTime = satEndTime = sunEndTime = "End Time";
        monChecked = tueChecked = wedChecked = thuChecked = friChecked = satChecked = sunChecked = false;

        MapUtility.apiKey = getResources().getString(R.string.places_api_key);
    }

    private void initDialogViews(View dialogView) {
        mon = dialogView.findViewById(R.id.mon);
        tue = dialogView.findViewById(R.id.tue);
        wed = dialogView.findViewById(R.id.wed);
        thu = dialogView.findViewById(R.id.thu);
        fri = dialogView.findViewById(R.id.fri);
        sat = dialogView.findViewById(R.id.sat);
        sun = dialogView.findViewById(R.id.sun);

        mon.setChecked(monChecked);
        tue.setChecked(tueChecked);
        wed.setChecked(wedChecked);
        thu.setChecked(thuChecked);
        fri.setChecked(friChecked);
        sat.setChecked(satChecked);
        sun.setChecked(sunChecked);

        mondayStartTimeTV = dialogView.findViewById(R.id.mondayStartTime);
        mondayStartTimeTV.setText(mondayStartTime);
        mondayEndTimeTV = dialogView.findViewById(R.id.mondayEndTime);
        mondayEndTimeTV.setText(mondayEndTime);

        tuesdayStartTimeTV = dialogView.findViewById(R.id.tuesdayStartTime);
        tuesdayStartTimeTV.setText(tuesdayStartTime);
        tuesdayEndTimeTV = dialogView.findViewById(R.id.tuesdayEndTime);
        tuesdayEndTimeTV.setText(tuesdayEndTime);

        wedStartTimeTV = dialogView.findViewById(R.id.wednesdayStartTime);
        wedStartTimeTV.setText(wedStartTime);
        wedEndTimeTV = dialogView.findViewById(R.id.wednesdayEndTime);
        wedEndTimeTV.setText(wedEndTime);

        thuStartTimeTV = dialogView.findViewById(R.id.thursdayStartTime);
        thuStartTimeTV.setText(thuStartTime);
        thuEndTimeTV = dialogView.findViewById(R.id.thursdayEndTime);
        thuEndTimeTV.setText(thuEndTime);

        friStartTimeTV = dialogView.findViewById(R.id.fridayStartTime);
        friStartTimeTV.setText(friStartTime);
        friEndTimeTV = dialogView.findViewById(R.id.fridayEndTime);
        friEndTimeTV.setText(friEndTime);

        satStartTimeTV = dialogView.findViewById(R.id.saturdayStartTime);
        satStartTimeTV.setText(satStartTime);
        satEndTimeTV = dialogView.findViewById(R.id.saturdayEndTime);
        satEndTimeTV.setText(satEndTime);

        sunStartTimeTV = dialogView.findViewById(R.id.sundayStartTime);
        sunStartTimeTV.setText(sunStartTime);
        sunEndTimeTV = dialogView.findViewById(R.id.sundayEndTime);
        sunEndTimeTV.setText(sunEndTime);

        mon.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
                if (isChecked) {
                    monChecked = true;
                    mondayStartTimeTV.setEnabled(true);
                    mondayEndTimeTV.setEnabled(true);

                } else {
                    monChecked = false;
                    mondayStartTimeTV.setEnabled(false);
                    mondayEndTimeTV.setEnabled(false);

                }
            }
        });
        tue.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
                if (isChecked) {
                    tueChecked = true;
                    tuesdayStartTimeTV.setEnabled(true);
                    tuesdayEndTimeTV.setEnabled(true);

                } else {
                    tueChecked = false;
                    tuesdayStartTimeTV.setEnabled(false);
                    tuesdayEndTimeTV.setEnabled(false);

                }
            }
        });
        wed.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
                if (isChecked) {
                    wedChecked = true;
                    wedStartTimeTV.setEnabled(true);
                    wedEndTimeTV.setEnabled(true);

                } else {
                    wedChecked = false;
                    wedStartTimeTV.setEnabled(false);
                    wedEndTimeTV.setEnabled(false);

                }
            }
        });
        thu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
                if (isChecked) {
                    thuChecked = true;
                    thuStartTimeTV.setEnabled(true);
                    thuEndTimeTV.setEnabled(true);
                } else {
                    thuChecked = false;
                    thuStartTimeTV.setEnabled(false);
                    thuEndTimeTV.setEnabled(false);
                }
            }
        });
        fri.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
                if (isChecked) {
                    friChecked = true;
                    friStartTimeTV.setEnabled(true);
                    friEndTimeTV.setEnabled(true);

                } else {
                    friChecked = false;
                    friStartTimeTV.setEnabled(false);
                    friEndTimeTV.setEnabled(false);

                }
            }
        });
        sat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
                if (isChecked) {
                    satChecked = true;
                    satStartTimeTV.setEnabled(true);
                    satEndTimeTV.setEnabled(true);

                } else {
                    satChecked = false;
                    satStartTimeTV.setEnabled(false);
                    satEndTimeTV.setEnabled(false);

                }
            }
        });
        sun.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
                if (isChecked) {
                    sunChecked = true;
                    sunStartTimeTV.setEnabled(true);
                    sunEndTimeTV.setEnabled(true);

                } else {
                    sunChecked = false;
                    sunStartTimeTV.setEnabled(false);
                    sunEndTimeTV.setEnabled(false);

                }
            }
        });
        mondayStartTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mondayStartTime = hourOfDay + ":" + minute;
                        mondayStartTimeTV.setText(mondayStartTime);
                    }
                });
            }
        });
        mondayEndTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mondayEndTime = hourOfDay + ":" + minute;
                        mondayEndTimeTV.setText(mondayEndTime);
                    }
                });
            }
        });
        tuesdayStartTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tuesdayStartTime = hourOfDay + ":" + minute;
                        tuesdayStartTimeTV.setText(tuesdayStartTime);
                    }
                });
            }
        });
        tuesdayEndTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tuesdayEndTime = hourOfDay + ":" + minute;
                        tuesdayEndTimeTV.setText(tuesdayEndTime);
                    }
                });
            }
        });
        wedStartTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        wedStartTime = hourOfDay + ":" + minute;
                        wedStartTimeTV.setText(wedStartTime);
                    }
                });
            }
        });
        wedEndTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        wedEndTime = hourOfDay + ":" + minute;
                        wedEndTimeTV.setText(wedEndTime);
                    }
                });
            }
        });
        thuStartTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        thuStartTime = hourOfDay + ":" + minute;
                        thuStartTimeTV.setText(thuStartTime);
                    }
                });
            }
        });
        thuEndTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        thuEndTime = hourOfDay + ":" + minute;
                        thuEndTimeTV.setText(hourOfDay + ":" + minute);
                    }
                });
            }
        });
        friStartTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        friStartTime = hourOfDay + ":" + minute;
                        friStartTimeTV.setText(friStartTime);
                    }
                });
            }
        });
        friEndTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        friEndTime = hourOfDay + ":" + minute;
                        friEndTimeTV.setText(friEndTime);
                    }
                });
            }
        });
        satStartTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        satStartTime = hourOfDay + ":" + minute;
                        satStartTimeTV.setText(satStartTime);
                    }
                });
            }
        });
        satEndTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        satEndTime = hourOfDay + ":" + minute;
                        satEndTimeTV.setText(satEndTime);
                    }
                });
            }
        });
        sunStartTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sunStartTime = hourOfDay + ":" + minute;
                        sunStartTimeTV.setText(sunStartTime);
                    }
                });
            }
        });
        sunEndTimeTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sunEndTime = hourOfDay + ":" + minute;
                        sunEndTimeTV.setText(sunEndTime);
                    }
                });
            }
        });
    }

    private void performScheduleValidation(View dialogView) {

    }

    private Boolean validateUserInfo() {
        if (userRole.getSelectedItem().toString().contains("Select")) {
            showToast("Please select use role");
            userRole.requestFocus();
        } else if (name.getText().toString().isEmpty()) {
            name.setError("Name cannot be empty");
            name.requestFocus();
            return false;
        } else if (email.getText().toString().isEmpty()) {
            email.setError("Email cannot be empty");
            email.requestFocus();
            return false;
        } else if (!isValid(email.getText().toString())) {
            email.setError("Invalid Email! ");
            email.requestFocus();
            return false;
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Password cannot be empty");
            password.requestFocus();
            return false;
        } else if (password.getText().toString().length() < 8) {
            password.setError("Password cannot be less than 8 characters");
            password.requestFocus();
            return false;
        } else if (confirmPassword.getText().toString().isEmpty()) {
            confirmPassword.setError("Confirm Password cannot be empty");
            confirmPassword.requestFocus();
            return false;
        } else if (!(password.getText().toString().equals(confirmPassword.getText().toString()))) {
            confirmPassword.setError("Passwords do not match!");
            confirmPassword.requestFocus();
            return false;
        } else if (phoneNumber.getText().toString().isEmpty()) {
            phoneNumber.setError("Phone number cannot be empty");
            phoneNumber.requestFocus();
            return false;
        } else if (phoneNumber.getText().toString().length() < 11) {
            phoneNumber.setError("Invalid Phone Number");
            phoneNumber.requestFocus();
            return false;
        } else if (address.getText().toString().isEmpty()) {
            address.setError("Address cannot be empty");
            address.requestFocus();
            return false;
        } else if (bitmap == null) {
            Toast.makeText(getApplicationContext(), "Please upload a profile picture", Toast.LENGTH_LONG).show();
            profileImageView.requestFocus();
            return false;
        }
        if (patientRegistrationForm.getVisibility() == View.VISIBLE) {
            if (trimester.getText().toString().isEmpty()) {
                trimester.setError("Trimester cannot be empty");
                trimester.requestFocus();
                return false;
            } else if (month.getText().toString().isEmpty()) {
                month.setError("Pregnancy Month cannot be empty");
                month.requestFocus();
                return false;
            } else if (week.getText().toString().isEmpty()) {
                week.setError("Pregnancy Week cannot be empty");
                week.requestFocus();
                return false;
            }
        } else if (doctorRegistrationForm.getVisibility() == View.VISIBLE) {
            if (specialization.getText().toString().isEmpty()) {
                specialization.setError("Experience cannot be empty");
                specialization.requestFocus();
                return false;
            } else if (qualification.getText().toString().isEmpty()) {
                qualification.setError("Qualification cannot be empty");
                qualification.requestFocus();
                return false;
            } else if (worksAt.getText().toString().isEmpty()) {
                worksAt.setError("This field cannot be empty! ");
                worksAt.requestFocus();
                return false;
            } else if (institute.getText().toString().isEmpty()) {
                institute.setError("Institute cannot be empty");
                institute.requestFocus();
                return false;
            } else if (charges.getText().toString().isEmpty()) {
                institute.setError("Fees/Charges cannot be empty");
                institute.requestFocus();
                return false;
            }
            if (location == null) {
                showToast("Please pick your location");
                locationImageView.requestFocus();
                return false;
            }
        } else if (pharmacyRegForm.getVisibility() == View.VISIBLE) {
            if (location == null) {
                showToast("Please pick your location");
                pharmacyLoc.requestFocus();
                return false;
            }
        }
        return true;
    }

    private Doctor getDoctorData() {
        Doctor doctor = new Doctor("", name.getText().toString(), email.getText().toString(), phoneNumber.getText().toString()
                , Integer.parseInt(specialization.getText().toString()), qualification.getText().toString(),
                worksAt.getText().toString(), institute.getText().toString(),
                mon.isChecked(), tue.isChecked(), wed.isChecked(), thu.isChecked(), fri.isChecked(), sat.isChecked(), sun.isChecked(), 5, Integer.parseInt(charges.getText().toString()));
        doctor.setProfilePic(bitmap);
        doctor.setMondayStartTime(mondayStartTime);
        doctor.setMondayEndTime(mondayEndTime);
        doctor.setTuesdayStartTime(tuesdayStartTime);
        doctor.setTuesdayEndTime(tuesdayEndTime);
        doctor.setWedStartTime(wedStartTime);
        doctor.setWedEndTime(wedEndTime);
        doctor.setThuStartTime(thuStartTime);
        doctor.setThuEndTime(thuEndTime);
        doctor.setFriStartTime(friStartTime);
        doctor.setFriEndTime(friEndTime);
        doctor.setSatStartTime(satStartTime);
        doctor.setSatEndTime(satEndTime);
        doctor.setSunStartTime(sunStartTime);
        doctor.setSunEndTime(sunEndTime);
        doctor.setUserLocation(location);
        return doctor;
    }

    private Patient getPatientData() {
        Patient patient = new Patient("", name.getText().toString(), email.getText().toString(), phoneNumber.getText().toString()
                , Integer.parseInt(trimester.getText().toString()), Integer.parseInt(month.getText().toString()), Integer.parseInt(week.getText().toString()));
        patient.setProfilePic(bitmap);
        return patient;
    }

    private Pharmacist getPharmacistData() {
        Pharmacist pharmacist = new Pharmacist("", name.getText().toString(), email.getText().toString(), phoneNumber.getText().toString()
                , address.getText().toString());
        pharmacist.setLocation(location);
        pharmacist.setProfilePic(bitmap);
        return pharmacist;
    }

    private static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                profileImageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    double selectedLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double selectedLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    location = new UserLocation(selectedLatitude, selectedLongitude);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_signup;
    }
}
