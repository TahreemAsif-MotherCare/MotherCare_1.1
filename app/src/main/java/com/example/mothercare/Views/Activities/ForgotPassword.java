package com.example.mothercare.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends BaseActivity {
    EditText passwordResetEmail;
    Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordResetEmail = findViewById(R.id.passwordResetEmail);
        resetPasswordButton = findViewById(R.id.resetPassword);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHideProgress(true, "Please wait");
                if (isValidated()) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(passwordResetEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        showToast("An activation email is sent to your email address!");
                                        showHideProgress(false, "");
                                        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        showHideProgress(false, "");
                                        showAlertDialog("Error", task.getException().getLocalizedMessage());
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean isValidated() {
        if (passwordResetEmail.getText().length() == 0) {
            passwordResetEmail.setError("Email cannot be empty!");
            passwordResetEmail.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_forgot_password;
    }
}
