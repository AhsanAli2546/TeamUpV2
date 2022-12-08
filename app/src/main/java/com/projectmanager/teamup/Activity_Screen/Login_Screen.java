package com.projectmanager.teamup.Activity_Screen;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projectmanager.teamup.Fragments_Screen.ProfilePageFragment;
import com.projectmanager.teamup.R;

public class Login_Screen extends AppCompatActivity {

    // creating variable for edit text, textview,
    // button, progress bar and firebase auth.
    private EditText userNameEdt, passwordEdt;
    private Button loginBtn;
    private TextView newUserTV, ForgetPasswordTV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String CHANNEL_ID = "MY Channel";
    private static final int NOTIFICATION_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        // initializing all our variables.
        userNameEdt = findViewById(R.id.idEdtUserName);
        passwordEdt = findViewById(R.id.idEdtPassword);
        loginBtn = findViewById(R.id.idBtnLogin);
        newUserTV = findViewById(R.id.idTVNewUser);
        ForgetPasswordTV = findViewById(R.id.ForgetPassword);
        mAuth = FirebaseAuth.getInstance();
        loadingPB = findViewById(R.id.idProgressBar);
        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        // adding click listener for our new user tv.
        newUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line opening a login activity.
                Intent i = new Intent(Login_Screen.this, Register_Screen.class);
                startActivity(i);
                finish();
            }
        });
        if (sharedPreferences.contains("email")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
            // adding on click listener for our login button.
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // hiding our progress bar.
                    loadingPB.setVisibility(View.VISIBLE);
                    // getting data from our edit text on below line.
                    String email = userNameEdt.getText().toString();
                    String password = passwordEdt.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.commit();
                    // on below line validating the text input.
                    if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                        userNameEdt.setError("Email is Necessary");
                        passwordEdt.setError("Password is Necessary");
                        loadingPB.setVisibility(View.GONE);

                        Toast.makeText(Login_Screen.this, "Please enter your credentials..", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // on below line we are calling a sign in method and passing email and password to it.
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // on below line we are checking if the task is success or not.
                            if (task.isSuccessful()) {
                                // on below line we are hiding our progress bar.
                                loadingPB.setVisibility(View.GONE);
                                //Toast.makeText(Login_Screen.this, "Login Successful..", Toast.LENGTH_SHORT).show();

//                                Fragment  f = new ProfilePageFragment();
//                                FragmentManager fm= getSupportFragmentManager();
//                                FragmentTransaction ft = fm.beginTransaction().add(R.id.container,f);
//                                ft.commit();
//                                // on below line we are opening our mainactivity.
                                Intent i = new Intent(Login_Screen.this, MainActivity.class);
                                startActivity(i);
                                finish();
                                createNotificationChannel();
                            } else {
                                // hiding our progress bar and displaying a toast message.
                                loadingPB.setVisibility(View.GONE);

                                Toast.makeText(Login_Screen.this, "Please enter valid user credentials..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        ForgetPasswordTV.setOnClickListener(view -> {

            showRecoverPasswordDialog();
            loadingPB.setVisibility(View.GONE);
        });
    }

    private void CcreateNotificationChannel() {

    }

    private void showRecoverPasswordDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        ConstraintLayout linearLayout = new ConstraintLayout(this);
        final EditText emailet = new EditText(this);
        emailet.setHint("Email");
        loadingPB.setVisibility(View.GONE);
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = emailet.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    emailet.setError("Email Enter is Necessary");
                }else {
                    beginRecovery(email);
                    loadingPB.setVisibility(View.GONE);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password

                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(Login_Screen.this, "Done sent", Toast.LENGTH_LONG).show();
                    ConstraintLayout linearLayout = new ConstraintLayout(Login_Screen.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login_Screen.this);
                    final TextView textView = new TextView(Login_Screen.this);
                    linearLayout.addView(textView);
                    textView.setText("Check You Inbox Or Spam Folder\nEmail: ");
                    textView.setTextColor(Color.RED);
                    builder.setView(linearLayout);



                } else {
                    Toast.makeText(Login_Screen.this, "Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(Login_Screen.this, "Error Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // in on start method checking if
//        // the user is already sign in.
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            // if the user is not null then we are
//            // opening a main activity on below line.
//            Intent i = new Intent(Login_Screen.this, MainActivity.class);
//            startActivity(i);
//            this.finish();
//        }
//    }
private void createNotificationChannel() {
//    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.notification)
//            .setContentTitle("Login")
//            .setContentText("Login Successfully...!!")
//            .setPriority(NotificationCompat.PRIORITY_HIGH);
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Login")
            .setContentText("Login Successfully...!!")
            .setPriority(NotificationCompat.PRIORITY_HIGH);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
    notificationManager.notify(NOTIFICATION_ID, builder.build());

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        CharSequence name = getString(R.string.channel_name);
//        String description = getString(R.string.channel_description);
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//        channel.enableLights(true);
//        channel.enableVibration(true);
//        channel.setDescription(description);
//        // Register the channel with the system; you can't change the importance
//        // or other notification behaviors after this
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);
//    }

}
}