package com.example.todo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Todo.class},version = 1)

public abstract class TodoRoomDatabase extends RoomDatabase {

    public abstract TodoDoa todoDoa();

    private static volatile TodoRoomDatabase INSTANCE;

     public static TodoRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TodoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoRoomDatabase.class, "Todo_Database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
