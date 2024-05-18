package com.example.poisonousking.helper_classes;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.poisonousking.R;

public class MusicService extends Service {
    private static final float BACKGROUND_MUSIC_VOLUME = 0.25f;
    private MediaPlayer mediaPlayer;

    public static final String ACTION_PLAY = "com.example.ACTION_PLAY";
    public static final String ACTION_STOP = "com.example.ACTION_STOP";
    private final BroadcastReceiver shutdownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                stopSelf();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.game_smooth_music);
        mediaPlayer.setVolume(BACKGROUND_MUSIC_VOLUME, BACKGROUND_MUSIC_VOLUME);
        mediaPlayer.setLooping(true);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        registerReceiver(shutdownReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_PLAY.equals(action)) {
                mediaPlayer.start();
            } else if (ACTION_STOP.equals(action)) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }
        return START_STICKY;
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        unregisterReceiver(shutdownReceiver);
    }*/

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


