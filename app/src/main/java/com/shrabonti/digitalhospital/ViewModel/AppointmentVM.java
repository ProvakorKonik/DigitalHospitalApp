package com.shrabonti.digitalhospital.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shrabonti.digitalhospital.Model.AppointmentModel;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class AppointmentVM extends ViewModel {

    private MutableLiveData<String> mText;

    public AppointmentVM() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    //////////Order List
    public MutableLiveData mOrderLiveData;
    public MutableLiveData<List<AppointmentModel>> callOrderList(String dsHospitalUID, String dUserType, String dUserUID) {
        List<AppointmentModel> listOrderItem ; listOrderItem =new ArrayList<>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notebookRef;
        Log.d("ViewModel", "allViewModel:4 mOrderLiveData start");

        notebookRef = db.collection("AllHospital").document(dsHospitalUID).collection("Orders")
                .document("Dates").collection("Today");

        if(mOrderLiveData == null) {
            mOrderLiveData = new MutableLiveData();
        }
        notebookRef
                .whereEqualTo(dUserType,dUserUID)
                //.orderBy("OrderiPriority", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {   //documnet er vitore je multiple document query ache er jonno for loop
                        String data = "";
                        if(queryDocumentSnapshots.isEmpty()) {
                            //String doctorUID, String uidHospitalCreator, String uidOrderCreator, String doctorName, String doctorPHOTO, String doctorNote, String doctorExtra, long doctoriPrice, Date doctorOrderTime
                            listOrderItem.add(new AppointmentModel("NULL","doctorUID", "uidHospitalCreator", "uidOrderCreator", "doctorName", "doctorPHOTO","doctorNote","doctorExtra", 0,null));
                            mOrderLiveData.postValue(listOrderItem);
                            Log.d("ViewModel", "allViewModel:4 queryDocumentSnapshots empty");
                        }else {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                AppointmentModel book_model = documentSnapshot.toObject(AppointmentModel.class);
                                //messageModel.setDocumentID(documentSnapshot.getId());
                                String dsOrderUID = documentSnapshot.getId();
                                String dsHospitalCreatorUID = book_model.getUidHospitalCreator();
                                Date ddDate = book_model.getDoctorOrderTime();
                                //String productUID, String productName, String productPhotoUrl, String productBio, String productCreator, long productiViews, long productiPriority, long productiRating, long productiOrders, long productiPrice, long productiDiscount, String productExtra, String productSize, String productCategory
                                listOrderItem.add(new AppointmentModel(dsOrderUID,"doctorUID", dsHospitalCreatorUID, "uidOrderCreator", "doctorName", "doctorPHOTO","doctorNote","doctorExtra", 0,ddDate));
                                mOrderLiveData.postValue(listOrderItem);
                            }
                            mOrderLiveData.postValue(listOrderItem);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        return mOrderLiveData;
    }
}
