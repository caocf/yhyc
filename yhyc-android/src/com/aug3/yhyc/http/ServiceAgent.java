package com.aug3.yhyc.http;

import java.io.File;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.SystemCache;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.io.PutExtra;

public class ServiceAgent {

	public static void updateFav() {

		if (UserPreference.getInstance().isFavUpdated()) {

			String items = TextUtils.join(Constants.COMMA, UserPreference
					.getInstance().getFav());

			RequestParams p = new RequestParams();
			p.put(Constants.ReqParam.USERID, UserPreference.getInstance()
					.getUid());
			p.put(Constants.ReqParam.FIELD, "fav");
			p.put(Constants.ReqParam.ITEMS, items);
			p.put(Constants.ReqParam.TYPE, 3);

			YhycRestClient.post(Constants.ReqUrl.USER_PREFS_UPDATE, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							UserPreference.getInstance().setFavUpdated(false);
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
						}
					});
		}
	}

	public static void updateUserName(final String nickName) {

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.USERID, UserPreference.getInstance().getUid());
		p.put(Constants.ReqParam.NAME, nickName);

		YhycRestClient.post(Constants.ReqUrl.USER_NAME, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						UserPreference.getInstance().setUserName(nickName);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

					}
				});
	}

	public static void updateCart() {

		if (UserPreference.getInstance().isCartUpdated()) {

			String items = TextUtils.join(Constants.COMMA, UserPreference
					.getInstance().getCart());

			RequestParams p = new RequestParams();
			p.put(Constants.ReqParam.USERID, UserPreference.getInstance()
					.getUid());
			p.put(Constants.ReqParam.FIELD, "cart");
			p.put(Constants.ReqParam.ITEMS, items);
			p.put(Constants.ReqParam.TYPE, 3);

			YhycRestClient.post(Constants.ReqUrl.USER_PREFS_UPDATE, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							UserPreference.getInstance().setCartUpdated(false);
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
						}
					});
		}
	}

	public static void uploadBitmap(final String fn) {

		final File f = new File(fn);

		final SystemCache sc = new SystemCache();
		final String cacheKey = f.getName();

		String uptoken = (String) sc.get(cacheKey);

		if (uptoken == null || "".equals(uptoken)) {

			RequestParams p = new RequestParams();
			p.put(Constants.ReqParam.BUCKET, "user");
			p.put("key", f.getName());

			YhycRestClient.get(Constants.ReqUrl.UPTOKEN, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								String token = response.get(
										Constants.RESP_RESULT).toString();
								sc.put(cacheKey, token, 3000);

								uploadFile(f, token);

							} catch (JSONException e) {
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Log.i("upload", throwable.getMessage());
						}
					});

		} else {
			uploadFile(f, uptoken);
		}

	}

	public static void uploadFile(File f, String uptoken) {

		if (uptoken == null) {
			return;
		}

		String key = f.getName(); // 自动生成key
		PutExtra extra = new PutExtra();
		extra.params = new HashMap<String, String>();
		extra.params.put("endUser", "android");
		// extra.params.put("fname", fn);

		IO.putFile(uptoken, key, f, extra, new JSONObjectRet() {

			@Override
			public void onSuccess(JSONObject obj) {

				// Log.i("upload", "upload success");

			}

			@Override
			public void onFailure(Exception ex) {

				Log.i("upload", ex.getMessage());

			}
		});

	}

}
