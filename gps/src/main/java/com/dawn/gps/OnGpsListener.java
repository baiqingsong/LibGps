package com.dawn.gps;

public interface OnGpsListener {
    void onGpsStart();
    void onGpsStop();
    void onGpsLocation(double longitude, double latitude);
    void onGpsLocationFail();
}
