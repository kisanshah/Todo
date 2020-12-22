package com.example.todo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDoa {

    @Insert
    void insertItem(Todo todo);

    @Query("SELECT * FROM todo_table")
    LiveData<List<Todo>> getAllTasks();

    @Query("SELECT * FROM todo_table WHERE completed  = :value ")
    LiveData<List<Todo>> getTaskByBoolean(int value);


    @Update
    void updateTodo(Todo todo);

    @Delete
    void deleteTodo(Todo todo);

    @Query("SELECT * FROM todo_table WHERE uid = :uid ")
    Todo getTodoById(int uid);

}
