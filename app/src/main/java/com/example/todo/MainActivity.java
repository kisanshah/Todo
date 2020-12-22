package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.adapter.MainRecyclerAdapter;
import com.example.todo.database.Todo;
import com.example.todo.database.TodoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MY_LOGS";

    FloatingActionButton add_fab;
    TodoViewModel todoViewModel;
    RecyclerView recyclerView;
    MainRecyclerAdapter mainAdapter;
    MainRecyclerAdapter.RecyclerViewInterface recyclerViewInterface;
    List<Todo> incomplete;
    List<Todo> complete;
    List<Section> sectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        add_fab = findViewById(R.id.add_fab);
        recyclerView = findViewById(R.id.recyclerView);

        complete = new ArrayList<>();
        incomplete = new ArrayList<>();

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.getAllTasks().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                String section1 = "My Tasks";
                String section2 = "Completed";
                complete.clear();
                incomplete.clear();
                for (Todo item : todos) {
                    if (item.isCompleted()) {
                        complete.add(item);
                    } else {
                        incomplete.add(item);
                    }
                }
                sectionList = new ArrayList<>();
                sectionList.add(new Section(section1, incomplete));
                if (complete.size() > 0) {
                    sectionList.add(new Section(section2, complete));
                }
                mainAdapter = new MainRecyclerAdapter(getBaseContext(), sectionList, recyclerViewInterface);
                recyclerView.setAdapter(mainAdapter);
            }
        });

        recyclerViewInterface = new MainRecyclerAdapter.RecyclerViewInterface() {
            @Override
            public void updateTodo(Todo todo) {
                todoViewModel.updateTodo(todo);
            }

            @Override
            public void deleteTodo(Todo todo) {
                todoViewModel.deleteTodo(todo);
            }

            @Override
            public void insertTodo(Todo todo) {
                todoViewModel.insertTodo(todo);
            }

            @Override
            public void swap(Todo todo1, Todo todo2) {
                Todo swap = todoViewModel.getTodoById(todo1.getUid());
                Todo swap2 = todoViewModel.getTodoById(todo2.getUid());

                swap.setTodo_title(todo2.getTodo_title());
                swap.setTodo_desc(todo2.getTodo_desc());

                swap2.setTodo_title(todo1.getTodo_title());
                swap2.setTodo_desc(todo1.getTodo_desc());

                todoViewModel.updateTodo(swap);
                todoViewModel.updateTodo(swap2);
            }

        };

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getApplicationContext().deleteDatabase("Todo_Database");
                startActivity(new Intent(MainActivity.this, AddTodo.class));
            }
        });
    }
}