package com.example.mothercare.Views.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.ViewReportsAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Report;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class AddReportsActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private RecyclerView recyclerView;
    private TextView noSharedReportsTextView;
    FloatingActionButton uploadReports;
    ArrayList<Report> reports = new ArrayList<>();
    ArrayList<Report> reportsWithPictures = new ArrayList<>();
    private static final int GET_FROM_GALLERY = 101;
    private Uri selectedImage;
    private Bitmap bitmap;
    private FirebaseUtil firebaseUtil;
    private String patientID;
    private ViewReportsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.sharedReportsRecylerView);
        noSharedReportsTextView = findViewById(R.id.noReportsShared);
        uploadReports = findViewById(R.id.newReport);
        firebaseUtil = new FirebaseUtil(this);
        Intent intent = getIntent();
        patientID = intent.getStringExtra("patientID");
        firebaseUtil.setFirebaseResponse(this);
        showHideProgress(true, "Please Wait");
        firebaseUtil.getSpecificPatientReport(patientID);

        uploadReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                showReportDialog(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showReportDialog(Bitmap reportImage) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddReportsActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.upload_report_dialog, null);
        ImageView reportPic = dialogView.findViewById(R.id.reportImageView);
        final EditText reportDescription = dialogView.findViewById(R.id.reportDescription);
        Button cancel = dialogView.findViewById(R.id.dismissReportUploadingDialog);
        final Button uploadReport = dialogView.findViewById(R.id.uploadReport);
        reportPic.setImageBitmap(reportImage);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        uploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report report = new Report(generateReportID(), firebaseUtil.getCurrentUserID(), patientID, reportDescription.getText().toString());
                report.setReportPicture(bitmap);
                firebaseUtil.uploadReport(patientID, report);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public String generateReportID() {
        String AlphaNumericString = "0123456789";
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i < 15; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_reports;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        showHideProgress(false, "");
        switch (firebaseResponses) {
            case SpecificPatientReport: {
                ArrayList<Report> reports = (ArrayList<Report>) o;
                if (!reports.isEmpty()) {
                    adapter = new ViewReportsAdapter(reports, this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(adapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noSharedReportsTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
