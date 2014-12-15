package com.aug3.yhyc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.DeviceUuidFactory;
import com.aug3.yhyc.util.UpdateManager;
import com.aug3.yhyc.util.UpdateManager.UpdateCallBack;

/**
 * 
 * class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 * 
 */
public class SplashActivity extends BaseActivity {

	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟2秒
	private static final long SPLASH_DELAY_MILLIS = 1500;

	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		init();
	}

	private void init() {

		if (UserPreference.getInstance().getUuid() == null) {
			String uuid = new DeviceUuidFactory(SplashActivity.this)
					.getDeviceUuid().toString();
			if (!TextUtils.isEmpty(uuid)) {
				UserPreference.getInstance().setUuid(uuid);
			}
		}

		new UpdateManager(this, new MainJob()).checkUpdate();

	}

	private class MainJob implements UpdateCallBack {

		@Override
		public void doAction() {
			// 读取SharedPreferences中需要的数据
			// 使用SharedPreferences来记录程序的使用次数

			// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
			isFirstIn = sp.getBoolean(Constants.SharedPrefs.FIRST_PREF, true);

			// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
			if (!isFirstIn) {
				// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
				mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
			} else {
				mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
			}
		}

	}

	private void goHome() {

		long uid = UserPreference.getInstance().getUid();

		if (uid == 0) {
			gotoActivity(new Intent(SplashActivity.this,
					UserLoginActivity.class));
		} else {
			gotoActivity(new Intent(SplashActivity.this, MainActivity.class));
		}

		finish();

	}

	private void goGuide() {
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

}
