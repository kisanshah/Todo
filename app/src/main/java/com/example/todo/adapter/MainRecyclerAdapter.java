package com.example.todo.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.Section;
import com.example.todo.database.Todo;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    List<Section> sectionList;
    RecyclerViewInterface recyclerViewInterface;
    Context context;

    public MainRecyclerAdapter(Context context, List<Section> sectionList, RecyclerViewInterface recyclerViewInterface) {
        this.sectionList = sectionList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
        return new MainViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String sectionTitle = sectionList.get(position).getSectionName();
        List<Todo> items = sectionList.get(position).getSectionItems();
        holder.sectionTitle.setText(sectionTitle);
        ChildRecyclerAdapter childRecyclerAdapter = new ChildRecyclerAdapter(context, items,recyclerViewInterface);
        holder.itemRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.itemRecyclerView.setAdapter(childRecyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            int from = -1;
            int to = -1;
            Todo fromTodo, toTodo;

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (from != -1 && to != -1 && from != to) {
                    recyclerViewInterface.swap(fromTodo, toTodo);
                }
                from = to = -1;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                from = viewHolder.getAdapterPosition();
                to = target.getAdapterPosition();
                fromTodo = items.get(from);
                toTodo = items.get(to);
                Collections.swap(items, from, to);
                recyclerView.getAdapter().notifyItemMoved(from, to);
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightActionIcon(R.drawable.done)
                        .addSwipeLeftActionIcon(R.drawable.delete)
                        .addSwipeRightBackgroundColor(context.getColor(R.color.purple_500))
                        .addSwipeLeftBackgroundColor(context.getColor(R.color.red))
                        .create().decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
                Todo item1 = items.get(viewHolder.getAdapterPosition());
                Todo item2 = items.get(target.getAdapterPosition());
                recyclerViewInterface.swap(item1, item2);
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                Todo currentItem = sectionList.get(position).getSectionItems().get(index);
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        recyclerViewInterface.deleteTodo(currentItem);
                        childRecyclerAdapter.notifyDataSetChanged();
                        Snackbar.make(holder.itemRecyclerView, "Todo deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerViewInterface.insertTodo(currentItem);
                                childRecyclerAdapter.notifyItemInserted(position);
                            }
                        }).show();
                        break;
                    case ItemTouchHelper.RIGHT:
                        currentItem.setCompleted(!currentItem.isCompleted());
                        recyclerViewInterface.updateTodo(currentItem);
                        childRecyclerAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(holder.itemRecyclerView);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }


    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView sectionTitle;
        RecyclerView itemRecyclerView;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.sectionTitle);
            itemRecyclerView = itemView.findViewById(R.id.itemRecyclerView);
        }
    }

    public interface RecyclerViewInterface {
        void swap(Todo todo1, Todo todo2);

        void updateTodo(Todo todo);

        void deleteTodo(Todo todo);

        void insertTodo(Todo todo);
    }
}

