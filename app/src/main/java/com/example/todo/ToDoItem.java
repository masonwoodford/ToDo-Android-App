package com.example.todo;

import androidx.annotation.NonNull;

public class ToDoItem {
    public String item;
    public boolean isChecked;

    public ToDoItem(String item) {
        String[] strArr = item.split(":");
        this.item = strArr[0];
        this.isChecked = Boolean.parseBoolean(strArr[strArr.length - 1]);
    }

    public ToDoItem(String item, boolean isChecked) {
        this.item = item;
        this.isChecked = isChecked;
    }

    @NonNull
    public String toString() {
        return this.item + ":" + this.isChecked;
    }

    public void checked() {
        this.isChecked = !this.isChecked;
    }
}
