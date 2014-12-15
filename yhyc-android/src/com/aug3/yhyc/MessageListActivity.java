package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MessageListActivity extends BaseActivity {

	private ActionBarView ab;

	private PullToRefreshListView mPullToRefreshListView;

	private List<Notify> listItemData = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);

		ab = (ActionBarView) findViewById(R.id.actionbar_user_notify);
		ab.setTitleText(R.string.title_message_center);

	}

	@Override
	protected void onResume() {

		super.onResume();
		initMessageList();

	}

	@SuppressLint("NewApi")
	private void initMessageList() {

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.user_notify_list);
		mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
		ILoadingLayout startLabels = mPullToRefreshListView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("品质生活，从此开始");
		startLabels.setReleaseLabel("品质生活，从此开始");

		listItemData = new ArrayList<Notify>();
		mPullToRefreshListView.setAdapter(listAdapter);

		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// pushDirection = 1;
						new FetchDataTask().execute();
						refreshView.post(new Runnable() {
							@Override
							public void run() {
								mPullToRefreshListView.onRefreshComplete();
							}
						});

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub

					}

				});

		asyncLoadMessages();

	}

	private class FetchDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			asyncLoadMessages();

			mPullToRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}

	}

	private void asyncLoadMessages() {

		RequestParams p = new RequestParams();
		p.put("uid", UserPreference.getInstance().getUid());

		YhycRestClient.get(Constants.ReqUrl.USER_MESSAGES, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONArray msgs = (JSONArray) response
									.get(Constants.RESP_RESULT);
							int len = msgs.length();

							listItemData.clear();
							for (int i = 0; i < len; i++) {

								JSONObject msg = (JSONObject) msgs.get(i);

								Notify itemdata = new Notify();
								itemdata.id = msg.getString("id");
								itemdata.topic = msg.getString("topic")
										.replace("\\n", "\n");
								itemdata.type = msg.getInt("type");
								itemdata.ts = msg.getString("ts");
								itemdata.web = msg.getString("web");
								listItemData.add(itemdata);
							}

							listAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Tools.ShowAPIError(MessageListActivity.this);
					}
				});
	}

	private BaseAdapter listAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			ListItemViewHolder holder;
			if (convertView == null) {
				vi = getLayoutInflater().inflate(R.layout.list_item_message,
						null);
				holder = new ListItemViewHolder();
				holder.type = (ImageView) vi.findViewById(R.id.msg_type);
				holder.title = (TextView) vi.findViewById(R.id.msg_title);
				holder.ts = (TextView) vi.findViewById(R.id.msg_ts);

				vi.setTag(holder);
			} else {
				holder = (ListItemViewHolder) vi.getTag();
			}

			final Notify itemdata = listItemData.get(position);
			if (itemdata.type == 0) {
				holder.type.setBackgroundResource(R.drawable.ic_action_notify);
			} else {
				holder.type
						.setBackgroundResource(R.drawable.ic_action_create_message);
			}
			holder.ts.setText(itemdata.ts);
			holder.title.setText(itemdata.topic);

			holder.title.setOnClickListener(getOnClickListener(position));

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

	private View.OnClickListener getOnClickListener(final int pos) {
		return new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Notify notify = (Notify) listAdapter.getItem(pos);

				if (TextUtils.isEmpty(notify.web)) {
					Intent i = new Intent(MessageListActivity.this,
							MessageShowActivity.class);
					i.putExtra(Constants.Extra.ID, notify.id);
					gotoActivity(i);
				} else {
					Intent i = new Intent(MessageListActivity.this,
							WebViewActivity.class);
					i.putExtra(Constants.Extra.TITLE, 0);
					i.putExtra(Constants.Extra.URL, notify.web);
					gotoActivity(i);
				}
			}
		};
	}

	public class ListItemViewHolder {

		public TextView title;
		public TextView ts;
		public ImageView type;

	}

	private class Notify {
		public String id;
		public String topic;
		public String ts;
		public String web;
		public int type;
	}

}
