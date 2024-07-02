package com.ppngp.ppn;

import android.util.Log;

import com.parse.ParseUser;

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
}
