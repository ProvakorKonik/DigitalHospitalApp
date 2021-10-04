package com.shrabonti.digitalhospital.Model;

public class HospitalModel {
    
    private String HospitalUID = "NO";
    private String HospitalName = "NO";
    private String HospitalPhotoUrl= "NO";
    private String HospitalBio= "NO";
    private String HospitalCreator= "NO";
    private String HospitalAddress= "NO";
    private long HospitaliPriority = 0;

    public HospitalModel(String hospitalUID,String hospitalName, String hospitalPhotoUrl, String hospitalBio, String hospitalCreator, String hospitalAddress, long hospitaliPriority) {
        HospitalUID = hospitalUID;
        HospitalName = hospitalName;
        HospitalPhotoUrl = hospitalPhotoUrl;
        HospitalBio = hospitalBio;
        HospitalCreator = hospitalCreator;
        HospitalAddress = hospitalAddress;
        HospitaliPriority = hospitaliPriority;
    }

    public HospitalModel() {
    }

    public String getHospitalUID() {
        return HospitalUID;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public String getHospitalPhotoUrl() {
        return HospitalPhotoUrl;
    }

    public String getHospitalBio() {
        return HospitalBio;
    }

    public String getHospitalCreator() {
        return HospitalCreator;
    }

    public String getHospitalAddress() {
        return HospitalAddress;
    }

    public long getHospitaliPriority() {
        return HospitaliPriority;
    }
}
