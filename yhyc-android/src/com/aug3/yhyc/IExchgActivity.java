package com.aug3.yhyc;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aug3.yhyc.api.RespType;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class IExchgActivity extends BaseActivity {

	private ActionBarView ab;

	private TextView tv_item;

	private EditText et_name, et_addr, et_mobi;

	private String itemName, itemid;

	private int itemac;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iexchg);

		ab = (ActionBarView) findViewById(R.id.title_view);
		ab.setTitleText(R.string.title_item_iexchg);

		initUserDeliveryForm();

	}

	@SuppressLint("NewApi")
	protected void initUserDeliveryForm() {

		Bundle bundle = getIntent().getExtras();
		itemName = bundle.getString(Constants.Extra.ITEM);
		itemid = bundle.getString(Constants.Extra.ITEMID);
		itemac = bundle.getInt(Constants.Extra.AC);

		tv_item = (TextView) findViewById(R.id.item_name);
		tv_item.setText(itemName);

		et_name = (EditText) findViewById(R.id.user_name);
		et_addr = (EditText) findViewById(R.id.user_addr);
		et_mobi = (EditText) findViewById(R.id.user_mobi);

		findViewById(R.id.iexchg_ok_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						String name = et_name.getText().toString();
						String addr = et_addr.getText().toString();
						String mobi = et_mobi.getText().toString();

						if (TextUtils.isEmpty(name) || TextUtils.isEmpty(addr)
								|| TextUtils.isEmpty(mobi)) {
							Tools.ShowTip(IExchgActivity.this,
									R.string.label_toast_fillin_all);
							return;
						}

						RequestParams p = new RequestParams();
						p.put(Constants.ReqParam.USERID, UserPreference
								.getInstance().getUid());
						p.put(Constants.ReqParam.ITEM, itemid);
						p.put(Constants.ReqParam.NAME, name);
						p.put("addr", addr);
						p.put(Constants.ReqParam.MOBI, mobi);

						YhycRestClient.post(Constants.ReqUrl.WEB_STORE_IEXCHG,
								p, new JsonHttpResponseHandler() {
									@Override
									public void onSuccess(int statusCode,
											Header[] headers,
											JSONObject response) {

										String code = "200";
										try {
											code = response
													.getString(Constants.RESP_CODE);

										} catch (JSONException e) {
										}

										if (RespType.SUCCESS.getCode().equals(
												code)) {

											UserPreference.getInstance().setAc(
													UserPreference
															.getInstance()
															.getAc()
															- itemac);

											Tools.ShowLongTip(
													IExchgActivity.this,
													R.string.label_toast_success_submit);

										} else {

											Tools.ShowTip(
													IExchgActivity.this,
													R.string.label_toast_failed_submit);
										}

									}

									@Override
									public void onFailure(int statusCode,
											Header[] headers,
											Throwable throwable,
											JSONObject errorResponse) {

										Tools.ShowAPIError(IExchgActivity.this);

									}
								});

					}
				});

	}

}
