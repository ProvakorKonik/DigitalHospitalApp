package com.shrabonti.digitalhospital.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shrabonti.digitalhospital.Adapter.AppointmentAdapter;
import com.shrabonti.digitalhospital.Model.AppointmentModel;
import com.shrabonti.digitalhospital.R;
import com.shrabonti.digitalhospital.RecylerviewClickInterface;
import com.shrabonti.digitalhospital.ViewModel.AppointmentVM;

import java.util.ArrayList;
import java.util.List;

public class Appointments extends AppCompatActivity implements RecylerviewClickInterface {

    private RecyclerView mOrderRecyclerView;
    List<AppointmentModel> listOrderItem = new ArrayList<>();
    AppointmentAdapter mOrder_Adapter;

    private String dUserUID = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        mOrderRecyclerView = (RecyclerView)findViewById(R.id.order_recyclerview);

        //Login Check
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if(user != null){
                    String dsUserName = user.getDisplayName();
                    dUserUID = user.getUid();
                    getIntentMethod();
                    checkUserType();
                    // Toast.makeText(getApplicationContext(),"Add Level3 Information", Toast.LENGTH_SHORT).show();;
                }else{
                    Toast.makeText(getApplicationContext(),"Login", Toast.LENGTH_SHORT).show();;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        };
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
                    //Picasso.get().load(dUserPhotoUrl).fit().centerCrop().into(mUserImage);

                    if(dUserType.equals("Creator")){
                        getOrderListMethod("UidHospitalCreator");
                    }else{
                        getOrderListMethod("UidOrderCreator");

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error User Information Not Found", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Appointments.this, LoginRegistration.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void getOrderListMethod(String dUserType) {
        AppointmentVM AppointmentVM = new ViewModelProvider(this).get(AppointmentVM.class);
        Log.d("ViewModel", "allViewModel:1 AppointmentVM start");
        AppointmentVM.callOrderList(dsHospitalUID, dUserType,dUserUID).observe(this, new Observer<List<AppointmentModel>>() {
            @Override
            public void onChanged(List<AppointmentModel> list_Order_Item) {
                Log.d("ViewModel", "allViewModel:1 onChanged listview4 size = "+list_Order_Item.size());

                if (list_Order_Item.get(0).getOrderUID().equals("NULL")){
                    Toast.makeText(getApplicationContext(),"No Category Found ", Toast.LENGTH_SHORT).show();;
                }else{

                    mOrder_Adapter = new AppointmentAdapter(getApplicationContext(),list_Order_Item, Appointments.this);
                    mOrder_Adapter.notifyDataSetChanged();
                    listOrderItem = list_Order_Item;

                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        mOrderRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                        mOrderRecyclerView.setAdapter(mOrder_Adapter);
                    } else {
                        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                        mOrderRecyclerView.setAdapter(mOrder_Adapter);
                    }
                }
            }
        });

    }

    String dsHospitalUID = "NO", dsHospitalName = "NO";
    private boolean intentFoundError = true;
    private void getIntentMethod() {
        //////////////GET INTENT DATA
        final Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            dsHospitalUID = intent.getExtras().getString("dsHospitalUID");
            dsHospitalName = intent.getExtras().getString("dsHospitalName");
            intentFoundError = CheckIntentMethod(dsHospitalUID);
            intentFoundError = CheckIntentMethod(dsHospitalName);

            if(!intentFoundError){

            }else{
                Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();;
            }
        }else{
            dsHospitalUID = "NO";
            dsHospitalName = "NO";
            Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean CheckIntentMethod(String dsTestIntent){
        if(TextUtils.isEmpty(dsTestIntent)) {
            dsTestIntent= "NO";
            Toast.makeText(getApplicationContext(), "intent NULL  " , Toast.LENGTH_SHORT).show();
        }else if (dsHospitalUID.equals("")){
            dsTestIntent= "NO";
            Toast.makeText(getApplicationContext(), "intent 404" , Toast.LENGTH_SHORT).show();
        }else{
            intentFoundError = false;
        }
        return intentFoundError;
    }

    @Override
    public void onItemClick(int position) {
        String dsOrderUID = listOrderItem.get(position).getOrderUID();
        String dsHospitalCreatorUID = listOrderItem.get(position).getUidHospitalCreator();
        String dsOrderDoctorNote = listOrderItem.get(position).getDoctorNote();
        String dsOrderPatientNote = listOrderItem.get(position).getDoctorNote();

        Intent intent = new Intent(getApplicationContext(), AppointmentDetails.class);
        intent.putExtra("dsHospitalUID", dsHospitalUID);
        intent.putExtra("dsHospitalCreatorUID", dsHospitalCreatorUID);
        intent.putExtra("dsOrderUID", dsOrderUID);
        intent.putExtra("dsOrderDoctorNote", dsOrderDoctorNote);
        intent.putExtra("dsOrderPatientNote", dsOrderPatientNote);
        startActivity(intent);
    }

    @Override
    public void onAddCircleClick(int position) {

    }

    @Override
    public void onDoctorItemClick(int position) {

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