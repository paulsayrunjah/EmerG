package com.groupbsse.ourapp.util;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.groupbsse.ourapp.WarningDetails;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Sayrunjah on 04/03/2017.
 */
public class MediaHelper {

    public MediaRecorder mediaRecorder;
    public String voiceStoragePath;
    static final String AB = "abcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();
    public  MediaPlayer mediaPlayer;

    Context context;
    Activity activity;

    public MediaHelper(Context context)
    {
        this.context = context;
        initializeMediaRecord();
    }

    public void initializeMediaRecord() {
        voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(voiceStoragePath + File.separator + "nameapp/voiceclips");
        if (!audioVoice.exists()) {
            audioVoice.mkdir();
        }
        voiceStoragePath = voiceStoragePath + File.separator + "nameapp/voiceclips/" +generateFilename(6) + ".mp3";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(voiceStoragePath);
    }

    public void startAudioRecording() {
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playLastStoredAudioMusic() {
        mediaPlayer = new MediaPlayer();
        try {
            //mediaPlayer.reset();
            WarningDetails warningDetails = (WarningDetails)activity;
            mediaPlayer.setDataSource(voiceStoragePath);
            mediaPlayer.prepare();

            //Toast.makeText(warningDetails.getApplicationContext(),"Playing audio",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


       mediaPlayer.start();

    }

    public void stopAudioRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            initializeMediaRecord();
        }

    }

    public String audiopath(){
        return this.voiceStoragePath;
    }

    public void stopAudioPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private String generateFilename(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }



}
