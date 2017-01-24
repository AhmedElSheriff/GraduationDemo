package com.example.android.graduationdemo.data;

/**
 * Created by Abshafi on 12/19/2016.
 */

public class EmergencyLocation {

    private String LatPosition;
    private String LongPosition;

//    public EmergencyLocation(String LatPosition, String LongPosition) {
//        this.LatPosition = LatPosition;
//        this.LongPosition = LongPosition;
//    }

    public EmergencyLocation() {
    }

    public void setLatPosition(String LatPosition) {
        this.LatPosition = LatPosition;
    }

    public void setLongPosition(String LongPosition) {
        this.LongPosition = LongPosition;
    }

    public String getLatPosition() {
        return LatPosition;
    }

    public String getLongPosition() {
        return LongPosition;
    }

}
