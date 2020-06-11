package com.example.mothercare.Views.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mothercare.R;
import com.github.chrisbanes.photoview.PhotoView;

public class PrescriptionPicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_pic);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageBitmap(bmp);
    }
}
