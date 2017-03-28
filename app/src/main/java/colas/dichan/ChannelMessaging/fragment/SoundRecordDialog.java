package colas.dichan.ChannelMessaging.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;
import java.io.IOException;

import colas.dichan.ChannelMessaging.LoginActivity;
import colas.dichan.ChannelMessaging.R;



public class SoundRecordDialog extends DialogFragment {
    private FloatingActionButton btnRecord;
    private FloatingActionButton btnStop;
    private FloatingActionButton btnPreview;
    private MediaRecorder mRecorder = null;
    private String mFileName = null;
    private MediaPlayer mPlayer = null;
    private boolean mRecording = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mFileName = getContext().getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";

        View v = inflater.inflate(R.layout.dialog_sound_record,null);

        btnRecord = (FloatingActionButton) v.findViewById(R.id.btnPlay);
        btnStop = (FloatingActionButton) v.findViewById(R.id.btnPause);
        btnPreview = (FloatingActionButton) v.findViewById(R.id.btnPreview);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRecording == false){
                    mRecording = true;
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setOutputFile(mFileName);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.e("audio record test", "prepare() failed");
                    }

                    mRecorder.start();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRecording == true){
                    mRecording = false;
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }
            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(mFileName);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    Log.e("play", "prepare() failed");
                }
            }
        });

        builder.setView(v).setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                File test = new File(mFileName);

                SharedPreferences settings = getContext().getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                String accesstoken = settings.getString("accesstoken","");

                /*
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("accesstoken",accesstoken));
                params.add(new BasicNameValuePair("channelid",getIntent().getStringExtra("channelid")));


                UploadFileToServer upload = new UploadFileToServer(SoundRecordDialog.this, test.getAbsolutePath(), params, new UploadFileToServer.OnUploadFileListener() {
                    @Override
                    public void onResponse(String result) {
                        System.out.println("onResponse : " + result);
                    }

                    @Override
                    public void onFailed(IOException error) {
                        System.out.println("onFailed : " + error);
                    }
                });

                upload.execute();
                */

            }
        }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SoundRecordDialog.this.getDialog().cancel();
            }
        });;




        return builder.create();
    }
}