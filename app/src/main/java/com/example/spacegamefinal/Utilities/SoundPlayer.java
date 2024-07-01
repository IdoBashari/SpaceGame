package com.example.spacegamefinal.Utilities;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {

    private Context context;
    private MediaPlayer effectPlayer;
    private MediaPlayer backgroundMusicPlayer;

    public SoundPlayer(Context context) {
        this.context = context;
    }

    public void playSound(int resID) {
        if (effectPlayer != null) {
            effectPlayer.release();
        }
        effectPlayer = MediaPlayer.create(context, resID);
        effectPlayer.setOnCompletionListener(MediaPlayer::release);
        effectPlayer.start();
    }

    public void playBackgroundMusic(int resID) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.release();
        }
        backgroundMusicPlayer = MediaPlayer.create(context, resID);
        backgroundMusicPlayer.setLooping(true);
        backgroundMusicPlayer.start();
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = null;
        }
    }

    public void stopAllSounds() {
        if (effectPlayer != null) {
            effectPlayer.release();
            effectPlayer = null;
        }
        stopBackgroundMusic();
    }
}