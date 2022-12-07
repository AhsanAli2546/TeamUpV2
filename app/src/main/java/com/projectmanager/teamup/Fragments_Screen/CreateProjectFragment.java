package com.projectmanager.teamup.Fragments_Screen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projectmanager.teamup.Modal.CardModal;
import com.projectmanager.teamup.R;

public class CreateProjectFragment extends Fragment {
    //    Button btnCalender, BtnSubmit, AddBtnMember;
//    TextView ViewDateTV;
    String Next;
    private Button TimeStartBtn, btnContinue;
    private EditText editTextProjectName, editTextDescription;
    private TextView idTVTime, idTVName;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    public String ProjectName, Description, Name;
    private FirebaseFirestore db;
    private static final String CHANNEL_ID = "MY Channel";
    private static final int NOTIFICATION_ID = 100;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CreateProjectFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_project, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        idTVName = view.findViewById(R.id.idTVName);
        TimeStartBtn = view.findViewById(R.id.calenderBtn);
        idTVTime = view.findViewById(R.id.idTVDueDate);
        btnContinue = view.findViewById(R.id.btnContinue);
        editTextDescription = view.findViewById(R.id.idETDescription);
        editTextProjectName = view.findViewById(R.id.idETProjectName);
        progressBar = view.findViewById(R.id.loading);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectName = editTextProjectName.getText().toString();
                Description = editTextDescription.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(ProjectName) && TextUtils.isEmpty(Description)) {
                    editTextProjectName.setError("Please fill the Full Field");
                    editTextDescription.setError("Please fill the Full Field");
                    progressBar.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(ProjectName)) {
                    progressBar.setVisibility(View.VISIBLE);
                    editTextProjectName.setError("Project Name is Compulsory");
                    progressBar.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(Description)) {
                    progressBar.setVisibility(View.VISIBLE);
                    editTextDescription.setError("Description is Compulsory");
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    addMyDataToFirestore(ProjectName, Description);
                    editTextProjectName.setText("");
                    editTextDescription.setText("");
                    createNotificationChannel();
                }

            }
        });

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String Name = firebaseUser.getEmail();
            String[] Email = Name.split("@");
            idTVName.setText(Email[0]);
        }

//        btnCalender = view.findViewById(R.id.calenderBtn);
//        ViewDateTV = view.findViewById(R.id.idTVDueDate);
//        BtnSubmit = view.findViewById(R.id.BtnSubmit);
//        AddBtnMember = view.findViewById(R.id.AddBtnMember);
//
//
//        btnCalender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Calendar c = Calendar.getInstance();
//
//
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        // on below line we are passing context.
//                        getContext(),
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                // on below line we are setting date to our text view.
//                                int dayMonth = monthOfYear + 1;
//                                Next = dayOfMonth + "/" + dayMonth + "/" + year;
//                                ViewDateTV.setText(Next);
//
//
//                            }
//                        },
//                        // on below line we are passing year,
//                        // month and day for selected date in our date picker.
//                        year, month, day);
//                // at last we are calling show to
//                // display our date picker dialog.
//                datePickerDialog.show();
//
//            }
//        });
//        BtnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Fragment f = new BlankFragment();
//                    FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
//                    fm.replace(R.id.container,f).commit();
//
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), "Please Fill The Full Foam", Toast.LENGTH_SHORT).show();
//                    e.fillInStackTrace();
//                }
//
//            }
//        });
//        AddBtnMember.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//
//                    Fragment f = new SearchFragment();
//                    FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
//                    fm.replace(R.id.container,f).commit();
//
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        return view;
    }

    private void addMyDataToFirestore(String projectName, String description) {
        CollectionReference dbTeamUp = db.collection("TeamUp");
        CardModal cardModal = new CardModal(projectName, description);

        dbTeamUp.add(cardModal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "Data Is Enter to Firebase is Successfully!!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Fragment f = new HomePageFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container, f).commit();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Fail to add Data \n" + e, Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle("Insert")
                    .setContentText("Insert Data Successfully...!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }


    }


}