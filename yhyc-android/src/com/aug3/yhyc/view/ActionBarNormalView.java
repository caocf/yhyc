package com.aug3.yhyc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.R;

public class ActionBarNormalView extends FrameLayout {

	public ImageView ivLeft;
	public TextView titleView;
	public TextView btnRight;

	public ActionBarNormalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.actionbar_normal, this);

		ivLeft = (ImageView) findViewById(R.id.actionbar_nor_left);
		titleView = (TextView) findViewById(R.id.actionbar_nor_title);
		btnRight = (TextView) findViewById(R.id.actionbar_nor_right);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setTitle(int resID) {
		titleView.setText(resID);
	}

	public void setLeftOnClickListener(View.OnClickListener ocl) {
		ivLeft.setOnClickListener(ocl);
	}

	public void setRightOnClickListener(View.OnClickListener ocl) {
		btnRight.setOnClickListener(ocl);
	}

}