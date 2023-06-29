package com.example.examiner.View.Navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.examiner.View.Authentication.Login;
import com.example.examiner.R;
import com.example.examiner.View.Student.StudentPage;
import com.example.examiner.View.Teacher.TeacherPage;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mfirebaseAuth;
    CardView student,teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        student=findViewById(R.id.studentcard);
        teacher=findViewById(R.id.teachercard);

        mfirebaseAuth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        MaterialToolbar toolbar=findViewById(R.id.topappbar);
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.navigation_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {
                    case R.id.nav_logout:
                        Intent intent= getIntent();
                        int flags= intent.getIntExtra("flag",0);//key
                        if(flags==1)
                        {
                            signOut();
                            Toast.makeText(HomeActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            mfirebaseAuth.signOut();
                            Toast.makeText(HomeActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        break;
                    case R.id.nav_share:
                        Intent intent1=new Intent(Intent.ACTION_SEND);
                        intent1.setType("text/plain");
                        String body="Download this App";
                        String Sub="https://drive.google.com/drive/folders/1XCZ6rIknICkPbxty6Gt1vCVQ-KQMHwJj?usp=sharing";
                        intent1.putExtra(Intent.EXTRA_TEXT,body);
                        intent1.putExtra(Intent.EXTRA_TEXT,Sub);
                        startActivity(Intent.createChooser(intent1,"Share Using"));
                        break;
                    case R.id.nav_help:
                        startActivity(new Intent(getApplicationContext(), Help.class));
                        overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
                        break;
                    case R.id.nav_about_us:
                        startActivity(new Intent(getApplicationContext(), AboutUs.class));
                        overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
                        break;
                    case R.id.nav_rate:
                        startActivity(new Intent(getApplicationContext(), Rate.class));
                        overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), StudentPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out, R.anim.right_slide_in);
            }
        });
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out, R.anim.right_slide_in);
            }
        });
    }
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
                finish();
            }
        });
    }
}