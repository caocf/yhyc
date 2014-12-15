package com.aug3.yhyc.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aug3.yhyc.MainActivity;
import com.aug3.yhyc.R;
import com.aug3.yhyc.UserLoginActivity;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.model.UserPreference;

public class ViewPagerAdapter extends PagerAdapter {

	// 界面列表
	private List<View> views;
	private Activity activity;

	public ViewPagerAdapter(List<View> views, Activity activity) {
		this.views = views;
		this.activity = activity;
	}

	// 销毁arg1位置的界面
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	// 获得当前界面数
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// 初始化arg1位置的界面
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1) {
			ImageView iv_start_yhyc = (ImageView) arg0
					.findViewById(R.id.iv_start_yhyc);
			ImageButton btn_start_yhyc = (ImageButton) arg0
					.findViewById(R.id.ib_start_yhyc);

			iv_start_yhyc.setOnClickListener(startTrialListener);

			btn_start_yhyc.setOnClickListener(startTrialListener);
		}
		return views.get(arg1);
	}

	private View.OnClickListener startTrialListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 设置已经引导
			setGuided();
			goHome();

		}
	};

	private void goHome() {
		long uid = UserPreference.getInstance().getUid();
		Intent intent;
		if (uid == 0) {
			intent = new Intent(activity, UserLoginActivity.class);
		} else {
			// 跳转
			intent = new Intent(activity, MainActivity.class);
		}
		activity.startActivity(intent);
		activity.finish();
		activity.overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	/**
	 * 
	 * method desc：设置已经引导过了，下次启动不用再次引导
	 */
	private void setGuided() {
		SharedPreferences sp = activity.getSharedPreferences(
				Constants.SharedPrefs.YHYC, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		// 存入数据
		editor.putBoolean(Constants.SharedPrefs.FIRST_PREF, false);
		// 提交修改
		editor.commit();
	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
