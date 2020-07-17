package com.example.mothercare.Views.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mothercare.R;

public class SymptomsOutputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms_output);
        String output = getIntent().getStringExtra("output");
        TextView outputDesc = findViewById(R.id.output_desc);
        outputDesc.setText(output);
    }
}
