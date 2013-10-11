package com.example.android_ocr_parking;

public class ParkingRequest {

    private String _imagePath;
    public String GetImagePath() { return _imagePath; }
    public void SetImagePath(String imagePath) { _imagePath = imagePath; }

    private int _userId = 1;
    public int GetUserId() { return _userId; }
    public void SetUserId(int userId) { _userId = userId; }

    private String _pin = "1111";
    public String GetPin() { return _pin; }
    public void SetPin(String pin) { _pin = pin; }

    private String _timeUnits = "";
    private int _timeValue = 0;
    public String GetDuration() { return _timeValue + " " + _timeUnits; }
    public void SetDuration(String units, int value) { _timeUnits = units; _timeValue = value; }
}
