package com.shrabonti.digitalhospital;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shrabonti.digitalhospital.Adapter.HospitalAdapter;
import com.shrabonti.digitalhospital.View.Category;
import com.shrabonti.digitalhospital.View.CategoryAdd;
import com.shrabonti.digitalhospital.View.Hospital_Add;
import com.shrabonti.digitalhospital.View.LoginRegistration;
import com.shrabonti.digitalhospital.ViewModel.HospitalVM;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecylerviewClickInterface {
    private Button mLoginBtn;
    private Button mAddHospitalBtn;
    //RecyclerView
    private RecyclerView mBook_RecyclerView;
    List<HospitalModel> listHospitalItem;
    HospitalAdapter mhospital_adapter;

    //Firebase Auth
    private String dUserUID = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginBtn = (Button)findViewById(R.id.main_login_btn);
        mAddHospitalBtn = (Button)findViewById(R.id.main_add_hospital);
        mBook_RecyclerView = (RecyclerView)findViewById(R.id.main_hospital_recyclerview);
        listHospitalItem = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    dUserUID = user.getUid();
                    mLoginBtn.setText("My Profile");
                    checkUserType();
                }else{
                    /*Toast.makeText(getApplicationContext(),"Please Login", Toast.LENGTH_SHORT).show();;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);*/
                }
            }
        };


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginCheck.class);
                intent.setFlags( intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        mAddHospitalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Hospital_Add.class);
                startActivity(intent);
            }
        });
        callViewModel();
    }

    //View Model
    private HospitalVM hospitalVm;
    private void callViewModel() {
        Log.d("ViewModel", "allViewModel:1 hospitalVm start");
        hospitalVm = new ViewModelProvider(this).get(HospitalVM.class);
        hospitalVm.LoadLevel4List().observe(this, new Observer<List<HospitalModel>>() {
            @Override
            public void onChanged(List<HospitalModel> list_hospital_models) {
                Log.d("ViewModel", "allViewModel:1 onChanged listview4 size = "+list_hospital_models.size());
                if (list_hospital_models.get(0).getHospitalName().equals("NULL")){
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "No Items Found", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }else{

                    mhospital_adapter = new HospitalAdapter(MainActivity.this,list_hospital_models,MainActivity.this);
                    mhospital_adapter.notifyDataSetChanged();
                    listHospitalItem = list_hospital_models;

                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        mBook_RecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                        mBook_RecyclerView.setAdapter(mhospital_adapter);
                    } else {
                        mBook_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                        mBook_RecyclerView.setAdapter(mhospital_adapter);
                    }
                }
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
                    if(dUserType.equals("Teacher")){
                        //mAddBook.setVisibility(View.VISIBLE);
                    }else{
                        //mAddBook.setVisibility(View.GONE);

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error User Information Not Found", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginRegistration.class);
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

    @Override
    public void onItemClick(int position) {
        String dsHospitalUID = listHospitalItem.get(position).getHospitalUID();
        String dsHospitalName = listHospitalItem.get(position).getHospitalName();

        Intent intent = new Intent(getApplicationContext(), Category.class);
        intent.putExtra("dsHospitalUID", dsHospitalUID);
        intent.putExtra("dsHospitalName", dsHospitalName);
        startActivity(intent);
    }
}