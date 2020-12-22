package com.example.todo.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_table")
public class Todo {


    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "todo_title")
    private String todo_title;

    @ColumnInfo(name = "todo_desc")
    private String todo_desc;

    @ColumnInfo(name = "completed")
    private boolean completed;

    public Todo(String todo_title, String todo_desc, boolean completed) {
        this.todo_title = todo_title;
        this.todo_desc = todo_desc;
        this.completed = completed;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTodo_title() {
        return todo_title;
    }

    public void setTodo_title(String todo_title) {
        this.todo_title = todo_title;
    }

    public String getTodo_desc() {
        return todo_desc;
    }

    public void setTodo_desc(String todo_desc) {
        this.todo_desc = todo_desc;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "\nTodo{" +
                "uid=" + uid +
                ", todo_title='" + todo_title + '\'' +
                ", todo_desc='" + todo_desc + '\'' +
                ", completed=" + completed +
                '}';
    }
}
