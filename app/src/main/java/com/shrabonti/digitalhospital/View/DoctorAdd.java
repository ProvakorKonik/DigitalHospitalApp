package com.shrabonti.digitalhospital.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shrabonti.digitalhospital.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DoctorAdd extends AppCompatActivity {

    //Note Search Key not added 404
    private ImageView mDoctor_ImageView;
    private EditText mDoctor_Name, mDoctor_Info,mDoctor_Address, mDoctor_TimeTable, mDoctor_Priority, mDoctor_Views;
    private EditText mDoctor_Rating, mDoctor_Orders, mDoctor_Price, mDoctor_Discount;
    private Button mDoctor_UpdateBtn;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page

    //Photo Selecting and Croping
    private final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropIng";
    Uri imageUri_storage;
    Uri imageUriResultCrop;

    //Firebase Storage
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();;
    StorageReference ref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Variablls
    private String dUserUID = "NO";
    private String dsPhotoUrl = "NO", dsDoctorName = "NO", dsDoctorInfo = "NO", dsDoctorPriority = "NO",  dsDoctorViews = "NO";
    private String  dsDoctorAddress = "NO", dsTimeTable = "NO", dsDoctor_Rating = "NO", dsDoctor_Orders = "NO", dsDoctor_Price = "NO", dsDoctor_Discount = "NO", dsDoctor_Extra = "NO";

    private int  diDoctorPriority = 0, diDoctorViews = 0;
    private int diRating = 0, diOrders = 0, diPrice = 0, diDiscount = 0;
    private String dsLevel1_Name = "NO", dsLevel2_Name = "NO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add);

        mDoctor_ImageView = (ImageView)findViewById(R.id.doctor_add_imageview);
        mDoctor_Name = (EditText)findViewById(R.id.doctor_add_name);
        mDoctor_Info = (EditText)findViewById(R.id.doctor_add_info);
        mDoctor_Address = (EditText)findViewById(R.id.doctor_add_address);
        mDoctor_TimeTable = (EditText)findViewById(R.id.doctor_add_Time);
        mDoctor_Priority = (EditText)findViewById(R.id.doctor_add_priority);
        mDoctor_Views = (EditText)findViewById(R.id.doctor_add_view);
        mDoctor_UpdateBtn = (Button)findViewById(R.id.doctor_add_update_btn);

        mDoctor_Rating = (EditText)findViewById(R.id.doctor_add_ratings);
        mDoctor_Orders = (EditText)findViewById(R.id.doctor_add_total_order);
        mDoctor_Price = (EditText)findViewById(R.id.doctor_add_price);
        mDoctor_Discount = (EditText) findViewById(R.id.doctor_add_discount);
        getIntentMethod();
        //Login Check
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if(user != null){
                    String dsUserName = user.getDisplayName();
                    dUserUID = user.getUid();

                    // Toast.makeText(getApplicationContext(),"Add Level3 Information", Toast.LENGTH_SHORT).show();;
                }else{
                    Toast.makeText(getApplicationContext(),"Login", Toast.LENGTH_SHORT).show();;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        };
        mDoctor_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent() //Image Selecting
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), CODE_IMG_GALLERY);
            }
        });

        mDoctor_UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dsDoctorName = mDoctor_Name.getText().toString();
                dsDoctorInfo = mDoctor_Info.getText().toString();
                dsDoctorPriority = mDoctor_Priority.getText().toString();
                dsDoctorViews = mDoctor_Views.getText().toString();

                dsDoctorAddress = mDoctor_Views.getText().toString();
                dsTimeTable = mDoctor_Views.getText().toString();

                dsDoctor_Rating = mDoctor_Rating.getText().toString();
                dsDoctor_Orders = mDoctor_Orders.getText().toString();
                dsDoctor_Price = mDoctor_Price.getText().toString();
                dsDoctor_Discount = mDoctor_Discount.getText().toString();
                dsDoctor_Extra = "NO";

                if(imageUriResultCrop == null){
                    if(imageUri_storage == null){
                        Toast.makeText(getApplicationContext(),"Please Select Image", Toast.LENGTH_SHORT).show();;
                    }else{
                        Toast.makeText(getApplicationContext(),"Please Crop Image", Toast.LENGTH_SHORT).show();;
                    }

                }else if(dsDoctorName.equals("NO")  || dsDoctorInfo.equals("NO") || dsDoctorPriority.equals("NO") ||  dsDoctorViews.equals("NO") ){
                    Toast.makeText(getApplicationContext(), "Please Fillup all", Toast.LENGTH_SHORT).show();
                }else if( dsDoctorName.equals("")  || dsDoctorInfo.equals("") || dsDoctorPriority.equals("") ||  dsDoctorViews.equals("") ){
                    Toast.makeText(getApplicationContext(), "Please Fillup all", Toast.LENGTH_SHORT).show();
                }else if(dsDoctorAddress.equals("NO")  || dsTimeTable.equals("NO") ||dsDoctor_Rating.equals("NO")  || dsDoctor_Orders.equals("NO") || dsDoctor_Price.equals("NO") ||  dsDoctor_Discount.equals("NO") ){
                    Toast.makeText(getApplicationContext(), "Please Fillup all", Toast.LENGTH_SHORT).show();
                }else if(dsDoctorAddress.equals("")  || dsTimeTable.equals("") ||dsDoctor_Rating.equals("")  || dsDoctor_Orders.equals("") || dsDoctor_Price.equals("") ||  dsDoctor_Discount.equals("") ){
                    Toast.makeText(getApplicationContext(), "Please Fillup all", Toast.LENGTH_SHORT).show();
                }else{
                    diDoctorPriority = Integer.parseInt(dsDoctorPriority);
                    diDoctorViews = Integer.parseInt(dsDoctorViews);

                    diRating   = Integer.parseInt(dsDoctor_Rating);
                    diOrders   = Integer.parseInt(dsDoctor_Orders);
                    diPrice    = Integer.parseInt(dsDoctor_Price);
                    diDiscount = Integer.parseInt(dsDoctor_Discount);
                    UploadCropedImageFunction(imageUriResultCrop);
                }
            }
        });
    }



    private void UploadCropedImageFunction(Uri filePath) {
        if(filePath != null)
        {
            dUserUID = FirebaseAuth.getInstance().getUid();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String dsTimeMiliSeconds = String.valueOf(System.currentTimeMillis());

            ref = storageReference.child("Hospital/CoverPic/"+dsHospitalName+"/Doctor/"+dsDoctorName+ dsTimeMiliSeconds +"."+getFileExtention(filePath));
            //ref = storageReference.child("Hospital/CoverPic/"+dsHospitalName+"/"+"Category/"+dsCategoryName+ dsTimeMiliSeconds +"."+getFileExtention(filePath));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        //Photo Uploaded now get the URL
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String dPhotoURL = uri.toString();
                                    Toast.makeText(getApplicationContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();

                                    Map<String, Object> note = new HashMap<>();
                                    note.put("DoctorName", dsDoctorName);
                                    note.put("DoctorPhotoUrl", dPhotoURL);
                                    note.put("DoctorBio", dsDoctorInfo);
                                    note.put("DoctorCreator", dUserUID);
                                    note.put("DoctorAddress", dsDoctorAddress);
                                    note.put("DoctorTimeTable", dsTimeTable);
                                    note.put("DoctorExtra", "NO");
                                    note.put("DoctorDesignation", "Senior Professor");
                                    note.put("DoctorCategory", dsCategoryUID);

                                    note.put("DoctoriViews", diDoctorViews);
                                    note.put("DoctoriPriority", diDoctorPriority);

                                    note.put("DoctoriRating", diRating);
                                    note.put("DoctoriOrders", diOrders);
                                    note.put("DoctoriPrice", diPrice);
                                    note.put("DoctoriDiscount", diDiscount);




                                    db.collection("AllHospital").document(dsHospitalUID).collection("AllDoctor").add(note)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    //String dsLevel3_UID = documentReference.getId();
                                                    Toast.makeText(getApplicationContext(),"Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    mDoctor_UpdateBtn.setText("Upload Again");
                                                    mDoctor_Name.setText("");
                                                    mDoctor_Info.setText("");
                                                    mDoctor_Priority.setText("");
                                                    mDoctor_Views.setText("");

                                                   /* finish();
                                                    Intent intent = new Intent(DoctorAdd.this, MainActivity.class);
                                                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    startActivity(intent);*/
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            mDoctor_UpdateBtn.setText("Try Again");
                                            mDoctor_Name.setText("Failed");
                                            mDoctor_Info.setText("");
                                            mDoctor_Priority.setText("");
                                            Toast.makeText(getApplicationContext(),"Failed Please Try Again", Toast.LENGTH_SHORT).show();

                                        }
                                    });



                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            mDoctor_UpdateBtn.setText("Failed Photo Upload");
                            Toast.makeText(getApplicationContext(), "Failed Photo"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Upload Failed Photo Not Found ", Toast.LENGTH_SHORT).show();
        }
    }


    //Dont forget to add class code on MainfestXml
    @Override   //Selecting Image
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK &&  data.getData() != null && data != null){
            //Photo Successfully Selected
            imageUri_storage = data.getData();
            String dFileSize = getSize(imageUri_storage);       //GETTING IMAGE FILE SIZE
            double  dFileSizeDouble = Double.parseDouble(dFileSize);
            int dMB = 1000;
            dFileSizeDouble =  dFileSizeDouble/dMB;
            //dFileSizeDouble =  dFileSizeDouble/dMB;

            if(dFileSizeDouble <= 5000){
                Picasso.get().load(imageUri_storage).into(mDoctor_ImageView);
                Toast.makeText(getApplicationContext(),"Selected",Toast.LENGTH_SHORT).show();
                //startCrop(imageUri_storage);
                imageUriResultCrop = imageUri_storage;
            }else{
                Toast.makeText(this, "Failed! (File is Larger Than 5MB)",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Canceled",Toast.LENGTH_SHORT).show();
        }
    }
    public String getSize(Uri uri) {
        String fileSize = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {

                // get file size
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getString(sizeIndex);
                }
            }
        } finally {
            cursor.close();
        }
        return fileSize;
    }
    private String getFileExtention(Uri uri){   //IMAGE
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        //Not worked in Croped File so i constant it
        return "JPEG";
    }





    String dsHospitalUID = "NO", dsHospitalName = "NO", dsCategoryUID = "NO", dsCategoryName = "NO";
    private boolean intentFoundError = true;
    private void getIntentMethod() {
        //////////////GET INTENT DATA
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            dsHospitalUID = intent.getExtras().getString( "dsHospitalUID");
            dsHospitalName = intent.getExtras().getString("dsHospitalName");
            dsCategoryUID = intent.getExtras().getString( "dsCategoryUID");
            dsCategoryName = intent.getExtras().getString("dsCategoryName");

            intentFoundError = CheckIntentMethod(dsHospitalUID);
            intentFoundError = CheckIntentMethod(dsHospitalName);
            intentFoundError = CheckIntentMethod(dsCategoryUID);
            intentFoundError = CheckIntentMethod(dsCategoryName);

            if (!intentFoundError) {

            } else {
                Toast.makeText(getApplicationContext(), "Intent error", Toast.LENGTH_SHORT).show();
                ;
            }
        } else {
            dsHospitalUID = "NO";
            dsHospitalName = "NO";
            dsCategoryUID = "NO";
            dsCategoryName = "NO";

            Toast.makeText(getApplicationContext(), "Intent error", Toast.LENGTH_SHORT).show();
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