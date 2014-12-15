package com.aug3.yhyc;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.aug3.yhyc.api.RequestShop;
import com.aug3.yhyc.api.RespType;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class JoinusActivity extends BaseActivity {

	private ActionBarView ab;

	private EditText name, idcard, mobi, mail, city, range, experience, desc;

	private Button clear_name, clear_idcard, clear_mobi, clear_mail,
			clear_city, clear_range, clear_experience, clear_desc, btn_submit;

	private CheckBox cbAgree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joinus);

		ab = (ActionBarView) findViewById(R.id.actionbar_back);
		ab.setTitleText(R.string.title_join_us);

		initView();

	}

	private void initView() {

		name = (EditText) findViewById(R.id.join_name);
		idcard = (EditText) findViewById(R.id.join_idcard);
		mobi = (EditText) findViewById(R.id.join_mobi);
		mail = (EditText) findViewById(R.id.join_mail);
		city = (EditText) findViewById(R.id.join_city_dist);
		range = (EditText) findViewById(R.id.join_range);
		experience = (EditText) findViewById(R.id.join_experience);
		desc = (EditText) findViewById(R.id.join_desc);
		cbAgree = (CheckBox) findViewById(R.id.cb_agreement);

		clear_name = (Button) findViewById(R.id.join_name_clear);
		clear_idcard = (Button) findViewById(R.id.join_idcard_clear);
		clear_mobi = (Button) findViewById(R.id.join_mobi_clear);
		clear_mail = (Button) findViewById(R.id.join_mail_clear);
		clear_city = (Button) findViewById(R.id.join_city_dist_clear);
		clear_range = (Button) findViewById(R.id.join_range_clear);
		clear_experience = (Button) findViewById(R.id.join_experience_clear);
		clear_desc = (Button) findViewById(R.id.join_desc_clear);

		bindClearEventToEditText(clear_name, name);
		bindClearEventToEditText(clear_idcard, idcard);
		bindClearEventToEditText(clear_mobi, mobi);
		bindClearEventToEditText(clear_mail, mail);
		bindClearEventToEditText(clear_city, city);
		bindClearEventToEditText(clear_range, range);
		bindClearEventToEditText(clear_experience, experience);
		bindClearEventToEditText(clear_desc, desc);

		btn_submit = (Button) findViewById(R.id.join_sumbit);
		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (TextUtils.isEmpty(name.getText().toString())
						|| TextUtils.isEmpty(idcard.getText().toString())
						|| TextUtils.isEmpty(mobi.getText().toString())
						|| TextUtils.isEmpty(city.getText().toString())
						|| TextUtils.isEmpty(range.getText().toString())
						|| TextUtils.isEmpty(experience.getText().toString())) {

					Tools.ShowTip(JoinusActivity.this,
							R.string.label_toast_fillin_all);

				} else if (idcard.getText().toString().length() != 18) {

					Tools.ShowTip(JoinusActivity.this,
							R.string.label_toast_invalid_idcard);

				} else if (mobi.getText().toString().length() < 7) {

					Tools.ShowTip(JoinusActivity.this,
							R.string.label_toast_invalid_mobile);

				} else if (!cbAgree.isChecked()) {

					Tools.ShowTip(JoinusActivity.this,
							R.string.label_toast_confirm_user_agreement);

				} else {

					RequestShop shop = new RequestShop();
					shop.setOwner(name.getText().toString());
					shop.setIdcard(idcard.getText().toString());
					shop.setTel(mobi.getText().toString());
					shop.setDist(city.getText().toString());
					shop.setMail(mail.getText().toString());
					shop.setBusi(range.getText().toString());
					shop.setExp(experience.getText().toString());
					shop.setDesc(desc.getText().toString());

					submitRequest(shop);

				}
			}
		});

	}

	private void submitRequest(RequestShop shop) {

		long uid = UserPreference.getInstance().getUid();
		if (uid != 0) {
			shop.setUid(uid);
		}
		RequestParams p = new RequestParams();
		p.put("shop", JSONUtil.toJson(shop));

		YhycRestClient.post(Constants.ReqUrl.SHOP_REQUEST, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						String code = null;

						try {
							code = response.getString(Constants.RESP_CODE);

						} catch (JSONException e) {
						}

						if (RespType.DUPLICATE_RECORD.getCode().equals(code)) {

							Tools.ShowTip(JoinusActivity.this,
									R.string.label_toast_duplicate_submit);

						} else {

							Tools.ShowTip(JoinusActivity.this,
									R.string.label_toast_success_submit);
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						Tools.ShowTip(JoinusActivity.this,
								R.string.label_toast_failed_submit);

					}
				});
	}

}
