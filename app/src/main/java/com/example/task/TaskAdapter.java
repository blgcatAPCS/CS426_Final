package com.example.task;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<Task> tasks;

    public TaskAdapter(Context context, ArrayList<Task> _tasks) {
        layoutInflater = LayoutInflater.from(context);
        tasks = _tasks;
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
        holder.setDeadline(task.getFormattedDate());

        holder.setCheckBox(task.isDone());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView taskName;
        private TextView deadline;
        private TaskAdapter adapter;


        public TaskViewHolder(@NonNull View itemView, TaskAdapter taskAdapter) {
            super(itemView);
            adapter = taskAdapter;

            loadComponent();
        }

        private void loadComponent() {
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else {
                        taskName.setPaintFlags(taskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                }
            });

            taskName = itemView.findViewById(R.id.text_view_task_name);
            deadline = itemView.findViewById(R.id.text_view_select_deadline);
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
    }
}