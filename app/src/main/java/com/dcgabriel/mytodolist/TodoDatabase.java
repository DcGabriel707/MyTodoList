package com.dcgabriel.mytodolist;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = TodoEntity.class, version = 1)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDataAccessObject todoDao();

    private static volatile TodoDatabase todoRoomInstance;

    static TodoDatabase getDatabase(final Context context) {
        if (todoRoomInstance == null) {
            synchronized (TodoDatabase.class) {
                if (todoRoomInstance == null) {
                    todoRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            TodoDatabase.class, "todo_database")
                            .build();
                }
            }
        }
        return todoRoomInstance;
    }


}
