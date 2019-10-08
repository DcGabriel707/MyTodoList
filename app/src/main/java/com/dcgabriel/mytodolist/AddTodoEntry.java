package com.dcgabriel.mytodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class AddTodoEntry extends AppCompatActivity {

    private EditText newTodoEditText;
    public static final String TODO_ADDED = "new_todo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_entry);
        newTodoEditText = findViewById(R.id.newTodoEditText);
    }

    public void save(View v) {
        Intent resultIntent = new Intent();

        if (TextUtils.isEmpty(newTodoEditText.getText())) {
            setResult(RESULT_CANCELED, resultIntent);
        } else {
            String todo = newTodoEditText.getText().toString();
            resultIntent.putExtra(TODO_ADDED, todo);
            setResult(RESULT_OK, resultIntent);
        }

        finish();
    }
}
