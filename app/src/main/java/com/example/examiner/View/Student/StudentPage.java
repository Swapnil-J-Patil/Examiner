package com.example.examiner.View.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.examiner.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentPage extends AppCompatActivity {

    EditText tokenet;
    Button proceed;
    TextView link,allthebest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);

        tokenet=findViewById(R.id.token_et);
        proceed=findViewById(R.id.btn_proceed);
        link=findViewById(R.id.link_tv);
        allthebest=findViewById(R.id.allthebest);

        link.setVisibility(View.GONE);
        allthebest.setVisibility(View.GONE);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = tokenet.getText().toString();
                if (key.isEmpty()) {
                    tokenet.setError("Please Enter a token");
                } else {
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Exam").child(key);
                    root.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getKey().equals("Link")) {
                                String url = (String) dataSnapshot.getValue();
                                link.setText(url);
                                allthebest.setVisibility(View.VISIBLE);
                                link.setVisibility(View.VISIBLE);

                                link.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), CheatDetection.class);
                                        intent.putExtra("link", url);
                                        intent.putExtra("token", key);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_slide_out, R.anim.right_slide_in);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}