package com.shrabonti.digitalhospital.Model;

public class
DoctorModel {

    private String DoctoUID = "NO";
    private String DoctorName = "NO";
    private String DoctorPhotoUrl= "NO";
    private String DoctorBio = "NO";
    private String DoctorCreator= "NO";
    private String DoctorAddress = "NO";
    private String DoctorTimeTable = "NO";
    private String DoctorExtra = "NO";
    private String DoctorDesignation = "NO";
    private String DoctorCategory = "NO";

    private long DoctoriViews = 0;
    private long DoctoriPriority = 0;

    private long DoctoriRating = 0;
    private long DoctoriOrders = 0;
    private long DoctoriPrice = 0;
    private long DoctoriDiscount = 0;

    public DoctorModel() {
    }

    public DoctorModel(String doctoUID, String doctorName, String doctorPhotoUrl, String doctorBio, String doctorCreator, String doctorAddress, String doctorTimeTable, String doctorExtra, String doctorDesignation, String doctorCategory, long doctoriViews, long doctoriPriority, long doctoriRating, long doctoriOrders, long doctoriPrice, long doctoriDiscount) {
        DoctoUID = doctoUID;
        DoctorName = doctorName;
        DoctorPhotoUrl = doctorPhotoUrl;
        DoctorBio = doctorBio;
        DoctorCreator = doctorCreator;
        DoctorAddress = doctorAddress;
        DoctorTimeTable = doctorTimeTable;
        DoctorExtra = doctorExtra;
        DoctorDesignation = doctorDesignation;
        DoctorCategory = doctorCategory;
        DoctoriViews = doctoriViews;
        DoctoriPriority = doctoriPriority;
        DoctoriRating = doctoriRating;
        DoctoriOrders = doctoriOrders;
        DoctoriPrice = doctoriPrice;
        DoctoriDiscount = doctoriDiscount;
    }

    public String getDoctoUID() {
        return DoctoUID;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public String getDoctorPhotoUrl() {
        return DoctorPhotoUrl;
    }

    public String getDoctorBio() {
        return DoctorBio;
    }

    public String getDoctorCreator() {
        return DoctorCreator;
    }

    public String getDoctorAddress() {
        return DoctorAddress;
    }

    public String getDoctorTimeTable() {
        return DoctorTimeTable;
    }

    public String getDoctorExtra() {
        return DoctorExtra;
    }

    public String getDoctorDesignation() {
        return DoctorDesignation;
    }

    public String getDoctorCategory() {
        return DoctorCategory;
    }

    public long getDoctoriViews() {
        return DoctoriViews;
    }

    public long getDoctoriPriority() {
        return DoctoriPriority;
    }

    public long getDoctoriRating() {
        return DoctoriRating;
    }

    public long getDoctoriOrders() {
        return DoctoriOrders;
    }

    public long getDoctoriPrice() {
        return DoctoriPrice;
    }

    public long getDoctoriDiscount() {
        return DoctoriDiscount;
    }

    public void setDoctoUID(String doctoUID) {
        DoctoUID = doctoUID;
    }
}































