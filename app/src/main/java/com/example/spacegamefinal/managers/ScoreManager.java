package com.example.spacegamefinal.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.spacegamefinal.models.Score;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager {
    private static final String PREFS_NAME = "ScorePrefs";
    private static final String SCORES_KEY = "Scores";
    private static final int MAX_SCORES = 10;
    private static ScoreManager instance;
    private List<Score> highScores;
    private Context context;

    private ScoreManager(Context context) {
        this.context = context.getApplicationContext();
        highScores = new ArrayList<>();
        loadScores();
    }

    public static ScoreManager getInstance(Context context) {
        if (instance == null) {
            instance = new ScoreManager(context);
        }
        return instance;
    }

    public void addScore(Score newScore) {
        highScores.add(newScore);
        Collections.sort(highScores, (s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));
        if (highScores.size() > MAX_SCORES) {
            highScores = highScores.subList(0, MAX_SCORES);
        }
        saveScores();
    }

    public List<Score> getHighScores() {
        return new ArrayList<>(highScores);
    }

    private void loadScores() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String scoresJson = prefs.getString(SCORES_KEY, null);
        if (scoresJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Score>>() {}.getType();
            highScores = gson.fromJson(scoresJson, type);
        }
    }

    private void saveScores() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String scoresJson = gson.toJson(highScores);
        editor.putString(SCORES_KEY, scoresJson);
        editor.apply();
    }
}