package com.dcgabriel.mytodolist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class TodoEntity {

    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    @ColumnInfo(name = "todo")
    private String mTodo;

    public TodoEntity(String id, String todo) {
        this.id = id;
        this.mTodo = todo;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTodo() {
        return mTodo;
    }

}
