package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;
import com.aug3.yhyc.widget.adroll.MyImgScroll;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WebStoreActivity extends BaseActivity {

	private ActionBarView ab;

	private GridView gridView;

	private TextView tv_myac;

	private List<WebStoreItem> items;

	private MyImgScroll myPager; // Í¼Æ¬ÈÝÆ÷
	private LinearLayout ovalLayout; // Ô²µãÈÝÆ÷
	private List<View> listViews; // Í¼Æ¬×é

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_store);

		ab = (ActionBarView) findViewById(R.id.title_view);
		ab.setTitleText(R.string.title_web_store);

		initAdScroll();

		tv_myac = (TextView) findViewById(R.id.webstore_my_ac);
		tv_myac.setText(String.valueOf(UserPreference.getInstance().getAc()));
		findViewById(R.id.webstore_view_ac_rule).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(WebStoreActivity.this,
								WebViewActivity.class);
						i.putExtra(Constants.Extra.TITLE,
								R.string.label_view_ac_rule);
						i.putExtra(Constants.Extra.URL,
								"http://www.1hyc.com/acrule.html");
						gotoActivity(i);
					}
				});

		initWebStoreItems();

	}

	@Override
	public void onResume() {
		super.onResume();

		tv_myac.setText(String.valueOf(UserPreference.getInstance().getAc()));

	}

	private void initAdScroll() {
		myPager = (MyImgScroll) findViewById(R.id.myvp);
		ovalLayout = (LinearLayout) findViewById(R.id.vb);
		InitViewPager();// ³õÊ¼»¯Í¼Æ¬
		// ¿ªÊ¼¹ö¶¯
		myPager.start(this, listViews, 6000, ovalLayout,
				R.layout.ad_bottom_item, R.id.ad_item_v,
				R.drawable.dot_focused, R.drawable.dot_normal);
	}

	@Override
	protected void onRestart() {
		myPager.startTimer();
		super.onRestart();
	}

	@Override
	protected void onStop() {
		myPager.stopTimer();
		super.onStop();
	}

	/**
	 * ³õÊ¼»¯Í¼Æ¬
	 */
	private void InitViewPager() {
		listViews = new ArrayList<View>();
		Map<String, String> urls = UserPreference.getInstance().getUrls();
		String ad01 = urls.get("ad_jfsc_01");
		String ad02 = urls.get("ad_jfsc_02");
		String ad03 = urls.get("ad_jfsc_03");

		ImageView imageNext = (ImageView) findViewById(R.id.image_next);
		Tools.setImageResource(imageNext, ad01, ScaleType.FIT_XY);

		ImageView imageLast = (ImageView) findViewById(R.id.image_last);
		Tools.setImageResource(imageLast, ad03, ScaleType.FIT_XY);

		ImageView imageMain = (ImageView) findViewById(R.id.image_main);
		Tools.setImageResource(imageMain, ad02, ScaleType.FIT_XY);

		listViews.add(imageNext);
		listViews.add(imageLast);
		listViews.add(imageMain);

	}

	@SuppressLint("NewApi")
	protected void initWebStoreItems() {

		items = new ArrayList<WebStoreItem>();

		RequestParams p = new RequestParams();

		YhycRestClient.get(Constants.ReqUrl.WEB_STORE_LIST, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONArray result = (JSONArray) response
									.get(Constants.RESP_RESULT);
							int len = result.length();
							for (int i = 0; i < len; i++) {
								JSONObject item = (JSONObject) result.get(i);
								WebStoreItem itemdata = new WebStoreItem();
								itemdata.setId(item.getString("id"));
								itemdata.setName(item.getString("name"));
								itemdata.setDesc(item.getString("desc"));
								itemdata.setPrice(item.getDouble("price"));
								itemdata.setAc(item.getInt("ac"));
								itemdata.setInvt(item.getInt("invt"));
								itemdata.setImage(item.getString("pic"));
								items.add(itemdata);
							}

							initGridList();

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						Tools.ShowAPIError(WebStoreActivity.this);

					}
				});

	}

	private void initGridList() {

		BaseAdapter gridAdapter = new BaseAdapter() {

			@Override
			public int getCount() {
				return items.size();
			}

			@Override
			public Object getItem(int pos) {
				return items.get(pos);
			}

			@Override
			public long getItemId(int pos) {
				return pos;
			}

			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {

				View v = convertView;
				WebStoreItemViewHolder holder;

				if (convertView == null) {
					v = getLayoutInflater().inflate(R.layout.webstore_item,
							parent, false);
					v.setBackgroundColor(getResources().getColor(R.color.white));

					holder = new WebStoreItemViewHolder();
					holder.image = (ImageView) v.findViewById(R.id.item_image);
					holder.tv_title = (TextView) v
							.findViewById(R.id.item_title);
					holder.tv_ac = (TextView) v.findViewById(R.id.item_ac);
					holder.btn_exchg = (Button) v.findViewById(R.id.item_exchg);

					v.setTag(holder);
				} else {
					holder = (WebStoreItemViewHolder) v.getTag();
				}

				final WebStoreItem vi = items.get(pos);

				AsyncImageLoader.displayImage(vi.getImage(), holder.image);
				holder.tv_title.setText(vi.getName());
				holder.tv_ac.setText(String.valueOf(vi.getAc()));
				if (vi.getInvt() > 0) {
					holder.btn_exchg.setText(R.string.label_web_store_exchg);
					holder.btn_exchg
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									if (UserPreference.getInstance().getAc() < vi
											.getAc()) {
										Tools.ShowTip(
												WebStoreActivity.this,
												R.string.label_toast_not_enough_ac);
									} else {
										Intent intent = new Intent(
												WebStoreActivity.this,
												IExchgActivity.class);

										intent.putExtra(Constants.Extra.ITEM,
												vi.getName());
										intent.putExtra(Constants.Extra.ITEMID,
												vi.getId());
										intent.putExtra(Constants.Extra.AC,
												vi.getAc());
										gotoActivity(intent);
									}

								}
							});
				} else {
					holder.btn_exchg
							.setText(R.string.label_web_store_exchg_finish);
					holder.btn_exchg
							.setBackgroundResource(R.drawable.btn_gray_default);
				}

				return v;
			}

		};
		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setAdapter(gridAdapter);
	}

	public class WebStoreItemViewHolder {

		public ImageView image;
		public TextView tv_title;
		public TextView tv_ac;
		public Button btn_exchg;

	}

	public class WebStoreItem {

		private String id;

		private String name;

		private String desc;

		private double price;

		public int ac;

		private int invt;

		public String image;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public int getAc() {
			return ac;
		}

		public void setAc(int ac) {
			this.ac = ac;
		}

		public int getInvt() {
			return invt;
		}

		public void setInvt(int invt) {
			this.invt = invt;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

	}

}
