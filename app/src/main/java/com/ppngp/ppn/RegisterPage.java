package com.ppngp.ppn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseUser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPage extends AppCompatActivity {
    TextInputEditText emailEt;
    TextInputEditText usernameEt;
    TextInputEditText pwdEt;
    TextInputEditText pwd2Et;
    MaterialButton registerBtn;
    TextView loginLink;

    String EMAIL_REGEX = "[a-zA-Z0-9_]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
    String USERNAME_REGEX = "^[a-zA-Z0-9_-]{5,15}$";
    String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        setElements();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerHandler();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEt.setText("");
                usernameEt.setText("");
                pwdEt.setText("");
                pwd2Et.setText("");
                tr(LoginPage.class);
            }
        });
    }

    private void setElements() {
        usernameEt = findViewById(R.id.usr_et);
        emailEt = findViewById(R.id.email_et);
        pwdEt = findViewById(R.id.pwd_et);
        pwd2Et = findViewById(R.id.pwd2_et);
        registerBtn = findViewById(R.id.reg_btn);
        loginLink = findViewById(R.id.login_link);
    }

    private void makeToast(String toast_str, int length) {
        Toast.makeText(RegisterPage.this, toast_str, length).show();
    }

    private void registerHandler() {
        String email = Objects.requireNonNull(emailEt.getText()).toString();
        String username = Objects.requireNonNull(usernameEt.getText()).toString();
        String password1 = Objects.requireNonNull(pwdEt.getText()).toString();
        String password2 = Objects.requireNonNull(pwd2Et.getText()).toString();

        int res;

        if (email.isEmpty() || username.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            makeToast("Please fill in all of the fields!", Toast.LENGTH_SHORT);
        } else if (!password1.equals(password2)) {
            makeToast("Passwords do not match!", Toast.LENGTH_SHORT);
            pwdEt.setText("");
            pwd2Et.setText("");
        } else if ((res = checkRegexes(email, username, password1)) > 0) {
            switch (res) {
                case 1:
                    makeToast("Invalid email address!", Toast.LENGTH_SHORT);
                    emailEt.setText("");
                    break;
                case 2:
                    makeToast("Username should be between 5, 15 chars long.", Toast.LENGTH_SHORT);
                    usernameEt.setText("");
                    break;
                case 3:
                    makeToast("Password should contain a number, a special char and at least 4 chars!", Toast.LENGTH_LONG);
                    pwdEt.setText("");
                    pwd2Et.setText("");
                    break;
                default:
                    break;
            }
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password1);
            user.setEmail(email);

            user.signUpInBackground(e -> {
                if (e == null) {
                    makeToast("Registration successful.", Toast.LENGTH_SHORT);
                    makeToast("Created an account for user: " + email + "/" + username + ".", Toast.LENGTH_SHORT);
                    usernameEt.setText("");
                    emailEt.setText("");
                    pwdEt.setText("");
                    pwd2Et.setText("");
                    tr(LoginPage.class);
                } else {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private int checkRegexes(String email, String username, String password) {
        Matcher emailMatcher = Pattern.compile(EMAIL_REGEX).matcher(email);
        Matcher usernameMatcher = Pattern.compile(USERNAME_REGEX).matcher(username);
        Matcher passwordMatcher = Pattern.compile(PASSWORD_REGEX).matcher(password);

        if (!emailMatcher.matches()) {
            return 1;
        } else if (!usernameMatcher.matches()) {
            return 2;
        } else if (!passwordMatcher.matches()) {
            return 3;
        }

        return 0;
    }

    private void tr(Class c) {
        Intent i = new Intent(this, c);
        startActivity(i);
        this.finish();
    }
}