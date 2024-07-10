# Space Game Final

This is an advanced version of the Space Game, featuring various game modes, sensor-based controls, and an integrated scoring system with location-based high score tracking. The game is developed using Java and Android Studio.

## Features

- **Five-lane road**
- **Spaceship that can move left and right**
- **Obstacles and stars on the road**
- **Variable game speed based on game mode**
- **Crash notification (toast message + vibration)**
- **Three lives system**
- **Endless game (resets to 3 lives after losing all)**
- **Sound effects and background music**
- **High score tracking with player names and locations**
- **Google Maps integration for high score locations**
- **Sensor-based controls option**
- **Modular approach to project implementation**

## Game Modes

- **Quick Game**: Obstacles move down faster.
- **Slow Game**: Obstacles move down at a normal speed.
- **Sensor Game**: Control the spaceship by tilting your device.

## Classes Overview

### GameManager
Manages the game logic, including movement of the spaceship, obstacles, and stars. Handles collisions, score updates, and game state.

#### Key Methods:
- `initGrid()`: Initializes the game grid.
- `moveSpaceshipLeft()`, `moveSpaceshipRight()`: Moves the spaceship left or right.
- `updateGame()`: Updates the game state, moving obstacles down and checking for collisions.
- `checkCollision()`: Checks for collisions and handles score updates or life reduction.
- `reset()`: Resets the game to its initial state.

### ScoreManager
Manages high scores, including saving and loading scores from shared preferences. Ensures only the top 10 scores are saved.

#### Key Methods:
- `addScore(Score newScore)`: Adds a new score to the list.
- `loadScores()`, `saveScores()`: Loads and saves scores to shared preferences.
- `clearScores()`: Clears all high scores.

### Score
Model class representing a score, including player name and location data.

#### Key Fields:
- `score`: The score value.
- `playerName`: The name of the player.
- `latitude`, `longitude`: The location where the score was achieved.

### MoveDetector
Utilizes the device's accelerometer to detect movements and control the spaceship in sensor mode.

#### Key Methods:
- `start()`, `stop()`: Starts and stops the sensor listener.
- `calculateMove(float x)`: Calculates the movement based on sensor data.

### SoundPlayer
Handles playing sound effects and background music.

#### Key Methods:
- `playSound(int resID)`: Plays a sound effect.
- `playBackgroundMusic(int resID)`: Plays background music in a loop.
- `stopBackgroundMusic()`, `stopAllSounds()`: Stops the background music and all sounds.

### MainActivity
The main activity where the game is played. Handles user inputs, game state updates, and UI refreshes.

#### Key Methods:
- `startGame()`, `stopGame()`, `pauseGame()`, `resumeGame()`: Controls the game state.
- `refreshUI()`: Refreshes the user interface based on the game state.
- `showNameInputDialog()`: Displays a dialog for entering the player's name after the game is over.
- `saveScore(String playerName, int score)`, `getLocationAndSaveScore(String playerName, int score)`: Saves the score with or without location data.

### MenuActivity
Displays the main menu where the user can choose the game mode, view the scoreboard, or exit the game.

#### Key Methods:
- `startGame(String gameMode)`: Starts the game with the selected mode.

### ScoreboardActivity
Displays the scoreboard with a list of high scores and a map showing the locations where the scores were achieved.

#### Key Methods:
- `onScoreSelected(Score score)`: Updates the map location when a score is selected.

### ScoreListFragment
Fragment displaying the list of high scores.

#### Key Methods:
- `updateScoreList()`: Updates the list of high scores.
- `setOnScoreSelectedListener(OnScoreSelectedListener listener)`: Sets the listener for score selection events.

### ScoreMapFragment
Fragment displaying the map with high score locations.

#### Key Methods:
- `updateMapLocation(double lat, double lng)`: Updates the map with the selected score's location.

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

- Android Studio
- Git

### Installation

1. Clone the repo:
   ```sh
   git clone https://github.com/IdoBashari/space-game-final.git
