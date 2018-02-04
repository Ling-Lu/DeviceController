package com.lulingfeng.devicecontroller;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.lulingfeng.viewpreference.EditPreferenceView;
import com.lulingfeng.viewpreference.PreferenceItemView;
import com.lulingfeng.viewpreference.SeekPreferenceView;
import com.lulingfeng.viewpreference.SwitchPreferenceView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ControllerActivity extends AppCompatActivity implements PreferenceItemView.OnPreferenceChangedListener,DeviceControllerApplication.OnDataReceiveListener{
    private final static String TAG = ControllerActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION = 0;
    private SwitchPreferenceView mVPowerSwitch;
    private EditPreferenceView mVChangeTemperature;
    private EditPreferenceView mVChangeClientId;
    private EditPreferenceView mVChangeGear;
    private PreferenceItemView mVTemperature;
    private SwitchPreferenceView mVPower;
    private PreferenceItemView mVClientId;
    private PreferenceItemView mVCurrentGear;
    private String mAppKey = "";
    private String mAppSecret = "";
    private String mAppId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        initViews();
        parseManifests();
        initPermissions();
        initListener();

        // cpu 架构
        Log.d(TAG, "cpu arch = " + (Build.VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0]));

        // 检查 so 是否存在
        File file = new File(this.getApplicationInfo().nativeLibraryDir + File.separator + "libgetuiext2.so");
        Log.e(TAG, "libgetuiext2.so exist = " + file.exists());


    }
    private void initListener() {
        DeviceControllerApplication.registerDataReceiveListener(this);


        // com.getui.demo.DemoPushService 为第三⽅方⾃自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), DeviceControllerPushService.class);
        // com.getui.demo.DemoIntentService 为第三⽅方⾃自定义的推送服务事件接收类
        // 在个推SDK初始化后，注册上述 IntentService 类：
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), CallbackIntentService.
                class);
    }
    private void initPermissions() {
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                getPackageManager().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        }
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            } else {
                Log.e(TAG, "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void initViews() {
        mVPowerSwitch = (SwitchPreferenceView) findViewById(R.id.id_switch);
        mVChangeTemperature = (EditPreferenceView) findViewById(R.id.id_temperature_changed);
        mVChangeClientId = (EditPreferenceView) findViewById(R.id.id_client_id_set);
        mVChangeGear = (EditPreferenceView) findViewById(R.id.id_gear_set);
        mVTemperature = (PreferenceItemView) findViewById(R.id.id_temperature);
        mVCurrentGear = (PreferenceItemView) findViewById(R.id.id_gear);
        mVPower = (SwitchPreferenceView) findViewById(R.id.id_power);
        mVClientId = (PreferenceItemView) findViewById(R.id.id_device_data);

        mVPower.setEnabled(false);
        mVPowerSwitch.setOnPreferenceChangeListener(this);
        mVChangeClientId.setOnPreferenceChangeListener(this);
        mVChangeTemperature.setOnPreferenceChangeListener(this);
        mVChangeGear.setOnPreferenceChangeListener(this);
    }
    private void parseManifests() {
        String packageName = getApplicationContext().getPackageName();
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                mAppId = appInfo.metaData.getString("PUSH_APPID");
                mAppSecret = appInfo.metaData.getString("PUSH_APPSECRET");
                mAppKey = appInfo.metaData.getString("PUSH_APPKEY");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断网络是否连接.
     */
    private boolean isNetworkConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
    @Override
    public boolean onPreferenceChange(PreferenceItemView preferenceItemView, Object newValue) {
        Map<String,Object> mData = new HashMap<>();
        String key = preferenceItemView.getKey();
        if(key == null) return false;
        if(key.equals(mVChangeClientId.getKey())) {
            mData.put(DeviceControllerUtils.ControllerConstants.KEY_SWITCH_STATE,mVPowerSwitch.isChecked());
            mData.put(DeviceControllerUtils.ControllerConstants.KEY_CURRENT_GEAR,mVChangeGear.getValue());
            mData.put(DeviceControllerUtils.ControllerConstants.KEY_CURRENT_TEMPERATURE,mVChangeTemperature.getValue());
        }
        if(key.equals(mVChangeGear.getKey())) {
            mData.put(DeviceControllerUtils.ControllerConstants.KEY_CURRENT_GEAR,newValue);
        } else {
            mData.put(DeviceControllerUtils.ControllerConstants.KEY_CURRENT_GEAR,mVChangeGear.getValue());
        }
        if(key.equals(mVPowerSwitch.getKey())) {
            mData.put(DeviceControllerUtils.ControllerConstants.KEY_SWITCH_STATE,newValue);
        } else {
            mData.put(DeviceControllerUtils.ControllerConstants.KEY_SWITCH_STATE,mVPowerSwitch.isChecked());
        }
        if(key.equals(mVChangeTemperature.getKey())) {
            mData.put(DeviceControllerUtils.ControllerConstants.KEY_CURRENT_TEMPERATURE,newValue);
        } else {
            mData.put(DeviceControllerUtils.ControllerConstants.KEY_CURRENT_TEMPERATURE,mVChangeTemperature.getValue());
        }
        JSONObject jsonObject = new JSONObject(mData);
        SendSingleMessage.sendMsg(mAppId,mAppKey,mVChangeClientId.getValue(),jsonObject.toString());
        return true;
    }

    @Override
    public void onReceiveClientId(String cid) {
        mVClientId.setSummary(cid);
    }

    @Override
    public void onReceiveTransmissionData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            int current_temperature = jsonObject.getInt(DeviceControllerUtils.ControllerConstants.KEY_CURRENT_TEMPERATURE);
            int current_gear = jsonObject.getInt(DeviceControllerUtils.ControllerConstants.KEY_CURRENT_GEAR);
            boolean current_power = jsonObject.getBoolean(DeviceControllerUtils.ControllerConstants.KEY_SWITCH_STATE);

            mVPower.setChecked(current_power);
            mVCurrentGear.setSummary(String.valueOf(current_gear));
            mVTemperature.setSummary(String.valueOf(current_temperature));
        } catch (JSONException e) {
            Log.e(TAG, "OnReceiveTransmissionData: getJsonData error", e);
        }
    }
}
