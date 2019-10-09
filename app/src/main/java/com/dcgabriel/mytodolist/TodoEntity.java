package com.dcgabriel.mytodolist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class TodoEntity {

    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    @ColumnInfo(name = "todo")
    private String mTodo;



    @ColumnInfo(name = "desc")
    private String description;

    @ColumnInfo(name = "deadline_time")
    private String time;

    @ColumnInfo(name = "deadline_date")
    private String date;

    @NonNull
    @ColumnInfo(name = "notification_id")
    private int notificationId;

    @ColumnInfo(name = "is_completed")
    private int isCompleted;

    @Ignore
    public TodoEntity(String id, String todo, String description) {
        this.id = id;
        this.mTodo = todo;
        this.description = description;
    }

    public TodoEntity(String id, String todo, String description, String time, String date, int notificationId, int isCompleted) {
        this.id = id;
        this.mTodo = todo;
        this.description = description;
        this.time = time;
        this.date = date;
        this.notificationId = notificationId;
        this.isCompleted = isCompleted;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTodo() {
        return mTodo;
    }

    public String getDescription() {return description;}

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public int getIsCompleted() {
        return isCompleted;
    }


}
