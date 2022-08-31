package com.example.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;

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
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.dayOfMonth.setText(listOfDays.get(position).getDayOfMonth());
        holder.llCell.setOnClickListener(v -> onItemListener.onItemClick(position));
    }


    @Override
    public int getItemCount()
    {
        return listOfDays.size();
    }
}
