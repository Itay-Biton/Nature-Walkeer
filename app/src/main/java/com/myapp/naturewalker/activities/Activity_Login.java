package com.myapp.naturewalker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.myapp.naturewalker.R;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.LocalData;
import com.myapp.naturewalker.utils.NetworkUtil;
import com.myapp.naturewalker.utils.Status;
import com.myapp.naturewalker.utils.UserManager;

public class Activity_Login extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button BTN_login;
    private AppCompatButton BTN_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        setListeners();
    }

    private void findViews() {
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        BTN_login = findViewById(R.id.BTN_login);
        BTN_sign_up = findViewById(R.id.BTN_sign_up);
    }

    private void setListeners() {
        BTN_login.setOnClickListener(v-> {
            tryLogin();
        });
        BTN_sign_up.setOnClickListener(v-> {
            gotoSignUp();
        });
    }

    private void gotoLoading() {
        Intent i = new Intent(getApplicationContext(), Activity_Loading.class);
        startActivity(i);
        finish();
    }

    private void gotoSignUp() {
        Intent i = new Intent(getApplicationContext(), Activity_SignUp.class);
        startActivity(i);
        finish();
    }

    private void tryLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (NetworkUtil.isInternetAvailable(this)) {
            UserManager.signIn(this, email, password, new GeneralCallback() {
                @Override
                public void success(Object o) {
                    gotoLoading();
                }
                @Override
                public void failed(Object o) {
                    if (o == Status.EMAIL_NOT_EXIST) { // TODO: better alert for wrong email
                        Toast.makeText(Activity_Login.this, "EMAIL NOT EXIST", Toast.LENGTH_SHORT).show();
                        gotoSignUp();
                    }
                    else if (o == Status.INCORRECT_PASSWORD) { // TODO: better alert for wrong password
                        Toast.makeText(Activity_Login.this, "INCORRECT PASSWORD", Toast.LENGTH_SHORT).show();
                    }
                    else
                        gotoSignUp();
                }
            });
        }
    }
}