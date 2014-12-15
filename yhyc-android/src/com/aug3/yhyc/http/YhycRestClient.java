package com.aug3.yhyc.http;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.aug3.yhyc.model.UserPreference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class YhycRestClient {

	// private static final String BASE_URL =
	// "http://192.168.1.106:8080/yhyc-api/services";
	private static final String BASE_URL = "http://api.1hyc.com/yhyc-api/services";

	private static AsyncHttpClient client = new AsyncHttpClient();

	static {

		client.addHeader("uuid", UserPreference.getInstance().getUuid());
		client.addHeader("ver",
				String.valueOf(UserPreference.getInstance().getVer()));
		client.addHeader("ostype", "android");

		// client.addHeader(Constants.ReqParam.TOKEN, "");
		client.setMaxRetriesAndTimeout(3, 10 * 1000);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void fetch(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		new SyncHttpClient().get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(Context context, String url, HttpEntity entity,
			AsyncHttpResponseHandler responseHandler) {
		client.post(context, getAbsoluteUrl(url), entity, "application/json",
				responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

}
