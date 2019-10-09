package com.dcgabriel.mytodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddTodoEntry extends AppCompatActivity {
    private static final String TAG = "AddTodoEntry";
    private EditText newTodoEditText;
    private EditText newDescEditText;
    private TextView newTimeTextView;
    private TextView newDateTextView;

    private Intent resultIntent = new Intent();

    public static final String TODO_ADDED = "new_todo";
    public static final String DESC_ADDED = "new_desc";
    public static final String TIME_ADDED = "new_time";
    public static final String DATE_ADDED = "new_date";

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_entry);
        newTodoEditText = findViewById(R.id.newTodoEditText);
        newDescEditText = findViewById(R.id.newDescEditText);
        newTimeTextView = findViewById(R.id.newTimeTextView);
        newDateTextView = findViewById(R.id.newDateTextView);

        resultIntent = new Intent();
    }

    public void save(View v) {
        if (TextUtils.isEmpty(newTodoEditText.getText())) {
            Toast.makeText(this, "Enter todo title", Toast.LENGTH_SHORT).show();
        } else {
            String todo = newTodoEditText.getText().toString();
            String desc = newDescEditText.getText().toString();
            String time = newTimeTextView.getText().toString();
            String date = newDateTextView.getText().toString();

            if (time.equals("Select time")) //todo: find more efficient way
                time = "00:00";
            if (date.equals("Select date")) //todo: find more efficient way
                date = "00/00/00";

            resultIntent.putExtra(TODO_ADDED, todo);
            resultIntent.putExtra(DESC_ADDED, desc);
            resultIntent.putExtra(TIME_ADDED, time);
            resultIntent.putExtra(DATE_ADDED, date);
            setResult(RESULT_OK, resultIntent);

            finish();
        }
    }

    public void newTime(View view) {
        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(this, newTimeTextView);
        myCalendarDialog.timeDialog();

        Log.d(TAG, "newTime: ");
    }

    public void newDate(View view) {
        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(this, newDateTextView);
        myCalendarDialog.dateDialog();
    }


}
