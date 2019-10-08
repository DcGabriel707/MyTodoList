package com.dcgabriel.mytodolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class EditTodoEntry extends AppCompatActivity {
    public static final String TAG = "EditTodoEntry";
    public static final String TODO_ID = "todo_id";
    public static final String UPDATED_TODO = "updated_todo";
    public static final String UPDATED_DESC = "updated_desc";
    private EditText editTodoEditText;
    private EditText editDescEditText;
    private Bundle bundle;
    private String todoId;
    private String todoString;
    private String todoDesc;
    private LiveData<TodoEntity> todo;
    EditTodoViewModel todoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo_entry);
        editTodoEditText = findViewById(R.id.editTodoEditText);
        editDescEditText = findViewById(R.id.editDescEditText);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            todoId = bundle.getString("todo_id");
        }

        todoViewModel = ViewModelProviders.of(this).get(EditTodoViewModel.class);
        todo = todoViewModel.getTodo(todoId);
        todo.observe(this, new Observer<TodoEntity>() {
            @Override
            public void onChanged(TodoEntity todoEntity) {
                editTodoEditText.setText(todoEntity.getTodo());
                editDescEditText.setText(todoEntity.getDescription());
            }
        });
    }

    public void updateTodo(View view) {
        String updatedTodo = editTodoEditText.getText().toString();
        String updatedDesc = editDescEditText.getText().toString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("todo_id", todoId);
        resultIntent.putExtra("updated_todo", updatedTodo);
        resultIntent.putExtra("updated_desc", updatedDesc);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void cancelUpdate(View v) {
        finish();
    }
}
