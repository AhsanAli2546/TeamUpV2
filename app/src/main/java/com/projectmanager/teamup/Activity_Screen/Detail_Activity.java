package com.projectmanager.teamup.Activity_Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.projectmanager.teamup.R;

public class Detail_Activity extends AppCompatActivity {
    private TextView Title, Description;
    private String title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Title = findViewById(R.id.idTVProjectName);
        Description = findViewById(R.id.idTVDescription);
        getData();
        setData();

    }

    private void setData() {
        if (getIntent().hasExtra("TVTitle") && getIntent().hasExtra("Description")) {
            title = getIntent().getStringExtra("TVTitle");
            description = getIntent().getStringExtra("Description");
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        Title.setText(title);
        Description.setText(description);
    }
}