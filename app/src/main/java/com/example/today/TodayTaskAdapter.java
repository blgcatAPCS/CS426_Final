package com.example.today;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Priority.Priority;
import com.example.finalproject.Helper;
import com.example.finalproject.R;
import com.example.task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class TodayTaskAdapter extends RecyclerView.Adapter<TodayTaskAdapter.TodayTaskViewHolder> {
    private final LayoutInflater layoutInflater;
    private final ArrayList<TodayTask> todayTasks;
    private final Context context;

    public TodayTaskAdapter(ArrayList<TodayTask> todayTasks, Context context) {
        this.todayTasks = todayTasks;
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public TodayTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.task_today_adapter, parent, false);
        return new TodayTaskViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayTaskViewHolder holder, int position) {
        TodayTask todayTask = todayTasks.get(position);

        holder.setTaskName(todayTask.getTask().getName());
        holder.setDeadline(todayTask.getTask().getDeadline());
        holder.setCheckBox(todayTask.getTask().isDone());
        holder.setPriority(todayTask.getTask().getPriority());
        changeTextBasedCheckBox(holder.isChecked(), holder.taskName, position);

        holder.setProject(todayTask.getProjectName());
    }

    @Override
    public int getItemCount() {
        return todayTasks.size();
    }

    public class TodayTaskViewHolder extends RecyclerView.ViewHolder{
        private CheckBox checkBox;
        private TextView taskName;
        private TextView deadline;
        private ImageView priority;
        private TextView project;

        private final TodayTaskAdapter adapter;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public TodayTaskViewHolder(@NonNull View itemView, TodayTaskAdapter taskAdapter) {
            super(itemView);
            adapter = taskAdapter;

            loadComponent();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void loadComponent() {
            checkBox = itemView.findViewById(R.id.checkbox_today);
            checkBox.setOnClickListener(v -> {
                int position = this.getAdapterPosition();
                changeTextBasedCheckBox(checkBox.isChecked(), taskName, position);
                TodayTask newTask = todayTasks.get(position);
                notifyAdapterItemMoved(position, newTask);
            });

            taskName = itemView.findViewById(R.id.text_view_task_name);
            deadline = itemView.findViewById(R.id.text_view_select_deadline);
            priority = itemView.findViewById(R.id.image_view_priority);
            project = itemView.findViewById(R.id.text_view_select_project);
        }

        public boolean isChecked() {
            return checkBox.isChecked();
        }

        public void setCheckBox(boolean checked) {
            checkBox.setChecked(checked);
        }

        public String getTaskName() {
            return taskName.getText().toString();
        }

        public void setTaskName(String taskName) {
            this.taskName.setText(taskName);
        }

        public Date getDeadline() {
            return Helper.stringToDate(deadline.getText().toString());
        }

        public void setDeadline(Date deadline) {
            this.deadline.setText(Helper.dateToString(deadline));
        }

        public ImageView getPriority() {
            return priority;
        }

        public void setPriority(Priority priority) {
            this.priority.setImageResource(Helper.getPriorityIconSrc(priority));
        }

        public String getProject() {
            return project.getText().toString();
        }

        public void setProject(String project) {
            this.project.setText(project);
        }

        public TodayTaskAdapter getAdapter() {
            return adapter;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void notifyAdapterItemMoved(int position, TodayTask newTask) {
        notifyItemChanged(position);
        todayTasks.remove(position);
        int newPosition = Collections.binarySearch(todayTasks,
                newTask,
                Comparator.comparing(TodayTask::isDone).thenComparing(TodayTask::getDeadline).thenComparing(TodayTask::getPriority, Comparator.reverseOrder()));
        if (newPosition<0){
            newPosition = -newPosition-1;
        }
        todayTasks.add(newPosition, newTask);
        notifyItemMoved(position, newPosition);
    }

    private void changeTextBasedCheckBox(boolean checked, TextView taskName, int position) {
        if (checked){
            taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else taskName.setPaintFlags(taskName.getPaintFlags() & ~ Paint.STRIKE_THRU_TEXT_FLAG);
        todayTasks.get(position).setDone(checked);
    }
}
