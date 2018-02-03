package com.lulingfeng.devicecontroller;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
/**
 * 继承 GTIntentService 接收来⾃自个推的消息, 所有消息在线程中回调, 如果注册了了该服务, 则务必要在 AndroidManifest中声明, 否则⽆无法接
 受消息<br>
 * onReceiveMessageData 处理理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理理回执 <br>
 */
public class CallbackIntentService extends GTIntentService {
    public CallbackIntentService() {
    }
    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);

            // 测试消息为了观察数据变化
//            if (data.equals(getResources().getString(R.string.push_transmission_data))) {
//                data = data + "-" + cnt;
//                cnt++;
//            }
        }

        Log.d(TAG, "----------------------------------------------------------------------------------------------");
    }
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }
    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
    }
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
    }
}