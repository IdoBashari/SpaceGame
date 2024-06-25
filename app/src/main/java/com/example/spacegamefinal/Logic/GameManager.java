package com.example.spacegamefinal.Logic;

import android.util.Log;
import android.widget.FrameLayout;
import androidx.appcompat.widget.AppCompatImageView;
import java.util.Arrays;
import java.util.Random;

public class GameManager {
    public static final int GRID_ROWS = 10;
    public static final int GRID_COLS = 3;
    public static final int EMPTY = 0;
    public static final int SPACESHIP = 1;
    public static final int OBSTACLE = 2;

    private int[][] grid;
    private int spaceshipRow;
    private int spaceshipCol;
    private int lives;
    private int rowsMoved;


    public GameManager() {
    }


    public GameManager(FrameLayout[] lanes, AppCompatImageView spaceship, AppCompatImageView[] hearts) {
        this.spaceshipRow = GRID_ROWS - 1;
        this.spaceshipCol = 1;
        this.lives = 3;
        this.rowsMoved = 0;
        this.grid = new int[GRID_ROWS][GRID_COLS];
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

    public void moveObstaclesDown() {
        Log.d("GameManager", "Moving obstacles down");

        for (int row = GRID_ROWS - 2; row >= 0; row--) {
            System.arraycopy(grid[row], 0, grid[row + 1], 0, GRID_COLS);
        }

        Arrays.fill(grid[0], EMPTY);

        rowsMoved++;
        if (rowsMoved >= 2) {
            generateNewObstacle();
            rowsMoved = 0;
        }
    }

    private void generateNewObstacle() {
        Random random = new Random();
        int randomCol = random.nextInt(GRID_COLS);
        grid[0][randomCol] = OBSTACLE;
    }

    public boolean checkCollision() {
        boolean collision = grid[spaceshipRow][spaceshipCol] == OBSTACLE;
        if (collision) {
            Log.d("GameManager", "Collision detected at (" + spaceshipRow + ", " + spaceshipCol + ")");
        } else {
            Log.d("GameManager", "No collision at (" + spaceshipRow + ", " + spaceshipCol + ")");
        }
        return collision;
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

    public void reset() {
        Log.d("GameManager", "Resetting game");
        lives = 3;
        spaceshipCol = 1;
        rowsMoved = 0;
        initGrid();
    }

    public void updateGame() {
        moveObstaclesDown();
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