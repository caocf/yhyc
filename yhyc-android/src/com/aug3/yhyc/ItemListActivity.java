package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.base.Constants.ReqParam;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.ItemData;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.SystemCache;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ProductCategoryView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ItemListActivity extends AbsListViewBaseActivity {

	// category filter
	private ProductCategoryView categoryView;

	private long workshop;
	private int product_cat_id;

	private List<ItemData> listData = null;

	private ItemAdapter listAdapter;

	private TextView shoppingcart_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		initActionBar();

		initProductList();

	}

	@Override
	public void onResume() {
		super.onResume();

		shoppingcart_number.setText(String.valueOf(UserPreference.getInstance()
				.getCart().size()));

		initItemListData(0);
	}

	private void initActionBar() {

		Bundle bundle = getIntent().getExtras();
		workshop = bundle.getLong(Constants.Extra.WORKSHOP);
		product_cat_id = bundle.getInt(Constants.Extra.PRODUCT_CAT_ID);

		String notice = bundle.getString(Constants.Extra.SHOP_NOTICE);
		if (!TextUtils.isEmpty(notice) && !"null".equalsIgnoreCase(notice)) {
			findViewById(R.id.shop_announce).setVisibility(View.VISIBLE);
			TextView tv_notice = (TextView) findViewById(R.id.shop_notice);
			tv_notice.setText(notice);
			tv_notice.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					gotoWorkshop(ItemListActivity.this, workshop);

				}
			});

		} else {
			findViewById(R.id.shop_announce).setVisibility(View.GONE);
		}

		String shop_name = bundle.getString(Constants.Extra.SHOP_NAME);

		findViewById(R.id.actionbar_back).setOnClickListener(goBack);
		findViewById(R.id.actionbar_title).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						gotoWorkshop(ItemListActivity.this, workshop);
					}
				});

		if (!TextUtils.isEmpty(shop_name)) {
			((TextView) findViewById(R.id.actionbar_title)).setText(shop_name);
		}

		findViewById(R.id.actionbar_category).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						boolean visible = (categoryView.getVisibility() == View.VISIBLE);
						showOrHideList(visible);

					}
				});

		initCategoryTree();

		findViewById(R.id.actionbar_shoppingcart).setOnClickListener(
				gotoShoppingcart);

		shoppingcart_number = ((TextView) findViewById(R.id.actionbar_shoppingcart_number));

	}

	@SuppressLint("NewApi")
	private void initProductList() {

		listView = (ListView) findViewById(android.R.id.list);

		listData = new ArrayList<ItemData>();

		listAdapter = new ItemAdapter();
		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ItemListActivity.this,
						ItemDetailActivity.class);
				ItemData item = listData.get(position);
				intent.putExtra(Constants.Extra.ITEMID, item.getId());
				gotoActivity(intent);
			}
		});

	}

	private void initItemListData(int type) {

		listData.clear();

		UserPreference.getInstance().setWorkshop(workshop);

		final SystemCache sc = new SystemCache();
		final String cacheKey = Constants.CacheKey.SHOP_ITEMS.getName(workshop
				+ "_" + product_cat_id + "_" + type);

		if (sc.containsKey(cacheKey)) {
			List<ItemData> items = (ArrayList<ItemData>) sc.get(cacheKey);
			if (items != null) {
				listData.addAll(items);
			}
			listAdapter.notifyDataSetChanged();
		} else {

			RequestParams p = new RequestParams();
			p.put(ReqParam.WORKSHOP, workshop);
			p.put(ReqParam.CAT, product_cat_id);
			p.put(ReqParam.TYPE, type);

			YhycRestClient.get(Constants.ReqUrl.ITEM_LIST, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								JSONArray items = (JSONArray) response
										.get(Constants.RESP_RESULT);
								int len = items.length();
								for (int i = 0; i < len; i++) {
									JSONObject item = (JSONObject) items.get(i);
									ItemData itemdata = new ItemData();
									itemdata.setTitle(item.getString("name"));
									itemdata.setPp(item.getDouble("pp"));
									itemdata.setMp(item.getDouble("mp"));
									itemdata.setId(item.getLong("id"));
									itemdata.setPid(item.getLong("pid"));
									itemdata.setPic(item.getString("pic"));
									itemdata.setAct(item.getInt("act"));
									itemdata.setSts(item.getInt("sts"));
									listData.add(itemdata);
								}

								List<ItemData> cloneList = new ArrayList<ItemData>();
								cloneList.addAll(listData);
								sc.put(cacheKey, cloneList,
										Constants.TTL_IN_SECONDS / 2);

								listAdapter.notifyDataSetChanged();
							} catch (JSONException e) {
								Log.i("workshoplist activity", e.getMessage());
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Log.i("itemlist activity", throwable.getMessage());
						}
					});
		}

	}

	protected void initCategoryTree() {

		categoryView = (ProductCategoryView) findViewById(R.id.list_category_view);
		categoryView.init(new ProductCategoryView.Listener() {

			@Override
			public void onSelected(com.aug3.yhyc.api.Category category) {
				categoryView.setVisibility(View.GONE);

				Tools.ShowTip(getApplication(), category.getName());

				initItemListData(category.getCode());

			}

			@Override
			public void onScroll(com.aug3.yhyc.api.Category category) {

			}
		}, product_cat_id);

	}

	protected void showOrHideList(boolean vis) {
		if (categoryView != null) {
			if (vis) {
				categoryView.setVisibility(View.GONE);
			} else {
				categoryView.setVisibility(View.VISIBLE);
			}

		}
	}

	private View.OnClickListener gotoShoppingcart = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ItemListActivity.this,
					ShoppingCartActivity.class);
			gotoActivity(intent);

		}
	};

	class ItemAdapter extends BaseAdapter {

		private class ViewHolder {
			public ImageView image;
			public ImageView act;
			public TextView title;
			public TextView price;
			public TextView origprice;
			public ImageButton addtocart;
		}

		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int pos) {
			return listData.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.list_item_product,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) view
						.findViewById(R.id.product_image);
				holder.act = (ImageView) view
						.findViewById(R.id.product_item_act);
				holder.title = (TextView) view.findViewById(R.id.product_title);
				holder.price = (TextView) view.findViewById(R.id.product_price);
				holder.origprice = (TextView) view
						.findViewById(R.id.product_origin_price);
				holder.addtocart = (ImageButton) view
						.findViewById(R.id.product_addtocart);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final ItemData item = listData.get(position);
			holder.title.setText(item.getTitle());
			holder.price.setText(resources.getString(R.string.label_rmb)
					+ item.getPp());
			holder.origprice.setText(resources.getString(R.string.label_rmb)
					+ item.getMp());
			holder.origprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			if (item.getSts() == 2) {
				holder.addtocart.setBackgroundResource(R.drawable.cart_nor);
				holder.addtocart.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						Tools.ShowTip(getApplicationContext(),
								R.string.info_already_sellout);

					}
				});
			} else {
				holder.addtocart.setBackgroundResource(R.drawable.cart_select);
				holder.addtocart.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Tools.ShowTip(getApplicationContext(),
								R.string.info_addtocart_success);

						UserPreference prefs = UserPreference.getInstance();

						if (!prefs.getCart().contains(item.getId())) {

							prefs.getCart().add(item.getId());
							UserPreference.getInstance().setCartUpdated(true);

							shoppingcart_number.setText(String
									.valueOf(UserPreference.getInstance()
											.getCart().size()));
						}

						prefs.increaseNumberOfItemInCart(item.getId(), 1);

					}
				});
			}

			AsyncImageLoader.displayImage(item.getPic(), holder.image);

			if (item.getAct() == 1) {
				holder.act.setBackgroundResource(R.drawable.ic_cuxiao);
			} else {
				holder.act.setBackgroundResource(0);
			}

			return view;
		}
	}

}
