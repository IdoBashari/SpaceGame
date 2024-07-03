package com.example.spacegamefinal.Logic;

import android.util.Log;
import android.widget.FrameLayout;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.spacegamefinal.R;
import com.example.spacegamefinal.Utilities.SoundPlayer;

import java.util.Arrays;
import java.util.Random;

public class GameManager {
    public static final int GRID_ROWS = 10;
    public static final int GRID_COLS = 5;
    public static final int EMPTY = 0;
    public static final int SPACESHIP = 1;
    public static final int OBSTACLE = 2;
    public static final int STAR = 3;

    private static final int STAR_GENERATION_CHANCE = 4;

    private int[][] grid;
    private int spaceshipRow;
    private int spaceshipCol;
    private int lives;
    private int rowsMoved;
    private int score;
    private SoundPlayer soundPlayer;
    private int gameSpeed;

    public GameManager(FrameLayout[] lanes, AppCompatImageView spaceship, AppCompatImageView[] hearts, SoundPlayer soundPlayer, String gameMode) {
        this.spaceshipRow = GRID_ROWS - 1;
        this.spaceshipCol = GRID_COLS / 2;
        this.lives = 3;
        this.rowsMoved = 0;
        this.score = 0;
        this.grid = new int[GRID_ROWS][GRID_COLS];
        this.soundPlayer = soundPlayer;

        if ("QUICK".equals(gameMode)) {
            this.gameSpeed = 2;
        } else {
            this.gameSpeed = 1;
        }

        initGrid();
    }

    private void initGrid() {
        for (int i = 0; i < GRID_ROWS; i++) {
            Arrays.fill(grid[i], EMPTY);
        }
        grid[spaceshipRow][spaceshipCol] = SPACESHIP;
    }

    public void moveSpaceshipLeft() {
        if (spaceshipCol > 0) {
            moveSpaceship(spaceshipCol - 1);
        }
    }

    public void moveSpaceshipRight() {
        if (spaceshipCol < GRID_COLS - 1) {
            moveSpaceship(spaceshipCol + 1);
        }
    }

    private void moveSpaceship(int newCol) {
        grid[spaceshipRow][spaceshipCol] = EMPTY;
        spaceshipCol = newCol;
        grid[spaceshipRow][spaceshipCol] = SPACESHIP;
    }

    public void updateGame() {
        for (int i = 0; i < gameSpeed; i++) {
            moveObstaclesDown();
            if (checkCollision()) {
                handleCollision();
            }
        }
    }

    private void moveObstaclesDown() {
        Log.d("GameManager", "Moving obstacles down");

        for (int row = GRID_ROWS - 2; row >= 0; row--) {
            System.arraycopy(grid[row], 0, grid[row + 1], 0, GRID_COLS);
        }

        Arrays.fill(grid[0], EMPTY);

        rowsMoved++;
        if (rowsMoved >= 2) {
            generateNewObject();
            rowsMoved = 0;
        }

        score += 10;
    }

    private void generateNewObject() {
        Random random = new Random();
        int randomCol = random.nextInt(GRID_COLS);
        int randomObject = random.nextInt(10) < STAR_GENERATION_CHANCE ? STAR : OBSTACLE;
        grid[0][randomCol] = randomObject;
    }

    public boolean checkCollision() {
        int collidedObject = grid[spaceshipRow][spaceshipCol];
        boolean collision = collidedObject == OBSTACLE || collidedObject == STAR;
        if (collision) {
            Log.d("GameManager", "Collision detected at (" + spaceshipRow + ", " + spaceshipCol + ")");
            if (collidedObject == OBSTACLE) {
                soundPlayer.playSound(R.raw.asteroid_sound);
                return true;
            } else if (collidedObject == STAR) {
                score += 100;
                grid[spaceshipRow][spaceshipCol] = EMPTY;
                soundPlayer.playSound(R.raw.star_sound);
            }
        } else {
            Log.d("GameManager", "No collision at (" + spaceshipRow + ", " + spaceshipCol + ")");
        }
        return false;
    }

    public int getLives() {
        return lives;
    }

    public void decreaseLife() {
        Log.d("GameManager", "Lives decreased from " + lives + " to " + (lives - 1));
        lives--;
    }

    public void ensureSpaceshipPosition() {
        grid[spaceshipRow][spaceshipCol] = SPACESHIP;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getSpaceshipCol() {
        return spaceshipCol;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        Log.d("GameManager", "Resetting game");
        lives = 3;
        spaceshipCol = GRID_COLS / 2;
        rowsMoved = 0;
        score = 0;
        initGrid();
    }

    public void handleCollision() {
        decreaseLife();
        ensureSpaceshipPosition();
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public void gameOver() {
        reset();
    }

    public void refreshGameState() {
        int[][] updatedGrid = new int[GRID_ROWS][GRID_COLS];
        for (int i = 0; i < GRID_ROWS; i++) {
            System.arraycopy(grid[i], 0, updatedGrid[i], 0, GRID_COLS);
        }
        grid = updatedGrid;
    }
}