package com.dawn.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;


/**
 * gps工厂类
 */
public class GpsFactory {
    private Context mContext;
    private static GpsFactory gpsFactory;
    private GpsFactory(Context context){
        this.mContext = context;
        Settings.Secure.setLocationProviderEnabled(context.getContentResolver(), LocationManager.GPS_PROVIDER, true);
    }
    public static GpsFactory getInstance(Context context){
        if(gpsFactory == null)
            gpsFactory = new GpsFactory(context);
        return gpsFactory;
    }

    private OnGpsListener mListener;
    private GPSLocationManager gpsLocationManager;

    /**gps相关*************************************************************************************/
    //获取gps信息
    public void gpsGetLocation(Activity activity, OnGpsListener listener){
//        Log.i("dawn", "获取gps信息");
        this.mListener = listener;
        if(gpsLocationManager == null)
            gpsLocationManager = GPSLocationManager.getInstances(activity);
        gpsLocationManager.start(new GPSListener());
    }

    class GPSListener implements GPSLocationListener {

        @Override
        public void UpdateLocation(Location location) {
            if (location != null) {
//                Log.i("dawn", "经度：" + location.getLongitude() + " 纬度：" + location.getLatitude());
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                gpsLocationManager.stop();
                if(mListener != null)
                    mListener.onGpsLocation(longitude, latitude);
            }else{
//                Log.i("dawn", "获取不到位置信息");
                if(mListener != null)
                    mListener.onGpsLocationFail();
            }
        }

        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) {
            if ("gps".equals(provider)) {
//                Log.i("dawn", "GPS状态为：" + status);
//                Toast.makeText(MainActivity.this, "定位类型：" + provider, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) {
            switch (gpsStatus) {
                case GPSProviderStatus.GPS_ENABLED:
//                    Toast.makeText(MainActivity.this, "GPS开启", Toast.LENGTH_SHORT).show();
//                    Log.i("dawn", "GPS开启");
                    if(mListener != null)
                        mListener.onGpsStart();
                    break;
                case GPSProviderStatus.GPS_DISABLED:
//                    Toast.makeText(MainActivity.this, "GPS关闭", Toast.LENGTH_SHORT).show();
//                    Log.i("dawn", "GPS关闭");
                    if(mListener != null)
                        mListener.onGpsStop();
                    break;
                case GPSProviderStatus.GPS_OUT_OF_SERVICE:
//                    Log.i("dawn", "GPS不可用");
                case GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE:
//                    Log.i("dawn", "GPS暂时不可用");
                    if(mListener != null)
                        mListener.onGpsLocationFail();
                    break;
                case GPSProviderStatus.GPS_AVAILABLE:
//                    Log.i("dawn", "GPS可用啦");
                    break;
            }
        }
    }
}
