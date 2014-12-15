package com.aug3.yhyc.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aug3.yhyc.ItemListActivity;
import com.aug3.yhyc.R;
import com.aug3.yhyc.SearchShequActivity;
import com.aug3.yhyc.api.Classification;
import com.aug3.yhyc.api.WorkshopDTO;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.ListItemChecked;
import com.aug3.yhyc.model.MyShequList;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.SystemCache;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarTitleView;
import com.aug3.yhyc.view.MyGridView;
import com.aug3.yhyc.widget.CheckedListAdapter;
import com.aug3.yhyc.widget.adroll.MyImgScroll;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HomeFragment extends BaseFragment {

	private View view;

	private ActionBarTitleView ab;

	private PopupWindow shequSelectorPopup;
	private View popup_shequ_view;
	private ListView shequListView;
	private Button shequSearchBtn;

	private ArrayList<ListItemChecked> listItemData;
	private CheckedListAdapter listAdapter;

	private SharedPreferences sp;

	private long myshequID;
	private String myshequ;
	private List<MyShequList.Shequ> myshequList = new ArrayList<MyShequList.Shequ>();

	private SparseArray<List<WorkshopDTO>> defaultShops;

	private List<Classification> listCat;
	private MyGridView gridView;

	// ad
	private MyImgScroll myPager; // 图片容器
	private LinearLayout ovalLayout; // 圆点容器
	private List<View> listViews; // 图片组

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_home, container, false);

		sp = getActivity().getSharedPreferences(Constants.SharedPrefs.YHYC,
				Context.MODE_PRIVATE);

		initActionBar();

		initAdScroll();

		initCategoryView();

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		myshequID = sp.getLong(Constants.SharedPrefs.MY_SHEQUID, 0);

		if (myshequID == 0) {
			Bundle bundle = getActivity().getIntent().getExtras();
			if (bundle != null) {
				long shequ = bundle.getLong(Constants.Extra.SHEQU, 0);
				if (shequ != 0) {
					myshequID = shequ;
					sp.edit()
							.putLong(Constants.SharedPrefs.MY_SHEQUID,
									myshequID).commit();
				}
			}
		}
		gotoShequSelection();
	}

	private void gotoShequSelection() {

		if (myshequID == 0) {
			Intent intent = new Intent(getActivity(), SearchShequActivity.class);
			gotoActivity(getActivity(), intent);
		}
	}

	@Override
	public void onResume() {

		super.onResume();
		
		myPager.startTimer();

		myshequID = sp.getLong(Constants.SharedPrefs.MY_SHEQUID, 0);
		myshequ = sp.getString(Constants.SharedPrefs.MY_SHEQU, "");

		if (shequSelectorPopup != null) {
			shequSelectorPopup.dismiss();
		}

		if (TextUtils.isEmpty(myshequ)) {
			ab.setTitleText(R.string.title_select_shequ);
		} else {
			ab.setTitleText(myshequ);
		}
	}

	@Override
	public void onStop() {
		myPager.stopTimer();
		super.onStop();
	}

	@SuppressLint("NewApi")
	protected void initActionBar() {

		ab = (ActionBarTitleView) view.findViewById(R.id.title_view);
		ab.setTitleExpandVisibility(true);

		ab.setTitleOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showShequSelectorPopup(v);

			}
		});

	}

	private void initAdScroll() {
		myPager = (MyImgScroll) view.findViewById(R.id.myvp);
		ovalLayout = (LinearLayout) view.findViewById(R.id.vb);
		InitViewPager();// 初始化图片
		// 开始滚动
		myPager.start(this.getActivity(), listViews, 6000, ovalLayout,
				R.layout.ad_bottom_item, R.id.ad_item_v,
				R.drawable.dot_focused, R.drawable.dot_normal);
	}

	/**
	 * 初始化图片
	 */
	private void InitViewPager() {
		listViews = new ArrayList<View>();
		Map<String, String> urls = UserPreference.getInstance().getUrls();
		String ad01 = urls.get("ad_home_01");
		String ad02 = urls.get("ad_home_02");
		String ad03 = urls.get("ad_home_03");

		ImageView imageNext = (ImageView) view.findViewById(R.id.image_next);
		Tools.setImageResource(imageNext, ad01, ScaleType.FIT_XY);

		ImageView imageLast = (ImageView) view.findViewById(R.id.image_last);
		Tools.setImageResource(imageLast, ad03, ScaleType.FIT_XY);

		ImageView imageMain = (ImageView) view.findViewById(R.id.image_main);
		Tools.setImageResource(imageMain, ad02, ScaleType.FIT_XY);

		listViews.add(imageNext);
		listViews.add(imageLast);
		listViews.add(imageMain);

	}

	private void initCategoryView() {

		final SystemCache sc = new SystemCache();
		final String cacheKey = Constants.CacheKey.CLASSIFICATION.getName();

		if (sc.containsKey(cacheKey)) {
			listCat = (List<Classification>) sc.get(cacheKey);

			initGridList();
		} else {
			RequestParams p = new RequestParams();

			YhycRestClient.get(Constants.ReqUrl.SHOP_GOODS_CLASSIFICATION, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								JSONArray cats = (JSONArray) response
										.get(Constants.RESP_RESULT);

								int len = cats.length();
								List<Classification> catlist = new ArrayList<Classification>();

								for (int i = 0; i < len; i++) {
									JSONObject cat = (JSONObject) cats.get(i);

									Classification classify = new Classification();
									classify.setCode(cat.getInt("code"));
									classify.setName(cat.getString("name"));
									classify.setPic(cat.getString("pic"));

									catlist.add(classify);
								}
								sc.put(cacheKey, catlist);

								listCat = catlist;

								initGridList();

							} catch (JSONException e) {
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Tools.ShowAPIError(getActivity());
						}
					});
		}

	}

	private void initGridList() {

		BaseAdapter gridAdapter = new BaseAdapter() {

			@Override
			public int getCount() {
				return listCat == null ? 0 : listCat.size();
			}

			@Override
			public Object getItem(int pos) {
				return listCat == null ? null : listCat.get(pos);
			}

			@Override
			public long getItemId(int pos) {
				return pos;
			}

			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {

				View v = convertView;
				CatItemViewHolder holder;

				if (convertView == null) {
					v = LayoutInflater.from(view.getContext()).inflate(
							R.layout.classification_item, parent, false);

					holder = new CatItemViewHolder();
					holder.image = (ImageView) v.findViewById(R.id.cat_image);
					holder.tv_title = (TextView) v.findViewById(R.id.cat_title);

					v.setTag(holder);
				} else {
					holder = (CatItemViewHolder) v.getTag();
				}

				final Classification vi = listCat.get(pos);

				displayCatImage(holder.image, vi.getPic());
				holder.tv_title.setText(vi.getName());

				holder.image.setOnClickListener(onCategoryClickListener(
						vi.getCode(), vi.getName()));

				return v;
			}

		};

		gridView = (MyGridView) view.findViewById(R.id.gridView);
		gridView.setAdapter(gridAdapter);
	}

	private void displayCatImage(ImageView iv, String pic) {

		if (pic.contains("category_fruit")) {
			iv.setBackgroundResource(R.drawable.category_fruit);
		} else if (pic.contains("vegetable")) {
			iv.setBackgroundResource(R.drawable.category_vegetable);
		} else if (pic.contains("meat")) {
			iv.setBackgroundResource(R.drawable.category_meat);
		} else if (pic.contains("category_sea")) {
			iv.setBackgroundResource(R.drawable.category_sea);
		} else if (pic.contains("organic")) {
			iv.setBackgroundResource(R.drawable.category_organic);
		} else if (pic.contains("category_bread")) {
			iv.setBackgroundResource(R.drawable.category_bread);
		} else if (pic.contains("category_oil")) {
			iv.setBackgroundResource(R.drawable.category_oil);
		} else {
			AsyncImageLoader.displayImage(pic, iv);
		}
	}

	public class CatItemViewHolder {

		public ImageView image;
		public TextView tv_title;

	}

	private View.OnClickListener onCategoryClickListener(final int catID,
			final String categoryName) {

		return new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (myshequID == 0) {
					gotoShequSelection();
					return;
				}

				gotoDefaultShop(catID, categoryName);

			}
		};

	}

	private void gotoDefaultShop(final int catID, final String categoryName) {

		final SystemCache sc = new SystemCache();
		final String cacheKey = Constants.CacheKey.SHEQU_SHOPS.getName(String
				.valueOf(myshequID));
		if (sc.containsKey(cacheKey)) {
			defaultShops = (SparseArray<List<WorkshopDTO>>) sc.get(cacheKey);
			gotoItemList(catID, categoryName);
		} else {
			RequestParams p = new RequestParams();
			p.put("shequ", myshequID);

			YhycRestClient.get(Constants.ReqUrl.SHOP_DEFAULT, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								JSONArray shops = (JSONArray) response
										.get(Constants.RESP_RESULT);

								int len = shops.length();

								SparseArray<List<WorkshopDTO>> catShops = new SparseArray<List<WorkshopDTO>>();
								for (int i = 0; i < len; i++) {
									WorkshopDTO shop = JSONUtil.fromJson(shops
											.get(i).toString(),
											WorkshopDTO.class);
									List<Integer> cats = shop.getCat();
									for (int cat : cats) {
										if (catShops.get(cat) == null) {
											List<WorkshopDTO> catshop = new ArrayList<WorkshopDTO>();
											catshop.add(shop);
											catShops.put(cat, catshop);
										} else {
											catShops.get(cat).add(shop);
										}
									}
								}
								sc.put(cacheKey, catShops);

								defaultShops = catShops.clone();

								gotoItemList(catID, categoryName);

							} catch (JSONException e) {
								Log.i("workshoplist activity", e.getMessage());
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Tools.ShowAPIError(getActivity());
						}
					});
		}
	}

	private void gotoItemList(int catID, String categoryName) {

		List<WorkshopDTO> catshops = defaultShops.get(catID);

		if (catshops == null) {
			Tools.ShowTip(getActivity(), R.string.label_workshop_coming_soon);
			return;
		} else if (catshops.size() == 1) {
			WorkshopDTO dto = catshops.get(0);

			Intent intent = new Intent(getActivity(), ItemListActivity.class);

			intent.putExtra(Constants.Extra.WORKSHOP, dto.getId());
			intent.putExtra(Constants.Extra.SHOP_NAME, dto.getName());
			intent.putExtra(Constants.Extra.SHOP_NOTICE, dto.getNotice());
			intent.putExtra(Constants.Extra.PRODUCT_CAT_ID, catID);

			gotoActivity(getActivity(), intent);
		} else {
			startWorkshopListActivity(getActivity(), myshequID, catID,
					categoryName);
		}

	}

	private void showShequSelectorPopup(View parent) {

		if (shequSelectorPopup == null) {

			LayoutInflater layoutInflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			popup_shequ_view = layoutInflater.inflate(
					R.layout.popup_shequ_selector, null);

			shequListView = (ListView) popup_shequ_view
					.findViewById(R.id.shequ_listview);

			listAdapter = new CheckedListAdapter(getActivity(), listItemData);
			shequListView.setAdapter(listAdapter);

			shequSearchBtn = (Button) popup_shequ_view
					.findViewById(R.id.shequ_search_btn);
			shequSearchBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							SearchShequActivity.class);

					gotoActivity(getActivity(), intent);
				}
			});

			shequSelectorPopup = new PopupWindow(popup_shequ_view,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		}

		// enable to click outside for disable
		shequSelectorPopup.setOutsideTouchable(true);

		// 点击“返回Back”也能使其消失，并且并不会影响你的背景
		shequSelectorPopup.setBackgroundDrawable(new BitmapDrawable());
		shequSelectorPopup.showAsDropDown(parent, 0, 18);

		shequListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				long newSelected = listItemData.get(position).getCode();
				if (myshequID != newSelected) {
					myshequID = newSelected;
					myshequ = listItemData.get(position).getName();
					ab.setTitleText(myshequ);
				}

				if (shequSelectorPopup != null) {
					shequSelectorPopup.dismiss();
				}
			}
		});

		addDataToList();

	}

	private void addDataToList() {

		String myshequlist = sp.getString(Constants.SharedPrefs.MY_SHEQU_LIST,
				"");

		myshequList = new MyShequList(myshequlist).getShequlist();

		if (listItemData != null) {
			listItemData.clear();
		} else {
			listItemData = new ArrayList<ListItemChecked>();
		}

		for (MyShequList.Shequ s : myshequList) {
			ListItemChecked item = new ListItemChecked();
			item.setCode(s.getId());
			item.setName(s.getName());
			item.setChecked(s.getId() == myshequID ? true : false);
			listItemData.add(item);
		}

		if (listAdapter != null) {
			listAdapter.setListItemData(listItemData);
			listAdapter.notifyDataSetChanged();
		}
	}

}
