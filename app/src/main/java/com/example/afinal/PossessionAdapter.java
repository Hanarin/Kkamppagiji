package com.example.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class PossessionAdapter extends RecyclerView.Adapter<PossessionAdapter.ViewHolder> {
    private OnDatabaseCallback callback;
    private ArrayList<PossessionItem> items = new ArrayList<PossessionItem>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView belongingTextView;
        private ImageView possessionImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            belongingTextView = itemView.findViewById(R.id.belongingTextView);
            possessionImageView = itemView.findViewById(R.id.possessionImageView);
        }
        public TextView getBelongingTextView() {
            return belongingTextView;
        }

        public ImageView getPossessionImageView() {
            return possessionImageView;
        }
    }

    public PossessionAdapter (OnDatabaseCallback callback, ArrayList<PossessionItem> items) {
        this.callback = callback;
        this.items = items;
    }
    //--------------------------------------------------

    @NonNull
    @Override   // ViewHolder 객체를 생성하여 리턴한다.
    public PossessionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.possession_item, parent, false);
        PossessionAdapter.ViewHolder viewHolder = new PossessionAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override   // ViewHolder안의 내용을 position에 해당되는 데이터로 교체한다.
    public void onBindViewHolder(@NonNull PossessionAdapter.ViewHolder holder, int position) {
        PossessionItem item = items.get(position);
        holder.belongingTextView.setText(callback.selectBelonging(item.getPossession().getBelongingId()).getName());
        holder.possessionImageView.setImageResource(item.getResId());
    }

    @Override   // 전체 데이터의 갯수를 리턴한다.
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<PossessionItem> items) {
        this.items = items;
    }
}