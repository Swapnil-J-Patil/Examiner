package com.example.examiner.View.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examiner.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class CheatDetection extends AppCompatActivity {
    TextView textView;
    CameraSource cameraSource;
    WebView webView;
    DatabaseReference databaseReference;
    private static final long TIMER_DELAY = 5000; // 5 seconds delay
    private Timer timer;
    private TimerTask timerTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat_detection);

        textView=findViewById(R.id.Activity_tv);
        webView=findViewById(R.id.website);

        Intent intent= getIntent();
        String url= intent.getStringExtra("link");//key
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.
                PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Give Camera Permission and Restart", Toast.LENGTH_SHORT).show();
        } else {
            createCameraSource();
        }
    }
    private class caught extends Tracker<Face> {
        private final float ThresHold = 0.75f;

        private caught() {}

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face) {
            if (face.getIsLeftEyeOpenProbability() > ThresHold || face.getIsRightEyeOpenProbability() > ThresHold) {
                showToast("Nice! You Are Attending Session Properly");
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
            }
            else
            {
                showToast("You are Not Looking at the screen");
            }
        }
        @Override
        public void onMissing(Detector.Detections<Face> detections) {
            super.onMissing(detections);
            showToast("You are Not Here");
            startTimer();
        }

        @Override
        public void onDone() {
            super.onDone();
        }
    }
    private class FaceTrackerFactory implements MultiProcessor.Factory<Face> {
        public FaceTrackerFactory() { }

        @Override
        public Tracker<Face> create(Face face) {
            return new caught();
        }
    }

    public void createCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerFactory()).build());

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 728)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)//for front Camera
                .setRequestedFps(30.0f)
                .build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            cameraSource.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            cameraSource.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent= getIntent();
        String token= intent.getStringExtra("token");//key
        databaseReference= FirebaseDatabase.getInstance().getReference().child("StudentAction").child(token);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String student = user.getDisplayName();

        if (user != null) {
            Date currentTime = Calendar.getInstance().getTime();
            String time=currentTime.toString();
            String childkey = student + " " + currentTime;

            String action = student + " has switched the screen.";

            HashMap hashMap = new HashMap();
            hashMap.put("Token", token);
            hashMap.put("Time", time);
            hashMap.put("Action", action);
            hashMap.put("Nodekey", childkey);

            databaseReference.child(childkey).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CheatDetection.this, "Your Action Has been shared with your teacher", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {}
    }
    public void showToast(final String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(message);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraSource!=null)
        {
            cameraSource.release();
        }
    }
    private void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent= getIntent();
                        String token= intent.getStringExtra("token");//key
                        databaseReference= FirebaseDatabase.getInstance().getReference().child("StudentAction").child(token);

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        String student = user.getDisplayName();

                        if (user != null) {
                            Date currentTime = Calendar.getInstance().getTime();
                            String time=currentTime.toString();
                            String childkey = student + " " + currentTime;

                            String action = student + " is not attending session properly.";

                            HashMap hashMap = new HashMap();
                            hashMap.put("Token", token);
                            hashMap.put("Time", time);
                            hashMap.put("Action", action);
                            hashMap.put("Nodekey", childkey);

                            databaseReference.child(childkey).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CheatDetection.this, "Your Action Has been shared with your teacher", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {}
                    }
                });
                timerTask.cancel();
            }
        };
        timer.schedule(timerTask, TIMER_DELAY);
    }
}