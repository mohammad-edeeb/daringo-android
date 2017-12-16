package badeeb.com.daringo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import badeeb.com.daringo.models.FacebookFriend;

/**
 * Created by meldeeb on 12/6/17.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> items;
    protected Context context;
    protected OnRecyclerItemClick<T> onRecyclerItemClick;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Context getContext() {
        return context;
    }

    public void setOnRecyclerItemClick(OnRecyclerItemClick<T> onRecyclerItemClick) {
        this.onRecyclerItemClick = onRecyclerItemClick;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void add(T item) {
        items.add((item));
        notifyItemInserted(getItemCount() - 1);
    }

    public void add(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(T item) {
        int position = items.indexOf(item);
        remove(position);
    }

    public List<T> getItems(){
        return items;
    }

    public T getItemAt(int position) {
        return items.get(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        T item = getItemAt(position);
        setupItem(holder, item, position);
    }

    public void setupItem(RecyclerView.ViewHolder viewHolder, final T item, final int position) {
        if (onRecyclerItemClick != null && item != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecyclerItemClick.OnRecyclerItemClick(view, item, position);
                }
            });
        }
    }

    public void refreshItem(int position) {
        notifyItemChanged(position);
    }
}
