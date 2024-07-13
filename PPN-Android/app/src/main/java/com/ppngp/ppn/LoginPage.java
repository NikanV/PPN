package com.ppngp.ppn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseUser;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {

    TextInputEditText usernameEt;
    TextInputEditText pwdEt;
    CheckBox rememberCb;
    TextView registerLink;
    MaterialButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        setElements();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Objects.requireNonNull(usernameEt.getText()).toString();
                String password = Objects.requireNonNull(pwdEt.getText()).toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Empty username or password!", Toast.LENGTH_SHORT).show();
                } else {
                    loginBtn.setClickable(false);
                    ParseUser.logInInBackground(username, password, (user, e) -> {
                        if (user != null) {
                            Toast.makeText(LoginPage.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                            usernameEt.setText("");
                            pwdEt.setText("");
                            tr(HomePage.class);
                        } else {
                            Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        loginBtn.setClickable(true);
                    });
                }
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEt.setText("");
                pwdEt.setText("");
                tr(RegisterPage.class);
            }
        });
    }

    private void setElements() {
        usernameEt = findViewById(R.id.usr_et);
        pwdEt = findViewById(R.id.pwd_et);
        rememberCb = findViewById(R.id.rem_cb);
        registerLink = findViewById(R.id.register_link);
        loginBtn = findViewById(R.id.login_btn);
    }

    private void tr(Class c) {
        Intent i = new Intent(this, c);
        startActivity(i);
        this.finish();
    }
}