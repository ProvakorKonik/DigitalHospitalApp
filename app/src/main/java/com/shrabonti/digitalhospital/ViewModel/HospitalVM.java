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
import com.shrabonti.digitalhospital.Model.HospitalModel;

import java.util.ArrayList;
import java.util.List;

public class HospitalVM extends AndroidViewModel {
    public HospitalVM(@NonNull Application application) {
        super(application);
        Log.d("ViewModel", "allViewModel:4 Level_D_VM start");
    }
    public MutableLiveData mLiveData;
    public MutableLiveData<List<HospitalModel>> LoadHospitalList() {
        List<HospitalModel> listHospitalItem ; listHospitalItem =new ArrayList<>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notebookRef;
        Log.d("ViewModel", "allViewModel:4 LoadLevel4List start");

        notebookRef = db.collection("AllHospital");

        if(mLiveData == null) {
            mLiveData = new MutableLiveData();
            notebookRef.orderBy("HospitaliPriority", Query.Direction.ASCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {   //documnet er vitore je multiple document query ache er jonno for loop
                            String data = "";
                            if(queryDocumentSnapshots.isEmpty()) {
                                //String UID, String hospitalName, String hospitalPhotoUrl, String hospitalBio, String hospitalCreator, String hospitalAddress, long hospitaliPriority
                                listHospitalItem.add(new HospitalModel("UID","NULL", "hospitalPhotoUrl", "hospitalBio", "hospitalCreator", "hospitalAddress",  0));
                                mLiveData.postValue(listHospitalItem);
                                Log.d("ViewModel", "allViewModel:4 queryDocumentSnapshots empty");
                            }else {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    HospitalModel hospital_model = documentSnapshot.toObject(HospitalModel.class);
                                    //messageModel.setDocumentID(documentSnapshot.getId());
                                    String dsHospital_UID = documentSnapshot.getId();
                                    String dsHospital_Name = hospital_model.getHospitalName();
                                    String dsHospital_PhotoUrl = hospital_model.getHospitalPhotoUrl();
                                    String dsHospital_Bio= hospital_model.getHospitalBio();
                                    String dsHospital_Creator= hospital_model.getHospitalCreator();
                                    String dsHospital_Address = hospital_model.getHospitalAddress();
                                    long dibookiPriority = hospital_model.getHospitaliPriority();

                                    //String UID, String hospitalName, String hospitalPhotoUrl, String hospitalBio, String hospitalCreator, String hospitalAddress, long hospitaliPriority
                                    listHospitalItem.add(new HospitalModel(dsHospital_UID,dsHospital_Name, dsHospital_PhotoUrl,dsHospital_Bio,dsHospital_Creator, dsHospital_Address, dibookiPriority));
                                    mLiveData.postValue(listHospitalItem);
                                }
                                mLiveData.postValue(listHospitalItem);    //All Items level 4 , it is a one type category

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
