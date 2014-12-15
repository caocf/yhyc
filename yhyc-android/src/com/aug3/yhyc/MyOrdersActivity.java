package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.api.ItemDTO;
import com.aug3.yhyc.api.Order;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarNormalView;
import com.aug3.yhyc.view.ConfirmDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyOrdersActivity extends AbsListViewBaseActivity {

	private ActionBarNormalView ab;

	private ItemAdapter listAdapter;

	private List<Order> orders;

	private TextView tv_ongoing, tv_finished;

	private static boolean bViewFinished = false;

	private static final int status_ongoing = 88;
	private static final int status_finished = 99;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_orders);

		initActionBar();

		initTabs();

		initOrderList();

	}

	@Override
	public void onResume() {
		super.onResume();

		if (bViewFinished) {
			getOrders(status_finished);
		} else {
			getOrders(status_ongoing);
		}
	}

	private void initActionBar() {

		ab = (ActionBarNormalView) findViewById(R.id.actionbar_normal);
		ab.setLeftOnClickListener(goBack);
		ab.setTitle(R.string.title_my_orders);
		ab.btnRight.setVisibility(View.GONE);

	}

	private void initTabs() {
		tv_ongoing = (TextView) findViewById(R.id.orders_tab_ongoing);
		tv_finished = (TextView) findViewById(R.id.orders_tab_finished);

		bViewFinished = false;

		tv_ongoing.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				getOrders(status_ongoing);

				tv_finished.setTextColor(resources.getColor(R.color.font_head));
				tv_finished
						.setBackgroundResource(R.drawable.btn_right_tab_nor_shape);
				tv_ongoing
						.setBackgroundResource(R.drawable.btn_left_tab_sel_shape);
				tv_ongoing.setTextColor(resources.getColor(R.color.white));

				bViewFinished = false;
			}
		});

		tv_finished.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// status>=5
				getOrders(status_finished);

				tv_ongoing.setTextColor(resources.getColor(R.color.font_head));
				tv_ongoing
						.setBackgroundResource(R.drawable.btn_left_tab_nor_shape);
				tv_finished
						.setBackgroundResource(R.drawable.btn_right_tab_sel_shape);
				tv_finished.setTextColor(resources.getColor(R.color.white));

				bViewFinished = true;
			}
		});
	}

	@SuppressLint("NewApi")
	private void initOrderList() {

		orders = new ArrayList<Order>();

		listView = (ListView) findViewById(android.R.id.list);
		listAdapter = new ItemAdapter();
		listView.setAdapter(listAdapter);

	}

	private void getOrders(int status) {

		orders.clear();

		long uid = UserPreference.getInstance().getUid();
		if (uid == 0) {
			List<Long> orderids = UserPreference.getInstance().getOrders();
			if (orderids.size() > 0) {
				RequestParams p = new RequestParams();
				p.put(Constants.ReqParam.ID,
						TextUtils.join(Constants.COMMA, orderids));
				p.put(Constants.ReqParam.STATUS, status);

				YhycRestClient.get(Constants.ReqUrl.ORDER_LIST_BYID, p,
						findOrdersResponseHandler);
			}

		} else {

			RequestParams p = new RequestParams();
			p.put(Constants.ReqParam.USERID, uid);
			p.put(Constants.ReqParam.STATUS, status);

			YhycRestClient.get(Constants.ReqUrl.ORDER_LIST, p,
					findOrdersResponseHandler);
		}
	}

	private JsonHttpResponseHandler findOrdersResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {

			try {
				JSONArray myorders = (JSONArray) response
						.get(Constants.RESP_RESULT);
				int len = myorders.length();
				for (int i = 0; i < len; i++) {
					JSONObject orderObj = (JSONObject) myorders.get(i);
					Order order = JSONUtil.fromJson(orderObj.toString(),
							Order.class);
					orders.add(order);
				}

				listAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				Log.i("my order activity", e.getMessage());
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			Log.i("my order activity", throwable.getMessage());
		}
	};

	class ItemAdapter extends BaseAdapter {

		private class ViewHolder {
			public ImageView image1;
			public ImageView image2;
			public ImageView image3;
			public TextView tv_ordertime;
			public TextView tv_orderid;
			public TextView tv_price;
			public TextView tv_quantity;
			public TextView tv_status;
			public Button btn_cancel;
			public Button btn_comment;
		}

		@Override
		public int getCount() {
			if (orders == null)
				return 0;
			return orders.size();
		}

		@Override
		public Object getItem(int pos) {
			if (orders == null)
				return null;
			return orders.get(pos);
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
				view = getLayoutInflater().inflate(R.layout.list_item_orders,
						parent, false);
				holder = new ViewHolder();
				holder.image1 = (ImageView) view
						.findViewById(R.id.li_order_product1);
				holder.image2 = (ImageView) view
						.findViewById(R.id.li_order_product2);
				holder.image3 = (ImageView) view
						.findViewById(R.id.li_order_product3);
				holder.tv_ordertime = (TextView) view
						.findViewById(R.id.li_order_time);
				holder.tv_price = (TextView) view
						.findViewById(R.id.li_order_totalprice);
				holder.tv_status = (TextView) view
						.findViewById(R.id.li_order_status);
				holder.tv_quantity = (TextView) view
						.findViewById(R.id.li_order_quantity);
				holder.btn_cancel = (Button) view
						.findViewById(R.id.li_order_cancel_btn);
				holder.btn_comment = (Button) view
						.findViewById(R.id.li_order_item_comment_btn);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final Order myorder = orders.get(position);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyOrdersActivity.this,
							OrderDetailActivity.class);
					intent.putExtra(Constants.Extra.ID, myorder.getId());
					gotoActivity(intent);

				}
			});

			holder.tv_ordertime.setText(Tools.iso_timestamp_formatter
					.format(myorder.getTs()));

			List<ItemDTO> items = myorder.getItems();
			int number = items.size();

			AsyncImageLoader.displayImage(items.get(0).getPic(), holder.image1);

			if (number > 1) {
				AsyncImageLoader.displayImage(items.get(1).getPic(),
						holder.image2);
			} else {
				holder.image2.setVisibility(View.GONE);
			}

			if (number > 2) {
				AsyncImageLoader.displayImage(items.get(2).getPic(),
						holder.image3);
			} else {
				holder.image3.setVisibility(View.GONE);
			}

			holder.tv_price.setText(resources.getString(R.string.label_rmb)
					+ myorder.getTotal());
			holder.tv_quantity.setText("(¹²" + items.size() + "¼þ)");

			final int status = myorder.getStatus();

			if (bViewFinished) {
				holder.btn_cancel.setVisibility(View.GONE);

				int sts = R.string.label_status_succeed;
				switch (status) {
				case 3:
					sts = R.string.label_status_cancel;
					break;
				case 4:
					sts = R.string.label_status_cancel_seller;
					break;
				case 5:
					sts = R.string.label_status_succeed;
					break;
				case 6:
					sts = R.string.label_status_comment;
					break;

				}
				holder.tv_status.setText(sts);

				if (status == 5) {
					holder.btn_comment.setVisibility(View.VISIBLE);
					holder.btn_comment
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {

									Intent intent = new Intent(
											MyOrdersActivity.this,
											ExperienceActivity.class);
									intent.putExtra(Constants.Extra.ID,
											myorder.getId());
									gotoActivity(intent);

								}
							});
				} else {
					holder.btn_comment.setVisibility(View.GONE);
				}

			} else {

				holder.btn_comment.setVisibility(View.GONE);

				int sts = R.string.label_status_to_confirm;
				switch (status) {
				case 0:
					sts = R.string.label_status_to_confirm;
					holder.btn_cancel.setVisibility(View.VISIBLE);
					holder.btn_cancel
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									cancelOrder(myorder.getId(), position);

								}

							});
					break;
				case 1:
					sts = R.string.label_status_processing;
					holder.btn_cancel.setVisibility(View.INVISIBLE);
					break;
				case 2:
					sts = R.string.label_status_delivering;
					holder.btn_cancel.setVisibility(View.INVISIBLE);
					break;

				}
				holder.tv_status.setText(sts);

			}

			return view;
		}
	}

	private void cancelOrder(final long orderid, final int position) {

		final ConfirmDialog ad = new ConfirmDialog(this);

		ad.setTitle(R.string.label_tips);
		ad.setMessage(R.string.label_tip_confirm_to_cancel_order);
		ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ad.dismiss();

				updateOrderStatus(orderid, 3, position);

			}
		});

		ad.setNegativeButton(R.string.btn_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});
	}

	private void updateOrderStatus(long orderid, int status, final int position) {

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.ORDER, orderid);
		p.put(Constants.ReqParam.STATUS, status);

		YhycRestClient.get(Constants.ReqUrl.ORDER_STATUS, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						orders.remove(position);
						listAdapter.notifyDataSetChanged();

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Log.i("my order activity", throwable.getMessage());
					}
				});
	}

}
