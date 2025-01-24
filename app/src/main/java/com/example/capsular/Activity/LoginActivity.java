package com.example.capsular.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.capsular.R;
import com.example.capsular.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Set up listeners
        setLoginListener();
        setSignupRedirectListener();
        setForgotPasswordListener(); // For the custom "Forgot Password" dialog
    }

    /**
     * Handles the login process when the "Login" button is clicked.
     */
    private void setLoginListener() {
        binding.loginBtn.setOnClickListener(view -> {
            String email = binding.userEdt.getText().toString().trim();
            String password = binding.passEdt.getText().toString().trim();

            // Debugging logs
            Log.d(TAG, "Email: " + email);
            Log.d(TAG, "Password: " + password);

            // Input Validation
            if (email.isEmpty()) {
                showError("Email cannot be empty");
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showError("Enter a valid email address");
                return;
            }

            if (password.isEmpty()) {
                showError("Password cannot be empty");
                return;
            }

            if (password.length() < 6) {
                showError("Password must be at least 6 characters");
                return;
            }

            // Proceed with Firebase Login
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    navigateToMainActivity(user);
                } else {
                    showError("Login failed: " + task.getException().getMessage());
                }
            });
        });
    }

    /**
     * Redirects to the SignupActivity when the "Sign up" text is clicked.
     */
    private void setSignupRedirectListener() {
        binding.signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Shows the custom "Forgot Password" dialog when the corresponding text is clicked.
     */
    private void setForgotPasswordListener() {
        binding.textView3.setOnClickListener(view -> showForgotPasswordDialog());
    }

    /**
     * Displays a custom dialog for entering the email address to reset the password.
     */
    private void showForgotPasswordDialog() {
        // Inflate the custom layout
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);

        // Create the custom dialog
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        // Access the UI elements from the custom layout
        EditText emailInput = dialogView.findViewById(R.id.emailInput);
        View cancelButton = dialogView.findViewById(R.id.cancelButton);
        View resetButton = dialogView.findViewById(R.id.resetButton);

        // Set button click listeners
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        resetButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                showError("Email cannot be empty");
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showError("Enter a valid email address");
            } else {
                sendPasswordResetEmail(email);
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    /**
     * Sends a password reset email to the provided email address.
     */
    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showSuccess("Password reset email sent successfully. Check your inbox!");
            } else {
                showError("Failed to send password reset email: " + task.getException().getMessage());
            }
        });
    }

    /**
     * Navigates to the MainActivity after successful login.
     */
    private void navigateToMainActivity(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Displays an error dialog with the specified message.
     */
    private void showError(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null) // Create the "OK" button
                .create();

        // Change the "OK" button color when the dialog is shown
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.maincolor));
        });

        dialog.show();
    }

    /**
     * Displays a success dialog with the specified message.
     */
    private void showSuccess(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", null) // Create the "OK" button
                .create();

        // Change the "OK" button color when the dialog is shown
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.maincolor));
        });

        dialog.show();
    }
}
