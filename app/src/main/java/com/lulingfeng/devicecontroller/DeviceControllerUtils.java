package com.lulingfeng.devicecontroller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Administrator on 2018/2/3 0003.
 */

public class DeviceControllerUtils {
    private final static String TAG = DeviceControllerUtils.class.getSimpleName();


    /**
     * 判断网络是否连接.
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo ni : info) {
                    if (ni.getState() == NetworkInfo.State.CONNECTED) {
                        Log.d(TAG, "type = " + (ni.getType() == 0 ? "mobile" : ((ni.getType() == 1) ? "wifi" : "none")));
                        return true;
                    }
                }
            }
        }

        return false;
    }





    class ControllerConstants {
        public static final boolean DEBUG = true;
        public final static String KEY_CURRENT_TEMPERATURE = "current_temperature";
        public final static String KEY_SWITCH_STATE = "switch_state";
        public final static String KEY_CURRENT_GEAR = "current_gear";
    }
}
