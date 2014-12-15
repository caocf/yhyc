package com.aug3.yhyc;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ActivityMgr extends Application {

	private static ActivityMgr activityMgr = null;

	public List<Activity> activities = new LinkedList<Activity>();

	public synchronized static ActivityMgr getInstance() {
		if (null == activityMgr) {
			activityMgr = new ActivityMgr();

		}
		return activityMgr;
	}

	public void addActivity(Activity activity) {
		if (activity != null) {
			activities.add(activity);

		}
	}

	public void exit() {
		for (Activity activity : activities) {
			if (activity != null) {
				activity.finish();
			}
		}
		System.exit(0);
	}

	public void delete(Activity activity) {
		if (activities.contains(activity)) {
			activities.remove(activity);

		}
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

}
