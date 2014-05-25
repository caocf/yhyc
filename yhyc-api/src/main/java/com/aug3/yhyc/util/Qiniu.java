package com.aug3.yhyc.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.EncoderException;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.URLUtils;

public class Qiniu {

	private static final Logger log = Logger.getLogger(Qiniu.class);

	private static final String accesskey = ConfigManager.getProperties()
			.getProperty("storage.key.access");
	private static final String secretkey = ConfigManager.getProperties()
			.getProperty("storage.key.secret");
	private static final String domain = ConfigManager.getProperties()
			.getProperty("storage.domain");
	private static final String bucket = ConfigManager.getProperties()
			.getProperty("storage.bucket");

	private static Mac mac = null;

	static {
		Config.ACCESS_KEY = accesskey;
		Config.SECRET_KEY = secretkey;
		mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
	}

	public static String uptoken() {

		// 请确保该bucket已经存在
		PutPolicy putPolicy = new PutPolicy(bucket);
		putPolicy.expires = 3600;

		String uptoken = "";
		try {
			uptoken = putPolicy.token(mac);
		} catch (AuthException e) {
			log.error(e.getMessage());
		} catch (JSONException e) {
			log.error(e.getMessage());
		}

		return uptoken;
	}

	public static Map<String, String> downloadUrls(String fn) {

		String[] keys = fn.split(",");
		Map<String, String> urlMap = new HashMap<String, String>();

		for (String key : keys) {

			urlMap.put(key, downloadUrl(key));
		}

		return urlMap;
	}

	public static String downloadUrl(String fn) {

		String baseUrl = null;
		try {
			baseUrl = URLUtils.makeBaseUrl(domain, fn);
		} catch (EncoderException e) {
			log.error(e.getMessage());
		}
		GetPolicy getPolicy = new GetPolicy();
		getPolicy.expires = 3600;

		String downloadUrl = null;
		try {
			downloadUrl = getPolicy.makeRequest(baseUrl, mac);
		} catch (AuthException e) {
			log.error(e.getMessage());
		}

		return downloadUrl;
	}

}
