package com.projectmanager.teamup.Fragments_Screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projectmanager.teamup.Activity_Screen.Login_Screen;
import com.projectmanager.teamup.Activity_Screen.MainActivity;
import com.projectmanager.teamup.R;


public class ProfilePageFragment extends Fragment {

    private Button Logout, MapOffice;
    private FirebaseAuth mAuth;
    private ImageView DP;
    private TextView MyEmail;
    private static final String CHANNEL_ID = "MY Channel";
    private static final int NOTIFICATION_ID = 100;
//    String Email;

    public ProfilePageFragment() {


        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String Email;
            Email = firebaseUser.getEmail();
            MyEmail.setText(Email);
            Log.d("Profile: Email: ", Email);


        } else {

            mAuth.signOut();

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);
        DP = view.findViewById(R.id.picId);
        Logout = view.findViewById(R.id.LogoutBtn);
        MyEmail = view.findViewById(R.id.CatchEmail);
        mAuth = FirebaseAuth.getInstance();
        MapOffice = view.findViewById(R.id.mapIdBtn);
        MapOffice.setText("Office Location");
        MapOffice.setTextColor(Color.BLACK);
        Logout.setText("Logout");
        Logout.setTextColor(Color.BLACK);
        MapOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Office.class));
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(getActivity(),Login_Screen.class));
                createNotificationChannel();


            }
        });

//        Bundle bundle = getArguments();

        DP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = new BlankFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container, f).commit();
            }
        });

        return view;
    }

    private void createNotificationChannel() {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Logout")
                .setContentText("Logout Successfully...!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

        notificationManager.notify(NOTIFICATION_ID, builder.build());



    }


}