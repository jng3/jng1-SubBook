package com.example.jng1_subbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Handles the main activity for the SubBook app
 *
 * @author jng1
 */

public class SubBookActivity extends Activity {

    private static final String FILENAME = "sub_list.sav";
    private static ArrayList<Subscription> subList;
    private ListView currentSubs;
    private CustomAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_book_main);
        Button addButton = (Button) findViewById(R.id.add);
        currentSubs = (ListView) findViewById(R.id.currentSubs);

        // Handles add button
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Create a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(SubBookActivity.this);
                // Get the layout inflater
                LayoutInflater inflater = SubBookActivity.this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.sub_edit_view, null))
                        // Add action buttons
                        // Handles save button for dialog
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Dialog dialogView = (Dialog) dialog;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                                EditText editTextName = dialogView.findViewById(R.id.editName);
                                EditText editTextDate = dialogView.findViewById(R.id.editDate);
                                EditText editTextCost = dialogView.findViewById(R.id.editCost);
                                EditText editTextComment = dialogView.findViewById(R.id.editComment);
                                try {
                                    String subName = editTextName.getText().toString().trim();
                                    Date subDate = sdf.parse(editTextDate.getText().toString().trim());
                                    int subCost = Integer.parseInt(editTextCost.getText().toString().trim());
                                    String subComment = editTextComment.getText().toString().trim();
                                    if (subName.length() == 0 || subName.length() > 20 || subDate.toString().length() == 0 ||
                                        Integer.toString(subCost).length() == 0 || subCost <= 0 || subComment.length() > 30) {
                                        Toast.makeText(getApplicationContext(), "Invalid Subscription", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    } else {
                                        Subscription newSub = new Subscription(subName, subDate, subCost, subComment);
                                        subList.add(newSub);
                                        adapter.notifyDataSetChanged();
                                        saveInFile();
                                        double sum = getSum(subList);
                                        TextView subSum = (TextView) findViewById(R.id.sum);
                                        subSum.setText("Total Cost: " + String.format("%.2f", sum));
                                        dialog.cancel();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Invalid Subscription", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            }
                        })
                        // Handles cancel button for dialog
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });

        // Handles clicking on items in list
        currentSubs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                // Create a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(SubBookActivity.this);
                LayoutInflater inflater = SubBookActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.sub_edit_view, null);
                builder.setView(dialogView);
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                final Subscription sub = (Subscription) currentSubs.getAdapter().getItem(position);
                final EditText editTextName = dialogView.findViewById(R.id.editName);
                editTextName.setText(sub.name);
                final EditText editTextDate = dialogView.findViewById(R.id.editDate);
                editTextDate.setText(sdf.format(sub.date).toString());
                final EditText editTextCost = dialogView.findViewById(R.id.editCost);
                editTextCost.setText(Integer.toString(sub.cost));
                final EditText editTextComment = dialogView.findViewById(R.id.editComment);
                editTextComment.setText(sub.comment);
                // Handles save button in dialog
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String subName = editTextName.getText().toString().trim();
                            Date subDate = sdf.parse(editTextDate.getText().toString().trim());
                            int subCost = Integer.parseInt(editTextCost.getText().toString().trim());
                            String subComment = editTextComment.getText().toString().trim();
                            if (subName.length() == 0 || subName.length() > 20 || subDate.toString().length() == 0 ||
                                    Integer.toString(subCost).length() == 0 || subCost <= 0 || subComment.length() > 30) {
                                Toast.makeText(getApplicationContext(), "Invalid Subscription", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            } else {
                                sub.name = subName;
                                sub.date = subDate;
                                sub.cost = subCost;
                                sub.comment = subComment;
                                adapter.notifyDataSetChanged();
                                saveInFile();
                                double sum = getSum(subList);
                                TextView subSum = (TextView) findViewById(R.id.sum);
                                subSum.setText("Total Cost: " + String.format("%.2f", sum));
                                dialog.cancel();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Invalid Subscription", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                });
                // Handles cancel button in dialog
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                // Handles delete button in dialog
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        subList.remove(sub);
                        double sum = getSum(subList);
                        TextView subSum = (TextView) findViewById(R.id.sum);
                        subSum.setText("Total Cost: " + String.format("%.2f", sum));
                        adapter.notifyDataSetChanged();
                        saveInFile();
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    /**
     * Called on start
     */
    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
        adapter = new CustomAdapter(this, subList);
        currentSubs.setAdapter(adapter);
        double sum = getSum(subList);
        TextView subSum = (TextView) findViewById(R.id.sum);
        subSum.setText("Total Cost: " + String.format("%.2f", sum));
    }

    /**
     * Load the subscriptions from file
     */
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylist
            // 2018-01-25
            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            subList = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            subList = new ArrayList<Subscription>();
        }
    }

    /**
     * Save the subscriptions to file
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(subList, out);
            out.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Get the total cost of the subscriptions
     * @param subList the list of subscriptions
     * @return double
     */
    public double getSum(ArrayList<Subscription> subList) {
        double sum = 0.00;
        if (!subList.isEmpty()) {
            for (int i = 0; i < subList.size(); i++) {
                sum = sum + subList.get(i).cost;
            }
        }
        return sum;
    }

    /**
     * Cleanup
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
