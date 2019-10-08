package com.dcgabriel.mytodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class AddTodoEntry extends AppCompatActivity {

    private EditText newTodoEditText;
    private EditText newDescEditText;
    private TextView newTimeTextView;
    private TextView newDateTextView;

    public static final String TODO_ADDED = "new_todo";
    public static final String DESC_ADDED = "new_desc";

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_entry);
        newTodoEditText = findViewById(R.id.newTodoEditText);
        newDescEditText = findViewById(R.id.newDescEditText);
        newTimeTextView = findViewById(R.id.newTimeTextView);
        newDateTextView = findViewById(R.id.newDateTextView);
    }

    public void save(View v) {
        Intent resultIntent = new Intent();

        if (TextUtils.isEmpty(newTodoEditText.getText())) {
            setResult(RESULT_CANCELED, resultIntent);
        } else {
            String todo = newTodoEditText.getText().toString();
            String desc = newDescEditText.getText().toString();
            resultIntent.putExtra(TODO_ADDED, todo);
            resultIntent.putExtra(DESC_ADDED, desc);
            setResult(RESULT_OK, resultIntent);
        }

        finish();
    }

    public void newTime(View view) {
        final Calendar calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                newTimeTextView.setText(hour + ":" + minute);
            }
        }, mHour, mMinute, false);

        timePickerDialog.show();
    }

    public void newDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mYear = year;
                mMinute = month + 1;
                mDay = day;
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


}
