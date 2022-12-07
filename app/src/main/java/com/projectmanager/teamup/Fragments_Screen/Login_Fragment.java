package com.projectmanager.teamup.Fragments_Screen;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projectmanager.teamup.Activity_Screen.Login_Screen;
import com.projectmanager.teamup.Activity_Screen.MainActivity;
import com.projectmanager.teamup.Activity_Screen.Register_Screen;
import com.projectmanager.teamup.R;


public class Login_Fragment extends Fragment {

    private EditText userNameEdt, passwordEdt;
    private Button loginBtn;
    private TextView newUserTV, ForgetPasswordTV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public Login_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_, container, false);
        mAuth = FirebaseAuth.getInstance();
        userNameEdt = view.findViewById(R.id.idEdtUserName);
        passwordEdt = view.findViewById(R.id.idEdtPassword);
        loginBtn = view.findViewById(R.id.idBtnLogin);
        newUserTV = view.findViewById(R.id.idTVNewUser);
        ForgetPasswordTV = view.findViewById(R.id.ForgetPassword);

        loadingPB = view.findViewById(R.id.idProgressBar);
        newUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line opening a login activity.
//                Intent i = new Intent(getContext(), Register_Screen.class);
//                startActivity(i);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // hiding our progress bar.
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from our edit text on below line.
                String email = userNameEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                editor.putString("email", email);
                editor.putString("password", password);
                editor.commit();
                // on below line validating the text input.
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Please enter your credentials..", Toast.LENGTH_SHORT).show();
                    return;
                }
                // on below line we are calling a sign in method and passing email and password to it.
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // on below line we are checking if the task is success or not.
                        if (task.isSuccessful()) {
                            // on below line we are hiding our progress bar.
                            try {
                                loadingPB.setVisibility(View.GONE);

                                Toast.makeText(getContext(), "Login Successful..", Toast.LENGTH_SHORT).show();
                                // on below line we are opening our mainactivity.

//                                Fragment Profile = new ProfilePageFragment();
//                                FragmentManager fm = getActivity().getSupportFragmentManager();
//                                FragmentTransaction ft = fm.beginTransaction();
//                                ft.replace(R.id.conta,Profile);
//                                ft.commit();

                            }catch (Exception e){
                                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            // hiding our progress bar and displaying a toast message.
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Please enter valid user credentials..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        ForgetPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
                loadingPB.setVisibility(View.VISIBLE);
            }
        });

//        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
        return view;
    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Recover Password");
        ConstraintLayout linearLayout = new ConstraintLayout(getContext());
        final EditText emailet = new EditText(getContext());
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailet.getText().toString().trim();
                beginRecovery(email);
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
                    Toast.makeText(getContext(), "Done sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}