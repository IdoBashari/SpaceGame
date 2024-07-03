package com.example.spacegamefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button quickGameButton;
    private Button slowGameButton;
    private Button sensorGameButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        quickGameButton = findViewById(R.id.quick_game_button);
        slowGameButton = findViewById(R.id.slow_game_button);
        sensorGameButton = findViewById(R.id.sensor_game_button);
        exitButton = findViewById(R.id.exit_button);

        quickGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("QUICK");
            }
        });

        slowGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("SLOW");
            }
        });

        sensorGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("SENSOR");
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startGame(String gameMode) {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.putExtra("GAME_MODE", gameMode);
        startActivity(intent);
    }
}