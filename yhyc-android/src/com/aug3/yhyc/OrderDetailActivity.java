package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.api.ItemDTO;
import com.aug3.yhyc.api.Order;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarNormalView;
import com.aug3.yhyc.view.ConfirmDialog;
import com.aug3.yhyc.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class OrderDetailActivity extends BaseActivity {

	private ActionBarNormalView ab;

	private List<ItemDTO> items = new ArrayList<ItemDTO>();

	private ListView listView = null;

	private ItemListAdapter listAdapter = null;

	private Button btnAddBack2Cart = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);

		initActionBar();

		initOrderDetail();

	}

	private void initActionBar() {
		ab = (ActionBarNormalView) findViewById(R.id.actionbar_normal);
		ab.setLeftOnClickListener(goBack);
		ab.setTitle(R.string.title_order_detail);
		ab.btnRight.setVisibility(View.GONE);

	}

	private void initOrderDetail() {

		btnAddBack2Cart = ((Button) findViewById(R.id.od_item_addtocart));

		Bundle bundle = getIntent().getExtras();
		long orderid = bundle.getLong(Constants.Extra.ID);

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.ID, orderid);

		YhycRestClient.get(Constants.ReqUrl.ORDER_SHOW, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONObject myorders = (JSONObject) response
									.get(Constants.RESP_RESULT);
							Order order = JSONUtil.fromJson(
									myorders.toString(), Order.class);

							((TextView) findViewById(R.id.od_orderid_tv))
									.setText(String.valueOf(order.getId()));
							((TextView) findViewById(R.id.od_ts_tv))
									.setText(Tools.iso_timestamp_formatter
											.format(order.getTs()));
							((TextView) findViewById(R.id.od_recip_tv))
									.setText(order.getDelivery().getRecip());
							((TextView) findViewById(R.id.od_address_tv))
									.setText(order.getDelivery().getAddr());
							((TextView) findViewById(R.id.od_mobile_tv))
									.setText(order.getDelivery().getMobi());

							((TextView) findViewById(R.id.od_wd_tv))
									.setText(order.getDelivery().getD() + " "
											+ order.getDelivery().getT());
							((TextView) findViewById(R.id.od_msg_tv))
									.setText(order.getMsg());

							((TextView) findViewById(R.id.od_total_price_tv))
									.setText(resources
											.getString(R.string.label_rmb)
											+ order.getTotal());

							int delivery_fee = order.getDelivery().getFee();
							if (delivery_fee == 0) {
								((TextView) findViewById(R.id.od_delivery_fee))
										.setVisibility(View.GONE);
							} else {
								((TextView) findViewById(R.id.od_delivery_fee)).setText(resources
										.getString(R.string.label_delivery_fee)
										.replace("{n}",
												String.valueOf(delivery_fee)));
							}

							items = order.getItems();

							listAdapter.notifyDataSetChanged();

							MyListView.setListViewHeightBasedOnChildren(
									listAdapter, listView);

							addItemBacktoShopcart();

							showWorkshop(order.getSid());

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Tools.ShowAPIError(OrderDetailActivity.this);
					}
				});

		listView = (ListView) findViewById(R.id.od_item_list_view);

		listAdapter = new ItemListAdapter();

		listView.setAdapter(listAdapter);

	}

	private void addItemBacktoShopcart() {

		btnAddBack2Cart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				confirmAddToCart();
			}
		});
	}

	private void confirmAddToCart() {

		final ConfirmDialog ad = new ConfirmDialog(this);

		ad.setTitle(R.string.label_tips);
		ad.setMessage(R.string.label_tip_confirm_to_addto_shoppingcart);
		ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ad.dismiss();

				if (items != null) {
					Set<Long> ids = new HashSet<Long>();
					for (ItemDTO i : items) {
						ids.add(i.getId());
					}
					UserPreference.getInstance().getCart().addAll(ids);
					UserPreference.getInstance().setCartUpdated(true);

					Tools.ShowTip(OrderDetailActivity.this,
							R.string.info_addtocart_success);

					btnAddBack2Cart.setVisibility(View.GONE);
				}

			}
		});

		ad.setNegativeButton(R.string.btn_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});
	}

	private void showWorkshop(final long workshop) {

		findViewById(R.id.od_show_workshop).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						gotoWorkshop(OrderDetailActivity.this, workshop);
					}
				});
	}

	class ItemListAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView title;
			public TextView price;
		}

		public ItemListAdapter() {
			super();

		}

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
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = convertView;
			ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(
						R.layout.list_item_orderitem, parent, false);
				holder = new ViewHolder();

				holder.title = (TextView) view
						.findViewById(R.id.order_item_title);
				holder.price = (TextView) view
						.findViewById(R.id.order_item_price);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			ItemDTO item = items.get(position);
			holder.title.setText(item.getName());
			holder.price
					.setText(item.getNum() + " * "
							+ resources.getString(R.string.label_rmb)
							+ item.getPrice());

			return view;
		}

	}

}
