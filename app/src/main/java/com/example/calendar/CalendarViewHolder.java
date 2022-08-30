package com.example.calendar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder
{
    public final TextView dayOfMonth;
    public TextView numOfTasks;
    public LinearLayout llCell;

    public CalendarViewHolder(@NonNull View itemView)
    {
        super(itemView);

        dayOfMonth = itemView.findViewById(R.id.text_view_day_of_month);
        numOfTasks = itemView.findViewById(R.id.text_view_num_of_work);
        llCell = itemView.findViewById(R.id.llCell);
    }

}