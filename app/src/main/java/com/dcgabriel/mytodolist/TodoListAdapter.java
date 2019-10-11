package com.dcgabriel.mytodolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
    public static final String TAG = "TodoListAdapter";
    private final LayoutInflater layoutInflater;
    private Context context;
    private List<TodoEntity> todoList;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClickListener(TodoEntity todo);
    }

    //inflates the layout
    public TodoListAdapter(Context mainActivityContext, OnDeleteClickListener onDeleteClickListener) {
        layoutInflater = LayoutInflater.from(mainActivityContext);
        context = mainActivityContext;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    //inflates the veiw in each entry
    public TodoListAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_entry_layout, parent, false);
        TodoViewHolder viewHolder = new TodoViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoViewHolder holder, int position) {
        if (todoList != null) {
            TodoEntity todo = todoList.get(position);
            Log.d(TAG, "******************************************onBindViewHolder: " + todo.getTodo() + " " + todo.getDescription());
            holder.setData(todo.getTodo(), todo.getDescription(), position);
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

    //sets
    public void setNotes(List<TodoEntity> todos) {
        this.todoList = todos;
        notifyDataSetChanged();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        private TextView todoEntryTextView;
        private TextView todoEntryDescTextView;
        private int position;
        private CardView deleteButton;
        private RelativeLayout updateEntry;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            todoEntryTextView = itemView.findViewById(R.id.todoEntryTextView);
            todoEntryDescTextView = itemView.findViewById(R.id.todoEntryDescTextView);
            deleteButton = itemView.findViewById(R.id.todoEntryDelete);
            updateEntry  = itemView.findViewById(R.id.todoEntryRelativeLayout);
        }

        //sets the data in each entry
        public void setData(String text, String desc, int position) {
            todoEntryTextView.setText(text);
            todoEntryDescTextView.setText(desc);
            this.position = position;
        }

        //sets the listeners
        public void setListeners(){
            updateEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditTodoEntry.class);
                    intent.putExtra("todo_id", todoList.get(position).getId());
                    intent.putExtra("todo_notification_id", todoList.get(position).getNotificationId());
                    intent.putExtra("todo_is_completed", todoList.get(position).getIsCompleted());
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
