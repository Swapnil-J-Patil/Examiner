package com.example.examiner.View.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examiner.R;
import com.example.examiner.View.Student.CheatDetection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {
    TextView alreadyHaveAnAccount;
    EditText inputemail,inputpassword,inputconfirmpassword;
    Button register;
    String emailPattern="[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        alreadyHaveAnAccount=findViewById(R.id.alreadyHaveAccount);
        inputemail=findViewById(R.id.emailregi);
        inputpassword=findViewById(R.id.passwordregi);
        inputconfirmpassword=findViewById(R.id.confirm_passwordregi);
        register=findViewById(R.id.btn_register);

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });

    }

    private void PerformAuth() {
        String email=inputemail.getText().toString();
        String password=inputpassword.getText().toString();
        String confirmpass=inputconfirmpassword.getText().toString();

        if(!email.matches(emailPattern))
        {
            inputemail.setError("Enter Correct E-mail");
        } else if (password.isEmpty() || password.length()<6)
        {
            inputpassword.setError("Enter Proper Password");
        } else if (!password.equals(confirmpass))
        {
            inputconfirmpassword.setError("Password does not match");
        }
        else
        {
            progressDialog.setMessage("Please Wait For Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendusetToNextActivity();
                        Toast.makeText(Registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Registration.this, "" +task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendusetToNextActivity() {
        Intent intent=new Intent(getApplicationContext(), CheatDetection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        overridePendingTransition(R.anim.left_slide_out, R.anim.right_slide_in);
        startActivity(intent);
    }
}