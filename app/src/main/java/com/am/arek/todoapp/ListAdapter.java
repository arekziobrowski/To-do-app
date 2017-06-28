package com.am.arek.todoapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by arek on 24.04.17.
 */

public class ListAdapter extends ArrayAdapter<Item> implements Filterable{

    private ArrayList<Item> mDataSet;
    Context mContext;

    private static class ViewHolder {
        ImageView priorityImage;
        TextView textViewTitle;
        TextView textViewDate;
    }

    public ListAdapter(Context context, ArrayList<Item> data) {
        super(context, R.layout.row_item, data);
        mDataSet = data;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item item = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.priorityImage = (ImageView) convertView.findViewById(R.id.priorityImage);
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.itemTitle);
            viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.itemDate);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.priorityImage.setImageResource(item.getmImageId());
        viewHolder.textViewTitle.setText(item.getmItemTitle());

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE, MMMM dd yyyy", Locale.US);
        viewHolder.textViewDate.setText(sdf.format(item.getmDate()));

        return convertView;
    }
}
