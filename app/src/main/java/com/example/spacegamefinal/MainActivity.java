package com.example.spacegamefinal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.spacegamefinal.Logic.GameManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton main_BTN_left;
    private FloatingActionButton main_BTN_right;
    private AppCompatImageView main_IMG_space_ship;
    private AppCompatImageView[] main_IMG_hearts;
    private FrameLayout[] lanes;

    private GameManager gameManager;
    private Timer timer;
    private Handler handler = new Handler();
    private Vibrator vibrator;
    private boolean isGamePaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        findViews();
        initViews();

        gameManager = new GameManager(lanes, main_IMG_space_ship, main_IMG_hearts);

        startGame();
    }

    private void findViews() {
        main_BTN_left = findViewById(R.id.main_BTN_L);
        main_BTN_right = findViewById(R.id.MAIN_BTN_R);
        main_IMG_space_ship = new AppCompatImageView(this);
        main_IMG_space_ship.setImageResource(R.drawable.space_ship);

        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };

        lanes = new FrameLayout[]{
                findViewById(R.id.main_Left_line),
                findViewById(R.id.main_Middle_line),
                findViewById(R.id.main_Right_line)
        };
    }

    private void initViews() {
        main_BTN_left.setOnClickListener(v -> {
            gameManager.moveSpaceshipLeft();
            refreshUI();
        });
        main_BTN_right.setOnClickListener(v -> {
            gameManager.moveSpaceshipRight();
            refreshUI();
        });

        addViewToLane(main_IMG_space_ship, 1, GameManager.GRID_ROWS - 1, true);
    }

    private void startGame() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    gameManager.updateGame();
                    if (gameManager.checkCollision()) {
                        handleCollision();
                    }
                    refreshUI();
                });
            }
        }, 0, 700);
    }

    private void handleCollision() {
        Log.d("Game", "Handling collision");
        gameManager.handleCollision();
        vibrator.vibrate(500);
        if (gameManager.isGameOver()) {
            gameOver();
        } else {
            Toast.makeText(this, "Crash!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gameOver() {
        Log.d("Game", "Game over");
        Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
        gameManager.gameOver();
        refreshUI();
    }

    public void refreshUI() {
        Log.d("Game", "Refreshing UI");
        gameManager.refreshGameState();
        int lives = gameManager.getLives();
        for (int i = main_IMG_hearts.length - 1; i >= 0; i--) {
            main_IMG_hearts[i].setVisibility(main_IMG_hearts.length - i <= lives ? View.VISIBLE : View.INVISIBLE);
        }

        for (FrameLayout lane : lanes) {
            lane.removeAllViews();
        }

        addViewToLane(main_IMG_space_ship, gameManager.getSpaceshipCol(), GameManager.GRID_ROWS - 1, true);

        int[][] grid = gameManager.getGrid();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == GameManager.OBSTACLE) {
                    addViewToLane(createObstacleView(), col, row, false);
                }
            }
        }
    }

    private void addViewToLane(View view, int laneIndex, int rowIndex, boolean isSpaceship) {
        FrameLayout lane = lanes[laneIndex];
        if (lane != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            if (isSpaceship) {
                params.topMargin = lane.getHeight() - 200;
            } else {
                params.topMargin = (lane.getHeight() / GameManager.GRID_ROWS) * rowIndex;
            }
            view.setLayoutParams(params);
            lane.addView(view);
        }
    }

    private AppCompatImageView createObstacleView() {
        AppCompatImageView obstacle = new AppCompatImageView(this);
        obstacle.setImageResource(R.drawable.asteroid);
        obstacle.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                100
        ));
        return obstacle;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGamePaused) {
            resumeGame();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopGame();
    }

    private void pauseGame() {
        if (timer != null) {
            timer.cancel();
        }
        isGamePaused = true;
    }

    private void resumeGame() {
        if (isGamePaused) {
            startGame();
            isGamePaused = false;
        }
    }

    private void stopGame() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}