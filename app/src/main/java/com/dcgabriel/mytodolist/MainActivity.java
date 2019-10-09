package com.dcgabriel.mytodolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
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
    public static final String TODO_TITLE = "todo_title";
    public static final String TODO_DESC = "todo_date";

    private int temp = 1;


    private TodoViewModel todoViewModel;
    private RecyclerView todoRecyclerView;
    private TodoListAdapter todoListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleRecyclerView();
    }

    private void handleRecyclerView() {
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
                    data.getStringExtra(AddTodoEntry.DESC_ADDED),
                    data.getStringExtra(AddTodoEntry.TIME_ADDED),
                    data.getStringExtra(AddTodoEntry.DATE_ADDED),
                    todoViewModel.generateNotificationId(),
                    0);

            setNotification(todo);
            todoViewModel.insert(todo);
            Log.d(TAG, "****************************onActivityResult: " + todo.getId() + " " + todo.getTodo() + " " + todo.getDescription() + " " + todo.getDate() + " " + todo.getTodo());
            Toast.makeText(this, "saved successfully", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_ENTRY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            TodoEntity todo = new TodoEntity(data.getStringExtra(EditTodoEntry.TODO_ID),
                    data.getStringExtra(EditTodoEntry.UPDATED_TODO),
                    data.getStringExtra(EditTodoEntry.UPDATED_DESC),
                    data.getStringExtra(EditTodoEntry.UPDATED_TIME),
                    data.getStringExtra(EditTodoEntry.UPDATED_DATE),
                    Integer.parseInt(data.getStringExtra(EditTodoEntry.TODO_NOTIFICATION_ID)),
                    Integer.parseInt(data.getStringExtra(EditTodoEntry.UPDATED_IS_COMPLETED))
                    );
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

    private void setNotification(TodoEntity todo){
        Log.d(TAG, "000000000000000000000setNotification: title=" + todo.getTodo() + " description=" + todo.getDescription() );

        long scheduledDateTime = todoViewModel.convertToTimeInMillisecond(todo.getDate(), todo.getTime());

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        myIntent.putExtra(TODO_TITLE, todo.getTodo());
        myIntent.putExtra(TODO_DESC, todo.getDescription());
        PendingIntent pendingIntent =  PendingIntent.getBroadcast(this,todo.getNotificationId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        manager.set(AlarmManager.RTC, scheduledDateTime+5000, pendingIntent );

    }



}
