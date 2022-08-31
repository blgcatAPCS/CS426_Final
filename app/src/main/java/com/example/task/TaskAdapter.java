package com.example.task;

import android.content.Context;
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

import com.example.Priority.Priority;
import com.example.finalproject.Helper;
import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final LayoutInflater layoutInflater;
    private final ArrayList<Task> tasks;
    private final Context mContext;
    private final CallbackInterface callbackInterface;

    public TaskAdapter(Context context, ArrayList<Task> _tasks) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        tasks = _tasks;

        callbackInterface = (CallbackInterface) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        Log.d("onBindView", task.toString());

        holder.setTaskName(task.getName());
<<<<<<< Updated upstream
        holder.setDeadline(Helper.dateToString(task.getDeadline()));
=======

        holder.setDeadline(Helper.dateToString(task.getDeadline()));

>>>>>>> Stashed changes
        holder.setCheckBox(task.isDone());
        holder.setPriority(task.getPriority());

        holder.taskMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.taskMenu);
            popupMenu.inflate(R.menu.task_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
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
        changeTextBasedCheckBox(holder.getCheckBox(), holder.taskName, position);
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
    public int getItemCount() {
        return tasks.size();
    }

    void changeTextBasedCheckBox(boolean isChecked, TextView taskName, int position) {
        if (isChecked) {
            taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else taskName.setPaintFlags(taskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        tasks.get(position).setDone(isChecked);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void notifyAdapterWholeDataSetChanged() {
        Collections.sort(tasks, Comparator.comparing(Task::isDone).thenComparing(Task::getDeadline).thenComparing(Task::getPriority, Comparator.reverseOrder()));
        notifyItemRangeChanged(0, tasks.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void notifyAdapterItemMoved(int oldPosition, Task newTask) {
        notifyItemChanged(oldPosition);
        tasks.remove(oldPosition);
        int newPosition = Collections.binarySearch(tasks,
                newTask,
                Comparator.comparing(Task::isDone).thenComparing(Task::getDeadline).thenComparing(Task::getPriority, Comparator.reverseOrder()));
        if (newPosition < 0) {
            newPosition = -newPosition - 1;
        }
        tasks.add(newPosition, newTask);
        notifyItemMoved(oldPosition, newPosition);
    }

    public interface CallbackInterface {
        void onHandleSelection(int position, Task task);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TaskAdapter adapter;
        private CheckBox checkBox;
        private TextView taskName;
        private TextView deadline;
        private ImageView taskMenu;
        private ImageView priorityImage;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public TaskViewHolder(@NonNull View itemView, TaskAdapter taskAdapter) {
            super(itemView);
            adapter = taskAdapter;

            loadComponent();

            itemView.setOnCreateContextMenuListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void loadComponent() {
            checkBox = itemView.findViewById(R.id.checkbox);
            taskName = itemView.findViewById(R.id.text_view_task_name);
            deadline = itemView.findViewById(R.id.text_view_select_deadline);
            taskMenu = itemView.findViewById(R.id.image_view_option_menu);

            checkBox.setOnClickListener(v -> {
                int position = this.getAdapterPosition();
                changeTextBasedCheckBox(checkBox.isChecked(), taskName, position);
                Task newTask = tasks.get(position);
                notifyAdapterItemMoved(position, newTask);
            });

            priorityImage = itemView.findViewById(R.id.image_view_priority);
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

        public void setPriority(Priority priority) {
            if (priority == null) priority = Priority.HIGH;
            Log.d("setPriority", String.valueOf(Helper.getPriorityIconSrc(priority)));
            Log.d("setPriority", String.valueOf(priorityImage.getId()));
            priorityImage.setImageResource(Helper.getPriorityIconSrc(priority));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.option_edit, Menu.NONE, "Edit task");
            menu.add(Menu.NONE, R.id.option_delete, Menu.NONE, "Delete task");
        }
    }
}