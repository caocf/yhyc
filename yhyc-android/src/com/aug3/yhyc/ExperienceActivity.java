package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.aug3.yhyc.api.Comment;
import com.aug3.yhyc.api.CommentReq;
import com.aug3.yhyc.api.ItemDTO;
import com.aug3.yhyc.api.Order;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.base.Constants.ReqParam;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarNormalView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ExperienceActivity extends BaseActivity {

	private ActionBarNormalView ab;

	private EditText et_experience;

	private ImageView rating1, rating2, rating3, rating4, rating5;

	private List<String> listitems = new ArrayList<String>();
	private List<ItemDTO> items;
	private long orderid;

	private CheckBox cb_comment_all;

	private ArrayAdapter<String> spinnerAdapter;
	private Spinner spinner;

	private Button btn_submit;

	private Map<Long, Comment> comments = new HashMap<Long, Comment>();
	private int rating;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experience);

		initActionBar();

		initCommentPanel();

	}

	public void onResume() {
		super.onResume();
		getOrderItems();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!comments.isEmpty()) {
			saveComments();
		}
	}

	private void saveComments() {

		RequestParams p = new RequestParams();

		CommentReq cr = new CommentReq();
		cr.setItemsRating(comments);
		cr.setOrderid(orderid);
		cr.setUid(UserPreference.getInstance().getUid());

		p.put(ReqParam.COMMENTS, JSONUtil.toJson(cr));

		YhycRestClient.post(Constants.ReqUrl.ITEM_COMMENT_NEW, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						comments.clear();

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						Tools.ShowAPIError(ExperienceActivity.this);
					}
				});

	}

	private void initActionBar() {

		ab = (ActionBarNormalView) findViewById(R.id.actionbar_normal);
		ab.setLeftOnClickListener(goBack);
		ab.setTitle(R.string.title_experience_comment);
		ab.btnRight.setVisibility(View.GONE);
	}

	private void getOrderItems() {

		// add cache for 10mins
		Bundle bundle = getIntent().getExtras();
		orderid = bundle.getLong(Constants.Extra.ID);

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.ID, orderid);

		YhycRestClient.get(Constants.ReqUrl.ORDER_SHOW, p,
				new JsonHttpResponseHandler() {
					@SuppressLint("NewApi")
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONObject myorders = (JSONObject) response
									.get(Constants.RESP_RESULT);
							Order order = JSONUtil.fromJson(
									myorders.toString(), Order.class);

							items = order.getItems();
							listitems = new ArrayList<String>();
							for (ItemDTO i : items) {
								listitems.add(i.getName());

							}
							// For an ArrayAdapter, notifyDataSetChanged only
							// works if you use the add, insert, remove, and
							// clear functions on the Adapter.
							spinnerAdapter.addAll(listitems);

							spinnerAdapter.notifyDataSetChanged();

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						Tools.ShowAPIError(ExperienceActivity.this);
					}
				});

		spinner = (Spinner) findViewById(R.id.experience_rating_obj);

		spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listitems);

		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(spinnerAdapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				long selected = items.get(position).getId();
				if (comments.containsKey(selected)) {
					doRating(comments.get(selected).getScore());
				} else {
					doRating(5);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void initCommentPanel() {

		cb_comment_all = (CheckBox) findViewById(R.id.experience_comment_all_cb);
		et_experience = (EditText) findViewById(R.id.experience_comment_text);

		rating1 = (ImageView) findViewById(R.id.experience_rating_iv1);
		rating2 = (ImageView) findViewById(R.id.experience_rating_iv2);
		rating3 = (ImageView) findViewById(R.id.experience_rating_iv3);
		rating4 = (ImageView) findViewById(R.id.experience_rating_iv4);
		rating5 = (ImageView) findViewById(R.id.experience_rating_iv5);

		setRatingBarClickListener(rating1, 1);
		setRatingBarClickListener(rating2, 2);
		setRatingBarClickListener(rating3, 3);
		setRatingBarClickListener(rating4, 4);
		setRatingBarClickListener(rating5, 5);

		btn_submit = (Button) findViewById(R.id.experience_submit_btn);
		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onSubmit();
			}
		});

	}

	private void onSubmit() {

		int count = 0;
		if (cb_comment_all.isChecked()) {
			for (ItemDTO item : items) {
				count += addComment(item.getId());
			}
		} else {
			count += addComment(items.get(spinner.getSelectedItemPosition())
					.getId());
		}

		if (count == 0) {
			Tools.ShowLongTip(ExperienceActivity.this,
					R.string.label_toast_success_submit);
		} else {
			UserPreference.getInstance().setAc(
					UserPreference.getInstance().getAc() + count);
			Tools.ShowLongTip(ExperienceActivity.this,
					resources.getString(R.string.label_add_acculative_score)
							.replace("{n}", String.valueOf(count)));
		}

	}

	private int addComment(long itemid) {

		int effect = 0;
		if (comments.containsKey(itemid)
				|| TextUtils.isEmpty(et_experience.getText().toString())) {
			effect = 0;
		} else {
			effect = 1;
		}

		Comment comment = new Comment();
		UserPreference prefs = UserPreference.getInstance();
		comment.setUid(prefs.getUid());
		comment.setName(prefs.getUserName() == null ? (prefs.getMobile() == null ? ""
				: prefs.getMobile())
				: prefs.getUserName());
		comment.setScore(rating);
		comment.setContent(et_experience.getText().toString());
		comments.put(itemid, comment);

		return effect;
	}

	private void setRatingBarClickListener(ImageView iv, final int score) {

		iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doRating(score);

			}
		});

	}

	private void doRating(int score) {
		switch (score) {
		case 1:
			rating1.setImageResource(R.drawable.item_star_yellow);
			rating2.setImageResource(R.drawable.item_star);
			rating3.setImageResource(R.drawable.item_star);
			rating4.setImageResource(R.drawable.item_star);
			rating5.setImageResource(R.drawable.item_star);
			et_experience.setHint(R.string.label_comment_star_1);
			rating = 1;
			break;
		case 2:
			rating1.setImageResource(R.drawable.item_star_yellow);
			rating2.setImageResource(R.drawable.item_star_yellow);
			rating3.setImageResource(R.drawable.item_star);
			rating4.setImageResource(R.drawable.item_star);
			rating5.setImageResource(R.drawable.item_star);
			et_experience.setHint(R.string.label_comment_star_2);
			rating = 2;
			break;
		case 3:
			rating1.setImageResource(R.drawable.item_star_yellow);
			rating2.setImageResource(R.drawable.item_star_yellow);
			rating3.setImageResource(R.drawable.item_star_yellow);
			rating4.setImageResource(R.drawable.item_star);
			rating5.setImageResource(R.drawable.item_star);
			et_experience.setHint(R.string.label_comment_star_3);
			rating = 3;
			break;
		case 4:
			rating1.setImageResource(R.drawable.item_star_yellow);
			rating2.setImageResource(R.drawable.item_star_yellow);
			rating3.setImageResource(R.drawable.item_star_yellow);
			rating4.setImageResource(R.drawable.item_star_yellow);
			rating5.setImageResource(R.drawable.item_star);
			et_experience.setHint(R.string.label_comment_star_4);
			rating = 4;
			break;
		case 5:
			rating1.setImageResource(R.drawable.item_star_yellow);
			rating2.setImageResource(R.drawable.item_star_yellow);
			rating3.setImageResource(R.drawable.item_star_yellow);
			rating4.setImageResource(R.drawable.item_star_yellow);
			rating5.setImageResource(R.drawable.item_star_yellow);
			et_experience.setHint(R.string.label_comment_star_5);
			rating = 5;

		}

	}

}
