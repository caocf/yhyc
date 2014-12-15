package com.aug3.yhyc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.R;

public class ActionBarCheckedView extends FrameLayout {

	public ImageView ivLeft;
	public TextView titleView;
	public CheckBox cb;

	public ActionBarCheckedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.actionbar_checked, this);

		ivLeft = (ImageView) findViewById(R.id.actionbar_checked_left);
		titleView = (TextView) findViewById(R.id.actionbar_checked_title);
		cb = (CheckBox) findViewById(R.id.actionbar_checkbox);
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

	public void setChecked(boolean checked) {
		cb.setChecked(checked);
	}

	public void setOnCheckedChangeListener(
			CompoundButton.OnCheckedChangeListener listener) {
		cb.setOnCheckedChangeListener(listener);
	}

}