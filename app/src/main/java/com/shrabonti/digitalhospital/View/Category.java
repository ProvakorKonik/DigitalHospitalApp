package com.shrabonti.digitalhospital.View;

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
import com.shrabonti.digitalhospital.Adapter.CategoryAdapter;
import com.shrabonti.digitalhospital.Model.CategoryModel;
import com.shrabonti.digitalhospital.MainActivity;
import com.shrabonti.digitalhospital.R;
import com.shrabonti.digitalhospital.RecylerviewClickInterface;
import com.shrabonti.digitalhospital.ViewModel.CategoryVM;
import com.shrabonti.digitalhospital.ViewModel.CategoryVM;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity implements RecylerviewClickInterface {

    private Button mAddCategoryBtn;
    //RecyclerView
    private RecyclerView mBook_RecyclerView;
    List<CategoryModel> listHospitalItem;
    CategoryAdapter mCategory_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mAddCategoryBtn = (Button)findViewById(R.id.category_add_btn);

        mBook_RecyclerView = (RecyclerView)findViewById(R.id.category_recyclerview);
        listHospitalItem = new ArrayList<>();
        getIntentMethod();
        
        
        mAddCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryAdd.class);
                intent.putExtra("dsHospitalUID",  dsHospitalUID);
                intent.putExtra("dsHospitalName", dsHospitalName);
                startActivity(intent);
            }
        });
    }
    private CategoryVM hospitalVm;
    private void callViewModel() {
        Log.d("ViewModel", "allViewModel:1 hospitalVm start");
        hospitalVm = new ViewModelProvider(this).get(CategoryVM.class);
        hospitalVm.LoadLevel4List(dsHospitalUID).observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> list_cateagory_models) {
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

                    mCategory_Adapter = new CategoryAdapter(Category.this,list_cateagory_models,Category.this);
                    mCategory_Adapter.notifyDataSetChanged();
                    listHospitalItem = list_cateagory_models;

                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        mBook_RecyclerView.setLayoutManager(new GridLayoutManager(Category.this,2));
                        mBook_RecyclerView.setAdapter(mCategory_Adapter);
                    } else {
                        mBook_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));
                        mBook_RecyclerView.setAdapter(mCategory_Adapter);
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
                callViewModel();
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

    }
}