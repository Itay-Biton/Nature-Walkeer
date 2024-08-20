package com.myapp.naturewalker.utils;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.myapp.naturewalker.callbacks.GeneralCallback;

public class UserManager {
    public static UserManager instance = null;

    private FirebaseAuth mAuth;

    public UserManager() {
        if (instance != null)
            return;
        instance = this;
        instance.mAuth = FirebaseAuth.getInstance();
    }

    static public String getUserId() {
        return instance.mAuth.getCurrentUser().getUid();
    }

    static public String getUserEmail() {
        return instance.mAuth.getCurrentUser().getEmail();
    }

    static public void changePassword(String newPassword, GeneralCallback generalCallback) {
        instance.mAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                generalCallback.success(null);
            else
                generalCallback.failed(null);
        });
    }

    static public void createAccount(Activity activity, String email, String password, GeneralCallback generalCallback) {
        instance.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful())
                        generalCallback.success(null);
                    else {
                        // User creation failed
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            // The email is already in use
                            generalCallback.failed(Status.EMAIL_IN_USE);
                        } catch (Exception e) {
                            // Other errors
                            generalCallback.failed(null);
                        }
                    }
                });
    }

    static public void signIn(Activity activity, String email, String password, GeneralCallback generalCallback) {
        instance.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in succeeded
                        generalCallback.success(null);
                    } else {
                        // Sign-in failed
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            // The email does not exist
                            generalCallback.failed(Status.EMAIL_NOT_EXIST);
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            // The email exists but the password is wrong
                            generalCallback.failed(Status.INCORRECT_PASSWORD);
                        } catch (Exception e) {
                            // Other errors
                            generalCallback.failed(null);
                        }
                    }
                });
    }

    static public void signOut() {
        instance.mAuth.signOut();
    }
}
