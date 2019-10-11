package com.dcgabriel.mytodolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
    public static final String TODO_IS_COMPLETE = "todo_is_complete";

    private int temp = 1;

    private TodoViewModel todoViewModel;
    private RecyclerView todoRecyclerView;
    private TodoListAdapter todoListAdapter;
    private BroadcastReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleRecyclerView();
        setUpNetworkUpdates();
    }

    private void handleRecyclerView() {
        todoRecyclerView = findViewById(R.id.todoRecyclerView);

        todoListAdapter = new TodoListAdapter(MainActivity.this, this); //initialize list adapter
        todoRecyclerView.setAdapter(todoListAdapter); //set adapter to recyclerview
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class); //set viewmodel
        todoViewModel.getAllTodos().observe(this, new Observer<List<TodoEntity>>() {
            @Override
            public void onChanged(List<TodoEntity> todoEntities) { //gets all the todo entries and populates the list
                todoListAdapter.setNotes(todoEntities);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //when the addEntryActivity finishes successfully
        if (requestCode == ADD_ENTRY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            final String todo_id = UUID.randomUUID().toString(); //generate unique if for each entry

            //creates a todoEntity object using the data from addEntryActivity
            TodoEntity todo = new TodoEntity(todo_id,
                    data.getStringExtra(AddTodoEntry.TODO_ADDED),
                    data.getStringExtra(AddTodoEntry.DESC_ADDED),
                    data.getStringExtra(AddTodoEntry.TIME_ADDED),
                    data.getStringExtra(AddTodoEntry.DATE_ADDED),
                    todoViewModel.generateNotificationId(),
                    data.getIntExtra(AddTodoEntry.IS_COMPLETE, -999));

            //if the user set a due date
            if ((!data.getStringExtra(AddTodoEntry.TIME_ADDED).equals("00:00") && !data.getStringExtra(AddTodoEntry.DATE_ADDED).equals("00/00/00"))) {
                Log.d(TAG, "onActivityResult: " + data.getIntExtra(AddTodoEntry.IS_COMPLETE, -999));

                //if the user set the todo item as done/completed
                if (data.getIntExtra(AddTodoEntry.IS_COMPLETE, -999) == 1) {
                    cancelNotification(todo); //cancel the notification be
                    Toast.makeText(this, "Notification canceled", Toast.LENGTH_SHORT).show();
                } else
                    setNotification(todo);
            }
            todoViewModel.saveCache(MainActivity.this, todo); //saves the inserted todoObject into cache
            todoViewModel.insert(todo); //inserts the new todoObject
            Toast.makeText(this, "saved successfully", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_ENTRY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) { //when the editEntryActivity finishes
            TodoEntity todo = new TodoEntity(data.getStringExtra(EditTodoEntry.TODO_ID),
                    data.getStringExtra(EditTodoEntry.UPDATED_TODO),
                    data.getStringExtra(EditTodoEntry.UPDATED_DESC),
                    data.getStringExtra(EditTodoEntry.UPDATED_TIME),
                    data.getStringExtra(EditTodoEntry.UPDATED_DATE),
                    data.getIntExtra(EditTodoEntry.TODO_NOTIFICATION_ID, -999),
                    data.getIntExtra(EditTodoEntry.UPDATED_IS_COMPLETED, -999)
            );
            Log.d(TAG, "onActivityResult: " + data.getIntExtra(EditTodoEntry.UPDATED_IS_COMPLETED, -999));
            //if the the user set the entry as done/completed, cancel the notification
            if (data.getIntExtra(EditTodoEntry.UPDATED_IS_COMPLETED, -999) == 1) {
                cancelNotification(todo);
            }
            //when the user updates the due date, update the notification
            else if (!data.getStringExtra(EditTodoEntry.UPDATED_TIME).equals("00:00") && !data.getStringExtra(EditTodoEntry.UPDATED_DATE).equals("00/00/00")) {
                setNotification(todo);
            } else //todo: fix if logic
                cancelNotification(todo);

            //update the database
            todoViewModel.update(todo);
            Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver); // pauses the networkReceiver so
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpNetworkUpdates();
    }

    public void addFAB(View view) {
        //starts the AddTodoEntry activity.
        Intent intent = new Intent(MainActivity.this, AddTodoEntry.class);
        startActivityForResult(intent, ADD_ENTRY_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onDeleteClickListener(TodoEntity todo) {
        todoViewModel.delete(todo);
    }

    private void setNotification(TodoEntity todo) {
        Log.d(TAG, "000000000000000000000setNotification: title=" + todo.getTodo() + " description=" + todo.getDescription());

        long scheduledDateTime = todoViewModel.convertToTimeInMillisecond(todo.getDate(), todo.getTime());

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        myIntent.putExtra(TODO_TITLE, todo.getTodo());
        myIntent.putExtra(TODO_DESC, todo.getDescription());
        myIntent.putExtra(TODO_IS_COMPLETE, todo.getIsCompleted());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, todo.getNotificationId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.set(AlarmManager.RTC, scheduledDateTime, pendingIntent);
    }

    private void cancelNotification(TodoEntity todo) {
        Log.d(TAG, "000000000000000000000cancelNotification: title=" + todo.getTodo() + " description=" + todo.getDescription());

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        myIntent.putExtra(TODO_TITLE, todo.getTodo());
        myIntent.putExtra(TODO_DESC, todo.getDescription());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, todo.getNotificationId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntent.cancel();
        manager.cancel(pendingIntent);
    }

    private void setUpNetworkUpdates(){
        networkReceiver = new NetworkChangeReceiver();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }



}
