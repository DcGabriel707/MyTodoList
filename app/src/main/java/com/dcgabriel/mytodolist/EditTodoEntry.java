package com.dcgabriel.mytodolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
    public static final String TODO_NOTIFICATION_ID = "todo_notification_id";
    public static final String UPDATED_IS_COMPLETED = "updated_is_completed";

    private Switch isCompletedSwitch;
    private EditText editTodoEditText;
    private EditText editDescEditText;
    private TextView editTimeTextView;
    private TextView editDateTextView;
    private TextView editNotCompleteTextView;
    private TextView editCompleteTextView;
    private LinearLayout addTimeLinearLayout;
    private LinearLayout addDateLinearLayout;
    private CardView editCloseDateTime;
    private Bundle bundle;
    private String todoId;
    private int todoNotificationId;
    private int todoIsCompleted;
    private int isComplete;

    private LiveData<TodoEntity> todo;
    private EditTodoViewModel todoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo_entry);
        editTodoEditText = findViewById(R.id.editTodoEditText);
        editDescEditText = findViewById(R.id.editDescEditText);
        editTimeTextView = findViewById(R.id.editTimeTextView);
        editDateTextView = findViewById(R.id.editDateTextView);
        isCompletedSwitch = findViewById(R.id.editIsCompletedSwitch);
        addTimeLinearLayout = findViewById(R.id.addTimeLinearLayout);
        addDateLinearLayout = findViewById(R.id.addDateLinearLayout);
        editNotCompleteTextView = findViewById(R.id.editNotCompleteTextView);
        editCompleteTextView = findViewById(R.id.editCompleteTextView);
        editCloseDateTime = findViewById(R.id.editCloseTimeDate);
        editCompleteTextView.setVisibility(View.GONE);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            todoId = bundle.getString("todo_id");
            todoNotificationId = bundle.getInt("todo_notification_id"); //todo fix
            todoIsCompleted = bundle.getInt("todo_is_completed");
        }


        todoViewModel = ViewModelProviders.of(this).get(EditTodoViewModel.class);
        todo = todoViewModel.getTodo(todoId);
        todo.observe(this, new Observer<TodoEntity>() {
            @Override
            public void onChanged(TodoEntity todoEntity) {
                //update views
                editTodoEditText.setText(todoEntity.getTodo());
                editDescEditText.setText(todoEntity.getDescription());
                editTimeTextView.setText(todoEntity.getTime());
                editDateTextView.setText(todoEntity.getDate());
                if (todoEntity.getIsCompleted() == 1) {
                    hideTimeDate();
                    isCompletedSwitch.setChecked(true);
                }
            }
        });
    }

    //when update is complete, send input data back to MainActivity
    public void updateTodo(View view) {

        String updatedTodo = editTodoEditText.getText().toString();
        String updatedDesc = editDescEditText.getText().toString();
        String updatedTime = editTimeTextView.getText().toString();
        String updatedDate = editDateTextView.getText().toString();

        if (updatedTime.equals("") || updatedTime.isEmpty()) //todo: find more efficient way
            updatedTime = "00:00";
        if (updatedDate.equals("") || updatedDate.isEmpty()) //todo: find more efficient way
            updatedDate = "00/00/00";

        Intent resultIntent = new Intent();
        resultIntent.putExtra("todo_id", todoId);
        resultIntent.putExtra(UPDATED_TODO, updatedTodo);
        resultIntent.putExtra(UPDATED_DESC, updatedDesc);
        resultIntent.putExtra(UPDATED_TIME, updatedTime);
        resultIntent.putExtra(UPDATED_DATE, updatedDate);
        resultIntent.putExtra(TODO_NOTIFICATION_ID, todoNotificationId);
        resultIntent.putExtra(UPDATED_IS_COMPLETED, isComplete); //todo change is completed  value

        setResult(RESULT_OK, resultIntent);
        finish();
    }


    public void cancelUpdate(View v) {
        finish();
    }

    //opens the time dialog box
    public void editTime(View view) {
        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(this, editTimeTextView);
        myCalendarDialog.timeDialog();

        Log.d(TAG, "newTime: ");
    }

    //opens the date dialog box
    public void editDate(View view) {
        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(this, editDateTextView);
        myCalendarDialog.dateDialog();
    }

    //when toggle switch is on, hide/unhide views for ux/ui
    public void editIsCompleteSwitch(View view) {
        if (isCompletedSwitch.isChecked()) {
            isComplete = 1;
            editNotCompleteTextView.setVisibility(View.GONE);
            editCompleteTextView.setVisibility(View.VISIBLE);
            editCloseDateTime.setVisibility(View.GONE);
            hideTimeDate();
            Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
        } else {
            isComplete = 0;
            Toast.makeText(this, "Not completed", Toast.LENGTH_SHORT).show();
            addTimeLinearLayout.setVisibility(View.VISIBLE);
            addDateLinearLayout.setVisibility(View.VISIBLE);
            editNotCompleteTextView.setVisibility(View.VISIBLE);
            editCompleteTextView.setVisibility(View.GONE);
        }
    }

    //hides the time/date views for ux
    public void hideTimeDate() {
        addTimeLinearLayout.setVisibility(View.GONE);
        addDateLinearLayout.setVisibility(View.GONE);
        editTimeTextView.setText("00:00");
        editDateTextView.setText("00/00/00");
    }

    //resets the data/time for ux
    public void editCloseDateTimeClick(View view) {
        editTimeTextView.setText("00:00");
        editDateTextView.setText("00/00/00");
    }
}
