package com.example.todo.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    TodoRoomDatabase todoRoomDatabase;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        todoRoomDatabase = TodoRoomDatabase.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Todo>> getAllTasks() {
        return todoRoomDatabase.todoDoa().getAllTasks();
    }


    public void insertTodo(Todo todo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                todoRoomDatabase.todoDoa().insertItem(todo);
            }
        }).start();
    }

    public void updateTodo(Todo todo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                todoRoomDatabase.todoDoa().updateTodo(todo);
            }
        }).start();
    }

    private static volatile Todo todo = null;

    public Todo getTodoById(int uid)  {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                todo = todoRoomDatabase.todoDoa().getTodoById(uid);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return todo;
    }

    public void deleteTodo(Todo todo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                todoRoomDatabase.todoDoa().deleteTodo(todo);
            }
        }).start();
    }
}
