package com.shrabonti.digitalhospital.Model;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class AppointmentModel {
    private String OrderUID= "NO";
    private String DoctorUID = "NO";
    private String UidHospitalCreator = "NO";
    private String UidOrderCreator = "NO";
    private String DoctorName = "NO";
    private String DoctorPHOTO = "NO";
    private String DoctorNote = "NO";
    private String DoctorExtra = "NO";
    private long   DoctoriPrice = 0;
    private @ServerTimestamp Date DoctorOrderTime  =  null;

    public AppointmentModel() {
    }

    public AppointmentModel(String orderUID, String doctorUID, String uidHospitalCreator, String uidOrderCreator, String doctorName, String doctorPHOTO, String doctorNote, String doctorExtra, long doctoriPrice, Date doctorOrderTime) {
        OrderUID = orderUID;
        DoctorUID = doctorUID;
        UidHospitalCreator = uidHospitalCreator;
        UidOrderCreator = uidOrderCreator;
        DoctorName = doctorName;
        DoctorPHOTO = doctorPHOTO;
        DoctorNote = doctorNote;
        DoctorExtra = doctorExtra;
        DoctoriPrice = doctoriPrice;
        DoctorOrderTime = doctorOrderTime;
    }

    public String getOrderUID() {
        return OrderUID;
    }

    public String getDoctorUID() {
        return DoctorUID;
    }

    public String getUidHospitalCreator() {
        return UidHospitalCreator;
    }

    public String getUidOrderCreator() {
        return UidOrderCreator;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public String getDoctorPHOTO() {
        return DoctorPHOTO;
    }

    public String getDoctorNote() {
        return DoctorNote;
    }

    public String getDoctorExtra() {
        return DoctorExtra;
    }

    public long getDoctoriPrice() {
        return DoctoriPrice;
    }

    public Date getDoctorOrderTime() {
        return DoctorOrderTime;
    }
}
