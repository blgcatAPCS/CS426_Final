package com.example.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    private final LayoutInflater layoutInflater;
    private final ArrayList<Task> tasks;
    private final Context mContext;

    public interface CallbackInterface{
        void onHandleSelection(int position, Task task);
    }

    private CallbackInterface callbackInterface;

    public TaskAdapter(Context context, ArrayList<Task> _tasks) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        tasks = _tasks;

        callbackInterface = (CallbackInterface) context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.setTaskName(task.getName());
        holder.setDeadline(task.getFormattedDateDeadline());
        holder.setCheckBox(task.isDone());

        holder.taskMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.taskMenu);
            popupMenu.inflate(R.menu.task_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.option_delete:
                        Log.d("menuClicked", "delete item at " + holder.getAdapterPosition());
                        removeItem(holder.getAdapterPosition());
                        return true;

                    case R.id.option_edit:
                        Log.d("menuClicked", "edit selected");
                        editItem(holder.getAdapterPosition());
                        return true;

                    default:
                        return false;
                }
            });
            popupMenu.show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void editItem(int adapterPosition) {
        callbackInterface.onHandleSelection(adapterPosition, tasks.get(adapterPosition));
    }

    private void removeItem(int position) {
        tasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tasks.size());
    }

    @Override
    public void onViewRecycled(@NonNull TaskViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private CheckBox checkBox;
        private TextView taskName;
        private TextView deadline;
        private ImageView taskMenu;
        private final TaskAdapter adapter;


        public TaskViewHolder(@NonNull View itemView, TaskAdapter taskAdapter) {
            super(itemView);
            adapter = taskAdapter;

            loadComponent();

            itemView.setOnCreateContextMenuListener(this);
        }

        private void loadComponent() {
            checkBox = itemView.findViewById(R.id.checkbox);
            taskName = itemView.findViewById(R.id.text_view_task_name);
            deadline = itemView.findViewById(R.id.text_view_select_deadline);
            taskMenu = itemView.findViewById(R.id.image_view_option_menu);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else taskName.setPaintFlags(taskName.getPaintFlags() & ~ Paint.STRIKE_THRU_TEXT_FLAG);
                }
            });
        }

        public boolean getCheckBox() {
            return checkBox.isChecked();
        }

        public void setCheckBox(boolean isDone) {
            this.checkBox.setChecked(isDone);
        }

        public String getTaskName() {
            return taskName.getText().toString();
        }

        public void setTaskName(String taskName) {
            this.taskName.setText(taskName);
        }

        public void setDeadline(String deadline) {
            this.deadline.setText(deadline);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE,R.id.option_edit, Menu.NONE, "Edit task");
            menu.add(Menu.NONE,R.id.option_delete, Menu.NONE, "Delete task");
        }
    }
}