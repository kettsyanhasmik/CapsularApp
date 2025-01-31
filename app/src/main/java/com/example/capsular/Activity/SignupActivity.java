package com.example.capsular.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capsular.R;
import com.example.capsular.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signupBtn.setOnClickListener(v -> performSignup());
    }

    private void performSignup() {
        String email = binding.userEdt.getText().toString().trim();
        String password = binding.passEdt.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showErrorDialog("Email and Password cannot be empty");
            return;
        }

        Log.d("SignupActivity", "Signup attempt with email: " + email);

        // Simulate successful signup
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Signup Failed")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
