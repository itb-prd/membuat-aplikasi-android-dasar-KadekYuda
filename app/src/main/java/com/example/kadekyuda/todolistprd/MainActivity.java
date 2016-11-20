package com.example.kadekyuda.todolistprd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoList = null;
    ArrayAdapter<String> adapter = null;
    ListView lv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        todoList = getArrayVal(getApplicationContext());
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, todoList);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItem.trim().equals(todoList.get(position).trim())) {
                    removeElement(selectedItem, position);
                } else {
                    Toast.makeText(getApplicationContext(),"Error Removing Element", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, final int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItem.trim().equals(todoList.get(position).trim())) {
                    editElement(selectedItem, position);
                } else {
                    Toast.makeText(getApplicationContext(),"Error Removing Element", Toast.LENGTH_LONG).show();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtask();
            }
        });
    }

    public void addtask () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add List");
        builder.setMessage("What are you going to do?");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String task = inputField.getText().toString();
                todoList.add(preferredCase(task));
                storeArrayVal(todoList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    public static String preferredCase(String original)
    {
        if (original.isEmpty())
            return original;

        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("How To Use This App");
            builder.setMessage("Click on the items to edit them. Hold on the items to remove them. Enjoy!");
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void removeElement(String selectedItem, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove " + selectedItem + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                todoList.remove(position);
                Collections.sort(todoList);
                storeArrayVal(todoList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void editElement(String selectedItem, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit " + selectedItem);
        final EditText inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                todoList.set(position,preferredCase(inputField.getText().toString()));
                storeArrayVal(todoList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public static void storeArrayVal( ArrayList<String> inArrayList, Context context)
    {
        Set<String> WhatToWrite = new HashSet<String>(inArrayList);
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        prefEditor.putStringSet("myArray", WhatToWrite);
        prefEditor.commit();
    }

    public static ArrayList getArrayVal( Context dan)
    {
        SharedPreferences WordSearchGetPrefs = dan.getSharedPreferences("dbArrayValues",Activity.MODE_PRIVATE);
        Set<String> tempSet = new HashSet<String>();
        tempSet = WordSearchGetPrefs.getStringSet("myArray", tempSet);
        return new ArrayList<String>(tempSet);
    }

}