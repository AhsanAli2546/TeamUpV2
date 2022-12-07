package com.projectmanager.teamup.Activity_Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.projectmanager.teamup.Fragments_Screen.Login_Fragment;
import com.projectmanager.teamup.R;

public class HelpFragmentWithActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_fragment_with);
        Fragment fragment = new Login_Fragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.HelpContainer,fragment);
        ft.commit();
    }
}