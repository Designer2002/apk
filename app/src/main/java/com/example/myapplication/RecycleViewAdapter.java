package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private final List<String> items;
    private int selectedPosition = -1;
    private final OnThemeSelectedListener listener;
    private  RecyclerView r;

    public interface OnThemeSelectedListener {
        void onThemeSelected(int themeIndex);
    }

    public RecycleViewAdapter(RecyclerView r, List<String> items, OnThemeSelectedListener listener) {
        this.items = items;
        this.listener = listener;
        this.r = r;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        FrameLayout card;

        @SuppressLint("NotifyDataSetChanged")
        ViewHolder(View view) {
            super(view);
            number = view.findViewWithTag("number");
            card =  view.findViewWithTag("number_card");

            view.setOnClickListener(v -> {
                int pos = getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    selectedPosition = pos;
                    notifyDataSetChanged();
                    listener.onThemeSelected(pos);

                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.number_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.number.setText(items.get(position));
        if (position == selectedPosition) {
            holder.card.setBackgroundResource(R.drawable.number_bg_selected);
        } else {
            holder.card.setBackgroundResource(R.drawable.number_bg);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
