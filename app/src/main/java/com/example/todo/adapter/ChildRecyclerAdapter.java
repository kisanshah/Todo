package com.example.todo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddTodo;
import com.example.todo.R;
import com.example.todo.database.Todo;

import java.util.List;

public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildRecyclerAdapter.TodoHolder> {

    Context context;
    List<Todo> list;
    MainRecyclerAdapter.RecyclerViewInterface recyclerViewInterface;

    public ChildRecyclerAdapter(Context context, List<Todo> list, MainRecyclerAdapter.RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TodoHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoHolder holder, int position) {

        holder.task.setText(list.get(position).getTodo_title());
        holder.task_done.setChecked(list.get(position).isCompleted());
        holder.task_desc.setText(list.get(position).getTodo_desc());

        if (!list.get(position).getTodo_desc().isEmpty()) {
            holder.task_desc.setVisibility(View.VISIBLE);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddTodo.class);
                intent.putExtra("uid", list.get(position).getUid());
                v.getContext().startActivity(intent);
            }
        });
        holder.task_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Todo todo = list.get(position);
                todo.setCompleted(isChecked);
                recyclerViewInterface.updateTodo(todo);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TodoHolder extends RecyclerView.ViewHolder {
        TextView task, task_desc;
        CheckBox task_done;
        LinearLayout linearLayout;

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task);
            task_desc = itemView.findViewById(R.id.task_desc);
            task_done = itemView.findViewById(R.id.task_done);
            linearLayout = itemView.findViewById(R.id.container);
        }
    }
}
