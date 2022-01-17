package com.example.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface CheckBoxClickListener {
        void onCheckBoxClicked(int position);
    }

    List<ToDoItem> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;
    CheckBoxClickListener cbListener;

    public ItemsAdapter(List<ToDoItem> items, OnLongClickListener longClickListener, OnClickListener clickListener, CheckBoxClickListener cbListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
        this.cbListener = cbListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        CheckBox cbItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
            cbItem = itemView.findViewById(R.id.cbItem);
        }

        public void bind(ToDoItem item) {
            tvItem.setText(item.item);
            cbItem.setChecked(item.isChecked);
            cbItem.setOnClickListener(c -> cbListener.onCheckBoxClicked(getAdapterPosition()));
            tvItem.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
            tvItem.setOnLongClickListener(v -> {
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            });
        }
    }
}
