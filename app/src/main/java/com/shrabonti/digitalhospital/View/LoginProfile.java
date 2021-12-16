package com.shrabonti.digitalhospital.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shrabonti.digitalhospital.R;
import com.squareup.picasso.Picasso;

public class LoginProfile extends AppCompatActivity {
    private Button mSignOutBtn;
    private ImageView mUserImage;
    private TextView mNameText, mExamAmount, mUserTypeText;
    //Firebase Auth
    private String dUserUID = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profile);

        mUserImage = (ImageView) findViewById(R.id.profile_picture);
        mSignOutBtn = (Button) findViewById(R.id.profile_sign_out_btn);
        mNameText = (TextView) findViewById(R.id.profile_name_text);
        mExamAmount = (TextView)findViewById(R.id.profile_exam_amount_text);
        mUserTypeText = (TextView)findViewById(R.id.profile_user_type_text);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    dUserUID = user.getUid();
                    checkUserType();
                }else{

                    Toast.makeText(getApplicationContext(),"Please Login", Toast.LENGTH_SHORT).show();;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(),"Please Login", Toast.LENGTH_SHORT).show();;
                startActivity(intent);
            }
        });
    }
    public void checkUserType(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(dUserUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String dUserType = documentSnapshot.getString("userType");
                    String dUserName= documentSnapshot.getString("name");
                    String dUserEmail= documentSnapshot.getString("email");
                    String dUserPhotoUrl = documentSnapshot.getString("photoURL");
                    Picasso.get().load(dUserPhotoUrl).fit().centerCrop().into(mUserImage);

                    mNameText.setText(dUserName);
                    mExamAmount.setText(dUserEmail);
                    mUserTypeText.setText("User Type: "+dUserType);
                    if(dUserType.equals("Teacher")){
                        //mAddBook.setVisibility(View.VISIBLE);
                    }else{
                        //mAddBook.setVisibility(View.GONE);

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error User Information Not Found", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginProfile.this, LoginRegistration.class);
                    startActivity(intent);
                }
            }
        });
    }

        @Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }
        @Override
        public void onStop() {
            super.onStop();
            if(mAuthListener != null){
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }
}