package com.aug3.yhyc.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.R;
import com.aug3.yhyc.api.Category;
import com.aug3.yhyc.api.CategoryDTO;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.SystemCache;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ProductCategoryView extends LinearLayout {

	private List<Category> categoryList;

	private Context context;
	private Listener listener;

	private ListView listView;

	private ListAdapter listAdapter;

	private int category;

	private int selected = 0;

	public ProductCategoryView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		context = ctx;
	}

	public void init(final Listener listener, int cat) {

		this.listener = listener;
		this.category = cat;

		categoryList = new ArrayList<Category>();

		listView = (ListView) findViewById(R.id.list_category);
		listView.setCacheColorHint(0);

		listAdapter = new ListAdapter();

		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Category selCat = categoryList.get(position);
				listener.onSelected(selCat);
				selected = selCat.getCode();
				listAdapter.notifyDataSetChanged();

			}
		});

		getCategories(category);

	}

	private void getCategories(final int cat) {

		final SystemCache sc = new SystemCache();

		Map<Integer, List<Category>> categoryMap = null;

		final String cacheKey = "categories";

		if (sc.get(cacheKey) == null) {

			RequestParams p = new RequestParams();

			YhycRestClient.get(Constants.ReqUrl.CATEGORY, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								JSONObject categoriesMap = (JSONObject) response
										.get(Constants.RESP_RESULT);

								CategoryDTO categoryDTO = JSONUtil.fromJson(
										categoriesMap.toString(),
										CategoryDTO.class);

								sc.put(cacheKey, categoryDTO.getCategories(),
										3600);

								categoryList.clear();
								List<Category> catlist = categoryDTO
										.getCategories().get(cat);
								if (catlist != null)
									categoryList.addAll(catlist);

								addCategoryAll();

								listAdapter.notifyDataSetChanged();
							} catch (JSONException e) {
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Log.i("get categories list", throwable.getMessage());
						}
					});

		} else {
			categoryMap = (Map<Integer, List<Category>>) sc.get(cacheKey);

			categoryList.clear();
			categoryList.addAll(categoryMap.get(cat));

			addCategoryAll();

			listAdapter.notifyDataSetChanged();
		}
	}

	private void addCategoryAll() {

		Category all = new Category();
		all.setCode(0);
		all.setName(getResources().getString(R.string.label_category_all));
		all.setP(0);
		categoryList.add(0, all);

		Category promo = new Category();
		promo.setCode(1);
		promo.setName(getResources().getString(R.string.label_category_promo));
		promo.setP(1);
		categoryList.add(1, promo);
	}

	class ListAdapter extends BaseAdapter {

		public ListAdapter() {
			super();
		}

		@Override
		public int getCount() {
			return categoryList.size();
		}

		@Override
		public Object getItem(int position) {
			return categoryList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View vi = convertView;
			SimpleViewHolder holder;
			if (convertView == null) {
				vi = LayoutInflater.from(context).inflate(
						R.layout.list_item_select, null);
				holder = new SimpleViewHolder();
				holder.text = (TextView) vi.findViewById(R.id.listitem_title);
				holder.image = (ImageView) vi
						.findViewById(R.id.listitem_checked);

				vi.setTag(holder);
			} else {
				holder = (SimpleViewHolder) vi.getTag();
			}

			Category cat = categoryList.get(position);
			holder.text.setText(cat.getName());

			if (cat.getCode() == selected) {
				holder.image.setVisibility(View.VISIBLE);
			} else {
				holder.image.setVisibility(View.GONE);
			}

			return vi;

		}
	}

	public interface Listener {

		public void onSelected(Category category);

		public void onScroll(Category category);
	}
}
