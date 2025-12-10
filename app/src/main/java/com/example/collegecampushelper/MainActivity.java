package com.example.collegecampushelper;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btNotes, btProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("CollegeCampusHelper");
        setContentView(R.layout.activity_main);

        btNotes = findViewById(R.id.btNotes);
        btProfile = findViewById(R.id.btProfile);

        btNotes.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NotesActivity.class)));

        btProfile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }
}