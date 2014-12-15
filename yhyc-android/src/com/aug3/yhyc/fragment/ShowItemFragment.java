package com.aug3.yhyc.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aug3.yhyc.ItemDetailActivity;
import com.aug3.yhyc.OrderConfirmActivity;
import com.aug3.yhyc.R;
import com.aug3.yhyc.api.Item;
import com.aug3.yhyc.api.ItemDTO;
import com.aug3.yhyc.api.ProductDTO;
import com.aug3.yhyc.api.ProductItem;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.base.Constants.Extra;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.GalleryItemPromotion;
import com.aug3.yhyc.model.ShoppingCartItem;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.SystemCache;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.AutoScaleViewGroup;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ShowItemFragment extends BaseFragment {

	private View view;

	TextView tv_originPrice;

	long itemid;

	private Button btnSub, btnAdd, btnOrder, btnCart;
	private EditText etQuantity;
	private static int quantity;

	private ProductItem productItem;

	private RatingBar productRating;

	private GridView gallery;

	private Resources resources;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// The last two arguments ensure LayoutParams are inflated
		// properly.
		view = inflater.inflate(R.layout.fragment_item_show, container, false);

		resources = getResources();

		getItemData();

		return view;
	}

	private void getItemData() {

		Bundle bundle = getActivity().getIntent().getExtras();
		itemid = bundle.getLong(Constants.Extra.ITEMID);

		final SystemCache sc = new SystemCache();
		final String cacheKey = Constants.CacheKey.ITEM.getName(String
				.valueOf(itemid));
		if (sc.containsKey(cacheKey)) {
			productItem = (ProductItem) sc.get(cacheKey);
			drawUIonDataReady();
		} else {

			RequestParams p = new RequestParams();
			p.put(Constants.ReqParam.ITEM, itemid);

			YhycRestClient.get(Constants.ReqUrl.ITEM_SHOW, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								JSONObject result = (JSONObject) response
										.get(Constants.RESP_RESULT);
								productItem = JSONUtil.fromJson(
										result.toString(), ProductItem.class);

								sc.put(cacheKey, productItem,
										Constants.TTL_IN_SECONDS);

								drawUIonDataReady();
							} catch (JSONException e) {
								Log.i("iteminfo fragment", e.getMessage());
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

	private void drawUIonDataReady() {
		initProductBuyArea();
		initItemBaseInfo();
		initSuggestProducts();
	}

	private void initItemBaseInfo() {

		Item i = productItem.getItem();
		ProductDTO p = productItem.getProduct();

		TextView tv_title = (TextView) view.findViewById(R.id.product_title);
		tv_title.setText(i.getName());
		if (p.isSeason()) {
			tv_title.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.icon_season, 0);
		}

		AsyncImageLoader.displayImage(i.getPic(),
				(ImageView) view.findViewById(R.id.product_img));

		((TextView) view.findViewById(R.id.product_price)).setText(resources
				.getString(R.string.label_rmb) + i.getPp());

		tv_originPrice = (TextView) view
				.findViewById(R.id.product_origin_price);
		tv_originPrice.setText(resources.getString(R.string.label_rmb)
				+ i.getMp());
		tv_originPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		((TextView) view.findViewById(R.id.product_sales_volume))
				.setText(String.valueOf(i.getSales()));

		productRating = (RatingBar) view.findViewById(R.id.product_ratingbar);

		// TODO
		float rating = 4.5f;
		if (rating >= 4) {
			productRating.setRating(rating);
			productRating.setVisibility(View.VISIBLE);
		}

		List<String> tags = p.getTags();

		if (tags != null && !tags.isEmpty()) {
			view.findViewById(R.id.product_tags_tv).setVisibility(View.VISIBLE);
			AutoScaleViewGroup tagslayout = (AutoScaleViewGroup) view
					.findViewById(R.id.product_tags);
			Activity context = getActivity();
			TextView tv_tag = null;
			for (String tag : tags) {
				tv_tag = new TextView(context);
				tv_tag.setText(tag);
				tv_tag.setTextSize(16);
				tv_tag.setTextColor(resources.getColor(R.color.white));
				tv_tag.setBackgroundResource(R.drawable.btn_submit_shape);
				tv_tag.setPadding(6, 2, 6, 2);
				tagslayout.addView(tv_tag);
			}
		} else {
			view.findViewById(R.id.product_tags_tv).setVisibility(View.GONE);
		}

		((TextView) view.findViewById(R.id.product_desc)).setText(p.getDesc());

		String cooked = p.getCooked();
		String cpic = p.getCpic();
		if (!TextUtils.isEmpty(cooked)) {
			((TextView) view.findViewById(R.id.product_cooked_tv))
					.setText(cooked);
			if (!TextUtils.isEmpty(cpic)) {
				AsyncImageLoader.displayImage(p.getCpic(),
						(ImageView) view.findViewById(R.id.product_cooked_iv));
			} else {
				view.findViewById(R.id.product_cooked_iv).setVisibility(
						View.GONE);
			}
		} else {

			if (!TextUtils.isEmpty(cpic)) {
				AsyncImageLoader.displayImage(p.getCpic(),
						(ImageView) view.findViewById(R.id.product_cooked_iv));
				view.findViewById(R.id.product_cooked_iv).setVisibility(
						View.VISIBLE);
			} else {
				view.findViewById(R.id.product_cooked_layout).setVisibility(
						View.GONE);
			}
		}

	}

	/**
	 * 初始化直接下单区域
	 */
	private void initProductBuyArea() {

		btnSub = (Button) view.findViewById(R.id.product_quantity_sub);
		btnAdd = (Button) view.findViewById(R.id.product_quantity_add);
		etQuantity = (EditText) view.findViewById(R.id.product_quantity);
		btnOrder = (Button) view.findViewById(R.id.product_direct_order);
		btnCart = (Button) view.findViewById(R.id.product_addtocart);

		quantity = 1;

		btnSub.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String num = etQuantity.getText().toString();
				if (TextUtils.isEmpty(num)) {
					quantity = 1;
					etQuantity.setText("1");
				} else {
					quantity = Integer.parseInt(num);
				}

				if (quantity > 1) {

					quantity = quantity - 1;
					etQuantity.setText(String.valueOf(quantity));

				}

			}
		});

		btnAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String num = etQuantity.getText().toString();
				if (TextUtils.isEmpty(num)) {
					quantity = 1;
					etQuantity.setText("1");
				} else {
					quantity = Integer.parseInt(num);
				}

				if (quantity < 100) {

					quantity = quantity + 1;
					etQuantity.setText(String.valueOf(quantity));

				}

			}
		});

		btnOrder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShoppingCartItem order = new ShoppingCartItem();
				Item i = productItem.getItem();
				order.setId(itemid);
				order.setTitle(i.getName());
				order.setGid(i.getSid());
				order.setQuantity(quantity);
				order.setPrice(i.getPp());

				Intent intent = new Intent(getActivity(),
						OrderConfirmActivity.class);

				intent.putExtra(Extra.ORDER_LIST,
						new String[] { order.toString() });

				startActivity(intent);
				getActivity().overridePendingTransition(
						android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);

			}
		});

		if (productItem.getItem().getSts() == 2) {
			btnCart.setBackgroundResource(R.drawable.cart_nor);
		} else {

			final UserPreference prefs = UserPreference.getInstance();

			btnCart.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (prefs.getCart().contains(itemid)) {

						prefs.increaseNumberOfItemInCart(itemid, quantity);

					} else {
						prefs.getCart().add(itemid);
						prefs.setCartUpdated(true);
						prefs.increaseNumberOfItemInCart(itemid, quantity);
						((ItemDetailActivity) getActivity()).shoppingcart_number
								.setText(String.valueOf(prefs.getCart().size()));

					}

					Tools.ShowTip(getActivity(),
							R.string.info_addtocart_success);
				}
			});
		}
	}

	private void initSuggestProducts() {

		List<ItemDTO> refs = productItem.getPromotion();

		if (refs != null && refs.size() > 0) {
			view.findViewById(R.id.product_promotion_layout).setVisibility(
					View.VISIBLE);

			gallery = (GridView) view
					.findViewById(R.id.product_recommend_gallery);

			final ArrayList<GalleryItemPromotion> list = new ArrayList<GalleryItemPromotion>();

			for (ItemDTO i : refs) {
				GalleryItemPromotion gi = new GalleryItemPromotion();
				gi.setId(i.getId());
				gi.setTitle(i.getName());
				gi.setUrl(i.getPic());
				gi.setPp(resources.getString(R.string.label_rmb) + i.getPrice());
				gi.setMp(String.valueOf(i.getOrigprice()));
				if (i.getAct() == 1) {
					gi.setDrawable(R.drawable.ic_cuxiao);
				} else {
					gi.setDrawable(0);
				}

				list.add(gi);
			}

			final ImageAdapter adapter = new ImageAdapter(
					ShowItemFragment.this.getActivity(), list);
			gallery.setAdapter(adapter);
			gallery.setNumColumns(5);

			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int select, long arg3) {

					startItemDetailActivity(getActivity(), list.get(select)
							.getId());
				}
			});

			gallery.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					v.clearFocus();
					return false;
				}
			});
		} else {
			view.findViewById(R.id.product_promotion_layout).setVisibility(
					View.GONE);
		}
	}

	class ImageAdapter extends BaseAdapter {

		private ArrayList<GalleryItemPromotion> list;
		private LayoutInflater inflater;

		private class ViewHolder {
			public ImageView image;
			public TextView title;
			public TextView price;
			public TextView origprice;
			public ImageView act;
		}

		public ImageAdapter(Context context,
				ArrayList<GalleryItemPromotion> list) {
			super();
			this.list = list;
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.gallery_item_promotion,
						null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.gallery_item_title);
				holder.act = (ImageView) convertView
						.findViewById(R.id.gallery_item_act);
				holder.image = (ImageView) convertView
						.findViewById(R.id.gallery_item_image);
				holder.price = (TextView) convertView
						.findViewById(R.id.gallery_item_price);
				holder.origprice = (TextView) convertView
						.findViewById(R.id.gallery_item_orgin_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			GalleryItemPromotion i = list.get(position);
			holder.title.setText(i.getTitle());
			if (i.getDrawable() != 0)
				holder.act.setBackgroundResource(i.getDrawable());
			AsyncImageLoader.displayImage(i.getUrl(), holder.image);
			holder.price.setText(i.getPp());
			holder.origprice.setText(i.getMp());
			holder.origprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			return convertView;
		}

	}

}
