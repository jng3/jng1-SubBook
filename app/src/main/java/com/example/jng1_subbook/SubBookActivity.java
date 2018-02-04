package com.example.jng1_subbook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Created by JN on 2018-02-03.
 */

public class SubBookActivity extends Activity {

    private static final String FILENAME = "sub_list.sav";
    private ListView currentSubs;
    private ArrayList<Subscription> subList;
    private CustomAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_main);

        Button addButton = (Button) findViewById(R.id.add);
        Button costButton = (Button) findViewById(R.id.cost);
        currentSubs = (ListView) findViewById(R.id.currentSubs);
/*
        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                String text = bodyText.getText().toString();
                Tweet tweet = new NormalTweet(text);
                tweetList.add(tweet);
                adapter.notifyDataSetChanged();
                saveInFile();
            }
        });

        costButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                String text = bodyText.getText().toString();
                saveInFile(text, new Date(System.currentTimeMillis()));
                finish();
            }
        });
*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
        adapter = new CustomAdapter(this, subList);
        currentSubs.setAdapter(adapter);
    }

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
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
