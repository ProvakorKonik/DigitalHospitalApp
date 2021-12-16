package com.shrabonti.digitalhospital.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.shrabonti.digitalhospital.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentDetails extends AppCompatActivity {

    private ImageView mDoctorImage, mPatientImage;
    private TextView mDoctorName, mDoctorNote, mDateTimeTxt;
    private TextView mPatientName, mPatientNote, mPatientPhoneNo;

    private LinearLayout mAdminSentLinearLayout, mPatientSentLinearLayout;
    private EditText mAdminNoteEdit, mPatientNoteEdit;
    private Button mAdminNoteSentBtn, mPatientNoteSentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        mDoctorImage = (ImageView) findViewById(R.id.details_doctor_image);
        mDateTimeTxt = (TextView) findViewById(R.id.details_time_date_text);
        mDoctorName = (TextView) findViewById(R.id.details_doctor_name);
        mDoctorNote = (TextView) findViewById(R.id.details_doctor_note_text);

        mPatientImage = (ImageView) findViewById(R.id.details_patient_iamge);
        mPatientName = (TextView) findViewById(R.id.details_patient_name);
        mPatientNote = (TextView) findViewById(R.id.details_patient_note_text);
        mPatientPhoneNo = (TextView) findViewById(R.id.details_patient_phone_text);

        mAdminSentLinearLayout = (LinearLayout) findViewById(R.id.details_admin_linearlayout);
        mPatientSentLinearLayout = (LinearLayout) findViewById(R.id.details_patient_linearlayout);
        mAdminNoteEdit = (EditText)findViewById(R.id.details_doctor_edit_note);
        mPatientNoteEdit = (EditText)findViewById(R.id.details_patient_edit_note);
        mAdminNoteSentBtn = (Button)findViewById(R.id.details_doctor_note_send_btn);
        mPatientNoteSentBtn = (Button)findViewById(R.id.details_patient_note_send_btn);


        getIntentMethod();

        mAdminNoteSentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dsGetAdminNote = mAdminNoteEdit.getText().toString();
                db.collection("AllHospital").document(dsHospitalUID).collection("Orders")
                        .document("Dates").collection("Today")
                        .document(dsOrderUID).update("DoctorNote",dsGetAdminNote);
                mDoctorNote.setText("Note: "+dsGetAdminNote);
                mAdminSentLinearLayout.setVisibility(View.GONE);
            }
        });
        mPatientNoteSentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dsGetPatientNote = mPatientNoteEdit.getText().toString();
                db.collection("AllHospital").document(dsHospitalUID).collection("Orders")
                        .document("Dates").collection("Today")
                        .document(dsOrderUID).update("DoctorExtra",dsGetPatientNote);
                mPatientNote.setText("Note: "+dsGetPatientNote);
                mPatientSentLinearLayout.setVisibility(View.GONE);
            }
        });

    }
    CollectionReference notebookRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void getDataFromServer() {


        notebookRef = db.collection("AllHospital").document(dsHospitalUID).collection("Orders")
                .document("Dates").collection("Today");
        notebookRef.document(dsOrderUID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String dsDoctorName = documentSnapshot.getString("DoctorName");
                            String dsDoctorPhotoURL = documentSnapshot.getString("DoctorPHOTO");
                            String dsDoctorNote = documentSnapshot.getString("DoctorNote");
                            String dsPatientUID = documentSnapshot.getString("UidOrderCreator");
                            String dsPatientNote = documentSnapshot.getString("DoctorExtra");
                            Date ddDate = documentSnapshot.getDate("DoctorOrderTime");

                            mDoctorName.setText(dsDoctorName);
                            mDoctorNote.setText("Note: " + dsDoctorNote);
                            mPatientNote.setText("Note: " + dsPatientNote);
                            Picasso.get().load(dsDoctorPhotoURL).fit().centerCrop().into(mDoctorImage);
                            getPatientData(dsPatientUID);
                            //Date
                            Date date =  ddDate;
                            SimpleDateFormat df2 = new SimpleDateFormat("hh:mma  dd/MMM/yy");
                            String dateText = df2.format(date);
                            mDateTimeTxt.setText(dateText);

                        } else {
                            Toast.makeText(getApplicationContext(), "Server Data 404", Toast.LENGTH_SHORT).show();


                        }
                    }
                });

    }
    private String dUserUID = "NO";
    public void getPatientData(String dsPatientUID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            dUserUID = user.getUid().toString();

            if(dUserUID.equals(dsHospitalCreatorUID)){
                //Hospital Owner Viewing this Appointment
                mPatientSentLinearLayout.setVisibility(View.GONE);
                mAdminSentLinearLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Welcome Hospital Owner ", Toast.LENGTH_SHORT).show();
            }else{
                mAdminSentLinearLayout.setVisibility(View.GONE);
                mPatientSentLinearLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Hi Patient "+dsHospitalCreatorUID, Toast.LENGTH_SHORT).show();
            }

            db.collection("Users").document(dsPatientUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String dUserType = documentSnapshot.getString("userType");
                        String photoURL = documentSnapshot.getString("photoURL");
                        String name = documentSnapshot.getString("name");
                        String dsPhoneNO = documentSnapshot.getString("phone_no");
                        Picasso.get().load(photoURL).fit().centerCrop().into(mPatientImage);
                        mPatientName.setText(name);
                        mPatientPhoneNo.setText("Call: "+dsPhoneNO);
                    }else{
                        Toast.makeText(getApplicationContext(),"Patient Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(getApplicationContext(),"Please Login", Toast.LENGTH_SHORT).show();
        }

    }

    String dsHospitalUID = "NO",dsHospitalCreatorUID = "NO", dsOrderUID = "NO";
    String dsOrderDoctorNote = "NO", dsOrderPatientNote = "NO";
    private boolean intentFoundError = true;

    private void getIntentMethod() {
        //////////////GET INTENT DATA
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            dsHospitalUID = intent.getExtras().getString("dsHospitalUID");
            dsHospitalCreatorUID = intent.getExtras().getString("dsHospitalCreatorUID");
            dsOrderUID = intent.getExtras().getString("dsOrderUID");
            dsOrderDoctorNote = intent.getExtras().getString("dsOrderDoctorNote");
            dsOrderPatientNote = intent.getExtras().getString("dsOrderPatientNote");

            intentFoundError = CheckIntentMethod(dsHospitalUID);
            intentFoundError = CheckIntentMethod(dsHospitalCreatorUID);
            intentFoundError = CheckIntentMethod(dsOrderUID);
            intentFoundError = CheckIntentMethod(dsOrderDoctorNote);
            intentFoundError = CheckIntentMethod(dsOrderPatientNote);

            if (!intentFoundError) {
                getDataFromServer();
            } else {
                Toast.makeText(getApplicationContext(), "Intent error", Toast.LENGTH_SHORT).show();
            }
        } else {
            dsHospitalUID = "NO";
            dsOrderUID = "NO";
            dsOrderDoctorNote = "NO";
            dsOrderPatientNote = "NO";
            Toast.makeText(getApplicationContext(), "Intent error", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean CheckIntentMethod(String dsTestIntent) {
        if (TextUtils.isEmpty(dsTestIntent)) {
            dsTestIntent = "NO";
            Toast.makeText(getApplicationContext(), "intent NULL  ", Toast.LENGTH_SHORT).show();
        } else if (dsTestIntent.equals("")) {
            dsTestIntent = "NO";
            Toast.makeText(getApplicationContext(), "intent 404", Toast.LENGTH_SHORT).show();
        } else {
            intentFoundError = false;
        }
        return intentFoundError;
    }

}