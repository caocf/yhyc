package com.aug3.yhyc.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aug3.sys.util.CommonHttpUtils;

public class NotificationService {

	private static final Logger log = Logger
			.getLogger(NotificationService.class);

	private static final String sms_url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

	private static final String sms_pwd = MD5Sum.MD5Encode("4627685guma");

	private static String sms_template = ConfigManager.getProperties().getProperty(
			"sendsms.content");

	public static final String UTF8 = "UTF-8";

	public static String sendSMS(String mobile) {

		String verifyCode = String
				.valueOf((int) ((Math.random() * 9 + 1) * 1000));

		String content = sms_template.replace("@verify@", verifyCode);

		Map<String, String> params = new HashMap<String, String>();

		params.put("account", "cf_yhyc");

		params.put("password", sms_pwd);

		params.put("mobile", mobile);

		params.put("content", content);

		String result = CommonHttpUtils.executePostMothedRequest(sms_url,
				params);

		if (result.contains("<code>2</code>")) {
			return verifyCode;
		} else {
			log.error(result);
			return null;
		}

	}

}
