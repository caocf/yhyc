package com.aug3.yhyc.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.R;

public class ActionBarView extends FrameLayout {

	private ImageView gobackBtn;

	private TextView titleText;

	private ImageView iv_title_expand;

	public ActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.actionbar_back, this);

		gobackBtn = (ImageView) findViewById(R.id.goback);
		gobackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity a = (Activity) getContext();
				a.finish();
				a.overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			}
		});

		titleText = (TextView) findViewById(R.id.title_text);

		iv_title_expand = (ImageView) findViewById(R.id.actionbar_title_expand);
	}

	public void invisibleGoBackBtn(){
		gobackBtn.setVisibility(View.GONE);
	}
	
	public void setTitleText(int resId) {
		titleText.setText(resId);
	}

	public void setTitleText(String title) {
		titleText.setText(title);
	}

	public void setGoBackListener(OnClickListener l) {
		gobackBtn.setOnClickListener(l);
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