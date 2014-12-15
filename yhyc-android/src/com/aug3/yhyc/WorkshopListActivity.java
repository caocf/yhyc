package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.api.WorkshopDTO;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.model.WorkshopListItem;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.SystemCache;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WorkshopListActivity extends AbsListViewBaseActivity {

	private ActionBarView ab;

	private ArrayList<WorkshopListItem> listItemData = null;

	private int cat;
	private long shequ;

	private ImageView iv_ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workshop_list);

		ab = (ActionBarView) findViewById(R.id.actionbar_back);
		ab.setTitleText(R.string.app_name);

		Bundle bundle = getIntent().getExtras();
		cat = bundle.getInt(Constants.Extra.PRODUCT_CAT_ID);
		shequ = bundle.getLong(Constants.Extra.SHEQU);

		String category = bundle.getString(Constants.Extra.CATEGORYTEXT);
		if (!TextUtils.isEmpty(category)) {
			ab.setTitleText(category);
		}

		initWorkshopList();

		iv_ad = (ImageView) findViewById(R.id.workshop_ad);
		Map<String, String> urls = UserPreference.getInstance().getUrls();
		if (urls != null && urls.containsKey("ad002")) {
			Tools.setImageResource(iv_ad, urls.get("ad002"), null);
		}
	}

	@SuppressLint("NewApi")
	private void initWorkshopList() {

		listView = (ListView) findViewById(android.R.id.list);

		listItemData = new ArrayList<WorkshopListItem>();

		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(listItemClickListener);

		final SystemCache sc = new SystemCache();
		final String cacheKey = Constants.CacheKey.SHEQU_CAT_SHOPS
				.getName(shequ + "_" + cat);

		if (sc.containsKey(cacheKey)) {
			ArrayList<WorkshopListItem> shops = (ArrayList<WorkshopListItem>) sc
					.get(cacheKey);
			if (shops != null) {
				listItemData.addAll(shops);
			}

			showPlatformNotice();
			listAdapter.notifyDataSetChanged();
		} else {

			RequestParams p = new RequestParams();
			p.put("shequ", shequ);
			p.put("cat", cat);

			YhycRestClient.get(Constants.ReqUrl.SHOP_LIST, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								JSONArray shops = (JSONArray) response
										.get(Constants.RESP_RESULT);
								int len = shops.length();
								for (int i = 0; i < len; i++) {
									WorkshopDTO shop = JSONUtil.fromJson(shops
											.get(i).toString(),
											WorkshopDTO.class);
									listItemData.add(new WorkshopListItem(shop
											.getId(), shop.getName(), shop
											.getAddr(), shop.getPic(), shop
											.getNotice(), shop.getCat(), shop
											.getTel()));
								}
								sc.put(cacheKey, listItemData,
										Constants.TTL_IN_SECONDS);

								showPlatformNotice();
								listAdapter.notifyDataSetChanged();
							} catch (JSONException e) {
								Log.i("workshoplist activity", e.getMessage());
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Tools.ShowAPIError(WorkshopListActivity.this);
						}
					});
		}

	}

	private void showPlatformNotice() {

		if (listItemData == null || listItemData.isEmpty())
			findViewById(R.id.workshop_notice).setVisibility(View.VISIBLE);

	}

	private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

			WorkshopListItem i = listItemData.get(pos);

			gotoItemList(WorkshopListActivity.this, i.id, i.title, i.notice,
					cat);

		}
	};

	private BaseAdapter listAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			ListItemViewHolder holder;
			if (convertView == null) {
				vi = getLayoutInflater().inflate(R.layout.list_item_workshop,
						null);
				holder = new ListItemViewHolder();
				holder.image = (ImageView) vi.findViewById(R.id.listitem_img);
				holder.title = (TextView) vi.findViewById(R.id.listitem_title);
				holder.subtitle = (TextView) vi
						.findViewById(R.id.listitem_subtitle);

				vi.setTag(holder);
			} else {
				holder = (ListItemViewHolder) vi.getTag();
			}
			final WorkshopListItem itemdata = listItemData.get(position);
			if (TextUtils.isEmpty(itemdata.image)) {
				holder.image.setImageResource(R.drawable.shop_default);
			} else {
				AsyncImageLoader.displayImage(itemdata.image, holder.image);
			}
			holder.title.setText(itemdata.title);
			holder.title.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(WorkshopListActivity.this,
							WorkshopActivity.class);
					i.putExtra(Constants.Extra.WORKSHOP, itemdata.id);
					gotoActivity(i);
				}
			});

			holder.subtitle.setText(itemdata.subtitle);

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

	public class ListItemViewHolder {

		public TextView title;
		public TextView subtitle;
		public ImageView image;

	}

}
