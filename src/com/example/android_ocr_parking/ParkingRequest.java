package com.example.android_ocr_parking;

/**
 * Created with IntelliJ IDEA.
 * User: alexmartin9999
 * Date: 10/10/2013
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class ParkingRequest {

    private String _imagePath = "/img1.jpg";
    public String GetImagePath() { return _imagePath; }
    public void SetImagePath(String imagePath) { _imagePath = imagePath; }

    public int GetUserId() { return 1; }
}
