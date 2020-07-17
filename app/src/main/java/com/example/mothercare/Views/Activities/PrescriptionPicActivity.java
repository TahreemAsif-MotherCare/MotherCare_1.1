package com.example.mothercare.Views.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PrescriptionPicActivity extends BaseActivity {
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoView = (PhotoView) findViewById(R.id.photo_view);
        if (getIntent().getStringExtra("type").equals("Order")) {
            byte[] byteArray = getIntent().getByteArrayExtra("image");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            photoView.setImageBitmap(bmp);
        } else {
            showHideProgress(true , "");
            String reportID = getIntent().getStringExtra("ID");
            StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("Reports").child(reportID);
            final long ONE_MEGABYTE = 1024 * 1024;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    photoView.setImageBitmap(bitmap);
                    showHideProgress(false , "");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_prescription_pic;
    }
}
