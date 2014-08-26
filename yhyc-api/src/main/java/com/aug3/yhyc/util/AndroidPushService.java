package com.aug3.yhyc.util;

import org.apache.log4j.Logger;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

public class AndroidPushService {

	private static final Logger logger = Logger
			.getLogger(AndroidPushService.class);

	private final static String apiKey = "Oeqg1zYGozVjAoS6U2imW6zy";
	private final static String secretKey = "q2TydzGq74xGq1sLPbD36A9rDUamw400";

	public static void main(String[] args) {
		sendNotification(4195335532167560669L, "956475999426674554", "1hyc",
				"a test");
	}

	/*
	 * @brief 推送组播消息(消息类型为透传，由开发方应用自己来解析消息内容) message_type = 0 (默认为0)
	 */
	public static void sendTagMessage(String tagName, String msg) {

		// 1. 设置developer平台的ApiKey/SecretKey

		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);

		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				// System.out.println(event.getMessage());
			}
		});

		try {

			// 4. 创建请求类对象
			PushTagMessageRequest request = new PushTagMessageRequest();
			request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
			// 4:ios 5:wp
			request.setTagName(tagName);
			request.setMessage(msg);
			// 若要通知，
			// request.setMessageType(1);
			// request.setMessage("{\"title\":\"Notify_title_danbo\",\"description\":\"Notify_description_content\"}");

			// 5. 调用pushMessage接口
			PushTagMessageResponse response = channelClient
					.pushTagMessage(request);

			// 6. 认证推送成功
			// System.out.println("push amount : " +
			// response.getSuccessAmount());

		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			System.out.println(String.format(
					"request_id: %d, error_code: %d, error_message: %s",
					e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		}
	}

	/*
	 * @brief 推送单播通知(Android Push SDK拦截并解析) message_type = 1 (默认为0)
	 */
	public static void sendNotification(long channelId, String userId,
			String notifyTitle, String notifyDesc) {

		// 1. 设置developer平台的ApiKey/SecretKey
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);

		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				// System.out.println(event.getMessage());
			}
		});

		try {

			// 4. 创建请求类对象
			// 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
			request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
			// 4:ios 5:wp
			request.setChannelId(channelId);
			request.setUserId(userId);

			request.setMessageType(1);
			request.setMessage("{\"title\":\"" + notifyTitle
					+ "\",\"description\":\"" + notifyDesc + "\"}");

			// 5. 调用pushMessage接口
			PushUnicastMessageResponse response = channelClient
					.pushUnicastMessage(request);

			// 6. 认证推送成功
			// System.out.println("push amount : " +
			// response.getSuccessAmount());

		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			logger.error("channel client exception : " + e.getMessage());
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			logger.error(String.format(
					"request_id: %d, error_code: %d, error_message: %s",
					e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		}
	}

}
