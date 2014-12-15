package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.api.Region;
import com.aug3.yhyc.api.Shequ;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.MyShequList;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;
import com.aug3.yhyc.view.RegionTreeView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SearchShequActivity extends BaseActivity {

	private List<Shequ> shequlist;

	private ActionBarView ab;
	// private LocationManager lm;
	// private Location lc;

	private EditText searchText;

	private ImageView ivClear;

	private ListView list;

	private MyAdatper listAdapter;

	private RegionTreeView cityTree;

	private int myDistrict = 0, myCity = 0;
	private long expShequ;
	private String myRegion;

	private long uid;

	private View notfound_notice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_shequ);

		initTitle();

		// initSearchList();

		if (myDistrict == 0) {
			selectRegion();
		}

	}

	public void onResume() {
		super.onResume();
		initSearchList();
	}

	private void initTitle() {

		myDistrict = sp.getInt("myDistrict", 0);
		myCity = sp.getInt("myCity", 0);
		myRegion = sp.getString("myRegion", "");

		if (myDistrict == 0) {
			myRegion = resources.getString(R.string.title_select_city);
		}

		long myShequID = sp.getLong(Constants.SharedPrefs.MY_SHEQUID, 0);
		ab = (ActionBarView) findViewById(R.id.title_view);
		if (myShequID == 0) {
			ab.invisibleGoBackBtn();
		}
		ab.setTitleText(myRegion);
		ab.setTitleExpandVisibility(true);
		ab.setTitleOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectRegion();
			}
		});

		notfound_notice = findViewById(R.id.shequ_not_found_notice);

		// findViewById(R.id.shequ_search_fast_try_btn).setOnClickListener(
		// new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// if (expShequ == 0) {
		// selectRegion();
		// } else {
		// finish();
		// }
		//
		// }
		// });

		uid = UserPreference.getInstance().getUid();
	}

	private void selectRegion() {

		if (cityTree == null) {
			cityTree = (RegionTreeView) findViewById(R.id.tree_2tier);
			cityTree.init(new RegionTreeView.Listener() {

				@Override
				public void onSelected(Region district, Region parentCity) {
					cityTree.setVisibility(View.GONE);

					myDistrict = district.getId();
					myCity = parentCity.getId();
					myRegion = parentCity.getName() + " " + district.getName();
					expShequ = district.getShequ();

					ab.setTitleText(myRegion);

					Editor editor = sp.edit();
					editor.putInt("myDistrict", myDistrict);
					editor.putInt("myCity", myCity);
					editor.putString("myRegion", myRegion);
					editor.putLong(Constants.SharedPrefs.MY_SHEQUID, expShequ);
					editor.commit();

					if (myDistrict != 0) {
						doSearch(null);
					}

				}

				@Override
				public void onScroll(Region city) {

				}
			});
		}
		boolean visible = (cityTree.getVisibility() == View.VISIBLE);
		if (visible) {
			cityTree.setVisibility(View.GONE);
		} else {
			cityTree.setVisibility(View.VISIBLE);
		}

	}

	private void initSearchList() {
		// lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// lc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		//
		// if (lc != null)
		// Log.v("searchme",
		// "经度:" + lc.getLongitude() + ", 纬度：" + lc.getLatitude());

		list = (ListView) findViewById(R.id.search_result_list);

		shequlist = new ArrayList<Shequ>();

		if (myDistrict != 0) {
			doSearch(null);
		}

		listAdapter = new MyAdatper(this);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(listItemClickListener);

		searchText = (EditText) findViewById(R.id.searchpoint_edittext);

		searchText.addTextChangedListener(textChangeAndSearch);
		searchText.setOnFocusChangeListener(focuscl);

		ivClear = (ImageView) findViewById(R.id.searchpoint_text_clear);
		ivClear.setOnClickListener(clearTextlistener);
	}

	private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

			Shequ sq = shequlist.get(pos);

			String myshequlist = sp.getString(
					Constants.SharedPrefs.MY_SHEQU_LIST, "");

			Editor editor = sp.edit();
			editor.putLong(Constants.SharedPrefs.MY_SHEQUID, sq.getId());
			editor.putString(Constants.SharedPrefs.MY_SHEQU, sq.getName());

			MyShequList msq = new MyShequList(myshequlist);

			if (!myshequlist.contains(String.valueOf(sq.getId()))) {
				msq.add(sq.getId(), sq.getName());
				editor.putString(Constants.SharedPrefs.MY_SHEQU_LIST,
						msq.toString());
			} else {
				if (msq.getShequlist().size() > 1) {
					msq.adjust(sq.getId(), sq.getName());
					editor.putString(Constants.SharedPrefs.MY_SHEQU_LIST,
							msq.toString());
				}
			}

			editor.commit();

			updatePoint(myDistrict, sq.getId());

			finish();

		}
	};

	private void updatePoint(int dist, long shequ) {

		if (uid != 0 && dist != 0 && shequ != 0) {

			RequestParams p = new RequestParams();
			p.put(Constants.ReqParam.USERID, uid);
			p.put("dist", dist);
			p.put("shequ", shequ);

			YhycRestClient.post("/user/point", p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {

						}
					});
		}
	}

	private TextWatcher textChangeAndSearch = new TextWatcher() {

		// 缓存上一次文本框内是否为空
		private boolean emptyInput = true;

		private CharSequence beforeText;

		@Override
		public void afterTextChanged(Editable s) {
			if (TextUtils.isEmpty(s)) {
				if (!emptyInput) {
					emptyInput = true;
				}
			} else {
				if (emptyInput) {
					ivClear.setVisibility(View.VISIBLE);
					emptyInput = false;
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// 进行一些输入前的准备，比如声明adapter，new一个progressbar等
			beforeText = s;
		}

		/**
		 * 随着文本框内容改变动态改变列表内容
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			if (myCity == 0) {
				Tools.ShowTip(SearchShequActivity.this,
						R.string.label_toast_select_city);
				selectRegion();
				return;
			}

			if (s.length() == 0) {
				beforeText = "";
				// shequlist.clear();
				// listAdapter.clear();
				doSearch(null);

			} else if (shequlist.size() > 0
					&& s.toString().contains(beforeText)) {

				listAdapter.getFilter().filter(s);

			} else {

				doSearch(s);
			}

		}
	};

	private void doSearch(CharSequence q) {
		shequlist.clear();

		RequestParams p = new RequestParams();
		p.put("city", myCity);
		p.put("dist", myDistrict);
		p.put("q", q);

		YhycRestClient.get("/shequ/query", p, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {

				try {
					JSONArray items = (JSONArray) response
							.get(Constants.RESP_RESULT);
					int len = items.length();
					if (len == 0) {
						notfound_notice.setVisibility(View.VISIBLE);
						notfound_notice.setOnClickListener(clearAllListener);
					} else {
						notfound_notice.setVisibility(View.GONE);
					}
					for (int i = 0; i < len; i++) {
						JSONObject shequObj = (JSONObject) items.get(i);
						Shequ sq = JSONUtil.fromJson(shequObj.toString(),
								Shequ.class);
						shequlist.add(sq);
					}

					listAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					Log.i("search shequ activity", e.getMessage());
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {

				Tools.ShowAPIError(SearchShequActivity.this);
			}
		});
	}

	private View.OnFocusChangeListener focuscl = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (!TextUtils.isEmpty(searchText.getText())) {
					ivClear.setVisibility(View.VISIBLE);
				}
			}

		}
	};

	private View.OnClickListener clearTextlistener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!TextUtils.isEmpty(searchText.getText())) {
				searchText.setText("");
				ivClear.setVisibility(View.INVISIBLE);
			}

		}

	};

	class MyAdatper extends BaseAdapter implements Filterable {

		List<Shequ> originalList;

		private Context mContext;

		private MyFilter mFilter;

		public MyAdatper(Context context) {
			mContext = context;
			originalList = shequlist;
		}

		public void clear() {
			shequlist.clear();
			originalList.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return shequlist == null ? 0 : shequlist.size();
		}

		@Override
		public Shequ getItem(int position) {
			return shequlist == null ? null : shequlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				TextView tv = new TextView(mContext);
				convertView = tv;
			}
			TextView tv = (TextView) convertView;
			tv.setPadding(40, 10, 20, 10);
			tv.setTextColor(resources.getColor(R.color.font_default));
			tv.setTextSize(18);
			tv.setText(shequlist.get(position).getName());
			return tv;
		}

		@Override
		public Filter getFilter() {
			if (mFilter == null) {
				mFilter = new MyFilter();
			}
			return mFilter;
		}

		private class MyFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				String input = constraint.toString();
				FilterResults results = new FilterResults();

				if (input != null) {
					List<Shequ> newlist = new ArrayList<Shequ>();
					boolean en = Tools.isLetter(input);
					for (Shequ s : originalList) {
						if (en) {
							if (s.getPy().contains(input)) {
								newlist.add(s);
							}
						} else {
							if (s.getName().contains(input)) {
								newlist.add(s);
							}
						}
					}
					results.values = newlist;
					results.count = newlist.size();
				} else {
					results.values = originalList;
					results.count = originalList.size();
				}
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				shequlist = (ArrayList) results.values;
				if (shequlist == null || shequlist.isEmpty()) {
					notfound_notice.setVisibility(View.VISIBLE);
					notfound_notice.setOnClickListener(clearAllListener);
				} else {
					notfound_notice.setVisibility(View.GONE);
				}
				notifyDataSetChanged();
			}
		}
	}

	private View.OnClickListener clearAllListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			notfound_notice.setVisibility(View.GONE);
			searchText.setText("");
		}
	};

}
