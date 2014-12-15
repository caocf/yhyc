package com.aug3.yhyc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.R;

public class ActionBarTitleView extends FrameLayout {

	private TextView titleText;
	private ImageView iv_title_expand;

	public ActionBarTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.actionbar_title, this);
		titleText = (TextView) findViewById(R.id.title_text);

		iv_title_expand = (ImageView) findViewById(R.id.actionbar_title_expand);

	}

	public void setTitleText(String text) {
		titleText.setText(text);
	}

	public void setTitleText(int titleResId) {
		titleText.setText(titleResId);
	}

	public void setTitleOnClickListener(OnClickListener l) {
		titleText.setOnClickListener(l);
		iv_title_expand.setOnClickListener(l);
	}

	public void setTitleExpandVisibility(boolean visible) {
		if (visible)
			iv_title_expand.setVisibility(View.VISIBLE);
		else
			iv_title_expand.setVisibility(View.INVISIBLE);
	}

}