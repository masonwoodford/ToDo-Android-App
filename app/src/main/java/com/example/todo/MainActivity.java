package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 5;

    List<ToDoItem> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);
        searchView = findViewById(R.id.searchItem);
        searchView.setSubmitButtonEnabled(true);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = position -> {
            items.remove(position);
            itemsAdapter.notifyItemRemoved(position);
            Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
            saveItems();
        };

        ItemsAdapter.OnClickListener onClickListener = position -> {
            Intent i = new Intent(MainActivity.this, EditActivity.class);
            i.putExtra(KEY_ITEM_TEXT, items.get(position).item);
            i.putExtra(KEY_ITEM_POSITION, position);
            startActivityForResult(i, EDIT_TEXT_CODE);
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(view -> {
            String todoItem = etItem.getText().toString();
            ToDoItem newToDoItem = new ToDoItem(todoItem, false);
            items.add(newToDoItem);
            itemsAdapter.notifyItemInserted(items.size()-1);
            etItem.setText("");
            Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
            saveItems();
            closeKeyboard();
        });

        searchView.setOnClickListener(view -> searchView.setIconified(false));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                onQueryTextChange(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
                    rvItems.setAdapter(itemsAdapter);
                    return false;
                }
                List<ToDoItem> filteredItems = new ArrayList<>();
                for(ToDoItem item: items) {
                    if (item.item.toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                        filteredItems.add(item);
                    }
                }
                itemsAdapter = new ItemsAdapter(filteredItems, onLongClickListener, onClickListener);
                rvItems.setAdapter(itemsAdapter);

                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            assert data != null;
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            ToDoItem newItem = new ToDoItem(itemText, false);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(position, newItem);
            itemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File getDataFile() {
        return new File(getFilesDir(), "items.txt");
    }

    private void loadItems() {
        try {
            ArrayList<String> itemsOut = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));

            // need to convert this string somehow
            // items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}