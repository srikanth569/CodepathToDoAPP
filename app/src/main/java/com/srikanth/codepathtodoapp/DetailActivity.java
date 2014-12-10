package com.srikanth.codepathtodoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by srikanth on 12/9/14.
 */
public class DetailActivity extends Activity implements View.OnClickListener {
    private String data;
    private int position = -1;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Intent intent = getIntent();
        data = intent.getStringExtra(MainActivity.DATA);
        position = intent.getIntExtra(MainActivity.POSITION, -1);
        editText = (EditText) findViewById(R.id.edtEditItem);
        editText.setText(data != null ? data : "");
        editText.setSelection(editText.getText().length());
        Button submit = (Button) findViewById(R.id.btnsave);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (hasDataChanged() && position != -1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.DATA, editText.getText().toString());
            intent.putExtra(MainActivity.POSITION, position);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "No edits registered", Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_CANCELED);
        }
    }

    private boolean hasDataChanged() {
        if (data != null && editText != null) {
            if (!(data.equals(editText.getText()))) {
                return true;
            }
        }
        return false;
    }
}
