package com.dcgabriel.mytodolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class EditTodoViewModel extends AndroidViewModel {
    private TodoDataAccessObject todoDao;
    private TodoDatabase todoDatabase;

    public EditTodoViewModel(@NonNull Application application) {
        super(application);
        todoDatabase = TodoDatabase.getDatabase(application);
        todoDao = todoDatabase.todoDao();
    }

    public LiveData<TodoEntity> getTodo(String todoId) {
        return todoDao.getTodo(todoId);
    }
}
