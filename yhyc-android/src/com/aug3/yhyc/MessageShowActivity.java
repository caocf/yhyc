package com.aug3.yhyc;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.Tools;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MessageShowActivity extends BaseActivity {

	private ArrayList<Reply> listItemData = null;

	private String msgID;

	private TextView titleText;

	private EditText sendText;

	private ImageView sendBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_show);

		initActionStack();

		Bundle bundle = getIntent().getExtras();
		msgID = bundle.getString(Constants.Extra.ID);

		sendText = (EditText) findViewById(R.id.msg_text);
		sendBtn = (ImageView) findViewById(R.id.msg_send);
		sendEventToEditText();

	}

	@Override
	protected void onResume() {
		super.onResume();

		initReplyList();
	}

	private void initActionStack() {
		titleText = (TextView) findViewById(R.id.actionbar_title);
		findViewById(R.id.actionbar_back).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeWindow();
					}
				});
	}

	public void sendEventToEditText() {

		sendBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = sendText.getText().toString();
				if (!TextUtils.isEmpty(text)) {
					sendMessage(text);
				}

			}

		});

		sendText.addTextChangedListener(new TextWatcher() {

			// 缓存上一次文本框内是否为空
			private boolean isnull = true;

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					sendBtn.setBackgroundResource(R.drawable.btn_send_disable);
					if (!isnull) {
						isnull = true;
					}
				} else {
					if (isnull) {
						sendBtn.setBackgroundResource(R.drawable.btn_send_normal);
						isnull = false;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			/**
			 * 随着文本框内容改变动态改变列表内容
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		});

		sendText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (!TextUtils.isEmpty(sendText.getText())) {
						sendBtn.setBackgroundResource(R.drawable.btn_send_normal);
					}
				} else {
					sendBtn.setBackgroundResource(R.drawable.btn_send_disable);
				}

			}
		});

	}

	private void sendMessage(final String text) {

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.ID, msgID);
		p.put(Constants.ReqParam.USERID, UserPreference.getInstance().getUid());
		p.put(Constants.ReqParam.NAME, UserPreference.getInstance()
				.getUserName());
		p.put("content", text);

		YhycRestClient.post(Constants.ReqUrl.USER_MESSAGE_COMMENT, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						Tools.ShowTip(MessageShowActivity.this,
								R.string.label_toast_success_submit);
						sendText.setText("");
						initReplyList();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Tools.ShowAPIError(MessageShowActivity.this);
					}
				});
	}

	@SuppressLint("NewApi")
	private void initReplyList() {

		ListView listView = (ListView) findViewById(android.R.id.list);

		listItemData = new ArrayList<Reply>();

		listView.setAdapter(listAdapter);

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.ID, msgID);

		YhycRestClient.get(Constants.ReqUrl.USER_MESSAGE, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONObject msg = (JSONObject) response
									.get(Constants.RESP_RESULT);
							String topic = msg.getString("topic").replace(
									"\\n", "\n");
							titleText.setText(topic);
							((TextView) findViewById(R.id.msg_topic))
									.setText(topic);
							((TextView) findViewById(R.id.msg_ts)).setText(msg
									.getString("ts"));

							JSONArray replies = msg.getJSONArray("reply");

							if (replies != null) {

								int len = replies.length();
								for (int i = 0; i < len; i++) {
									Reply rep = new Reply();
									JSONObject obj = (JSONObject) replies
											.get(i);
									rep.author = obj.getString("name");
									rep.content = obj.getString("content");
									rep.ts = obj.getString("ts");
									listItemData.add(rep);
								}

								listAdapter.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							Log.i("workshoplist activity", e.getMessage());
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Tools.ShowAPIError(MessageShowActivity.this);
					}
				});

	}

	private BaseAdapter listAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			ListItemViewHolder holder;
			if (convertView == null) {
				vi = getLayoutInflater().inflate(R.layout.list_item_msg_reply,
						null);
				holder = new ListItemViewHolder();
				holder.content = (TextView) vi.findViewById(R.id.reply_content);
				holder.ts = (TextView) vi.findViewById(R.id.reply_ts);
				holder.author = (TextView) vi.findViewById(R.id.reply_author);

				vi.setTag(holder);
			} else {
				holder = (ListItemViewHolder) vi.getTag();
			}
			final Reply itemdata = listItemData.get(position);

			holder.content.setText(itemdata.content);
			holder.ts.setText(itemdata.ts);
			holder.author.setText(itemdata.author);

			return vi;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return listItemData.get(position);
		}

		@Override
		public int getCount() {
			return listItemData.size();
		}
	};

	private class Reply {
		public String content;
		public String author;
		public String ts;
	}

	public class ListItemViewHolder {

		public TextView content;
		public TextView author;
		public TextView ts;

	}

}
