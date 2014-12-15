package com.aug3.yhyc;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarTitleView;

public class OrderSuccessActivity extends BaseActivity {

	private ActionBarTitleView ab;

	private ImageView iv_ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_success);

		ab = (ActionBarTitleView) findViewById(R.id.actionbar_title);
		ab.setTitleText(R.string.title_confirm_info);

		findViewById(R.id.orderinfo_goto_myorders_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(OrderSuccessActivity.this,
								MyOrdersActivity.class);
						gotoActivity(intent);

					}
				});

		findViewById(R.id.orderinfo_goback_home_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ActivityMgr.getInstance().exit();
						// myOverridePendingTransition();
					}
				});

		iv_ad = (ImageView) findViewById(R.id.orderinfo_ad);
		Map<String, String> urls = UserPreference.getInstance().getUrls();
		if (urls != null && urls.containsKey("ad003")) {
			Tools.setImageResource(iv_ad, urls.get("ad003"), null);
		}
	}

}
