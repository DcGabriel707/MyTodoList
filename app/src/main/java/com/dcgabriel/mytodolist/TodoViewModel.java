package com.dcgabriel.mytodolist;

import android.app.Application;
import android.os.AsyncTask;
import android.os.SystemClock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TodoViewModel extends AndroidViewModel {
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

    public int generateNotificationId(){
        int id = (int) SystemClock.uptimeMillis();
        return id;
    }

    public long convertToTimeInMillisecond(String date, String time){
        //Specifying the pattern of input date and time
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateString = "22-03-2017 11:18:32";
        Calendar calendar  = Calendar.getInstance();
        try{
            //formatting the dateString to convert it into a Date
            Date mDate = sdf.parse(dateString);
            calendar.setTime(mDate);
        }catch(ParseException e){
            e.printStackTrace();
        }

        return calendar.getTimeInMillis();

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

    private class UpdateAsyncTask extends  AsyncTask<TodoEntity, Void, Void> {
        TodoDataAccessObject todoDao;
        public UpdateAsyncTask (TodoDataAccessObject todoDao) {
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
