package com.groupbsse.ourapp;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.groupbsse.ourapp.util.MediaHelper;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Sayrunjah on 04/03/2017.
 */
public class AudioDialog extends DialogFragment {

    private MediaRecorder mediaRecorder;
    String voiceStoragePath;

    static final String AB = "abcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();

    MediaPlayer mediaPlayer;
    ImageButton record,play;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.audio_record, container, false);
        record = (ImageButton)v.findViewById(R.id.record);
        final ImageButton stop = (ImageButton)v.findViewById(R.id.stop);
         play = (ImageButton)v.findViewById(R.id.play);
        Button done = (Button)v.findViewById(R.id.done);
        getDialog().setTitle("Record Audio");
        initializeMediaRecord();
        stop.setEnabled(false);
        play.setEnabled(false);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startAudioRecording();
                stop.setEnabled(true);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               playLastStoredAudioMusic();
                mediaPlayerPlaying();

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudioRecording();
                play.setEnabled(true);
                record.setEnabled(false);

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //WarningDetails warningDetails = (WarningDetails)getActivity();
               // Toast.makeText(warningDetails.getApplicationContext(),voiceStoragePath,Toast.LENGTH_SHORT).show();
                OnDoneClicked activity = (OnDoneClicked) getActivity();
                activity.GetAudioPath(voiceStoragePath);
                dismiss();
            }
        });

        return v;
    }

    /* audio recording */

    private String generateVoiceFilename(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private void startAudioRecording() {
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* record.setEnabled(false);
        stop.setEnabled(true);*/
    }

    private void stopAudioRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            /*fileName.setText(voiceStoragePath);*/
        }

       /* stop.setEnabled(false);
        play.setEnabled(true);*/
    }

    private void playLastStoredAudioMusic() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(voiceStoragePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        //record.setEnabled(true);
        // play.setEnabled(false);
    }

    private void stopAudioPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            initializeMediaRecord();
            WarningDetails warningDetails = (WarningDetails)getActivity();
            Toast.makeText(warningDetails.getApplicationContext(),"stoping noww",Toast.LENGTH_SHORT).show();
        }
    }


    private void mediaPlayerPlaying() {
        if (!mediaPlayer.isPlaying()) {
            stopAudioPlay();
            record.setEnabled(true);
            play.setEnabled(false);
        }
    }

    private void initializeMediaRecord() {
        voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(voiceStoragePath + File.separator + "wetase/audioclips");
        if (!audioVoice.exists()) {
            audioVoice.mkdir();
        }
        voiceStoragePath = voiceStoragePath + File.separator + "wetase/audioclips" + generateVoiceFilename(6) + ".mp3";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(voiceStoragePath);
    }


    public interface OnDoneClicked{
        void GetAudioPath(String path);
    }

}
