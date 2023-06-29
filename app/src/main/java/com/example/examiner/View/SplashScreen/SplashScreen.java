package com.example.examiner.View.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.examiner.View.Authentication.Login;
import com.example.examiner.R;

public class SplashScreen extends AppCompatActivity {

    ImageView imageView;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView=findViewById(R.id.logo);
        imageView.animate().scaleX(1.5F).scaleY(1.5F).setDuration(2500).start();

        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.audio);
        mediaPlayer.start();
        Thread thread=new Thread()
        {
            public void run() {
                try {
                    sleep(3000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    Intent intent=new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    mediaPlayer.release();
                    overridePendingTransition(R.anim.up_slide_out,R.anim.down_slide_in);
                    finish();
                }
            }
        };thread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}