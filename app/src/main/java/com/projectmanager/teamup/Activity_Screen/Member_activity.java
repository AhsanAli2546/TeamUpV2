package com.projectmanager.teamup.Activity_Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projectmanager.teamup.Fragments_Screen.CreateProjectFragment;
import com.projectmanager.teamup.R;

public class Member_activity extends AppCompatActivity {
Button btn;
EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        btn = findViewById(R.id.btn);
        editText = findViewById(R.id.Name);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String Name = "";
                Bundle bundle = new Bundle();
                Name = editText.getText().toString();
                Fragment fragment = new CreateProjectFragment();
                bundle.putString("arg",Name);
                fragment.setArguments(bundle);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Frame_2,fragment);
                ft.commit();


            }
        });
    }
    public void FragmentMethod() {
        Toast.makeText(Member_activity.this, "Method called From Fragment", Toast.LENGTH_LONG).show();
    }
}