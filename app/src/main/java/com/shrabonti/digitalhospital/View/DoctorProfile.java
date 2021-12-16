package com.shrabonti.digitalhospital.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shrabonti.digitalhospital.Model.DoctorModel;
import com.shrabonti.digitalhospital.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DoctorProfile extends AppCompatActivity {
    private ImageView mDoctorImage;
    private Button mDoctorBookedBtn;
    private TextView mDoctorName, mDoctorDesignation, mDoctorTimeTable, mDoctorRatingTxt, mDoctorInfo, mDoctorTotalOrderText, mDoctorTotalViewText, mDoctorPriceText, mDoctorAddress;
    private String dsDoctorName = "NO", dsDoctorDesignation = "NO", dsDoctorTimeTable = "NO", dsDoctorRatingTxt = "NO", dsDoctorInfo = "NO", dsDoctorTotalOrderText = "NO", dsDoctorTotalViewText = "NO", dsDoctorPriceText = "NO", dsDoctorAddress = "NO";

    //Firebase Auth
    private String dUserUID = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        mDoctorImage  = (ImageView)findViewById(R.id.doctor_profile_imageview);
        mDoctorName = (TextView)findViewById(R.id.doctor_profile_name);
        mDoctorDesignation = (TextView)findViewById(R.id.doctor_profile_designation);
        mDoctorTimeTable = (TextView)findViewById(R.id.doctor_profile_time_table);
        mDoctorRatingTxt = (TextView)findViewById(R.id.doctor_profile_rating_text);
        mDoctorInfo = (TextView)findViewById(R.id.doctor_profile_info_about);
        mDoctorTotalOrderText = (TextView)findViewById(R.id.doctor_profile_total_order);
        mDoctorTotalViewText =(TextView)findViewById(R.id.doctor_profile_total_view);
        mDoctorPriceText = (TextView)findViewById(R.id.doctor_profile_charge);
        mDoctorAddress = (TextView)findViewById(R.id.doctor_profile_address);
        mDoctorBookedBtn = (Button)findViewById(R.id.doctor_profile_booked_btn);


        //Coding
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d("HomeFragment", " user not null");
                    dUserUID = user.getUid();
                    getIntentMethod();
                }else{
                    Log.d("HomeFragment","user not null");
                    //mLoginBtn.setText("Login Here");
                    /*Toast.makeText(getApplicationContext(),"Please Login", Toast.LENGTH_SHORT).show();;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);*/
                }
            }


        };


        mDoctorBookedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dUserUID.equals("NO")){
                    Toast.makeText(getApplicationContext(), "Login Please", Toast.LENGTH_LONG).show();
                }else if(!intentFoundError && doctorModel != null)
                    setDataToServer();
                else
                    Toast.makeText(getApplicationContext(), "Intent Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setDataToServer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ViewModel", "allViewModel:4 mDoctorModelLiveData start");
        db.collection("AllHospital").document(dsHospitalUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String dsHospitalCreatorUID = documentSnapshot.getString("HospitalCreator");
                    Map<String, Object> cart = new HashMap<>();
                    String dsDoctorUID = doctorModel.getDoctoUID();
                    cart.put("DoctorUID", dsDoctorUID);
                    cart.put("DoctorName", doctorModel.getDoctorName());
                    cart.put("DoctorPHOTO", doctorModel.getDoctorPhotoUrl()); //map is done
                    cart.put("DoctorNote", "NO");
                    cart.put("DoctorExtra", "NO");
                    cart.put("DoctoriPrice", doctorModel.getDoctoriPrice()); //Integer
                    FieldValue ddDate =  FieldValue.serverTimestamp();
                    cart.put("DoctorOrderTime", ddDate); //Integer
                    cart.put("UidHospitalCreator", dsHospitalCreatorUID); //Integer
                    cart.put("UidOrderCreator", dUserUID); //Integer

                    db.collection("AllHospital").document(dsHospitalUID).collection("Appointments")
                            .document("Dates").collection("Today").add(cart).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Docter Booked", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }
    CollectionReference notebookRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DoctorModel doctorModel;
    private void getDoctorData(String dsHospitalUID, String dsDoctorUID) {

         db = FirebaseFirestore.getInstance();
        Log.d("ViewModel", "allViewModel:4 mDoctorModelLiveData start");
        notebookRef = db.collection("AllHospital").document(dsHospitalUID).collection("AllDoctor");
        notebookRef.document(dsDoctorUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    doctorModel = documentSnapshot.toObject(DoctorModel.class);
                    String DoctorUID = documentSnapshot.getId();
                    doctorModel.setDoctoUID(DoctorUID);
                    String doctorName = doctorModel.getDoctorName();
                    String doctorPhotoUrl = doctorModel.getDoctorPhotoUrl();
                    String doctorBio =  doctorModel.getDoctorBio();
                    String doctorCreator =  doctorModel.getDoctorCreator();
                    String doctorAddress =  doctorModel.getDoctorAddress();
                    String doctorTimeTable =  doctorModel.getDoctorTimeTable();
                    String doctorExtra =  doctorModel.getDoctorExtra();
                    String doctorDesignation =  doctorModel.getDoctorDesignation();

                    String doctorCategory =  doctorModel.getDoctorCategory();
                    long doctoriViews =  doctorModel.getDoctoriViews();
                    long doctoriPriority =  doctorModel.getDoctoriPriority();
                    long doctoriRating = doctorModel.getDoctoriRating();
                    long doctoriOrders =  doctorModel.getDoctoriOrders();
                    long doctoriPrice =  doctorModel.getDoctoriPrice();
                    long doctoriDiscount =  doctorModel.getDoctoriDiscount();

                    Picasso.get().load(doctorPhotoUrl).fit().centerCrop().into(mDoctorImage);
                    mDoctorName.setText(doctorName);
                    mDoctorDesignation.setText(doctorDesignation);
                    mDoctorInfo.setText(doctorBio);
                    mDoctorTimeTable.setText(doctorTimeTable);
                    mDoctorRatingTxt.setText(String.valueOf(doctoriRating));
                    mDoctorTotalOrderText.setText(String.valueOf(doctoriOrders)+"+ Appointments");
                    mDoctorTotalViewText.setText(String.valueOf(doctoriViews)+"+ Viewed");
                    mDoctorPriceText.setText(String.valueOf(doctoriPrice)+"+ Charge");
                    mDoctorAddress.setText(doctorAddress);
                }else{
                    Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    String dsHospitalUID = "NO", dsHospitalName = "NO", dsDoctorUID = "NO";
    private boolean intentFoundError = true;
    private void getIntentMethod() {
        //////////////GET INTENT DATA
        final Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            dsHospitalUID = intent.getExtras().getString("dsHospitalUID");
            dsHospitalName = intent.getExtras().getString("dsHospitalName");
            dsDoctorUID = intent.getExtras().getString("dsDoctorUID");
            intentFoundError = CheckIntentMethod(dsHospitalUID);
            intentFoundError = CheckIntentMethod(dsHospitalName);
            intentFoundError = CheckIntentMethod(dsDoctorUID);

            if(!intentFoundError){
                getDoctorData(dsHospitalUID, dsDoctorUID);
            }else{
                Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();;
            }
        }else{
            dsHospitalUID = "NO";
            dsHospitalName = "NO";
            dsDoctorUID = "NO";
            Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean CheckIntentMethod(String dsTestIntent){
        if(TextUtils.isEmpty(dsTestIntent)) {
            dsTestIntent= "NO";
            Toast.makeText(getApplicationContext(), "intent NULL  " , Toast.LENGTH_SHORT).show();
        }else if (dsTestIntent.equals("")){
            dsTestIntent= "NO";
            Toast.makeText(getApplicationContext(), "intent 404" , Toast.LENGTH_SHORT).show();
        }else{
            intentFoundError = false;
        }
        return intentFoundError;
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