package com.springapp.push;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.springapp.push.jdbc.MessageBean;
import com.springapp.push.jdbc.MessageImpl;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/push")
public class HelloController {

	private static String masterSecret = "645a1bdd167c16a5bd1822b2";
	private static String appKey = "4a30a83b70c92f14147718f3";
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private MessageImpl messageImpl;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String printWelcome(String pushMessage) {
		// 发送推送信息
		testSendPushWithCustomConfig(pushMessage);
		// 信息保存至数据库中
		MessageBean messageBean = new MessageBean();
		messageBean.setMessage(pushMessage);
		messageBean.setTime(simpleDateFormat.format(new Date()));
		messageImpl.addMessage(messageBean);
		return "ok";
	}

	@RequestMapping(value = "/testadd",method = RequestMethod.GET)
	@ResponseBody
	public String testJDBC(String pushMessage) {
		MessageBean messageBean = new MessageBean();
		messageBean.setMessage(pushMessage);
		messageBean.setTime(simpleDateFormat.format(new Date()));
		messageImpl.addMessage(messageBean);
		// 信息保存至数据库中
		return "ok";
	}

	@RequestMapping(value = "/list",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String listMessage() {
		JSONArray a = JSONArray.fromObject(messageImpl.queryAllMessage());
		return a.toString();
	}

	public static PushPayload buildPushObject_all_all_alert(String pushMessage) {
		String json = "{\"message\":\""+pushMessage+"\",\"time\":\""+simpleDateFormat.format(new Date())+"\"}";
		return PushPayload.messageAll(json);
	}

	public static PushPayload buildPushObject_android_tag_Message(String pushMessage) {
		String json = "{\"message\":\""+pushMessage+"\",\"time\":\""+simpleDateFormat.format(new Date())+"\"}";
		return PushPayload.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.all())
				.setMessage(Message.content(json)).build();
	}

	public static PushPayload buildPushObject_IOS_tag_Message(String pushMessage) {
		String json = "{\"message\":\""+pushMessage+"\",\"time\":\""+simpleDateFormat.format(new Date())+"\"}";
		String[] tags = new String[1];
		tags[0] = "wangshibin_tf";
		return PushPayload.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(Audience.alias(tags))
				.setNotification(Notification.newBuilder().addPlatformNotification(
						IosNotification.newBuilder().setAlert(pushMessage).incrBadge(1).build()).build()).build();
//		return PushPayload.alertAll(pushMessage);
	}

	public static void testSendPushWithCustomConfig(String pushMessage) {
		ClientConfig config = ClientConfig.getInstance();
		// Setup the custom hostname
		config.setPushHostName("https://api.jpush.cn");

		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3, null, config);
		// For push, all you need do is to build PushPayload object.
//		PushPayload payload = buildPushObject_all_all_alert(pushMessage);
//		PushPayload payload = buildPushObject_android_tag_Message(pushMessage);
		PushPayload payloadIOS = buildPushObject_IOS_tag_Message(pushMessage);

		try {
//			PushResult result = jpushClient.sendPush(payload);
			PushResult resultIOS = jpushClient.sendPush(payloadIOS);
			System.out.println("Got result - " + resultIOS);

		} catch (APIConnectionException e) {
			System.out.println("Connection error. Should retry later. ");

		} catch (APIRequestException e) {
			System.out.println("Error response from JPush server. Should review and fix it. ");
			System.out.println("HTTP Status: " + e.getStatus());
			System.out.println("Error Code: " + e.getErrorCode());
			System.out.println("Error Message: " + e.getErrorMessage());
			System.out.println("Msg ID: " + e.getMsgId());
		}
	}
}