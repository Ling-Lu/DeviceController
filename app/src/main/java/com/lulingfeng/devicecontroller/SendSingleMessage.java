package com.lulingfeng.devicecontroller;


import android.util.Log;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;

public class SendSingleMessage {
    private final static String TAG = SendSingleMessage.class.getSimpleName();
    //采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
    private static String mAppId = "";
    private static String mAppKey = "";
    private static String mMasterSecret = "US6z2w89aR8SxBJBtq0OL1";

    static String mCID = "";
    //别名推送方式
    // static String Alias = "";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    public static void sendMsg(String appId,String appKey,String cid,final String content) {
        mAppId = appId;
        mAppKey = appKey;
        mCID = cid;
        new Thread(new Runnable() {
            @Override
            public void run() {
                IGtPush push = new IGtPush(mAppKey, mMasterSecret,true);
                TransmissionTemplate template = transmissionTemplateDemo(content);
                SingleMessage message = new SingleMessage();
//        message.setPriority(0);
                message.setOffline(true);
                // 离线有效时间，单位为毫秒，可选
                message.setOfflineExpireTime(1 * 3600 * 1000);
                message.setData(template);
                // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
                message.setPushNetWorkType(0);
                Target target = new Target();
                target.setAppId(mAppId);
                target.setClientId(mCID);
                //target.setAlias(Alias);
                IPushResult ret = null;
                Log.d(TAG, "run: " + push.getClientIdStatus(mAppId,mCID));
                try {
                    ret = push.pushMessageToSingle(message, target);
                } catch (RequestException e) {
                    e.printStackTrace();
                    ret = push.pushMessageToSingle(message, target, e.getRequestId());
                }
                if (ret != null) {
                    System.out.println(ret.getResponse().toString());
                } else {
                    System.out.println("服务器响应异常");
                }
            }
        }).start();

    }
    public static LinkTemplate linkTemplateDemo() {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(mAppId);
        template.setAppkey(mAppKey);

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle("请输入通知栏标题");
        style.setText("请输入通知栏内容");
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        // 设置打开的网址地址
        template.setUrl("http://www.baidu.com");
        return template;
    }
    public static TransmissionTemplate transmissionTemplateDemo(String content) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(mAppId);
        template.setAppkey(mAppKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(content);
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return template;
    }
}