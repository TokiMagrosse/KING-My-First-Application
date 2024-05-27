package com.example.poisonousking.inside_of_king;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poisonousking.R;
import com.example.poisonousking.helper_classes.GameRulesAdapter;

import java.util.Arrays;
import java.util.List;

public class GameRulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_rules);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Sample data for the RecyclerView
        List<String> rulesList = Arrays.asList(
                "Rule 1: Do you already have some idea about the game King? If so, you can already play in this room where the competition for victory will be very intense.",
                "Rule 2: All 8 modes will be played (No Jacks, No Queens, No Last 2, No PKing, Take Tricks (Spades, Clubs, Diamonds, Hearts)). Each will have 2 variations."
                // Add more rules here
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new GameRulesAdapter(rulesList));
    }
}