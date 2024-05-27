package com.example.poisonousking.helper_classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poisonousking.R;

import java.util.List;

public class GameRulesAdapter extends RecyclerView.Adapter<GameRulesAdapter.GameRulesViewHolder> {

    private final List<String> rulesList;

    public GameRulesAdapter(List<String> rulesList) {
        this.rulesList = rulesList;
    }

    @NonNull
    @Override
    public GameRulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_rule, parent, false);
        return new GameRulesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameRulesViewHolder holder, int position) {
        holder.bind(rulesList.get(position));
    }

    @Override
    public int getItemCount() {
        return rulesList.size();
    }

    public static class GameRulesViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public GameRulesViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView cardView = itemView.findViewById(R.id.cardView);
            textView = itemView.findViewById(R.id.textView);
        }

        public void bind(String rule) {
            textView.setText(rule);
        }
    }
}

