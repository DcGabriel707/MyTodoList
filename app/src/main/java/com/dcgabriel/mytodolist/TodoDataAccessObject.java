package com.dcgabriel.mytodolist;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TodoDataAccessObject {

    @Insert
    void insert(TodoEntity todo);

    @Query("SELECT * FROM todos")
    LiveData<List<TodoEntity>> getAllTodos();

    @Query("SELECT * FROM todos WHERE id=:todoId")
    LiveData<TodoEntity> getTodo(String todoId);

    @Update
    void update(TodoEntity todo);

    @Delete
    int delete(TodoEntity todo);
}
