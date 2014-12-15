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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.api.ItemDTO;
import com.aug3.yhyc.api.ShopItem;
import com.aug3.yhyc.api.WorkshopDTO;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.ServiceAgent;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.GroupListItemBase;
import com.aug3.yhyc.model.ShoppingCartItem;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.model.WorkshopGroupItem;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarCheckedView;
import com.aug3.yhyc.view.ConfirmDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyFavoriateActivity extends AbsListViewBaseActivity {

	private ActionBarCheckedView ab;

	private ItemAdapter listAdapter;

	private List<GroupListItemBase> favItems;

	private Button btn_addto_cart, del_selected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myfavorite);

		initActionBar();

		initProductList();

		initActionArea();

	}

	@Override
	public void onResume() {
		super.onResume();
		getFavItems();
	}

	@Override
	protected void onPause() {

		super.onPause();

		updatePrefs();

	}

	private void updatePrefs() {

		ServiceAgent.updateFav();
	}

	private void initActionBar() {

		ab = (ActionBarCheckedView) findViewById(R.id.actionbar_checked_view);
		ab.setLeftOnClickListener(goBack);
		ab.setTitle(R.string.title_myfavorite);
		ab.setOnCheckedChangeListener(checkAllListener);

	}

	private CompoundButton.OnCheckedChangeListener checkAllListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton v, boolean isChecked) {
			int size = favItems.size();
			if (v.isChecked()) {
				for (int i = 0; i < size; i++) {
					favItems.get(i).setChecked(true);
				}
			} else {
				for (int i = 0; i < size; i++) {
					favItems.get(i).setChecked(false);
				}
			}
			listAdapter.notifyDataSetChanged();

		}
	};

	private CompoundButton.OnCheckedChangeListener onGroupCheckedChangeListener(
			final int idx) {

		return new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				int size = favItems.size();
				if (v.isChecked()) {
					favItems.get(idx).setChecked(true);
					for (int i = idx + 1; i < size; i++) {

						if (favItems.get(i).isGroup()) {
							break;
						} else {
							favItems.get(i).setChecked(true);
						}
					}
				} else {
					favItems.get(idx).setChecked(false);
					for (int i = idx + 1; i < size; i++) {

						if (favItems.get(i).isGroup()) {
							break;
						} else {
							favItems.get(i).setChecked(false);
						}
					}
				}
				listAdapter.notifyDataSetChanged();

			}
		};
	}

	private void initActionArea() {

		del_selected = (Button) findViewById(R.id.fav_delete_selected);
		btn_addto_cart = (Button) findViewById(R.id.fav_add_to_cart);

		del_selected.setOnClickListener(deleteSelectedBtnListener);

		btn_addto_cart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				List<Long> selectedItems = new ArrayList<Long>();
				for (GroupListItemBase i : favItems) {

					if (!i.isGroup() && i.isChecked()) {
						ShoppingCartItem ichild = (ShoppingCartItem) i;
						selectedItems.add(ichild.getId());
					}

				}

				if (selectedItems.size() == 0) {
					Tools.ShowTip(MyFavoriateActivity.this,
							R.string.label_select_items);

				} else {

					UserPreference.getInstance().getCart()
							.addAll(selectedItems);
					UserPreference.getInstance().setCartUpdated(true);

					Tools.ShowTip(MyFavoriateActivity.this,
							R.string.info_addtocart_success);
				}
			}
		});
	}

	private View.OnClickListener deleteSelectedBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			final ConfirmDialog ad = new ConfirmDialog(MyFavoriateActivity.this);

			ad.setTitle(R.string.label_tips);
			ad.setMessage(R.string.label_confirm_to_remove_items);
			ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					ad.dismiss();

					// Ω· ¯’‚∏ˆActivity
					List<GroupListItemBase> removeOnes = new ArrayList<GroupListItemBase>();
					List<Long> removeItems = new ArrayList<Long>();
					for (GroupListItemBase item : favItems) {
						if (item.isChecked()) {
							removeOnes.add(item);
							removeItems.add(item.getId());
						}
					}

					if (removeOnes.size() > 0) {
						favItems.removeAll(removeOnes);
						listAdapter.notifyDataSetChanged();

						UserPreference.getInstance().getFav()
								.removeAll(removeItems);
						UserPreference.getInstance().setFavUpdated(true);
					}

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
	private void initProductList() {

		favItems = new ArrayList<GroupListItemBase>();

		listView = (ListView) findViewById(android.R.id.list);
		listAdapter = new ItemAdapter();
		listView.setAdapter(listAdapter);

	}

	private void getFavItems() {

		long uid = UserPreference.getInstance().getUid();

		if (uid == 0) {
			return;
		}

		updatePrefs();

		favItems.clear();

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.USERID, UserPreference.getInstance().getUid());

		YhycRestClient.get(Constants.ReqUrl.ITEM_MYFAV, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONArray shopItems = (JSONArray) response
									.get(Constants.RESP_RESULT);
							int len = shopItems.length();
							for (int i = 0; i < len; i++) {
								JSONObject itemObj = (JSONObject) shopItems
										.get(i);
								ShopItem shopItem = JSONUtil.fromJson(
										itemObj.toString(), ShopItem.class);
								WorkshopDTO shopDTO = shopItem.getShop();

								WorkshopGroupItem group = new WorkshopGroupItem();
								group.setTitle(shopDTO.getName());
								group.setId(shopDTO.getId());
								group.setGroup(true);
								group.setCat(shopDTO.getCat());
								group.setNotice(shopDTO.getNotice());
								favItems.add(group);

								List<ItemDTO> items = shopItem.getItem();
								for (ItemDTO item : items) {
									ShoppingCartItem child = new ShoppingCartItem();
									child.setId(item.getId());
									child.setGid(item.getSid());
									child.setUrl(item.getPic());
									child.setTitle(item.getName());
									child.setPrice(item.getPrice());
									child.setOrigPrice(item.getOrigprice());
									favItems.add(child);

									UserPreference.getInstance().getFav()
											.add(item.getId());
								}

							}

							listAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							Log.i("myfav activity", e.getMessage());
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Log.i("myfav activity", throwable.getMessage());
					}
				});
	}

	class ItemAdapter extends BaseAdapter {

		private class ChildViewHolder {
			public ImageView image;
			public TextView tv_title;
			public TextView tv_price;
			public TextView tv_origprice;
			public CheckBox cb_selected;
		}

		private class GroupViewHolder {
			public TextView tv_title;
			public CheckBox cb_selected;
		}

		@Override
		public int getCount() {
			if (favItems == null)
				return 0;
			return favItems.size();
		}

		@Override
		public Object getItem(int pos) {
			if (favItems == null)
				return null;
			return favItems.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			boolean isGroup = favItems.get(position).isGroup();

			View view = convertView;

			if (!isGroup) {

				final ChildViewHolder holder;

				if (convertView == null
						|| convertView.getTag() instanceof GroupViewHolder) {
					view = getLayoutInflater().inflate(R.layout.list_item_fav,
							parent, false);
					holder = new ChildViewHolder();
					holder.image = (ImageView) view
							.findViewById(R.id.fav_item_image);
					holder.tv_title = (TextView) view
							.findViewById(R.id.fav_item_title);
					holder.tv_price = (TextView) view
							.findViewById(R.id.fav_item_price);
					holder.tv_origprice = (TextView) view
							.findViewById(R.id.fav_item_origin_price);
					holder.cb_selected = (CheckBox) view
							.findViewById(R.id.fav_item_selected);

					view.setTag(holder);
				} else {
					holder = (ChildViewHolder) view.getTag();
				}

				final ShoppingCartItem i = (ShoppingCartItem) favItems
						.get(position);
				holder.tv_title.setText(i.getTitle());
				holder.tv_title.setOnClickListener(showItemDetail(i.getId()));
				holder.tv_price.setText(resources.getString(R.string.label_rmb)
						+ i.getPrice());
				holder.tv_origprice.setText(resources
						.getString(R.string.label_rmb) + i.getOrigPrice());
				holder.tv_origprice.getPaint().setFlags(
						Paint.STRIKE_THRU_TEXT_FLAG);
				holder.cb_selected.setChecked(i.isChecked());
				holder.cb_selected
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (holder.cb_selected.isChecked()) {
									favItems.get(position).setChecked(true);
								} else {
									favItems.get(position).setChecked(false);
								}
							}
						});

				AsyncImageLoader.displayImage(i.getUrl(), holder.image);

				view.setOnClickListener(showItemDetail(i.getId()));

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

				WorkshopGroupItem i = (WorkshopGroupItem) favItems
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

	private View.OnClickListener showWorkshop(final WorkshopGroupItem shop) {

		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyFavoriateActivity.this,
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
				Intent intent = new Intent(MyFavoriateActivity.this,
						ItemDetailActivity.class);
				intent.putExtra(Constants.Extra.ITEMID, itemid);
				gotoActivity(intent);

			}
		};
	}

}
