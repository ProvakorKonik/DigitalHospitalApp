package com.shrabonti.digitalhospital.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shrabonti.digitalhospital.Model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryVM extends AndroidViewModel {
    public CategoryVM(@NonNull Application application) {
        super(application);
        Log.d("ViewModel", "allViewModel:4 Level_D_VM start");
    }
    public MutableLiveData mLiveData;
    public MutableLiveData<List<CategoryModel>> LoadLevel4List(String dsHospitalUID) {
        List<CategoryModel> listCategoryItem ; listCategoryItem =new ArrayList<>();

        CollectionReference notebookRef;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ViewModel", "allViewModel:4 LoadLevel4List start");

        notebookRef = db.collection("AllHospital").document(dsHospitalUID).collection("AllCategory");

        if(mLiveData == null) {
            mLiveData = new MutableLiveData();
            notebookRef.orderBy("CategoryiPriority", Query.Direction.ASCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {   //documnet er vitore je multiple document query ache er jonno for loop
                            String data = "";
                            if(queryDocumentSnapshots.isEmpty()) {
                                //String categoryUID, String categoryName, String categoryPhotoUrl, String categoryBio, String categoryCreator, long categoryiViews, long categoryiPriority, long categoryiClicked
                                listCategoryItem.add(new CategoryModel("UID","NULL", "categoryPhotoUrl", "categoryBio", "categoryCreator",0,0, 0));
                                mLiveData.postValue(listCategoryItem);
                                Log.d("ViewModel", "allViewModel:4 queryDocumentSnapshots empty");
                            }else {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    CategoryModel book_model = documentSnapshot.toObject(CategoryModel.class);
                                    //messageModel.setDocumentID(documentSnapshot.getId());
                                    String dsCategory_UID = documentSnapshot.getId();
                                    String dsCategory_Name = book_model.getCategoryName();
                                    String dsCategory_PhotoUrl = book_model.getCategoryPhotoUrl();
                                    String dsCategory_Bio= book_model.getCategoryBio();
                                    String dsCategory_Creator= book_model.getCategoryCreator();
                                    long diCategoryView = book_model.getCategoryiViews();
                                    long diCategoryiPriority = book_model.getCategoryiPriority();


                                    //String UID, String hospitalName, String hospitalPhotoUrl, String hospitalBio, String hospitalCreator, String hospitalAddress, long hospitaliPriority
                                    listCategoryItem.add(new CategoryModel(dsCategory_UID, dsCategory_Name,dsCategory_PhotoUrl,dsCategory_Bio, dsCategory_Creator,diCategoryView,diCategoryiPriority, 0));
                                    mLiveData.postValue(listCategoryItem);
                                }
                                mLiveData.postValue(listCategoryItem);    //All Items level 4 , it is a one type category

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

        return mLiveData;
    }
}
