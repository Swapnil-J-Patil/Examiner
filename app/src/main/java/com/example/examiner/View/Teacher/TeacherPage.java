package com.example.examiner.View.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examiner.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TeacherPage extends AppCompatActivity {
    private EditText link,subjectname,teachername;
    private TextView token,alreadyhavetoken;
    private Button btnGetToken;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_page);

        subjectname=findViewById(R.id.subject_et);
        teachername=findViewById(R.id.teachername_et);
        link=findViewById(R.id.token_et);
        btnGetToken=findViewById(R.id.btn_get_token);
        token=findViewById(R.id.token);
        alreadyhavetoken=findViewById(R.id.alreadyHaveToken);
        token.setVisibility(View.GONE);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Exam");

        btnGetToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Subjectname=subjectname.getText().toString();
                final String Teachername = teachername.getText().toString();
                final String Link = link.getText().toString();

                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("subjectname", Subjectname);
                editor.putString("teachername", Teachername);
                editor.apply();

                if(Subjectname.isEmpty())
                {
                    subjectname.setError("Please Enter Subject Name");
                }
                else if(Teachername.isEmpty())
                {
                    teachername.setError("Please Enter Your Name");
                }
                else if(Link.isEmpty())
                {
                    link.setError("Please Enter exam link");
                }
                else if(Subjectname!=null && Teachername!=null && Link!=null)
                {
                    upload(Subjectname,Teachername,Link);
                }
                else
                {
                    Toast.makeText(TeacherPage.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alreadyhavetoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Detection.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out, R.anim.right_slide_in);
            }
        });
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String subject = preferences.getString("subjectname", "");
        String teacher = preferences.getString("teachername", "");

        subjectname.setText(subject);
        teachername.setText(teacher);
    }
    private void upload(String subject,String teacher,String getlink)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        String emailnew=email.replace(".","A");

        if (user != null) {

            Date currentTime = Calendar.getInstance().getTime();
            String childkey=subject + emailnew+" : "+currentTime;

            HashMap hashMap=new HashMap();
            hashMap.put("Teacher",teacher);
            hashMap.put("Subject",subject);
            hashMap.put("Link",getlink);
            hashMap.put("nodekey",childkey);

            databaseReference.child(childkey).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    link.setText("");
                    Toast.makeText(TeacherPage.this, "Share this token with student", Toast.LENGTH_SHORT).show();
                    token.setVisibility(View.VISIBLE);
                    token.setText(childkey);
                }
            });
        }else {}

    }
}