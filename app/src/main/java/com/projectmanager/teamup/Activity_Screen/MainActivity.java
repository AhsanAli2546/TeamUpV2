package com.projectmanager.teamup.Activity_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projectmanager.teamup.Fragments_Screen.CreateProjectFragment;
import com.projectmanager.teamup.Fragments_Screen.HomePageFragment;
import com.projectmanager.teamup.Fragments_Screen.ProfilePageFragment;
import com.projectmanager.teamup.Fragments_Screen.SettingPageFragment;
import com.projectmanager.teamup.R;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.idLogOut){
            Toast.makeText(this, "Logout....!!", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.TopMenu);
        bottomNavigationView = findViewById(R.id.navibottom);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //Button with ClickOnListener editor.clear() and editor.commit(); Intent Activity
        editor.clear();
        editor.commit();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("TeamUp");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitle("Let's Work With Team");
        toolbar.setSubtitleTextColor(Color.WHITE);


//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                int id = item.getItemId();
//                if(id==R.id.home){
//                    loadFragment(new HomePageFragment(),true);
//                }else if(id == R.id.CreateProject){
//                    loadFragment(new CreateProjectFragment(),false);
//                }else if(id == R.id.setting){
//                    loadFragment(new SettingPageFragment(),false);
//                }
//                else {
//                    loadFragment(new ProfilePageFragment(),false);
//                }
//                return true;
//            }
//        });
//        bottomNavigationView.setSelectedItemId(R.id.home);
//
//
//    }
//
//    public void loadFragment(Fragment fragment, boolean flag){
//
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//
//
//        if(flag==false){
//            ft.add(R.id.container,fragment);
//        }
//        else {
//            ft.replace(R.id.container,fragment);
//        }
//        ft.commit();
//
//    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        // adding a click listener for option selected on below line.
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.idLogOut:
//                // displaying a toast message on user logged out inside on click.
//                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
//                // on below line we are signing out our user.
//
//                mAuth.signOut();
//                // on below line we are opening our login activity.
//                Intent i = new Intent(MainActivity.this, Login_Screen.class);
//                startActivity(i);
//                this.finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


//        @Override
//        public boolean onCreateOptionsMenu (Menu menu){
//            // on below line we are inflating our menu
//            // file for displaying our menu options.
//            getMenuInflater().inflate(R.menu.top_menu, menu);
//            return true;
//        }
        FragmentTransaction fragmentTransaction;

        bottomNavigationView.setOnItemSelectedListener(MainActivity.this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    Fragment Home = new HomePageFragment();
    Fragment Profile = new ProfilePageFragment();
    Fragment Create = new CreateProjectFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Home).commit();
                return true;

            case R.id.Profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Profile).commit();

                return true;

            case R.id.CreateProject:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Create).commit();
                return true;
        }
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.getEmail();
            Log.d("Your Email: ", firebaseUser.getEmail());


        } else {
            Intent i = new Intent(getApplicationContext(), Login_Screen.class);
            startActivity(i);
            finish();
        }
    }
}