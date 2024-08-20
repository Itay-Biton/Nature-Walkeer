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
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.LocalData;
import com.myapp.naturewalker.utils.NetworkUtil;
import com.myapp.naturewalker.utils.Status;
import com.myapp.naturewalker.utils.UserManager;

public class Activity_SignUp extends AppCompatActivity {
    private EditText email_edit_text, password1_edit_text, password2_edit_text;
    private Button BTN_login;
    private AppCompatButton BTN_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();
        setListeners();
    }

    private void findViews() {
        email_edit_text = findViewById(R.id.email_edit_text);
        password1_edit_text = findViewById(R.id.password1_edit_text);
        password2_edit_text = findViewById(R.id.password2_edit_text);
        BTN_login = findViewById(R.id.BTN_login);
        BTN_sign_up = findViewById(R.id.BTN_sign_up);
    }

    private void setListeners() {
        BTN_login.setOnClickListener(v-> {
            gotoLogin();
        });
        BTN_sign_up.setOnClickListener(v-> {
            trySignup();
        });
    }

    private void gotoLogin() {
        Intent i = new Intent(getApplicationContext(), Activity_Login.class);
        startActivity(i);
        finish();
    }

    private void gotoDashboard() {
        Intent i = new Intent(getApplicationContext(), Activity_Dashboard.class);
        startActivity(i);
        finish();
    }

    private void trySignup() {
        String email = email_edit_text.getText().toString();
        String password1 = password1_edit_text.getText().toString();
        String password2 = password2_edit_text.getText().toString();

        if (!password1.equals(password2)) {// TODO: better alert for passwords not the same
            Toast.makeText(Activity_SignUp.this, "Passwords don't Match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (NetworkUtil.isInternetAvailable(this)) {
            UserManager.createAccount(this, email, password1, new GeneralCallback() {
                @Override
                public void success(Object o) {
                    DataManager.addUser();
                    gotoLoading();
                }

                @Override
                public void failed(Object o) {
                    if (o == Status.EMAIL_IN_USE) { // TODO: better alert for email in use
                        Toast.makeText(Activity_SignUp.this, "EMAIL IN USE", Toast.LENGTH_SHORT).show();
                    }
                    else { // TODO: better alert for signup failed
                        Toast.makeText(Activity_SignUp.this, "signup failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void gotoLoading() {
        Intent i = new Intent(getApplicationContext(), Activity_Loading.class);
        startActivity(i);
        finish();
    }
}