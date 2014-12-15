package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aug3.yhyc.api.Tag;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarNormalView;
import com.aug3.yhyc.view.AutoScaleViewGroup;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserTagsActivity extends BaseActivity {

	private ActionBarNormalView ab;
	private List<Tag> tags;

	private Set<Integer> userTags;
	private Set<Integer> userNewTags;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_tags);

		initActionBar();

		getAllTags();

	}

	private void initActionBar() {
		ab = (ActionBarNormalView) findViewById(R.id.actionbar_usertags);
		ab.setTitle(R.string.label_user_tags);

		ab.setLeftOnClickListener(btnSaveListener);
		ab.setRightOnClickListener(btnSaveListener);

	}

	private View.OnClickListener btnSaveListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			updateUserTags();
			closeWindow();
		}
	};

	private void updateUserTags() {

		if (userNewTags.size() == userTags.size()
				&& userNewTags.containsAll(userTags)) {

		} else {
			RequestParams p = new RequestParams();
			p.put("uid", UserPreference.getInstance().getUid());
			p.put("tags", TextUtils.join(Constants.COMMA, userNewTags));

			YhycRestClient.post(Constants.ReqUrl.USER_TAGS, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Tools.ShowAPIError(UserTagsActivity.this);
						}
					});
		}

	}

	private void getAllTags() {

		RequestParams p = new RequestParams();

		YhycRestClient.get(Constants.ReqUrl.TAGS, p,
				new JsonHttpResponseHandler() {
					@SuppressLint("NewApi")
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONArray taglist = (JSONArray) response
									.get(Constants.RESP_RESULT);
							int len = taglist.length();
							tags = new ArrayList<Tag>();
							for (int i = 0; i < len; i++) {
								Tag tag = JSONUtil.fromJson(taglist.get(i)
										.toString(), Tag.class);
								tags.add(tag);
							}

							getUserTags();

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Tools.ShowAPIError(UserTagsActivity.this);
					}
				});

	}

	private void getUserTags() {

		RequestParams p = new RequestParams();
		p.put("uid", UserPreference.getInstance().getUid());

		YhycRestClient.get(Constants.ReqUrl.USER_TAGS, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONArray taglist = (JSONArray) response
									.get(Constants.RESP_RESULT);
							int len = taglist.length();
							userTags = new HashSet<Integer>();
							userNewTags = new HashSet<Integer>();
							for (int i = 0; i < len; i++) {
								int tag = Integer.parseInt(taglist.get(i)
										.toString());
								userTags.add(tag);
							}
							userNewTags.addAll(userTags);

							drawTags();

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Tools.ShowAPIError(UserTagsActivity.this);
					}
				});

	}

	private void drawTags() {

		if (tags != null && !tags.isEmpty()) {

			AutoScaleViewGroup tagslayout = (AutoScaleViewGroup) findViewById(R.id.user_tags);
			TextView tv_tag = null;

			for (Tag tag : tags) {
				tv_tag = new TextView(this);
				tv_tag.setText(tag.getName());
				tv_tag.setTextSize(18);
				tv_tag.setTextColor(resources.getColor(R.color.white));
				tv_tag.setPadding(12, 6, 12, 6);

				if (userTags.contains(tag.getCode())) {
					tv_tag.setBackgroundResource(R.drawable.btn_submit_shape);
				} else {
					tv_tag.setBackgroundResource(R.drawable.btn_gray_shape);
				}
				final int id = tag.getCode();
				tv_tag.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						if (userNewTags.contains(id)) {
							userNewTags.remove(id);
							v.setBackgroundResource(R.drawable.btn_gray_shape);
						} else {
							userNewTags.add(id);
							v.setBackgroundResource(R.drawable.btn_submit_shape);
						}

					}
				});
				tagslayout.addView(tv_tag);
			}
		}
	}

}
