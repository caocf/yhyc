package com.aug3.yhyc;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.base.Constants.ReqParam;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;
import com.aug3.yhyc.view.SugguestionDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AboutusActivity extends BaseActivity {

	private ActionBarView ab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);

		initActionBar();

		initServiceProtocol();

		initAppVersion();
	}

	private void initActionBar() {
		ab = (ActionBarView) findViewById(R.id.actionbar_back);
		ab.setTitleText(R.string.label_aboutus);

	}

	private void initServiceProtocol() {

		findViewById(R.id.aboutus_yhyc).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(AboutusActivity.this,
								WebViewActivity.class);
						i.putExtra(Constants.Extra.TITLE, R.string.app_name);
						i.putExtra(Constants.Extra.URL,
								"http://www.1hyc.com/aboutus.html");
						gotoActivity(i);
					}
				});

		findViewById(R.id.aboutus_service_protocol).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(AboutusActivity.this,
								WebViewActivity.class);
						i.putExtra(Constants.Extra.TITLE,
								R.string.label_service_protocol);
						i.putExtra(Constants.Extra.URL,
								"http://www.1hyc.com/sla.html");
						gotoActivity(i);
					}
				});

		findViewById(R.id.aboutus_service_after_sales).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(AboutusActivity.this,
								WebViewActivity.class);
						i.putExtra(Constants.Extra.TITLE,
								R.string.label_service_after_sales);
						i.putExtra(Constants.Extra.URL,
								"http://www.1hyc.com/service.html");
						gotoActivity(i);
					}
				});

		AsyncImageLoader.displayImage("http://www.1hyc.com/app/catchword.png",
				(ImageView) findViewById(R.id.aboutus_catchword));

		findViewById(R.id.aboutus_complaints_proposals).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						sugguestion();
					}
				});
	}

	private void initAppVersion() {

		String verName = null;
		try {
			verName = this.getPackageManager().getPackageInfo("com.aug3.yhyc",
					0).versionName;
		} catch (NameNotFoundException e) {
		}

		if (!TextUtils.isEmpty(verName)) {
			((TextView) findViewById(R.id.aboutus_ver)).setText(verName);
		}

	}

	private void sugguestion() {

		final SugguestionDialog ad = new SugguestionDialog(this);

		ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(ad.getUserContent())
						|| TextUtils.isEmpty(ad.getUserMobi())) {
					Tools.ShowTip(AboutusActivity.this,
							R.string.label_toast_fillin_all);
					return;
				}

				RequestParams p = new RequestParams();
				p.put(ReqParam.USERID, UserPreference.getInstance().getUid());
				p.put(ReqParam.NAME, ad.getUserName());
				p.put(ReqParam.MOBI, ad.getUserMobi());
				p.put("content", ad.getUserContent());

				YhycRestClient.post(Constants.ReqUrl.USER_SUGGUEST, p,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {

								Tools.ShowTip(AboutusActivity.this,
										R.string.label_toast_success_submit);
								ad.dismiss();
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {

								Tools.ShowAPIError(AboutusActivity.this);

							}
						});

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
