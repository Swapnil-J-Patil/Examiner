package com.example.examiner.View.Navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.examiner.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Rate extends AppCompatActivity {

    EditText writereview;
    RatingBar ratingBar;
    Button submit;
    DatabaseReference databaseReference;

    float ratevalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Rating");
        ratingBar=findViewById(R.id.ratingbar);
        submit=findViewById(R.id.btn_submitrating);
        writereview=findViewById(R.id.writereview);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ratinggiven;
                        HashMap hashMap=new HashMap();
                        ratevalue=ratingBar.getRating();
                        String ratingwritten=writereview.getText().toString();

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String email = user.getEmail();
                            String emailnew = email.replace(".", "A");
                            if (ratevalue <= 1 && ratevalue > 0) {
                                ratinggiven = "Bad " + ratevalue + "/5";
                                hashMap.put("Rate", ratinggiven);
                                hashMap.put("Review", ratingwritten);
                                databaseReference.child(emailnew).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Rate.this, "Your Review Has Been Recorded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (ratevalue <= 2 && ratevalue > 1) {
                                ratinggiven = "Ok " + ratevalue + "/5";
                                hashMap.put("Rate", ratinggiven);
                                hashMap.put("Review", ratingwritten);
                                databaseReference.child(emailnew).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Rate.this, "Your Review Has Been Recorded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (ratevalue <= 3 && ratevalue > 2) {
                                ratinggiven = "Good " + ratevalue + "/5";
                                hashMap.put("Rate", ratinggiven);
                                hashMap.put("Review", ratingwritten);
                                databaseReference.child(emailnew).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Rate.this, "Your Review Has Been Recorded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (ratevalue <= 4 && ratevalue > 3) {
                                ratinggiven = "Very Good " + ratevalue + "/5";
                                hashMap.put("Rate", ratinggiven);
                                hashMap.put("Review", ratingwritten);
                                databaseReference.child(emailnew).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Rate.this, "Your Review Has Been Recorded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (ratevalue <= 5 && ratevalue > 4) {
                                ratinggiven = "Best " + ratevalue + "/5";
                                hashMap.put("Rate", ratinggiven);
                                hashMap.put("Review", ratingwritten);
                                databaseReference.child(emailnew).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Rate.this, "Your Review Has Been Recorded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(Rate.this, "Give some Rating", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
}