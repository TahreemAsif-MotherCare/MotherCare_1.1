package com.example.mothercare.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Appointment;
import com.example.mothercare.Models.AppointmentRequest;
import com.example.mothercare.Models.Chat;
import com.example.mothercare.Models.Doctor;
import com.example.mothercare.Models.Medicine;
import com.example.mothercare.Models.MedicineOrder;
import com.example.mothercare.Models.Notifications;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.Models.PatientMedicalHistory;
import com.example.mothercare.Models.Pharmacist;
import com.example.mothercare.Models.Report;
import com.example.mothercare.Models.Request;
import com.example.mothercare.Views.Activities.AddReportsActivity;
import com.example.mothercare.Views.Activities.LoginActivity;
import com.example.mothercare.Views.Activities.PharmacistMedicinesActivity;
import com.example.mothercare.Views.Activities.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseUtil {

    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private FirebaseResponse firebaseResponse;

    public FirebaseUtil(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUtil() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void signUpDoctor(final Doctor doctor, String password) {
        mAuth.createUserWithEmailAndPassword(doctor.email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            if (task.isSuccessful()) {
                                createDoctorProfile(doctor);
                            }
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onComplete: Email Sent");
                                }
                            });
                        } else {
                            ((SignupActivity) context).showHideProgress(false, "");
                            ((SignupActivity) context).showAlertDialog("Authentication Failed", Objects.requireNonNull(task.getException()).toString());
                        }
                    }
                });
    }

    public void signUpPatient(final Patient patient, final String password) {
        mAuth.createUserWithEmailAndPassword(patient.email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            if (task.isSuccessful()) {
                                createPatientProfile(patient);
                            }
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onComplete: Email Sent");
                                }
                            });
                        } else {
                            ((SignupActivity) context).showHideProgress(false, "");
                            ((SignupActivity) context).showAlertDialog("Error!", Objects.requireNonNull(task.getException()).toString());
                        }
                    }
                });
    }

    public void signUpPharmacist(final Pharmacist pharmacist, final String password) {
        mAuth.createUserWithEmailAndPassword(pharmacist.email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            if (task.isSuccessful()) {
                                createPharmacistProfile(pharmacist);
                            }
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onComplete: Email Sent");
                                }
                            });
                        } else {
                            ((SignupActivity) context).showHideProgress(false, "");
                            ((SignupActivity) context).showAlertDialog("Error!", Objects.requireNonNull(task.getException()).toString());
                        }
                    }
                });
    }

    public void createDoctorProfile(Doctor doctor) {
        mDatabase = FirebaseDatabase.getInstance();
        try {
            DatabaseReference doctorRef = mDatabase.getReference().child("Doctors").child(mAuth.getInstance().getCurrentUser().getUid());
            Bitmap bitmap = doctor.profilePic;
            doctor.setProfilePic(null);
            doctor.setDoctorID(mAuth.getCurrentUser().getUid());
            doctorRef.setValue(doctor);
            uploadFile("Doctor", bitmap);
        } catch (Exception e) {
            Log.d(TAG, "createDoctorProfile: " + e.getLocalizedMessage());
            ((SignupActivity) context).showHideProgress(false, "");
            ((SignupActivity) context).showAlertDialog("Error!", e.getLocalizedMessage());
        }
    }

    public void createPatientProfile(Patient patient) {
        mDatabase = FirebaseDatabase.getInstance();
        try {
            DatabaseReference doctorRef = mDatabase.getReference().child("Patient").child(mAuth.getInstance().getCurrentUser().getUid());
            Bitmap bitmap = patient.profilePic;
            patient.setProfilePic(null);
            patient.setPatientID(mAuth.getCurrentUser().getUid());
            doctorRef.setValue(patient);
            uploadFile("Patient", bitmap);
        } catch (Exception e) {
            Log.d(TAG, "createPatientProfile: " + e.getLocalizedMessage());
            ((SignupActivity) context).showHideProgress(false, "");
            ((SignupActivity) context).showAlertDialog("Error!", e.getLocalizedMessage());
        }
    }

    public void createPharmacistProfile(Pharmacist pharmacist) {
        mDatabase = FirebaseDatabase.getInstance();
        try {
            DatabaseReference doctorRef = mDatabase.getReference().child("Pharmacist").child(mAuth.getInstance().getCurrentUser().getUid());
            Bitmap bitmap = pharmacist.profilePic;
            pharmacist.setProfilePic(null);
            pharmacist.setPharmacistID(mAuth.getCurrentUser().getUid());
            doctorRef.setValue(pharmacist);
            uploadFile("Pharmacist", bitmap);
        } catch (Exception e) {
            Log.d(TAG, "createPharmacistProfile: " + e.getLocalizedMessage());
            ((SignupActivity) context).showHideProgress(false, "");
            ((SignupActivity) context).showAlertDialog("Error!", e.getLocalizedMessage());
        }
    }

    private void uploadFile(String user, Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mountainImagesRef = FirebaseStorage.getInstance().getReference().child(user).child(mAuth.getCurrentUser().getUid());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                ((SignupActivity) context).showHideProgress(false, "");
                ((SignupActivity) context).showAlertDialog("Error!", exception.getLocalizedMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ((SignupActivity) context).showHideProgress(false, "");
                ((SignupActivity) context).showToast("Registration Successful! A verification email is sent to your provided email address");
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                ((SignupActivity) context).finish();
            }
        });
    }

    public String getCurrentUserID() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser.getUid();
    }

    public void addFriend(Context context, String patientID) {
        DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference().child("AddedDoctors").child(patientID +
                "__" + getCurrentUserID());
        Request request = new Request(patientID, getCurrentUserID());
        doctorRef.setValue(request);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests")
                .child(patientID + "__" + getCurrentUserID());
        reference.removeValue();
    }

    public void completeAppointment(String appointmentID) {
        FirebaseDatabase.getInstance().getReference().child("Appointments").child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).child(appointmentID).child("status").setValue("Completed");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveNotification(String title, String notificationStatus, String userID) {
        Notifications notifications = new Notifications();
        String AlphaNumericString = "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "ABCDEFGHJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i < 20; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("Notifications").child(userID).child(sb.toString());
        if (notificationStatus.equals("Medication")) {
            notifications = new Notifications("You have a medication schedule named: " + title, sdf.format(new Date()));
        } else if (notificationStatus.equals("Appointment")) {
            notifications = new Notifications(title, sdf.format(new Date()));
        }
        notificationRef.setValue(notifications);
    }

    public void completMedicationSchedule(String patientID, String medicationID) {
        FirebaseDatabase.getInstance().getReference("MedicationSchedule").child(patientID).child(medicationID).child("status").setValue("Completed");
    }

    public void deleteAppointmentRequest(AppointmentRequest appointmentRequest) {
        FirebaseDatabase.getInstance().getReference("AppointmentRequest").child(appointmentRequest.patientID + "_" + appointmentRequest.doctorID)
                .removeValue();
    }

    public void uploadReport(String patientID, Report report) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reports").child(patientID + "_" + report.doctorID).child(report.reportID);
        Bitmap bitmap = report.getReportPicture();
        report.setReportPicture(null);
        reference.setValue(report);
        uploadReportImage(report, bitmap);
    }

    private void uploadReportImage(Report report, Bitmap image) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mountainImagesRef = FirebaseStorage.getInstance().getReference().child("Reports").child(report.reportID);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                ((AddReportsActivity) context).showHideProgress(false, "");
                ((AddReportsActivity) context).showAlertDialog("Error!", exception.getLocalizedMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ((AddReportsActivity) context).showHideProgress(false, "");
            }
        });
    }

    public void saveChatMessage(Chat chat) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chat").child(chat.discussionID);
        reference.setValue(chat);
    }

    public void savePatientChatMessage(Chat chat, String doctorID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersChat")
                .child(doctorID + "_" + chat.userID).child(chat.discussionID);
        reference.setValue(chat);
    }

    public void saveDoctorChatMessage(Chat chat, String patientID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersChat")
                .child(chat.userID + "_" + patientID).child(chat.discussionID);
        reference.setValue(chat);
    }

    public void removeDoctor(final Float rating, final Context context) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("AddedDoctors");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Request addedDoctor = postSnapshot.getValue(Request.class);
                        if (addedDoctor.patientID.equals(getCurrentUserID())) {
                            rootRef.child(addedDoctor.patientID + "__" + addedDoctor.doctorID)
                                    .removeValue();
                            rateDoctor(addedDoctor.doctorID, rating);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
        }
    }

    private void rateDoctor(final String doctorID, final Float rating) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors").child(doctorID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                doctor.rating = Math.round(rating);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctors")
                        .child(doctorID);
                ref.setValue(doctor);
                firebaseResponse.firebaseResponse(true, FirebaseResponses.DoctorRemoved);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseResponse.firebaseResponse(false, FirebaseResponses.DoctorRemoved);
            }
        });
    }

    public void getMedicineRemindersCount(String userID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MedicationSchedule").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count++;
                }
                firebaseResponse.firebaseResponse(count, FirebaseResponses.MedicineRemindersCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getRequestsCount() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Requests");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Request request = postSnapshot.getValue(Request.class);
                        if (request.doctorID.equals(getCurrentUserID())) {
                            count++;
                        }
                    }
                    firebaseResponse.firebaseResponse(count, FirebaseResponses.RequestsCount);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
        }
    }

    public void getAppointmentsCount() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointments").child(getCurrentUserID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count++;
                }
                firebaseResponse.firebaseResponse(count, FirebaseResponses.AppointmentsCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkUserStatus() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Patient").child(getCurrentUserID());
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        firebaseResponse.firebaseResponse(dataSnapshot, FirebaseResponses.isPatient);
                    } else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors").child(getCurrentUserID());
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    firebaseResponse.firebaseResponse(snapshot, FirebaseResponses.isDoctor);
                                } else {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pharmacist").child(getCurrentUserID());
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                firebaseResponse.firebaseResponse(dataSnapshot, FirebaseResponses.isPharmacist);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
        }
    }

    public void getProfilePicture(String user, String pictureID) {
        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child(user).child(pictureID);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                firebaseResponse.firebaseResponse(bitmap, FirebaseResponses.ProfilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                firebaseResponse.firebaseResponse(exception, FirebaseResponses.Error);
            }
        });
    }

    public void getAddedDoctors() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("AddedDoctors");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final ArrayList<Request> addedDoctorArrayList = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Request addedDoctor = postSnapshot.getValue(Request.class);
                        if (addedDoctor.patientID.equals(getCurrentUserID()))
                            addedDoctorArrayList.add(addedDoctor);
                    }
                    final ArrayList<Doctor> doctorArrayList = new ArrayList<>();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Doctor doctor = postSnapshot.getValue(Doctor.class);
                                for (Request request : addedDoctorArrayList) {
                                    if (doctor.doctorID.equals(request.doctorID)) {
                                        doctorArrayList.add(doctor);
                                    }
                                }
                            }
                            firebaseResponse.firebaseResponse(doctorArrayList, FirebaseResponses.AddedDoctors);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            firebaseResponse.firebaseResponse(e.getMessage(), FirebaseResponses.Error);
        }
    }

    public void getAddedPatients() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("AddedDoctors");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final ArrayList<Request> addedPatientsArrayList = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Request addedPatient = postSnapshot.getValue(Request.class);
                        if (addedPatient.doctorID.equals(getCurrentUserID()))
                            addedPatientsArrayList.add(addedPatient);
                    }
                    final ArrayList<Patient> patientArrayList = new ArrayList<>();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patient");
                    reference.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Patient patient = postSnapshot.getValue(Patient.class);
                                for (Request request : addedPatientsArrayList) {
                                    if (patient.patientID.equals(request.patientID)) {
                                        patientArrayList.add(patient);
                                    }
                                }
                            }
                            firebaseResponse.firebaseResponse(patientArrayList, FirebaseResponses.AddedPatients);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            firebaseResponse.firebaseResponse(e.getMessage(), FirebaseResponses.Error);
        }
    }

    public void getSpecificPatientReport(final String patientID) {
        final ArrayList<Report> reports = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reports");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        Report report = postSnapShot.getValue(Report.class);
                        if ((report.doctorID.equals(getCurrentUserID())) && (report.patientID.equals(patientID))) {
                            reports.add(report);
                        }
                    }
                }
                firebaseResponse.firebaseResponse(reports, FirebaseResponses.SpecificPatientReport);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getDoctorInfo(final String doctorID) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Doctors");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Doctor doctor = dataSnapshot.child(doctorID).getValue(Doctor.class);
                    firebaseResponse.firebaseResponse(doctor, FirebaseResponses.SpecificDoctorInformation);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            firebaseResponse.firebaseResponse(e.getMessage(), FirebaseResponses.Error);
        }
    }

    public void getPharmacistInfo() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Pharmacist");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Pharmacist pharmacist = dataSnapshot.child(getCurrentUserID()).getValue(Pharmacist.class);
                    firebaseResponse.firebaseResponse(pharmacist, FirebaseResponses.Pharmacist);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            firebaseResponse.firebaseResponse(e.getMessage(), FirebaseResponses.Error);
        }
    }

    public void getAppointments(final String status) {
        final ArrayList<Appointment> appointments = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Appointments");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                        Appointment appointment = snapshot.getValue(Appointment.class);
                        if (status.equals("YourDoctor")) {
                            if (appointment.getPatientID().equals(getCurrentUserID())) {
                                appointments.add(appointment);
                            }
                        } else {
                            if (appointment.getDoctorID().equals(getCurrentUserID())) {
                                appointments.add(appointment);
                            }
                        }
                    }
                }
                firebaseResponse.firebaseResponse(appointments, FirebaseResponses.Appointments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
            }
        });
    }

    public void getDiscussionMessages() {
        final ArrayList<Chat> chatArrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    chatArrayList.add(chat);
                }
                Collections.sort(chatArrayList, new Comparator<Chat>() {
                    @Override
                    public int compare(Chat object1, Chat object2) {
                        return object1.date.compareTo(object2.date);
                    }
                });
                firebaseResponse.firebaseResponse(chatArrayList, FirebaseResponses.DiscussionMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
            }
        });
    }

    public void getChatMessages(String userID, String userType) {
        final ArrayList<Chat> chatArrayList = new ArrayList<>();
        DatabaseReference reference = null;
        if (userType.equals("DoctorChat"))
            reference = FirebaseDatabase.getInstance().getReference("UsersChat").child(getCurrentUserID() + "_" + userID);
        else
            reference = FirebaseDatabase.getInstance().getReference("UsersChat").child(userID + "_" + getCurrentUserID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    chatArrayList.add(chat);
                }
                Collections.sort(chatArrayList, new Comparator<Chat>() {
                    @Override
                    public int compare(Chat object1, Chat object2) {
                        return object1.date.compareTo(object2.date);
                    }
                });
                firebaseResponse.firebaseResponse(chatArrayList, FirebaseResponses.ChatMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.ChatMessages);
            }
        });
    }

    public void getDoctors() {
        final ArrayList<Doctor> doctorArrayList = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Doctors");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Doctor doctor = postSnapshot.getValue(Doctor.class);
                        doctorArrayList.add(doctor);
                    }
                    firebaseResponse.firebaseResponse(doctorArrayList, FirebaseResponses.Doctors);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
                }
            });
        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
        }
    }

    public void getMedicationHistory() {
        final ArrayList<PatientMedicalHistory> patientMedicalHistories = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("MedicationSchedule").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PatientMedicalHistory patientMedicalHistory = postSnapshot.getValue(PatientMedicalHistory.class);
                    patientMedicalHistories.add(patientMedicalHistory);
                }
                firebaseResponse.firebaseResponse(patientMedicalHistories, FirebaseResponses.MedicineReminders);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPatientInfo(final String patientID) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Patient");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Patient patient = dataSnapshot.child(patientID).getValue(Patient.class);
                    firebaseResponse.firebaseResponse(patient, FirebaseResponses.SpecificPatientInformation);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            firebaseResponse.firebaseResponse(e.getMessage(), FirebaseResponses.Error);
        }
    }

    public void getRequests() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Requests");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final ArrayList<String> patientIds = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Request request = postSnapshot.getValue(Request.class);
                        if (request.doctorID.equals(getCurrentUserID())) {
                            patientIds.add(request.patientID);
                        }
                    }
                    final ArrayList<Patient> patientArrayList = new ArrayList<>();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patient");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Patient patient = postSnapshot.getValue(Patient.class);
                                for (String patientID : patientIds) {
                                    if (patient.patientID.equals(patientID)) {
                                        patientArrayList.add(patient);
                                    }
                                }
                            }
                            firebaseResponse.firebaseResponse(patientArrayList, FirebaseResponses.PatientRequests);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            firebaseResponse.firebaseResponse(e.getMessage(), FirebaseResponses.Error);
        }
    }

    public void getAddedDoctor() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("AddedDoctors");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isDoctorFound = false;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Request addedDoctor = postSnapshot.getValue(Request.class);
                        if (addedDoctor.patientID.equals(getCurrentUserID())) {
                            isDoctorFound = true;
                            firebaseResponse.firebaseResponse(addedDoctor.doctorID, FirebaseResponses.PatientsAddedDoctor);
                        }
                    }
                    if (!isDoctorFound) {
                        firebaseResponse.firebaseResponse("", FirebaseResponses.PatientsAddedDoctor);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TAG", "onCancelled: " + databaseError);
                    firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            firebaseResponse.firebaseResponse(e.getMessage(), FirebaseResponses.Error);
        }
    }

    public void uploadMedicine(Medicine medicine, Bitmap bitmap) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Medicines")
                .child(getCurrentUserID()).child(medicine.getMedicineID());
        reference.setValue(medicine);
        if (bitmap != null) {
            uploadMedicinePicture(medicine, bitmap);
        }
    }

    private void uploadMedicinePicture(Medicine medicine, Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mountainImagesRef = FirebaseStorage.getInstance().getReference().child("Medicine").
                child(medicine.getMedicineID());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                ((PharmacistMedicinesActivity) context).showHideProgress(false, "");
                ((PharmacistMedicinesActivity) context).showAlertDialog("Error!", exception.getLocalizedMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ((PharmacistMedicinesActivity) context).showHideProgress(false, "");
                ((PharmacistMedicinesActivity) context).showToast("Medicine added successfully");
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                ((PharmacistMedicinesActivity) context).finish();
            }
        });
    }

    public void uploadPrescriptionPicture(Bitmap bitmap, final String orderID) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mountainImagesRef = FirebaseStorage.getInstance().getReference().child("Prescriptions").
                child(orderID);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                firebaseResponse.firebaseResponse(orderID, FirebaseResponses.uploadPrescription);
            }
        });
    }

    public void getAllMedicines() {
        final ArrayList<Medicine> medicineArrayList = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Medicines");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                        Medicine medicine = snapshot.getValue(Medicine.class);
                        medicineArrayList.add(medicine);
                    }
                }
                firebaseResponse.firebaseResponse(medicineArrayList, FirebaseResponses.getMedicines);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getOrderHistory() {
        final ArrayList<MedicineOrder> orderHistoryArrayList = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Order");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MedicineOrder medicineOrder = postSnapshot.getValue(MedicineOrder.class);
                    if (medicineOrder.patient.patientID.equals(getCurrentUserID()) || medicineOrder.medicine.get(0).pharmacyID.equals(getCurrentUserID())) {
                        orderHistoryArrayList.add(medicineOrder);
                    }
                }
                firebaseResponse.firebaseResponse(orderHistoryArrayList, FirebaseResponses.getOrderHistory);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getSpecificPharmacyMedicines() {
        final ArrayList<Medicine> medicineArrayList = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Medicines").child(getCurrentUserID());
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Medicine medicine = postSnapshot.getValue(Medicine.class);
                    medicineArrayList.add(medicine);
                }
                firebaseResponse.firebaseResponse(medicineArrayList, FirebaseResponses.getMedicines);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPharmacies() {
        final ArrayList<Pharmacist> pharmacistArrayList = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Pharmacist");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Pharmacist pharmacist = postSnapshot.getValue(Pharmacist.class);
                        pharmacistArrayList.add(pharmacist);
                    }
                    firebaseResponse.firebaseResponse(pharmacistArrayList, FirebaseResponses.Pharmacist);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseResponse.firebaseResponse(databaseError.getMessage(), FirebaseResponses.Error);
                }
            });
        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
        }
    }

    public interface FirebaseResponse<T> {
        void firebaseResponse(T t, FirebaseResponses firebaseResponses);
    }

    public void setFirebaseResponse(FirebaseResponse firebaseResponse) {
        this.firebaseResponse = firebaseResponse;
    }
}
