package com.aug3.yhyc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.ServiceAgent;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;
import com.aug3.yhyc.view.ConfirmDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingsActivity extends BaseActivity {

	private ActionBarView ab;

	private TextView tv_name;

	private String userNameBeforeEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		initActionBar();

		initView();
	}

	@Override
	public void onResume() {
		super.onResume();

		TextView mobile = (TextView) findViewById(R.id.settings_bind_mobile);

		String mobi = UserPreference.getInstance().getMobile();

		if (TextUtils.isEmpty(mobi)) {

			mobile.setText(R.string.label_bind_mobile);
			mobile.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(SettingsActivity.this,
							UserLoginActivity.class);
					i.putExtra(Constants.Extra.MOBILE_BIND, true);
					gotoActivity(i);

				}
			});
		} else {
			mobile.setText(resources
					.getString(R.string.label_bind_mobile_already)
					+ " "
					+ mobi.replace(mobi.substring(4, 8), "****"));
		}

	}

	private void initActionBar() {

		userNameBeforeEdit = UserPreference.getInstance().getUserName();

		tv_name = (TextView) findViewById(R.id.user_nickname);

		if (!TextUtils.isEmpty(userNameBeforeEdit)) {
			tv_name.setText(userNameBeforeEdit);
		}

		ab = (ActionBarView) findViewById(R.id.actionbar_back);
		ab.setTitleText(R.string.action_settings);
		ab.setGoBackListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String name = tv_name.getText().toString();
				if (name != null && !name.equals(userNameBeforeEdit)) {
					ServiceAgent.updateUserName(name);
				}

				closeWindow();

			}
		});

	}

	private void initView() {

		findViewById(R.id.settings_mytags).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(SettingsActivity.this,
								UserTagsActivity.class);
						gotoActivity(i);

					}
				});

		findViewById(R.id.settings_clear_img_cache).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						clearCache();
					}
				});

	}

	private void clearCache() {

		final ConfirmDialog ad = new ConfirmDialog(this);

		ad.setTitle(R.string.label_tips);
		ad.setMessage(R.string.label_tip_cache);
		ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ImageLoader.getInstance().clearDiscCache();
				ImageLoader.getInstance().clearMemoryCache();
				Tools.ShowTip(SettingsActivity.this,
						R.string.label_tip_cache_cleared);

				ad.dismiss();

			}
		});

		ad.setNegativeButton(R.string.btn_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});
	}

}
