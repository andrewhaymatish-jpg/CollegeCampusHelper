package com.example.collegecampushelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private EditText etNote;
    private Button btAdd, btClearAll, btBackHome;
    private ListView notesListView;

    private ArrayList<String> notesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private SharedPreferences prefs;
    private static final String PREF_NAME = "notesPref";
    private static final String KEY_NOTES_JSON = "notes_json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        etNote = findViewById(R.id.etNote);
        btAdd = findViewById(R.id.btAdd);
        btClearAll = findViewById(R.id.btClearAll);
        btBackHome = findViewById(R.id.btBackHome);
        notesListView = findViewById(R.id.notesListView);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(adapter);

        loadNotes();

        btAdd.setOnClickListener(v -> {
            String text = etNote.getText().toString().trim();
            if (!text.isEmpty()) {
                notesList.add(0, text);
                adapter.notifyDataSetChanged();
                saveNotes();
                etNote.setText("");
            } else {
                etNote.setError("Please enter a note.");
            }
        });

        btClearAll.setOnClickListener(v -> {
            if (notesList.isEmpty()) return;
            new AlertDialog.Builder(NotesActivity.this)
                    .setTitle("Clear All Notes")
                    .setMessage("Are you sure you want to delete all notes?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        notesList.clear();
                        adapter.notifyDataSetChanged();
                        saveNotes();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        notesListView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String note = notesList.get(position);
            new AlertDialog.Builder(NotesActivity.this)
                    .setTitle("Delete note")
                    .setMessage("Delete this note?\n\n" + note)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        notesList.remove(position);
                        adapter.notifyDataSetChanged();
                        saveNotes();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });

        btBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(NotesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveNotes() {
        JSONArray jsonArray = new JSONArray(notesList);
        prefs.edit().putString(KEY_NOTES_JSON, jsonArray.toString()).apply();
    }

    private void loadNotes() {
        String json = prefs.getString(KEY_NOTES_JSON, "[]");

        notesList.clear();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                notesList.add(arr.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
