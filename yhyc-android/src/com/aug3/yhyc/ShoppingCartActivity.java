package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.api.ItemDTO;
import com.aug3.yhyc.api.ShopItem;
import com.aug3.yhyc.api.WorkshopDTO;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.base.Constants.Extra;
import com.aug3.yhyc.http.ServiceAgent;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.GroupListItemBase;
import com.aug3.yhyc.model.ShoppingCartItem;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.model.WorkshopGroupItem;
import com.aug3.yhyc.util.Arith;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarCheckedView;
import com.aug3.yhyc.view.ConfirmDialog;
import com.aug3.yhyc.view.NumberPickerDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ShoppingCartActivity extends AbsListViewBaseActivity {

	private ActionBarCheckedView ab;

	private ItemAdapter listAdapter;

	private List<GroupListItemBase> cartItems;

	private TextView tv_total;

	private List<Long> selectedItems = new ArrayList<Long>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoppingcart);

		initActionBar();

		initCartItemList();

		initActionArea();

	}

	@Override
	public void onResume() {
		super.onResume();
		getShoppingcartItems();
	}

	@Override
	protected void onPause() {

		super.onPause();

		ServiceAgent.updateCart();

	}

	private void initActionBar() {

		ab = (ActionBarCheckedView) findViewById(R.id.actionbar_checked_view);
		ab.setLeftOnClickListener(goBack);
		ab.setTitle(R.string.title_shopping_cart);
		ab.setChecked(true);
		ab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				checkAll(v.isChecked());
			}
		});
	}

	private void checkAll(boolean isCheckAll) {
		int size = cartItems.size();
		double total = 0;
		if (isCheckAll) {
			for (int i = 0; i < size; i++) {
				cartItems.get(i).setChecked(true);
				if (!cartItems.get(i).isGroup()) {
					ShoppingCartItem item = (ShoppingCartItem) cartItems.get(i);
					total = Arith.add(total,
							item.getPrice() * item.getQuantity());
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				cartItems.get(i).setChecked(false);
			}
		}
		listAdapter.notifyDataSetChanged();
		tv_total.setText(resources.getString(R.string.label_rmb) + total);
	}

	private CompoundButton.OnCheckedChangeListener onGroupCheckedChangeListener(
			final int idx) {

		return new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				int size = cartItems.size();
				if (v.isChecked()) {
					cartItems.get(idx).setChecked(true);
					for (int i = idx + 1; i < size; i++) {

						if (cartItems.get(i).isGroup()) {
							break;
						} else {
							cartItems.get(i).setChecked(true);
						}
					}
				} else {
					cartItems.get(idx).setChecked(false);
					for (int i = idx + 1; i < size; i++) {

						if (cartItems.get(i).isGroup()) {
							break;
						} else {
							cartItems.get(i).setChecked(false);
						}
					}
				}
				listAdapter.notifyDataSetChanged();

				calculateTotalPrice();

			}
		};
	}

	private void calculateTotalPrice() {
		double total = 0;
		int size = cartItems.size();
		for (int i = 0; i < size; i++) {
			if (!cartItems.get(i).isGroup() && cartItems.get(i).isChecked()) {
				ShoppingCartItem item = ((ShoppingCartItem) cartItems.get(i));
				total = Arith.add(total, item.getPrice() * item.getQuantity());
			}
		}
		tv_total.setText(resources.getString(R.string.label_rmb) + total);
	}

	private void initActionArea() {

		tv_total = (TextView) findViewById(R.id.shoppingcart_totalprice);

		findViewById(R.id.shoppingcart_delete_selected).setOnClickListener(
				deleteSelectedBtnListener);

		findViewById(R.id.shoppingcart_confirm_order).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						List<String> selected = new ArrayList<String>();
						selectedItems.clear();

						for (GroupListItemBase i : cartItems) {

							if (!i.isGroup() && i.isChecked()) {
								ShoppingCartItem item = (ShoppingCartItem) i;
								selected.add(item.toString());
								selectedItems.add(item.getId());
							}

						}

						if (selected.size() == 0) {
							Tools.ShowTip(ShoppingCartActivity.this,
									R.string.label_select_items);

						} else {

							Intent intent = new Intent(
									ShoppingCartActivity.this,
									OrderConfirmActivity.class);

							intent.putExtra(Extra.ORDER_LIST,
									selected.toArray(new String[] {}));

							gotoActivity(intent);
						}
					}
				});
	}

	private View.OnClickListener deleteSelectedBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			final ConfirmDialog ad = new ConfirmDialog(
					ShoppingCartActivity.this);

			ad.setTitle(R.string.label_tips);
			ad.setMessage(R.string.label_confirm_to_remove_items);
			ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					ad.dismiss();

					List<GroupListItemBase> removeOnes = new ArrayList<GroupListItemBase>();
					List<Long> removeItems = new ArrayList<Long>();
					UserPreference prefs = UserPreference.getInstance();
					for (GroupListItemBase item : cartItems) {
						if (item.isChecked()) {
							removeOnes.add(item);
							removeItems.add(item.getId());
						}
					}

					if (removeOnes.size() > 0) {
						cartItems.removeAll(removeOnes);
						listAdapter.notifyDataSetChanged();

						prefs.getCart().removeAll(removeItems);
						prefs.getCartItemsMap().keySet().removeAll(removeItems);

						prefs.setCartUpdated(true);

					}

					removeOnes = null;
					removeItems = null;

					calculateTotalPrice();

				}
			});

			ad.setNegativeButton(R.string.btn_cancel,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							ad.dismiss();
						}
					});

		}
	};

	@SuppressLint("NewApi")
	private void initCartItemList() {

		cartItems = new ArrayList<GroupListItemBase>();

		listView = (ListView) findViewById(android.R.id.list);
		listAdapter = new ItemAdapter();
		listView.setAdapter(listAdapter);

	}

	private void getShoppingcartItems() {

		cartItems.clear();

		long uid = UserPreference.getInstance().getUid();

		Set<Long> itemsInCart = UserPreference.getInstance().getCart();

		if (uid == 0) {
			if (itemsInCart.isEmpty()) {
				return;
			} else {
				RequestParams p = new RequestParams();
				p.put(Constants.ReqParam.ITEMS,
						TextUtils.join(Constants.COMMA, itemsInCart));

				YhycRestClient.get(Constants.ReqUrl.ITEM_GROUPBY_SHOP, p,
						responseHandler);
			}

		} else {

			if (UserPreference.getInstance().isCartUpdated()) {
				Set<Long> items = UserPreference.getInstance().getCart();

				RequestParams p = new RequestParams();
				p.put(Constants.ReqParam.ITEMS,
						TextUtils.join(Constants.COMMA, items));

				YhycRestClient.get(Constants.ReqUrl.ITEM_GROUPBY_SHOP, p,
						responseHandler);
			} else {
				RequestParams p = new RequestParams();
				p.put(Constants.ReqParam.USERID, uid);

				YhycRestClient.get(Constants.ReqUrl.ITEM_SHOPPINGCART, p,
						responseHandler);
			}
		}
	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {

			try {
				JSONArray shopItems = (JSONArray) response
						.get(Constants.RESP_RESULT);
				int len = shopItems.length();
				int selected = selectedItems.size();
				for (int i = 0; i < len; i++) {
					JSONObject itemObj = (JSONObject) shopItems.get(i);
					ShopItem shopItem = JSONUtil.fromJson(itemObj.toString(),
							ShopItem.class);
					WorkshopDTO shopDTO = shopItem.getShop();

					WorkshopGroupItem group = new WorkshopGroupItem();
					group.setTitle(shopDTO.getName());
					group.setId(shopDTO.getId());
					group.setGroup(true);
					group.setCat(shopDTO.getCat());
					group.setNotice(shopDTO.getNotice());
					cartItems.add(group);

					List<ItemDTO> items = shopItem.getItem();
					ShoppingCartItem child = null;
					for (ItemDTO item : items) {
						child = new ShoppingCartItem();
						child.setId(item.getId());
						child.setGid(item.getSid());
						child.setUrl(item.getPic());
						child.setAct(item.getAct());
						child.setTitle(item.getName());
						child.setPrice(item.getPrice());
						child.setOrigPrice(item.getOrigprice());
						child.setQuantity(UserPreference.getInstance()
								.getNumberOfItemInCart(item.getId()));
						if (selected == 0) {
							child.setChecked(true);
						} else if (selectedItems.contains(item.getId())) {
							child.setChecked(true);
						}
						cartItems.add(child);

						UserPreference.getInstance().getCart()
								.add(item.getId());
					}

				}

				listAdapter.notifyDataSetChanged();

				if (selected == 0) {
					calculateTotalPrice();
				}

			} catch (JSONException e) {
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {

			Tools.ShowAPIError(ShoppingCartActivity.this);
		}
	};

	class ItemAdapter extends BaseAdapter {

		private class ChildViewHolder {
			public ImageView image;
			public ImageView act;
			public TextView tv_title;
			public TextView tv_price;
			public TextView tv_origprice;
			public EditText quantity;
			public CheckBox cb_selected;
		}

		private class GroupViewHolder {
			public TextView tv_title;
			public CheckBox cb_selected;
		}

		@Override
		public int getCount() {
			return cartItems.size();
		}

		@Override
		public Object getItem(int pos) {
			return cartItems.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			boolean isGroup = cartItems.get(position).isGroup();

			View view = convertView;

			if (!isGroup) {
				final ChildViewHolder holder;
				if (convertView == null
						|| convertView.getTag() instanceof GroupViewHolder) {
					view = getLayoutInflater().inflate(
							R.layout.list_item_shoppingcart, parent, false);
					holder = new ChildViewHolder();
					holder.image = (ImageView) view
							.findViewById(R.id.cart_item_image);
					holder.act = (ImageView) view
							.findViewById(R.id.cart_item_act);
					holder.tv_title = (TextView) view
							.findViewById(R.id.cart_item_title);
					holder.tv_price = (TextView) view
							.findViewById(R.id.cart_price);
					holder.tv_origprice = (TextView) view
							.findViewById(R.id.cart_origin_price);
					holder.quantity = (EditText) view
							.findViewById(R.id.cart_quantity);
					holder.cb_selected = (CheckBox) view
							.findViewById(R.id.cart_item_selected);

					view.setTag(holder);

				} else {

					holder = (ChildViewHolder) view.getTag();
				}

				ShoppingCartItem i = (ShoppingCartItem) cartItems.get(position);
				holder.tv_title.setText(i.getTitle());
				holder.tv_title.setOnClickListener(showItemDetail(i.getId()));

				holder.tv_price.setText(currency + i.getPrice());
				holder.tv_origprice.setText(currency + i.getOrigPrice());
				holder.tv_origprice.getPaint().setFlags(
						Paint.STRIKE_THRU_TEXT_FLAG);
				holder.quantity.setText(i.getQuantity() + "");
				holder.cb_selected.setChecked(i.isChecked());
				holder.cb_selected
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (holder.cb_selected.isChecked()) {
									cartItems.get(position).setChecked(true);
								} else {
									cartItems.get(position).setChecked(false);
								}
								calculateTotalPrice();
							}
						});

				holder.quantity.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						pickNumber(position);
					}
				});

				holder.quantity.addTextChangedListener(textWatcherListener);

				AsyncImageLoader.displayImage(i.getUrl(), holder.image);

				if (i.getAct() == 1) {
					holder.act.setBackgroundResource(R.drawable.ic_cuxiao);
				} else {
					holder.act.setBackgroundResource(0);
				}

			} else {
				final GroupViewHolder holder;
				if (convertView == null
						|| convertView.getTag() instanceof ChildViewHolder) {
					view = getLayoutInflater().inflate(
							R.layout.list_group_item_workshop, parent, false);
					holder = new GroupViewHolder();
					holder.tv_title = (TextView) view
							.findViewById(R.id.cart_group_item_title);
					holder.cb_selected = (CheckBox) view
							.findViewById(R.id.cart_group_item_selected);

					view.setTag(holder);

				} else {
					holder = (GroupViewHolder) view.getTag();
				}

				WorkshopGroupItem i = (WorkshopGroupItem) cartItems
						.get(position);
				holder.tv_title.setText(i.getTitle());
				holder.tv_title.setOnClickListener(showWorkshop(i));

				holder.cb_selected.setChecked(i.isChecked());
				holder.cb_selected
						.setOnCheckedChangeListener(onGroupCheckedChangeListener(position));

			}
			return view;
		}
	}

	private TextWatcher textWatcherListener = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			calculateTotalPrice();

		}

	};

	private View.OnClickListener showWorkshop(final WorkshopGroupItem shop) {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShoppingCartActivity.this,
						ItemListActivity.class);
				intent.putExtra(Constants.Extra.WORKSHOP, shop.getId());
				intent.putExtra(Constants.Extra.SHOP_NAME, shop.getTitle());
				intent.putExtra(Constants.Extra.PRODUCT_CAT_ID, shop.getCat()
						.get(0));
				intent.putExtra(Constants.Extra.SHOP_NOTICE, shop.getNotice());
				gotoActivity(intent);

			}
		};
	}

	private View.OnClickListener showItemDetail(final long itemid) {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShoppingCartActivity.this,
						ItemDetailActivity.class);
				intent.putExtra(Constants.Extra.ITEMID, itemid);
				gotoActivity(intent);

			}
		};
	}

	private void pickNumber(final int position) {

		final NumberPickerDialog ad = new NumberPickerDialog(this);
		ad.setInitialNumber(((ShoppingCartItem) cartItems.get(position))
				.getQuantity());

		ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ShoppingCartItem sci = (ShoppingCartItem) cartItems
						.get(position);
				int num = ad.getEditNumber();
				sci.setQuantity(num);
				UserPreference.getInstance().setNumberOfItemInCart(sci.getId(),
						num);
				ad.dismiss();
				listAdapter.notifyDataSetChanged();

			}
		});

		ad.setNegativeButton(R.string.btn_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});
	}
}
