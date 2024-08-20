package com.myapp.naturewalker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.bumptech.glide.Glide;
import com.myapp.naturewalker.R;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.UserManager;

public class Activity_Profile extends AppCompatActivity {
    private AppCompatImageButton BTN_back;
    private TextView TXT_title, TXT_name, TXT_email;
    private EditText TXT_image_url, TXT_new_password, TXT_confirm_password;
    private Button BTN_save_image, BTN_change_password;
    private ImageView IMG_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViews();
        setListeners();
        updateUI();
    }

    private void findViews() {
        BTN_back = findViewById(R.id.BTN_back);

        TXT_title = findViewById(R.id.TXT_title);
        TXT_title.setText(getString(R.string.profile));

        IMG_profile = findViewById(R.id.IMG_profile);
        TXT_image_url = findViewById(R.id.TXT_image_url);

        BTN_save_image = findViewById(R.id.BTN_save_image);

        TXT_name = findViewById(R.id.TXT_name);
        TXT_email = findViewById(R.id.TXT_email);

        TXT_new_password = findViewById(R.id.TXT_new_password);
        TXT_confirm_password = findViewById(R.id.TXT_confirm_password);

        BTN_change_password = findViewById(R.id.BTN_change_password);
    }

    private void setListeners() {
        BTN_back.setOnClickListener(v -> gotoDashboard());

        BTN_save_image.setOnClickListener(v -> {
            changeImage();
        });

        BTN_change_password.setOnClickListener(v -> {
            tryChangePassword();
        });
    }

    private void gotoDashboard() {
        Intent i = new Intent(getApplicationContext(), Activity_Dashboard.class);
        startActivity(i);
        finish();
    }

    private void changeImage() {
        String imgUrl = TXT_image_url.getText().toString();
        User.getInstance().setImgURL(imgUrl);
        DataManager.updateUserImage(imgUrl);
        updateUI();
    }

    private void tryChangePassword() {
        String password1 = TXT_new_password.getText().toString();
        String password2 = TXT_confirm_password.getText().toString();

        if (!password1.equals(password2)) {
            Toast.makeText(Activity_Profile.this, "Passwords don't Match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password1.isEmpty()) {
            Toast.makeText(Activity_Profile.this, "Passwords empty", Toast.LENGTH_SHORT).show();
            return;
        }
        UserManager.changePassword(password1, new GeneralCallback() {
            @Override
            public void success(Object o) {
                Toast.makeText(Activity_Profile.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failed(Object o) {
                Toast.makeText(Activity_Profile.this, "Failed to change password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        User user = User.getInstance();
        TXT_email.setText(user.getEmail());
        TXT_name.setText(user.getEmail().split("@")[0]);
        if (user.getImgURL() != null)
            TXT_image_url.setText(user.getImgURL());
        Glide.with(this)
                .load(user.getImgURL())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .fitCenter()
                .into(IMG_profile);
    }
}