package com.dcgabriel.mytodolist;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TodoViewModel extends AndroidViewModel {
    private static final String TAG = "TodoViewModel";
    private TodoDataAccessObject todoDAO;
    private TodoDatabase todoDatabase;
    private LiveData<List<TodoEntity>> allTodos;

    public TodoViewModel(@NonNull Application application) {
        super(application);

        todoDatabase = TodoDatabase.getDatabase(application);
        todoDAO = todoDatabase.todoDao();
        allTodos = todoDAO.getAllTodos();
    }

    public void insert(TodoEntity todo) {
        new InsertAsyncTask(todoDAO).execute(todo);
    }

    LiveData<List<TodoEntity>> getAllTodos() {
        return allTodos;
    }

    public void update(TodoEntity todo) {
        new UpdateAsyncTask(todoDAO).execute(todo);
    }

    public void delete(TodoEntity todo) {
        new DeleteAsyncTask(todoDAO).execute(todo);
    }

    public int generateNotificationId() {
        int id = (int) SystemClock.uptimeMillis();
        return id;
    }

    public long convertToTimeInMillisecond(String date, String time) {
        //Specifying the pattern of input date and time
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String dateString = date + " " + time;
        Calendar calendar = Calendar.getInstance();

        Log.d(TAG, "convertToTimeInMillisecond: " + dateString);
        Date mDate = null;
        try {
            Log.d(TAG, "33333333333333333333333333333333333convertToTimeInMillisecond: " + dateString);
            mDate = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(mDate);

        Log.d(TAG, "convertToTimeInMillisecond: " + date + " " + time + calendar.getTimeInMillis());
        return calendar.getTimeInMillis();

    }

    public void saveCache(Context context, TodoEntity todo) {
        Gson gson = new Gson();
        ArrayList<TodoEntity> todoList = new ArrayList<>();
        todoList.add(todo);

        String testJson = gson.toJson(todoList);
        Log.d(TAG, "-----------saveMyData: " + testJson);
        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(), "") + "cachefile.txt"));
            out.writeObject(testJson);

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        retrieveCache(context);
    }


    private String retrieveCache(Context context) {
        String fileContent = "";
        try {
            String currentLine;
            BufferedReader bufferedReader;
            FileInputStream fileInputStream = new FileInputStream(context.getCacheDir() + "cachefile.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));

            while ((currentLine = bufferedReader.readLine()) != null) {
                fileContent += currentLine + '\n';
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();

            fileContent = null;
        }
        Log.d(TAG, "-----------saveMyData: " +  fileContent);
        return fileContent;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }

    private class InsertAsyncTask extends AsyncTask<TodoEntity, Void, Void> {
        TodoDataAccessObject todoDAO;

        public InsertAsyncTask(TodoDataAccessObject todoDAO) {
            this.todoDAO = todoDAO;
        }

        @Override
        protected Void doInBackground(TodoEntity... todos) {
            todoDAO.insert(todos[0]);
            return null;
        }
    }

    private class UpdateAsyncTask extends AsyncTask<TodoEntity, Void, Void> {
        TodoDataAccessObject todoDao;

        public UpdateAsyncTask(TodoDataAccessObject todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(TodoEntity... todos) {
            todoDao.update(todos[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<TodoEntity, Void, Void> {
        TodoDataAccessObject todoDao;

        public DeleteAsyncTask(TodoDataAccessObject todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(TodoEntity... todos) {
            todoDao.delete(todos[0]);
            return null;
        }
    }
}
