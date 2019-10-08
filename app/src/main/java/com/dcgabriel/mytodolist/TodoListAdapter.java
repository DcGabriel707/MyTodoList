package com.dcgabriel.mytodolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context context;
    private List<TodoEntity> todoList;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClickListener(TodoEntity todo);
    }

    public TodoListAdapter(Context mainActivityContext, OnDeleteClickListener onDeleteClickListener) {
        layoutInflater = LayoutInflater.from(mainActivityContext);
        context = mainActivityContext;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public TodoListAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_entry_layout, parent, false);
        TodoViewHolder viewHolder = new TodoViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoViewHolder holder, int position) {
        if (todoList != null) {
            TodoEntity todo = todoList.get(position);
            holder.setData(todo.getTodo(), position);
            holder.setListeners();
        } else {
            holder.todoEntryTextView.setText("Empty");
        }
    }

    @Override
    public int getItemCount() {
        if (todoList != null) {
            return todoList.size();
        } else
            return 0;
    }

    public void setNotes(List<TodoEntity> todos) {
        this.todoList = todos;
        notifyDataSetChanged();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        private TextView todoEntryTextView;
        private int position;
        private CardView deleteButton;
        private RelativeLayout updateEntry;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            todoEntryTextView = itemView.findViewById(R.id.todoEntryTextView);
            deleteButton = itemView.findViewById(R.id.todoEntryDelete);
            updateEntry  = itemView.findViewById(R.id.todoEntryRelativeLayout);
        }

        public void setData(String text, int position) {
            todoEntryTextView.setText(text);
            this.position = position;
        }
        public void setListeners(){
            updateEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditTodoEntry.class);
                    intent.putExtra("todo_id", todoList.get(position).getId());
                    ((Activity)context).startActivityForResult(intent, MainActivity.EDIT_ENTRY_ACTIVITY_REQUEST_CODE);
                }
            });


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClickListener(todoList.get(position));
                    }
                }
            });
        }
    }
}
