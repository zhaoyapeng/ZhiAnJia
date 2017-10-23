package com.zhianjia.m.zhianjia.components;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.config.Const;

/**
 * Created by Zibet.K on 2016/8/11.
 */

public class SoundManager {

    private static Context context;
    private static SoundManager instance;

    public static void setContext(Context context) {
        SoundManager.context = context;
        instance = new SoundManager();
    }

    public static SoundManager sharedInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    //==============================================================

    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundMap, playingIDMap, pauseIDMap;
    private AudioManager mgr;

    private SoundManager() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundMap = new HashMap<>();
        playingIDMap = new HashMap<>();
        pauseIDMap = new HashMap<>();
        soundMap.put(Const.Sounds.INCOME_RING, soundPool.load(context, R.raw.income, 1));
        soundMap.put(Const.Sounds.FINISH_RING, soundPool.load(context, R.raw.time_over_flow, 1));
        mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void play(int soundID) {
        play(soundID, 0);
    }

    public void play(int soundID, int replayTime) {
        if (soundMap.containsKey(soundID)) {
            float streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
            int sid = soundMap.get(soundID);
            int playing = getPlayingID(soundID);
            if (playing < 0) {
                int pauseID = resume(soundID);
                if (pauseID < 0) {
                    int playID = soundPool.play(sid, streamVolume, streamVolume, 10, replayTime, 1f);
                    playingIDMap.put(soundID, playID);
                }
            }
        }
    }

    public int resume(int soundID) {
        int pauseID = getPauseID(soundID);
        if (pauseID >= 0) {
            soundPool.resume(pauseID);
            playingIDMap.put(soundID, pauseID);
            pauseIDMap.remove(soundID);
        }
        return pauseID;
    }

    public int getPauseID(int soundID) {
        if (pauseIDMap.containsKey(soundID)) {
            int pauseID = pauseIDMap.get(soundID);
            return pauseID;
        }
        return -1;
    }

    public int getPlayingID(int soundID) {
        if (playingIDMap.containsKey(soundID)) {
            int playingID = playingIDMap.get(soundID);
            return playingID;
        }
        return -1;
    }

    public int stop(int soundID) {
        int playingID = getPlayingID(soundID);
        if (playingID > 0) {
            soundPool.pause(playingID);
            playingIDMap.remove(playingID);
            pauseIDMap.put(soundID, playingID);
            return playingID;
        }
        return -1;
    }


}
