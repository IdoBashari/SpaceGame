package com.example.spacegamefinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import com.example.spacegamefinal.Interfaces.MoveCallback;
import com.example.spacegamefinal.Logic.GameManager;
import com.example.spacegamefinal.Utilities.MoveDetector;
import com.example.spacegamefinal.Utilities.SoundPlayer;
import com.example.spacegamefinal.managers.ScoreManager;
import com.example.spacegamefinal.models.Score;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton main_BTN_left;
    private FloatingActionButton main_BTN_right;
    private AppCompatImageView main_IMG_space_ship;
    private AppCompatImageView[] main_IMG_hearts;
    private FrameLayout[] lanes;
    private TextView main_score;

    private GameManager gameManager;
    private Timer timer;
    private Handler handler = new Handler();
    private Vibrator vibrator;
    private boolean isGamePaused = false;

    private SoundPlayer soundPlayer;
    private MoveDetector moveDetector;

    private String gameMode;
    private static final int QUICK_GAME_INTERVAL = 500;
    private static final int SLOW_GAME_INTERVAL = 1000;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameMode = getIntent().getStringExtra("GAME_MODE");

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        soundPlayer = new SoundPlayer(this);
        findViews();
        initViews();
        gameManager = new GameManager(lanes, main_IMG_space_ship, main_IMG_hearts, soundPlayer, gameMode);

        if ("SENSOR".equals(gameMode)) {
            initSensorMode();
        } else {
            initButtonMode();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        startGame();
        soundPlayer.playBackgroundMusic(R.raw.lost_in_space);
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
                findViewById(R.id.main_first_line),
                findViewById(R.id.main_second_line),
                findViewById(R.id.main_third_line),
                findViewById(R.id.main_fourth_line),
                findViewById(R.id.main_fifth_line)
        };

        main_score = findViewById(R.id.main_score);
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

    private void initSensorMode() {
        main_BTN_left.setVisibility(View.GONE);
        main_BTN_right.setVisibility(View.GONE);

        moveDetector = new MoveDetector(this, new MoveCallback() {
            @Override
            public void moveLeft() {
                gameManager.moveSpaceshipLeft();
                refreshUI();
            }

            @Override
            public void moveRight() {
                gameManager.moveSpaceshipRight();
                refreshUI();
            }
        });
    }

    private void initButtonMode() {
        main_BTN_left.setVisibility(View.VISIBLE);
        main_BTN_right.setVisibility(View.VISIBLE);
    }

    private void startGame() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        int interval = "QUICK".equals(gameMode) ? QUICK_GAME_INTERVAL : SLOW_GAME_INTERVAL;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    gameManager.updateGame();
                    if (gameManager.isGameOver()) {
                        gameOver();
                    } else {
                        refreshUI();
                    }
                });
            }
        }, 0, interval);
    }

    private void gameOver() {
        Log.d("Game", "Game over");
        runOnUiThread(() -> {
            stopGame();
            soundPlayer.stopBackgroundMusic();
            showNameInputDialog();
        });
    }

    private void showNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String playerName = input.getText().toString();
            saveScore(playerName, gameManager.getScore());
        });
        builder.show();
    }

    private void saveScore(String playerName, int score) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocationAndSaveScore(playerName, score);
        }
    }

    private void getLocationAndSaveScore(String playerName, int score) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Score newScore = new Score(score, playerName, location.getLatitude(), location.getLongitude());
                        ScoreManager.getInstance(this).addScore(newScore);
                    } else {
                        Score newScore = new Score(score, playerName, 0, 0);
                        ScoreManager.getInstance(this).addScore(newScore);
                    }
                    Intent intent = new Intent(MainActivity.this, ScoreboardActivity.class);
                    startActivity(intent);
                    finish();
                });
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
                } else if (grid[row][col] == GameManager.STAR) {
                    addViewToLane(createStarView(), col, row, false);
                }
            }
        }

        main_score.setText(String.format("%03d", gameManager.getScore()));
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
        obstacle.setLayoutParams(new FrameLayout.LayoutParams(100, 100));
        return obstacle;
    }

    private AppCompatImageView createStarView() {
        AppCompatImageView star = new AppCompatImageView(this);
        star.setImageResource(R.drawable.star);
        star.setLayoutParams(new FrameLayout.LayoutParams(100, 100));
        return star;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
        if ("SENSOR".equals(gameMode)) {
            moveDetector.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGamePaused) {
            resumeGame();
        }
        if ("SENSOR".equals(gameMode)) {
            moveDetector.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopGame();
        soundPlayer.stopAllSounds();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveScore("", gameManager.getScore());
            } else {
                Score newScore = new Score(gameManager.getScore(), "", 0, 0);
                ScoreManager.getInstance(this).addScore(newScore);
                Intent intent = new Intent(this, ScoreboardActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}