package com.aug3.yhyc;

import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.api.RespType;
import com.aug3.yhyc.api.User;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.base.Constants.ReqParam;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.DeviceUuidFactory;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserLoginActivity extends BaseActivity {

	private ImageView iv_ad;

	private EditText mobile, verify;

	private TextView tv_timer;

	private Button btn_verify, btn_submit;

	private boolean bind = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			bind = bundle.getBoolean(Constants.Extra.MOBILE_BIND, false);
		}

		if (UserPreference.getInstance().getUuid() == null) {
			String uuid = new DeviceUuidFactory(getApplicationContext())
					.getDeviceUuid().toString();

			if (!TextUtils.isEmpty(uuid)) {
				UserPreference.getInstance().setUuid(uuid);
			}
		}

		initActionBar();

		initLoginUI();
	}

	private void initActionBar() {

		findViewById(R.id.actionbar_login_jumpover).setVisibility(View.GONE);

		if (bind) {
			TextView tv_title = (TextView) findViewById(R.id.actionbar_login_title);
			tv_title.setText(R.string.label_bind_mobile);

		} else {
			findViewById(R.id.actionbar_login_jumpover).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							createTempUser();
						}
					});
		}

		iv_ad = (ImageView) findViewById(R.id.login_ad);
		Map<String, String> urls = UserPreference.getInstance().getUrls();
		if (urls != null && urls.containsKey("ad002")) {
			Tools.setImageResource(iv_ad, urls.get("ad002"), null);
		}
	}

	private void createTempUser() {

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.UUID, UserPreference.getInstance().getUuid());

		YhycRestClient.get(Constants.ReqUrl.USER_NEW_TEMP, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							User u = JSONUtil.fromJson(
									response.getString(Constants.RESP_RESULT),
									User.class);

							loginSuccess(u, u.getMobi());

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						Tools.ShowAPIError(UserLoginActivity.this);

					}
				});
	}

	private void initLoginUI() {

		mobile = (EditText) findViewById(R.id.login_mobile);

		verify = (EditText) findViewById(R.id.login_verify);

		btn_verify = (Button) findViewById(R.id.login_get_verification);

		tv_timer = (TextView) findViewById(R.id.login_timer);

		btn_submit = (Button) findViewById(R.id.login_btn);

		btn_verify.setOnClickListener(getVerificationListener);

		if (bind) {
			btn_submit.setText(R.string.label_bind_mobile);
			btn_submit.setOnClickListener(bindListener);
		} else {
			btn_submit.setOnClickListener(loginListener);
		}

	}

	private View.OnClickListener getVerificationListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			String mobile_value = mobile.getText().toString();
			if (TextUtils.isEmpty(mobile_value) || mobile_value.length() != 11) {
				Tools.ShowTip(UserLoginActivity.this,
						R.string.label_input_mobile_tips);
				return;
			}

			btn_verify.setBackgroundResource(R.drawable.btn_gray_shape);
			btn_verify.setEnabled(false);

			invokeVerification(mobile_value);

			tv_timer.setVisibility(View.VISIBLE);

			timer.start();

			findViewById(R.id.login_input_verification).setVisibility(
					View.VISIBLE);
			btn_submit.setVisibility(View.VISIBLE);

		}
	};

	private void invokeVerification(String mobi) {

		RequestParams p = new RequestParams();
		p.put(ReqParam.MOBI, mobi);
		p.put(Constants.ReqParam.UUID, UserPreference.getInstance().getUuid());

		YhycRestClient.get(Constants.ReqUrl.USER_VERIFICATION, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							String code = response
									.getString(Constants.RESP_CODE);

							if (!RespType.SUCCESS.getCode().equals(code)) {

								Tools.ShowTip(
										UserLoginActivity.this,
										R.string.label_login_fetch_verification_failed);

							}
						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						Tools.ShowAPIError(UserLoginActivity.this);

					}
				});
	}

	private CountDownTimer timer = new CountDownTimer(61000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			tv_timer.setText(String.valueOf(millisUntilFinished / 1000));
		}

		@Override
		public void onFinish() {
			btn_verify.setBackgroundResource(R.drawable.btn_submit_shape);
			btn_verify.setEnabled(true);
			tv_timer.setVisibility(View.INVISIBLE);
		}
	};

	private View.OnClickListener loginListener = new OnClickListener() {

		public void onClick(View v) {

			final String mobile_value = mobile.getText().toString();
			final String verify_value = verify.getText().toString();

			RequestParams p = new RequestParams();
			p.put(ReqParam.MOBI, mobile_value);
			p.put(ReqParam.VERIFY_CODE, verify_value);

			YhycRestClient.get(Constants.ReqUrl.USER_VERIFY, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								String code = response
										.getString(Constants.RESP_CODE);

								if (RespType.SUCCESS.getCode().equals(code)) {

									User user = JSONUtil.fromJson(
											response.get(Constants.RESP_RESULT)
													.toString(), User.class);

									loginSuccess(user, mobile_value);

								} else {
									String res = response.get(
											Constants.RESP_RESULT).toString();
									Tools.ShowTip(UserLoginActivity.this, res);
								}
							} catch (JSONException e) {
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {

							Tools.ShowAPIError(UserLoginActivity.this);

						}
					});

		}
	};

	private View.OnClickListener bindListener = new OnClickListener() {

		public void onClick(View v) {

			final String mobile_value = mobile.getText().toString();
			final String verify_value = verify.getText().toString();

			RequestParams p = new RequestParams();
			p.put(ReqParam.MOBI, mobile_value);
			p.put(ReqParam.VERIFY_CODE, verify_value);
			p.put(ReqParam.USERID, UserPreference.getInstance().getUid());
			p.put(Constants.ReqParam.UUID, UserPreference.getInstance()
					.getUuid());

			YhycRestClient.get(Constants.ReqUrl.USER_BIND, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								String code = response
										.getString(Constants.RESP_CODE);

								if (RespType.SUCCESS.getCode().equals(code)) {

									User user = JSONUtil.fromJson(
											response.get(Constants.RESP_RESULT)
													.toString(), User.class);

									loginSuccess(user, mobile_value);

								} else {
									String res = response.get(
											Constants.RESP_RESULT).toString();
									Tools.ShowTip(UserLoginActivity.this, res);
								}
							} catch (JSONException e) {
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {

							Tools.ShowAPIError(UserLoginActivity.this);

						}
					});

		}
	};

	private void loginSuccess(User user, String mobile) {

		Editor editor = sp.edit();

		editor.putString(Constants.SharedPrefs.KEY_USERMOBI, mobile);
		editor.putLong(Constants.SharedPrefs.KEY_USERID, user.getUid());
		editor.putString(Constants.SharedPrefs.KEY_USERNAME, user.getName());
		long shequ = user.getShequ();
		if (shequ != 0) {
			editor.putLong(Constants.SharedPrefs.MY_SHEQUID, shequ);
		}

		editor.commit();

		UserPreference.getInstance().setUserName(user.getName());
		UserPreference.getInstance().setUid(user.getUid());
		UserPreference.getInstance().setMobile(mobile);
		UserPreference.getInstance().setAc(user.getAc());

		finish();

		if (!bind) {
			Intent i = new Intent(UserLoginActivity.this, MainActivity.class);
			if (user.getDist() != 0) {
				i.putExtra(Constants.Extra.DISTRICT, user.getDist());
			}
			if (user.getShequ() != 0) {
				i.putExtra(Constants.Extra.SHEQU, user.getShequ());
			}
			gotoActivity(i);
		}
	}

}
