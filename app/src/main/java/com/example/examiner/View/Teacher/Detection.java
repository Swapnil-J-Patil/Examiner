package com.example.examiner.View.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examiner.Model.ProjectModel;
import com.example.examiner.R;
import com.example.examiner.Controller.TeacherAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Detection extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ProjectModel> recyclelist;
    Button submit;
    EditText token;
    ImageView imageView;
    TextView eventlog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        submit=findViewById(R.id.btn_submit_token);
        eventlog=findViewById(R.id.event);
        token=findViewById(R.id.search_token);
        imageView=findViewById(R.id.help_and_support);
        recyclerView = findViewById(R.id.recyclerviewteacher);
        recyclelist = new ArrayList<>();

        TeacherAdapter recyclerAdapter=new TeacherAdapter(getApplicationContext(), recyclelist);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.setVisibility(View.GONE);
        eventlog.setVisibility(View.GONE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String istoken = token.getText().toString();
                if (istoken.isEmpty()) {
                    token.setError("Please Enter a token");
                }
                else {
                    submit.setVisibility(View.GONE);
                    token.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    eventlog.setVisibility(View.VISIBLE);

                    Toast.makeText(Detection.this, "You can monitor your student's actions now..", Toast.LENGTH_LONG).show();

                    String getToken = token.getText().toString();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("StudentAction").child(getToken);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            recyclerAdapter.clearData();
                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                ProjectModel projectModel=dataSnapshot.getValue(ProjectModel.class);
                                recyclelist.add(projectModel);
                            }
                            recyclerAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}