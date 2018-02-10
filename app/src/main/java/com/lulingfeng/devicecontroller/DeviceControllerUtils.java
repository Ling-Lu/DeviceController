package com.lulingfeng.devicecontroller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/2/3 0003.
 */

public class DeviceControllerUtils {
    private final static String TAG = DeviceControllerUtils.class.getSimpleName();
    private final static String VALID_NUM = "[1]\\d{10}";


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
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）/^0?1[3|4|5|7|8][0-9]\d{8}$/
    总结起来就是第一位必定为1，第二位必定为3或5或8或7（电信运营商），其他位置的可以为0-9
    */
        public static boolean isValidPhoneNum(String phone) {
        if(TextUtils.isEmpty(phone)) return false;
        Pattern p = Pattern
                .compile("^(0|86|17951)?(13[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$");

        Matcher m = p.matcher(phone);
        return m.matches();
    }
    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }




    class ControllerConstants {
        public static final boolean DEBUG = true;
        public final static String KEY_CURRENT_TEMPERATURE = "current_temperature";
        public final static String KEY_SWITCH_STATE = "switch_state";
        public final static String KEY_CURRENT_GEAR = "current_gear";
        public final static String KEY_WARNING_MSG = "warning_message";
    }
}
