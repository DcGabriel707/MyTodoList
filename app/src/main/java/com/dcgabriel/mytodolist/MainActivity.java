package com.dcgabriel.mytodolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements TodoListAdapter.OnDeleteClickListener {
    public static final String TAG = "MainActivity";
    public static final int ADD_ENTRY_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_ENTRY_ACTIVITY_REQUEST_CODE = 2;
    private TodoViewModel todoViewModel;
    private RecyclerView todoRecyclerView;
    private TodoListAdapter todoListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        todoListAdapter = new TodoListAdapter(MainActivity.this, this);
        todoRecyclerView.setAdapter(todoListAdapter);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.getAllTodos().observe(this, new Observer<List<TodoEntity>>() {
            @Override
            public void onChanged(List<TodoEntity> todoEntities) {
                todoListAdapter.setNotes(todoEntities);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ENTRY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            final String todo_id = UUID.randomUUID().toString();
            TodoEntity todo = new TodoEntity(todo_id,
                    data.getStringExtra(AddTodoEntry.TODO_ADDED),
                    data.getStringExtra(AddTodoEntry.DESC_ADDED));
            todoViewModel.insert(todo);
            Log.d(TAG, "****************************onActivityResult: " + todo.getId() + " " + todo.getTodo() + " " + todo.getDescription());
            Toast.makeText(this, "saved successfully", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_ENTRY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            TodoEntity todo = new TodoEntity(data.getStringExtra(EditTodoEntry.TODO_ID),
                    data.getStringExtra(EditTodoEntry.UPDATED_TODO),
                    data.getStringExtra(EditTodoEntry.UPDATED_DESC));
            todoViewModel.update(todo);
            Toast.makeText(this, "updated  successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "save failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void addFAB(View view) {
        Intent intent = new Intent(MainActivity.this, AddTodoEntry.class);
        startActivityForResult(intent, ADD_ENTRY_ACTIVITY_REQUEST_CODE);
    }


    @Override
    public void onDeleteClickListener(TodoEntity todo) {
        todoViewModel.delete(todo);
    }
}
