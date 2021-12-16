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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shrabonti.digitalhospital.Adapter.CategoryAdapter;
import com.shrabonti.digitalhospital.Adapter.DoctorAdapter;
import com.shrabonti.digitalhospital.Model.CategoryModel;
import com.shrabonti.digitalhospital.Model.DoctorModel;
import com.shrabonti.digitalhospital.R;
import com.shrabonti.digitalhospital.RecylerviewClickInterface;
import com.shrabonti.digitalhospital.ViewModel.CategoryVM;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity implements RecylerviewClickInterface {

    private Button mAddCategoryBtn;
    private Button mOrderActivityBtn;
    //RecyclerView
    private RecyclerView mCategory_RecyclerView;
    List<CategoryModel> listCategoryItem = new ArrayList<>();;
    CategoryAdapter mCategory_Adapter;

    //Doctor RecyclerView
    private RecyclerView mDoctor_RecyclerView;
    List<DoctorModel> listDoctorItemMain = new ArrayList<>();;
    DoctorAdapter mDoctor_Adapter;

    //Firebase Auth
    private String dUserUID = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page

    boolean HospitalOwnerFoundBool = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mAddCategoryBtn = (Button)findViewById(R.id.category_add_btn);
        mOrderActivityBtn = (Button)findViewById(R.id.category_orders_btn);

        mCategory_RecyclerView = (RecyclerView)findViewById(R.id.category_recyclerview);
        mDoctor_RecyclerView = (RecyclerView)findViewById(R.id.category_doctor_recyclerview);
        categoryVM = new ViewModelProvider(this).get(CategoryVM.class); //View Model Initialize
        getIntentMethod();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    dUserUID = user.getUid();
                }
                if(dUserUID.equals(dsHospitalCreatorUID)){
                    HospitalOwnerFoundBool = true;
                    mAddCategoryBtn.setVisibility(View.VISIBLE);
                }
            }
        };

        mAddCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dUserUID.equals(dsHospitalCreatorUID)){
                    Intent intent = new Intent(getApplicationContext(), CategoryAdd.class);
                    intent.putExtra("dsHospitalUID",  dsHospitalUID);
                    intent.putExtra("dsHospitalName", dsHospitalName);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"You are not Owner of this Hospital", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mOrderActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Appointments.class);  //Error Check login before going to this activity
                intent.putExtra("dsHospitalUID",  dsHospitalUID );
                intent.putExtra("dsHospitalName", dsHospitalName);
                startActivity(intent);
            }
        });

    }
    private CategoryVM categoryVM;
    private void callViewModel() {
        Log.d("ViewModel", "allViewModel:1 categoryVM start");

        categoryVM.LoadCategoryList(dsHospitalUID).observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> list_cateagory_models) {
                if(list_cateagory_models != null){
                    Log.d("ViewModel", "allViewModel:1 onChanged listview4 size = "+list_cateagory_models.size());
                    if (list_cateagory_models.get(0).getCategoryName().equals("NULL")){
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

                        mCategory_Adapter = new CategoryAdapter(Category.this,list_cateagory_models,Category.this,HospitalOwnerFoundBool);
                        mCategory_Adapter.notifyDataSetChanged();
                        listCategoryItem = list_cateagory_models;

                        int orientation = getResources().getConfiguration().orientation;
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            mCategory_RecyclerView.setLayoutManager(new GridLayoutManager(Category.this,2));
                            mCategory_RecyclerView.setAdapter(mCategory_Adapter);
                        } else {
                            mCategory_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));
                            mCategory_RecyclerView.setAdapter(mCategory_Adapter);
                        }
                    }
                }

            }
        });
    }

    private void callDoctorViewModel(String dsCategoryUID) {
        Log.d("ViewModel", "allViewModel:1 homeViewModel start");
        categoryVM.callDoctorModelList(dsHospitalUID, dsCategoryUID).observe(this, new Observer<List<DoctorModel>>() {
            @Override
            public void onChanged(List<DoctorModel> list_Doctor_Item) {
                Log.d("ViewModel", "allViewModel:1 onChanged listview4 size = "+list_Doctor_Item.size());
                if(list_Doctor_Item != null){
                    if (list_Doctor_Item.get(0).getDoctorName().equals("NULL")){
                        Toast.makeText(getApplicationContext(),"No Doctor Found", Toast.LENGTH_SHORT).show();;
                        mDoctor_RecyclerView.setVisibility(View.INVISIBLE);
                    }else{
                        mDoctor_RecyclerView.setVisibility(View.VISIBLE);
                        mDoctor_Adapter = new DoctorAdapter(getApplicationContext(),list_Doctor_Item,Category.this);
                        mDoctor_Adapter.notifyDataSetChanged();
                        listDoctorItemMain = list_Doctor_Item;

                        int orientation = getResources().getConfiguration().orientation;
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            mDoctor_RecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                            mDoctor_RecyclerView.setAdapter(mDoctor_Adapter);
                        } else {
                            mDoctor_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                            mDoctor_RecyclerView.setAdapter(mDoctor_Adapter);
                        }
                    }
                }

            }
        });
    }

    String dsHospitalUID = "NO", dsHospitalName = "NO", dsHospitalCreatorUID = "NO";
    private boolean intentFoundError = true;
    private void getIntentMethod() {
        //////////////GET INTENT DATA
        final Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            dsHospitalUID = intent.getExtras().getString("dsHospitalUID");
            dsHospitalName = intent.getExtras().getString("dsHospitalName");
            dsHospitalCreatorUID = intent.getExtras().getString("dsHospitalCreatorUID");
            intentFoundError = CheckIntentMethod(dsHospitalUID);
            intentFoundError = CheckIntentMethod(dsHospitalName);

            if(!intentFoundError){
                if(dUserUID.equals(dsHospitalCreatorUID)){
                    HospitalOwnerFoundBool = true;
                    mAddCategoryBtn.setVisibility(View.VISIBLE);
                }
                callViewModel();
            }else{
                Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();;
            }
        }else{
            dsHospitalUID = "NO";
            dsHospitalName = "NO";
            dsHospitalCreatorUID = "NO";
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
    public void onItemClick(int position) {
        String dsCategoryUID = listCategoryItem.get(position).getCategoryUID();
        callDoctorViewModel(dsCategoryUID);
    }

    @Override
    public void onAddCircleClick(int position) {
        if(dUserUID.equals(dsHospitalCreatorUID)){
            String dsCategoryUID = listCategoryItem.get(position).getCategoryUID();
            String dsCategoryName= listCategoryItem.get(position).getCategoryName();

            Intent intent = new Intent(getApplicationContext(), DoctorAdd.class);
            intent.putExtra("dsHospitalUID",  dsHospitalUID);
            intent.putExtra("dsHospitalName",  dsHospitalName);
            intent.putExtra("dsCategoryUID",  dsCategoryUID);
            intent.putExtra("dsCategoryName", dsCategoryName);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"You are not Owner of this Hospital", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDoctorItemClick(int position) {
        String dsDoctorUID = listDoctorItemMain.get(position).getDoctoUID();
        Intent intent = new Intent(getApplicationContext(), DoctorProfile.class);
        intent.putExtra("dsHospitalUID",  dsHospitalUID);
        intent.putExtra("dsHospitalName",  dsHospitalName);
        intent.putExtra("dsDoctorUID",  dsDoctorUID);
        startActivity(intent);
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