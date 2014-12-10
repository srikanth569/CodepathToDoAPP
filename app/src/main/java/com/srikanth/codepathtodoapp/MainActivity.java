package com.srikanth.codepathtodoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener, ListView.OnItemLongClickListener, ListView.OnItemClickListener {

    private ListView mToDoList;
    private ArrayList<String> mItems;
    private ArrayAdapter<String> mAdapter;
    private Button mSubmit;
    private EditText etItemText;
    private final static int RESULT_FROM_DETAILS_ACTIVITY = 0;
    public final static String DATA = "data";
    public final static String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToDoList = (ListView) findViewById(R.id.todoList);
        mSubmit = (Button) findViewById(R.id.btnSubmit);
        etItemText = (EditText) findViewById(R.id.edtNewItem);
        mSubmit.setOnClickListener(this);
        readItems();
        mToDoList.setOnItemLongClickListener(this);
        mToDoList.setOnItemClickListener(this);
        mToDoList.setEmptyView(findViewById(android.R.id.empty));
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mItems);
        mToDoList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            String str = etItemText.getText().toString();
            if (str.length() > 0) {
                mItems.add(str);
                etItemText.setText("");
                mAdapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_FROM_DETAILS_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                int pos = data.getIntExtra(POSITION, -1);
                String changedData = data.getStringExtra(DATA);
                if (pos != -1 && changedData != null) {
                    mItems.set(pos, changedData);
                    mAdapter.notifyDataSetChanged();
                    writeItems();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mItems.remove(position);
        mAdapter.notifyDataSetChanged();
        writeItems();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mItems != null) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DATA, mItems.get(position));
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, RESULT_FROM_DETAILS_ACTIVITY);
        }
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            mItems = new ArrayList(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            mItems = new ArrayList();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, mItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
