package com.lulingfeng.devicecontroller;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lulingfeng.devicecontroller.demo.GetuiSdkDemoActivity;

/**
 * Created by Administrator on 2018/2/3 0003.
 */

public class DeviceControllerApplication extends Application {
    private static final String TAG = "GetuiSdkDemo";

    private static Handler mHandler;
    private static OnDataReceiveListener mOnDataReceiveListener;
    public static GetuiSdkDemoActivity demoActivity;

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "DemoApplication onCreate");

        if (mHandler == null) {
            mHandler = new MessageDispatchHandler();
        }
    }

    public static void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    public class MessageDispatchHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (demoActivity != null) {
                        payloadData.append((String) msg.obj);
                        payloadData.append("\n");
                        Log.d(TAG, "handleMessage: payloadData" + payloadData);
                        if (GetuiSdkDemoActivity.tLogView != null) {
                            GetuiSdkDemoActivity.tLogView.append(msg.obj + "\n");
                        }
                    }
                    if(mOnDataReceiveListener != null) {
                        mOnDataReceiveListener.onReceiveTransmissionData((String) msg.obj);
                    }
                    break;

                case 1:
                    if (demoActivity != null) {
                        if (GetuiSdkDemoActivity.tLogView != null) {
                            GetuiSdkDemoActivity.tView.setText((String) msg.obj);
                        }
                    }
                    if(mOnDataReceiveListener != null) {
                        mOnDataReceiveListener.onReceiveClientId((String) msg.obj);
                    }
                    break;
            }
        }
    }
    public static void registerDataReceiveListener(OnDataReceiveListener onDataReceiveListener) {
        mOnDataReceiveListener = onDataReceiveListener;
    }
    public interface OnDataReceiveListener{
        void onReceiveClientId(String cid);
        void onReceiveTransmissionData(String data);
    }
}
