package com.example.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Helper;
import com.example.finalproject.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<Task> tasks;

    public TaskAdapter(Context context, ArrayList<Task> _tasks){
        layoutInflater = LayoutInflater.from(context);
        tasks = _tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.setTaskName(task.getName());

        holder.setCheckBox(task.isDone());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (Helper.tasks == null) return 0;
        return Helper.tasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{
        private CheckBox checkBox;
        private TextView taskName;
        private TaskAdapter adapter;


        public TaskViewHolder(@NonNull View itemView, TaskAdapter taskAdapter) {
            super(itemView);
            adapter = taskAdapter;

            loadComponent();
        }

        private void loadComponent(){
            checkBox = itemView.findViewById(R.id.checkbox);
            taskName = itemView.findViewById(R.id.text_view_task_name);
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
    }
}