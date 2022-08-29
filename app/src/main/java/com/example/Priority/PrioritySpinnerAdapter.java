package com.example.Priority;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;

public class PrioritySpinnerAdapter extends BaseAdapter {
    private Context context;
    private int[] priorityIcon;
    private String[] priorityText;
    private LayoutInflater layoutInflater;

    public PrioritySpinnerAdapter(Context context, int[] priorityIcon, String[] priorityText) {
        setContext(context);
        setPriorityIcon(priorityIcon);
        setPriorityText(priorityText);
        setLayoutInflater(LayoutInflater.from(context));
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int[] getPriorityIcon() {
        return priorityIcon;
    }

    public void setPriorityIcon(int[] priorityIcon) {
        this.priorityIcon = priorityIcon;
    }

    public String[] getPriorityText() {
        return priorityText;
    }

    public void setPriorityText(String[] priorityText) {
        this.priorityText = priorityText;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return priorityIcon.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.priority_spinner_adapter, null);
        ImageView icon = convertView.findViewById(R.id.image_view_priority);
        TextView textViewPriority = convertView.findViewById(R.id.text_view_priority);
        icon.setImageResource(priorityIcon[position]);
        textViewPriority.setText(priorityText[position]);
        return convertView;
    }
}
