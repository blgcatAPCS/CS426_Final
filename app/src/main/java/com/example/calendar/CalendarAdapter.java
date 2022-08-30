package com.example.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.example.finalproject.R;
import com.example.task.Task;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<Dates> listOfDays;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<Dates> daysOfMonth, OnItemListener onItemListener)
    {
        this.listOfDays = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.date_item, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        Dates date = listOfDays.get(position);
        holder.dayOfMonth.setText(listOfDays.get(position).getDayOfMonth());

        String numTasks = "";
        ArrayList<Task> listOfTask = listOfDays.get(position).getListOfTasks();
        if (listOfTask != null && listOfTask.size() > 0){
            if (listOfTask.size() == 1)
                numTasks = "1 task";
            else
                numTasks = listOfTask.size() + " tasks";
        }

        holder.numOfTasks.setText(numTasks);

        holder.llCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onItemClick(date, position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listOfDays.size();
    }
}
