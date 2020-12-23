package com.example.todo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.todo.database.Todo;
import com.example.todo.database.TodoViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddTodo extends AppCompatActivity {

    TodoViewModel todoViewModel;
    EditText task_title, task_desc;
    SwitchMaterial task_done;
    Toolbar toolbar;
    Spinner spinner;
    EditText date, reminder;
    boolean update;
    Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);


        spinner = findViewById(R.id.spinner);
        task_title = findViewById(R.id.title);
        task_desc = findViewById(R.id.desc);
        task_done = findViewById(R.id.task_done);
        date = findViewById(R.id.date);
        reminder = findViewById(R.id.reminder);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            update = true;
            int uid = Integer.parseInt(intent.get("uid").toString());
            todo = todoViewModel.getTodoById(uid);
            task_title.setText(todo.getTodo_title());
            task_desc.setText(todo.getTodo_desc());
        }

        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build();

        MaterialDatePicker.Builder<Long> dateBuilder = MaterialDatePicker.Builder.datePicker();

        MaterialDatePicker<Long> datePicker = dateBuilder.build();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                materialTimePicker.show(getSupportFragmentManager(), "");
                date.setText(datePicker.getHeaderText());
            }
        });

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int hour = materialTimePicker.getHour();
                String min = String.valueOf(materialTimePicker.getMinute());
                if (hour < 10) {
                    date.setText("0" + hour + ":" + min + "" + date.getText());
                } else {
                    date.setText(hour + ":" + min + " " + date.getText());
                }

            }
        });
        List<String> list = new ArrayList<>();
        list.add("My Task");
        list.add("Second List");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (update) {
                    todo.setTodo_title(task_title.getText().toString());
                    todo.setTodo_desc(task_desc.getText().toString());
                    updateTodo(todo);
                } else {
                    saveTodo();
                }
                return true;
            case R.id.delete:
                 if (update) {
                    Snackbar.make(task_desc, "Delete todo?", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            todoViewModel.deleteTodo(todo);
                            finish();
                        }
                    }).show();
                } else {
                    finish();
                }
                return true;
            default:
                return false;
        }
    }

    private void saveTodo() {
        if (!task_title.getText().toString().isEmpty()) {
            Todo todo = new Todo(task_title.getText().toString(), task_desc.getText().toString(), false);
            todoViewModel.insertTodo(todo);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Task title is required", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTodo(Todo todo) {
        todoViewModel.updateTodo(todo);
        finish();
    }
}
