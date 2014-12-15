package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aug3.yhyc.api.DeliveryContact;
import com.aug3.yhyc.api.DeliveryTime;
import com.aug3.yhyc.api.ItemDTO;
import com.aug3.yhyc.api.Orders;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.base.Constants.Extra;
import com.aug3.yhyc.base.Constants.ReqParam;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.Arith;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.SystemCache;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarNormalView;
import com.aug3.yhyc.view.ConfirmDialog;
import com.aug3.yhyc.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class OrderConfirmActivity extends BaseActivity {

	private ActionBarNormalView ab;

	private ListView listView;

	private List<ItemDTO> items = new ArrayList<ItemDTO>();

	private List<Long> itemsID = new ArrayList<Long>();

	private ItemListAdapter listAdapter = null;

	private Spinner dateSpinner, timeSpinner;

	private ArrayAdapter<String> dateAdapter, timeAdapter;

	private Button btn_edit;

	private String myshequ;

	private TextView name_tv, addr_tv, mobile_tv, shequ_edit, shequ_show;
	private EditText name_et, addr_et, mobile_et;

	private String recip, mobi, addr;

	private boolean address_edit_status = true;

	private double total = 0;

	private Map<Long, List<ItemDTO>> itemsMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm);

		initActionBar();

		initOrderInfo();

		initOrderList();

	}

	protected void onResume() {
		super.onResume();

		if (TextUtils.isEmpty(myshequ)) {
			myshequ = sp.getString(Constants.SharedPrefs.MY_SHEQU, "");
			if (TextUtils.isEmpty(myshequ)) {
				shequ_edit.setText(R.string.label_order_shequ_empty);
				shequ_edit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						gotoActivity(new Intent(OrderConfirmActivity.this,
								SearchShequActivity.class));

					}
				});
			} else {
				shequ_edit.setText(myshequ);
				shequ_show.setText(myshequ);
			}
		}
	}

	private void initActionBar() {
		ab = (ActionBarNormalView) findViewById(R.id.actionbar_normal);
		ab.setLeftOnClickListener(goBack);
		ab.setTitle(R.string.title_confirm_order);
		ab.btnRight.setVisibility(View.GONE);

		findViewById(R.id.co_goback_shoppingcart).setOnClickListener(
				gobackShoppingListener);

		findViewById(R.id.co_submit_order_btn).setOnClickListener(
				submitListener);
	}

	private View.OnClickListener gobackShoppingListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			closeWindow();
		}
	};

	private View.OnClickListener submitListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {

			if (TextUtils.isEmpty(myshequ)) {
				Tools.ShowLongTip(OrderConfirmActivity.this,
						R.string.label_order_shequ_empty);
				gotoActivity(new Intent(OrderConfirmActivity.this,
						SearchShequActivity.class));
				return;
			}

			if (total < 10) {
				Tools.ShowTip(OrderConfirmActivity.this,
						R.string.label_order_price_limit);
				return;
			}

			if (!checkAddress()) {
				return;
			}

			submitOrder();
		}
	};

	private void submitOrder() {

		final ConfirmDialog ad = new ConfirmDialog(this);

		ad.setTitle(R.string.label_tips);
		ad.setMessage(R.string.label_tip_confirm_to_submit_order);
		ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ad.dismiss();

				doSubmit();

			}
		});

		ad.setNegativeButton(R.string.btn_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});
	}

	private void doSubmit() {

		Orders order = new Orders();

		order.setUid(UserPreference.getInstance().getUid());

		order.setItems(itemsMap);

		DeliveryContact contact = new DeliveryContact();
		contact.setRecip(recip);
		contact.setAddr(myshequ + " " + addr);
		contact.setMobi(mobi);
		contact.setD(dateSpinner.getSelectedItem().toString());
		contact.setT(timeSpinner.getSelectedItem().toString());
		order.setDelivery(contact);

		String msg = ((EditText) findViewById(R.id.co_leave_msg)).getText()
				.toString();
		if (!TextUtils.isEmpty(msg)) {
			order.setMsg(msg);
		}

		saveOrder(JSONUtil.toJson(order));

	}

	private void saveOrder(String order) {

		RequestParams p = new RequestParams();
		p.put(ReqParam.ORDER, order);
		p.put(ReqParam.USERID, UserPreference.getInstance().getUid());

		YhycRestClient.post(Constants.ReqUrl.ORDER_NEW, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							JSONArray orderids = (JSONArray) response
									.get(Constants.RESP_RESULT);
							int len = orderids.length();
							List<Long> myorders = UserPreference.getInstance()
									.getOrders();
							for (int i = 0; i < len; i++) {
								Long orderid = (Long) orderids.get(i);

								myorders.add(0, orderid);
								if (myorders.size() > 8) {
									myorders.remove(8);
								}
							}
							sp.edit()
									.putString(
											Constants.SharedPrefs.MY_ORDER_LIST,
											TextUtils.join(Constants.COMMA,
													myorders)).commit();

						} catch (JSONException e) {
						}

						UserPreference.getInstance().getCart()
								.removeAll(itemsID);
						UserPreference.getInstance().getCartItemsMap()
								.remove(itemsID);

						gotoActivity(new Intent(OrderConfirmActivity.this,
								OrderSuccessActivity.class));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						Tools.ShowAPIError(OrderConfirmActivity.this);
					}
				});

	}

	private void getDeliveryTime() {

		final SystemCache sc = new SystemCache();
		final String cacheKey = Constants.CacheKey.DELIVERY_TIME.getName();
		if (sc.containsKey(cacheKey)) {
			DeliveryTime dt = (DeliveryTime) sc.get(cacheKey);
			initDeliveryTimeSpinner(dt);

		} else {
			RequestParams p = new RequestParams();

			YhycRestClient.get(Constants.ReqUrl.DELIVERY_TIME, p,
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							try {
								DeliveryTime dt = JSONUtil.fromJson(response
										.get(Constants.RESP_RESULT).toString(),
										DeliveryTime.class);
								initDeliveryTimeSpinner(dt);
								sc.put(cacheKey, dt,
										Constants.TTL_IN_SECONDS / 2);

							} catch (JSONException e) {
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Tools.ShowAPIError(OrderConfirmActivity.this);
						}
					});
		}
	}

	private void getshipAgreement() {

		final SystemCache sc = new SystemCache();
		final String cacheKey = Constants.CacheKey.SHIP_AGREEMENT.getName();
		if (sc.containsKey(cacheKey)) {
			String shipnote = (String) sc.get(cacheKey);
			((TextView) findViewById(R.id.co_ship_agreement)).setText(shipnote);

		} else {
			RequestParams p = new RequestParams();

			YhycRestClient.get(Constants.ReqUrl.SYSTEM_SETTINGS, p,
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							try {
								JSONObject settings = (JSONObject) response
										.get(Constants.RESP_RESULT);
								String shipnote = settings
										.getString("shipnote");
								if (!TextUtils.isEmpty(shipnote)) {
									shipnote = shipnote.replace("\\n", "\n");
									sc.put(cacheKey, shipnote,
											Constants.TTL_IN_SECONDS);
									((TextView) findViewById(R.id.co_ship_agreement))
											.setText(shipnote);
								}

							} catch (JSONException e) {
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Tools.ShowAPIError(OrderConfirmActivity.this);
						}
					});
		}
	}

	private void initDeliveryTimeSpinner(DeliveryTime dt) {
		dateAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dt.getDate());
		timeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dt.getTime());
		dateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		dateSpinner.setAdapter(dateAdapter);
		timeSpinner.setAdapter(timeAdapter);
	}

	private void initOrderInfo() {

		name_tv = (TextView) findViewById(R.id.co_user_name_tv);
		addr_tv = (TextView) findViewById(R.id.co_address_tv);
		mobile_tv = (TextView) findViewById(R.id.co_user_mobile_tv);

		shequ_edit = (TextView) findViewById(R.id.co_shequ_edit);
		shequ_show = (TextView) findViewById(R.id.co_shequ_show);

		name_et = (EditText) findViewById(R.id.co_user_name_et);
		addr_et = (EditText) findViewById(R.id.co_address_et);
		mobile_et = (EditText) findViewById(R.id.co_user_mobile_et);

		/**
		 * read from sp
		 */
		sp = this.getSharedPreferences(Constants.SharedPrefs.YHYC,
				Context.MODE_PRIVATE);
		recip = sp.getString(Constants.SharedPrefs.RECIP, "");
		addr = sp.getString(Constants.SharedPrefs.ADDR, "");
		mobi = sp.getString(Constants.SharedPrefs.MOBILE, "");

		if (!TextUtils.isEmpty(recip)) {
			name_et.setText(recip);
			name_tv.setText(recip);
		}

		if (!TextUtils.isEmpty(addr)) {
			addr_et.setText(addr);
			addr_tv.setText(addr);
		}

		if (!TextUtils.isEmpty(mobi)) {
			mobile_et.setText(mobi);
			mobile_tv.setText(mobi);
		}

		/**
		 * click listener for edit button
		 */
		btn_edit = (Button) findViewById(R.id.co_address_edit_btn);
		btn_edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				processAddress();

			}
		});

		findViewById(R.id.co_fetch_contact_btn).setOnClickListener(
				new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_PICK,
								ContactsContract.Contacts.CONTENT_URI);
						OrderConfirmActivity.this.startActivityForResult(
								intent, 1);

					}
				});

		dateSpinner = (Spinner) findViewById(R.id.co_deliver_date_spinner);
		timeSpinner = (Spinner) findViewById(R.id.co_deliver_time_spinner);

		getDeliveryTime();

		getshipAgreement();

	}

	/**
	 * edit or save address
	 * 
	 * @return
	 */
	private boolean processAddress() {
		if (!address_edit_status) {

			btn_edit.setText(R.string.btn_save);
			findViewById(R.id.co_address_view_show).setVisibility(View.GONE);
			findViewById(R.id.co_address_view_edit).setVisibility(View.VISIBLE);
			name_et.setText(name_tv.getText());
			addr_et.setText(addr_tv.getText());
			mobile_et.setText(mobile_tv.getText());

			address_edit_status = !address_edit_status;

		} else {

			if (!checkAddress()) {
				return false;
			}

			name_tv.setText(recip);
			addr_tv.setText(addr);
			mobile_tv.setText(mobi);

			findViewById(R.id.co_address_view_show).setVisibility(View.VISIBLE);
			findViewById(R.id.co_address_view_edit).setVisibility(View.GONE);

			btn_edit.setText(R.string.btn_edit);
			address_edit_status = !address_edit_status;
		}

		return true;
	}

	/**
	 * check and save address
	 * 
	 * @return
	 */
	private boolean checkAddress() {

		recip = name_et.getText().toString();
		addr = addr_et.getText().toString();
		mobi = mobile_et.getText().toString();

		if (TextUtils.isEmpty(recip) || TextUtils.isEmpty(addr)
				|| TextUtils.isEmpty(mobi)) {
			Tools.ShowTip(OrderConfirmActivity.this,
					R.string.label_toast_invalid_address);
			return false;
		}
		if (mobi.length() < 11) {
			Tools.ShowTip(OrderConfirmActivity.this,
					R.string.label_toast_invalid_mobile);
			return false;
		}

		// save to user preference
		Editor editor = sp.edit();
		editor.putString(Constants.SharedPrefs.RECIP, recip);
		editor.putString(Constants.SharedPrefs.ADDR, addr);
		editor.putString(Constants.SharedPrefs.MOBILE, mobi);
		editor.commit();

		return true;
	}

	/**
	 * 通过函数onActivityResult获取选择的联系人，然后通过Content Provider查询联系人的手机号码
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Uri contactData = data.getData();
				Cursor cursor = managedQuery(contactData, null, null, null,
						null);
				cursor.moveToFirst();
				String phoneNumber = Tools.getContactPhone(
						OrderConfirmActivity.this, cursor);
				((EditText) findViewById(R.id.co_user_mobile_et))
						.setText(phoneNumber);
			}
			break;

		default:
			break;
		}
	}

	private void initOrderList() {

		Bundle bundle = getIntent().getExtras();
		String[] orderItems = bundle.getStringArray(Extra.ORDER_LIST);

		itemsMap = new HashMap<Long, List<ItemDTO>>();

		double subtotal = 0;
		int shipfee = 0;
		total = 0;

		int i = 0;
		for (String orderitem : orderItems) {

			String[] parts = orderitem.split(Constants.COMMA);

			ItemDTO item = buildItem(parts);

			items.add(item);

			total = Arith.add(total, item.getPrice() * item.getNum());

			long sid = Long.parseLong(parts[2]);
			if (itemsMap.containsKey(sid)) {
				itemsMap.get(sid).add(item);

				subtotal = Arith.add(subtotal, item.getPrice() * item.getNum());
			} else {
				if (i > 0) {
					addSubTotal(subtotal, -1);
				}
				List<ItemDTO> listItem = new ArrayList<ItemDTO>();
				listItem.add(item);
				itemsMap.put(sid, listItem);

				if (subtotal > 0 && subtotal < 30) {
					shipfee += 3;
				}

				subtotal = item.getPrice() * item.getNum();
			}

			i += 1;

		}

		TextView splitNotice = (TextView) findViewById(R.id.co_order_split_tv);
		int n = itemsMap.size();
		if (n > 1) {
			splitNotice.setVisibility(View.VISIBLE);
			splitNotice.setText(resources.getString(R.string.info_order_split)
					.replace("{n}", String.valueOf(n)));

			addSubTotal(subtotal, 0);

		} else {
			splitNotice.setVisibility(View.GONE);
		}

		listView = (ListView) findViewById(R.id.co_order_list);

		listAdapter = new ItemListAdapter();

		listView.setAdapter(listAdapter);

		MyListView.setListViewHeightBasedOnChildren(listAdapter, listView);

		if (subtotal > 0 && subtotal < 30) {
			shipfee += 3;
		}

		((TextView) findViewById(R.id.co_total_price_tv)).setText(currency
				+ total);
		((TextView) findViewById(R.id.co_shipping_fee_tv)).setText(currency
				+ shipfee);

	}

	private void addSubTotal(double subtotal, int pos) {

		ItemDTO sub = new ItemDTO();
		sub.setPrice(subtotal);
		if (pos < 0)
			items.add(items.size() + pos, sub);
		else
			items.add(sub);
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

			if (TextUtils.isEmpty(item.getName())) {
				holder.title.setVisibility(View.GONE);
				holder.price.setText("计:" + currency + item.getPrice());
				holder.price.setTextColor(resources.getColor(R.color.red));
			} else {
				holder.title.setVisibility(View.VISIBLE);
				holder.title.setText(item.getName());
				holder.price.setText(currency + item.getPrice() + " * "
						+ item.getNum());
				holder.price.setTextColor(resources
						.getColor(R.color.font_content));
			}

			return view;
		}

	}

	private ItemDTO buildItem(String[] parts) {

		ItemDTO item = new ItemDTO();
		item.setId(Long.parseLong(parts[0]));
		item.setName(parts[1]);
		item.setSid(Long.parseLong(parts[2]));
		item.setNum(Integer.parseInt(parts[3]));
		item.setPrice(Double.parseDouble(parts[4]));

		itemsID.add(item.getId());

		return item;
	}

}
