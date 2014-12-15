package com.aug3.yhyc;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;

import com.aug3.yhyc.api.UserPrefs;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.base.Constants.Config;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.DeviceUuidFactory;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @author Roger
 */
public class YhycApplication extends Application {

	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (Config.DEVELOPER_MODE
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}

		super.onCreate();

		Context context = getApplicationContext();

		getDeviceUUID();

		initImageLoader(context);
		initUserPreference(context);
	}

	private void getDeviceUUID() {
		if (UserPreference.getInstance().getUuid() == null) {
			String uuid = new DeviceUuidFactory(getApplicationContext())
					.getDeviceUuid().toString();
			if (!TextUtils.isEmpty(uuid)) {
				UserPreference.getInstance().setUuid(uuid);
			}
		}

		try {
			PackageInfo pi = getApplicationContext().getPackageManager()
					.getPackageInfo("com.aug3.yhyc", 0);
			int ver = pi.versionCode;
			String verName = pi.versionName;
			UserPreference.getInstance().setVer(ver);
			UserPreference.getInstance().setVerName(verName);

		} catch (NameNotFoundException e) {
		}
	}

	public void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public void initUserPreference(Context context) {

		SharedPreferences sp = context.getSharedPreferences(
				Constants.SharedPrefs.YHYC, Context.MODE_PRIVATE);
		long uid = sp.getLong(Constants.SharedPrefs.KEY_USERID, 0);
		if (uid != 0) {
			UserPreference.getInstance().setUid(uid);
			initUserPrefs(uid);
		}

		String userName = sp.getString(Constants.SharedPrefs.KEY_USERNAME, "");
		if (!TextUtils.isEmpty(userName)) {
			UserPreference.getInstance().setUserName(userName);
		}

		String mobile = sp.getString(Constants.SharedPrefs.KEY_USERMOBI, "");
		if (!TextUtils.isEmpty(mobile)) {
			UserPreference.getInstance().setMobile(mobile);
		}

		String token = sp.getString(Constants.SharedPrefs.KEY_TOKEN, "");
		if (!TextUtils.isEmpty(token)) {
			UserPreference.getInstance().setToken(token);
		}

		String orders = sp.getString(Constants.SharedPrefs.MY_ORDER_LIST, "");
		if (!TextUtils.isEmpty(orders)) {
			UserPreference.getInstance().setOrders(
					Tools.transfer2ListLong(orders));
		}
	}

	private void initUserPrefs(long uid) {

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.USERID, uid);

		YhycRestClient.get(Constants.ReqUrl.USER_PREFS, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							UserPrefs userPrefs = JSONUtil.fromJson(response
									.get(Constants.RESP_RESULT).toString(),
									UserPrefs.class);

							if (userPrefs != null) {
								if (userPrefs.getFav() != null) {
									UserPreference.getInstance().getFav()
											.addAll(userPrefs.getFav());
								}
								if (userPrefs.getCart() != null) {
									UserPreference.getInstance().getCart()
											.addAll(userPrefs.getCart());
								}

								if (userPrefs.getUrls() != null) {
									UserPreference.getInstance().setUrls(
											userPrefs.getUrls());
								}

								UserPreference.getInstance().setAc(
										userPrefs.getAc());

							}

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// Tools.ShowAPIError(YhycApplication.this);
					}
				});

	}
}