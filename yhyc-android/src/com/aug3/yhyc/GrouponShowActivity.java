package com.aug3.yhyc;

import android.os.Bundle;
import android.widget.TextView;

public class GrouponShowActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupon_show);

		findViewById(R.id.actionbar_back).setOnClickListener(goBack);

		// TODO
		((TextView) findViewById(R.id.actionbar_title)).setText("咕咾肉好吃是吧不错很好");

	}

}
