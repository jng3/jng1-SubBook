package com.example.jng1_subbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Custom adapter created for ListView
 *
 * @author jng1
 */

public class CustomAdapter extends ArrayAdapter<Subscription> {

    /**
     * Constructor
     * @param context
     * @param subList the list of subscriptions
     */
    public CustomAdapter(Context context, ArrayList<Subscription> subList) {
        super(context, 0, subList);
    }

    /**
     * Gets the view for the list
     * @param position
     * @param convertView
     * @param parent
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Subscription sub = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sub_list_view, parent, false);
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            TextView listNameView = (TextView) convertView.findViewById(R.id.listName);
            TextView listDateView = (TextView) convertView.findViewById(R.id.listDate);
            TextView listCostView = (TextView) convertView.findViewById(R.id.listCost);
            listNameView.setText(sub.name);
            listDateView.setText(sdf.format(sub.date).toString());
            listCostView.setText(Integer.toString(sub.cost));
        } catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
