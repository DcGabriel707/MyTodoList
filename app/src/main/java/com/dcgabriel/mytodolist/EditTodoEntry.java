package com.dcgabriel.mytodolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class EditTodoEntry extends AppCompatActivity {
    public static final String TAG = "EditTodoEntry";
    public static final String TODO_ID = "todo_id";
    public static final String UPDATED_TODO = "updated_todo";
    public static final String UPDATED_DESC = "updated_desc";
    public static final String UPDATED_TIME = "updated_time";
    public static final String UPDATED_DATE = "updated_date";
    private EditText editTodoEditText;
    private EditText editDescEditText;
    private TextView editTimeTextView;
    private TextView editDateTextView;
    private Bundle bundle;
    private String todoId;
    private LiveData<TodoEntity> todo;
    EditTodoViewModel todoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo_entry);
        editTodoEditText = findViewById(R.id.editTodoEditText);
        editDescEditText = findViewById(R.id.editDescEditText);
        editTimeTextView = findViewById(R.id.editTimeTextView);
        editDateTextView = findViewById(R.id.editDateTextView);

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
                editTimeTextView.setText(todoEntity.getTime());
                editDateTextView.setText(todoEntity.getDate());
            }
        });
    }

    public void updateTodo(View view) {
        String updatedTodo = editTodoEditText.getText().toString();
        String updatedDesc = editDescEditText.getText().toString();
        String updatedTime = editTimeTextView.getText().toString();
        String updatedDate = editDateTextView.getText().toString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("todo_id", todoId);
        resultIntent.putExtra(UPDATED_TODO, updatedTodo);
        resultIntent.putExtra(UPDATED_DESC, updatedDesc);
        resultIntent.putExtra(UPDATED_TIME, updatedTime);
        resultIntent.putExtra(UPDATED_DATE, updatedDate);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void cancelUpdate(View v) {
        finish();
    }

    public void editTime(View view) {
        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(this,editTimeTextView);
        myCalendarDialog.timeDialog();

        Log.d(TAG, "newTime: ");
    }

    public void editDate(View view) {
        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(this, editDateTextView);
        myCalendarDialog.dateDialog();
    }

}
