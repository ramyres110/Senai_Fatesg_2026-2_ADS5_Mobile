package com.ramyres.appsampleview;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText editDescription = findViewById(R.id.edit_description);
        Button buttonSave = findViewById(R.id.button_save);

        buttonSave.setOnClickListener(v -> {
            String description = editDescription.getText().toString().trim();
            if (!description.isEmpty()) {
                TaskRepository.getInstance().addTask(new Task(description));
                finish();
            } else {
                Toast.makeText(AddTaskActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}