package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);
        
        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        btnSave.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
            intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
            intent.putExtra(MainActivity.KEY_ITEM_CHECKED_STATUS, getIntent().getExtras().getBoolean(MainActivity.KEY_ITEM_CHECKED_STATUS));
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}