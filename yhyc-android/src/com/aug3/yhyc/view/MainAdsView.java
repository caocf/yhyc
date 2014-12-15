package com.aug3.yhyc.view;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.aug3.yhyc.R;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.widget.Rotate3D;

public class MainAdsView extends FrameLayout implements OnTouchListener {

	private ImageView imageMain, imageNext, imageLast;

	private VelocityTracker mVelocityTracker;

	private int mLastMotionX;

	private float degree = (float) 0.0, perDegree;

	private Rotate3D rotate3d, rotate3d2, rotate3d3;

	private int mCenterX, mCenterY;

	private int currentTab = 0;

	public MainAdsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.fragment_ads, this);

		Map<String, String> urls = UserPreference.getInstance().getUrls();
		String ad01 = urls.get("ad_home_01");
		String ad02 = urls.get("ad_home_02");
		String ad03 = urls.get("ad_home_03");

		imageMain = (ImageView) findViewById(R.id.layout_main_ad);
		Tools.setImageResource(imageMain, ad01, ScaleType.FIT_XY);
		imageMain.setOnTouchListener(this);

		imageNext = (ImageView) findViewById(R.id.layout_next_ad);
		Tools.setImageResource(imageNext, ad02, ScaleType.FIT_XY);
		imageNext.setOnTouchListener(this);

		imageLast = (ImageView) findViewById(R.id.layout_last_ad);
		Tools.setImageResource(imageLast, ad03, ScaleType.FIT_XY);
		imageLast.setOnTouchListener(this);

		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		mCenterX = dm.widthPixels / 2;
		mCenterY = dm.heightPixels / 2;
		perDegree = (float) (90.0 / dm.widthPixels);

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		int x = (int) event.getX();
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();// 获得VelocityTracker类实例
		}
		mVelocityTracker.addMovement(event);// 将事件加入到VelocityTracker类实例中
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			mVelocityTracker.computeCurrentVelocity(1000, 1000);
			int dx = x - mLastMotionX;
			if (dx != 0) {
				doRotate(dx);
				if (degree > 90) {
					degree = 0;
					break;
				}
			} else {
				return false;
			}
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_UP:
			// 设置units的值为1000，意思为一秒时间内运动了多少个像素
			mVelocityTracker.computeCurrentVelocity(1000);
			float VelocityX = mVelocityTracker.getXVelocity();
			if (VelocityX > 500 || VelocityX < -500) {
				endRotateByVelocity();
			} else {
				endRotate();
			}
			releaseVelocityTracker();
			break;

		case MotionEvent.ACTION_CANCEL:
			releaseVelocityTracker();
			break;
		}
		return true;
	}

	private void doRotate(int dx) {
		float xd = degree;
		imageNext.setVisibility(View.VISIBLE);
		imageMain.setVisibility(View.VISIBLE);
		imageLast.setVisibility(View.VISIBLE);

		degree += perDegree * dx;
		rotate3d = new Rotate3D(xd, degree, 0, mCenterX, mCenterY);
		rotate3d2 = new Rotate3D(90 + xd, 90 + degree, 0, mCenterX, mCenterY);
		rotate3d3 = new Rotate3D(-90 + xd, -90 + degree, 0, mCenterX, mCenterY);
		if (currentTab == 0) {
			imageMain.startAnimation(rotate3d);
			imageNext.startAnimation(rotate3d2);
			imageLast.startAnimation(rotate3d3);
		} else if (currentTab == 1) {
			imageMain.startAnimation(rotate3d3);
			imageNext.startAnimation(rotate3d);
			imageLast.startAnimation(rotate3d2);
		} else if (currentTab == 2) {
			imageMain.startAnimation(rotate3d2);
			imageNext.startAnimation(rotate3d3);
			imageLast.startAnimation(rotate3d);
		}
		rotate3d.setFillAfter(true);
		rotate3d2.setFillAfter(true);
		rotate3d3.setFillAfter(true);
	}

	private void releaseVelocityTracker() {
		if (null != mVelocityTracker) {
			mVelocityTracker.clear();
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}

	}

	private void endRotateByVelocity() {
		if (degree > 0) {
			rotate3d = new Rotate3D(degree, 90, 0, mCenterX, mCenterY);
			rotate3d3 = new Rotate3D(-90 + degree, 0, 0, mCenterX, mCenterY);
			rotate3d.setDuration(300);
			rotate3d3.setDuration(300);
			if (currentTab == 0) {
				imageMain.startAnimation(rotate3d);
				imageLast.startAnimation(rotate3d3);
			} else if (currentTab == 2) {
				imageLast.startAnimation(rotate3d);
				imageNext.startAnimation(rotate3d3);
			} else if (currentTab == 1) {
				imageNext.startAnimation(rotate3d);
				imageMain.startAnimation(rotate3d3);
			}

			currentTab = (currentTab - 1) % 3;
			if (currentTab < 0) {
				currentTab = 2;
			}
		} else if (degree < 0) {
			rotate3d = new Rotate3D(degree, -90, 0, mCenterX, mCenterY);
			rotate3d2 = new Rotate3D(90 + degree, 0, 0, mCenterX, mCenterY);
			rotate3d.setDuration(300);
			rotate3d2.setDuration(300);
			if (currentTab == 0) {
				imageMain.startAnimation(rotate3d);
				imageNext.startAnimation(rotate3d2);
			} else if (currentTab == 1) {
				imageNext.startAnimation(rotate3d);
				imageLast.startAnimation(rotate3d2);
			} else if (currentTab == 2) {
				imageLast.startAnimation(rotate3d);
				imageMain.startAnimation(rotate3d2);
			}

			currentTab = (currentTab + 1) % 3;
		}

		setViewVisibile();

		degree = 0;

	}

	private void endRotate() {
		if (degree > 45) {
			rotate3d = new Rotate3D(degree, 90, 0, mCenterX, mCenterY);
			rotate3d3 = new Rotate3D(-90 + degree, 0, 0, mCenterX, mCenterY);
			rotate3d.setDuration(300);
			rotate3d3.setDuration(300);
			if (currentTab == 0) {
				imageMain.startAnimation(rotate3d);
				imageLast.startAnimation(rotate3d3);
			} else if (currentTab == 2) {
				imageLast.startAnimation(rotate3d);
				imageNext.startAnimation(rotate3d3);
			} else if (currentTab == 1) {
				imageNext.startAnimation(rotate3d);
				imageMain.startAnimation(rotate3d3);
			}

			currentTab = (currentTab - 1) % 3;
			if (currentTab < 0) {
				currentTab = 2;
			}
		} else if (degree < -45) {
			rotate3d = new Rotate3D(degree, -90, 0, mCenterX, mCenterY);
			rotate3d2 = new Rotate3D(90 + degree, 0, 0, mCenterX, mCenterY);
			rotate3d.setDuration(300);
			rotate3d2.setDuration(300);
			if (currentTab == 0) {
				imageMain.startAnimation(rotate3d);
				imageNext.startAnimation(rotate3d2);
			} else if (currentTab == 1) {
				imageNext.startAnimation(rotate3d);
				imageLast.startAnimation(rotate3d2);
			} else if (currentTab == 2) {
				imageLast.startAnimation(rotate3d);
				imageMain.startAnimation(rotate3d2);
			}

			currentTab = (currentTab + 1) % 3;
		} else {
			rotate3d = new Rotate3D(degree, 0, 0, mCenterX, mCenterY);
			rotate3d2 = new Rotate3D(90 + degree, 90, 0, mCenterX, mCenterY);
			rotate3d3 = new Rotate3D(-90 + degree, -90, 0, mCenterX, mCenterY);
			rotate3d.setDuration(500);
			rotate3d2.setDuration(500);
			rotate3d3.setDuration(500);
			if (currentTab == 0) {
				imageMain.startAnimation(rotate3d);
				imageNext.startAnimation(rotate3d2);
				imageLast.startAnimation(rotate3d3);
			} else if (currentTab == 1) {
				imageNext.startAnimation(rotate3d);
				imageLast.startAnimation(rotate3d2);
				imageMain.startAnimation(rotate3d3);
			} else if (currentTab == 2) {
				imageLast.startAnimation(rotate3d);
				imageMain.startAnimation(rotate3d2);
				imageNext.startAnimation(rotate3d3);
			}
		}

		System.out.println(">>>>>>>>degree:" + degree + " currentTab:"
				+ currentTab);
		setViewVisibile();

		degree = 0;
	}

	private void setViewVisibile() {
		if (currentTab == 0) {
			imageMain.setVisibility(View.VISIBLE);
			imageNext.setVisibility(View.GONE);
			imageLast.setVisibility(View.GONE);
		} else if (currentTab == 1) {
			imageMain.setVisibility(View.GONE);
			imageNext.setVisibility(View.VISIBLE);
			imageLast.setVisibility(View.GONE);
		} else if (currentTab == 2) {
			imageMain.setVisibility(View.GONE);
			imageNext.setVisibility(View.GONE);
			imageLast.setVisibility(View.VISIBLE);
		}
	}

}