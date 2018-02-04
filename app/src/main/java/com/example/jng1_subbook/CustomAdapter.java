package com.example.jng1_subbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JN on 2018-02-04.
 */

public class CustomAdapter extends ArrayAdapter<Subscription> {

    private static ArrayList<Subscription> subList;

    public CustomAdapter(Context context, ArrayList<Subscription> subList) {
        super(context, 0, subList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Subscription sub = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sub_list_view, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView cost = (TextView) convertView.findViewById(R.id.cost);
        name.setText(sub.name);
        date.setText(sub.date.toString());
        cost.setText(sub.cost);
        return convertView;
    }
}
