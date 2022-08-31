package com.example.calendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Helper;
import com.example.finalproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        Calendar c = Calendar.getInstance();
        SimpleDateFormat getMonthYear = new SimpleDateFormat("MMMM yyyy");
        String monthYearCurrent = getMonthYear.format(c.getTime());

        SimpleDateFormat getDay = new SimpleDateFormat("dd");
        String dayCurrent = getDay.format(c.getTime());

        if (listOfDays.get(position).getDayOfMonth().equals(dayCurrent) && monthYearCurrent.equals(CalendarFragment.monthYear)){
            holder.dayOfMonth.setTextColor(Color.parseColor("#FD0A5A"));
        }

        holder.dayOfMonth.setText(listOfDays.get(position).getDayOfMonth());
        holder.llCell.setOnClickListener(v -> onItemListener.onItemClick(position));
    }


    @Override
    public int getItemCount()
    {
        return listOfDays.size();
    }
}
