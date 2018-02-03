package com.lulingfeng.devicecontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.igexin.sdk.PushManager;
import com.lulingfeng.viewpreference.EditPreferenceView;
import com.lulingfeng.viewpreference.PreferenceItemView;
import com.lulingfeng.viewpreference.SwitchPreferenceView;

public class ControllerActivity extends AppCompatActivity implements PreferenceItemView.OnPreferenceChangedListener{
    private SwitchPreferenceView mPowerSwitch;
    private EditPreferenceView mChangeTemperature;
    private PreferenceItemView mTemperature;
    private SwitchPreferenceView mWorkingSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        initViews();

        // com.getui.demo.DemoPushService 为第三⽅方⾃自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), DeviceControllerPushService.class);
        // com.getui.demo.DemoIntentService 为第三⽅方⾃自定义的推送服务事件接收类
        // 在个推SDK初始化后，注册上述 IntentService 类：
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), CallbackIntentService.
                class);
    }
    private void initViews() {
        mPowerSwitch = (SwitchPreferenceView) findViewById(R.id.id_switch);
        mChangeTemperature = (EditPreferenceView) findViewById(R.id.id_temperature_changed);
        mTemperature = (PreferenceItemView) findViewById(R.id.id_temperature);
        mWorkingSwitch = (SwitchPreferenceView) findViewById(R.id.id_working);

        mPowerSwitch.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(PreferenceItemView preferenceItemView, Object newValue) {
        return false;
    }
}
