package com.ppngp.ppn;

import android.util.Log;

import com.parse.ParseUser;

import java.util.Date;
import java.util.Objects;

public class UserHandler {

    public static void passwordReset(String email) {
        ParseUser.requestPasswordResetInBackground(email, e -> {
            if (e != null) {
                Log.d("Password reset", "Error: " + e.getMessage());
            }
        });
    }

    public static ParseUser getCurrentUser() {
        return ParseUser.getCurrentUser();
    }

    private void logoutUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ParseUser.logOut();
            Log.d("Logout", "User logged out.");
        } else {
            Log.d("Logout", "No user found.");
        }
    }

    private void updateExpireDate(Date date) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("expire_date", date);
        currentUser.saveInBackground(e -> {
            if (e == null) {
                Log.d("Logout", "User logged out.");
            } else {
                Log.d("Logout", Objects.requireNonNull(e.getMessage()));
            }
        });
    }
}
