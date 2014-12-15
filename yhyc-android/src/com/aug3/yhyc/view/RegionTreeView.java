package com.aug3.yhyc.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
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
import com.aug3.yhyc.api.Region;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.SystemCache;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegionTreeView extends LinearLayout {

	private SparseArray<List<Region>> treeData;

	private Context context;
	private Listener listener;

	private ListView groupListView, childListView;

	private GroupAdapter gAdapter;
	private ChildAdapter cAdapter;

	private int gPosSelected;

	private static final int parent_level = 0;

	private Resources resources;

	public RegionTreeView(Context c, AttributeSet attrs) {
		super(c, attrs);
		context = c;
		resources = context.getResources();
	}

	public void init(final Listener listener) {

		this.listener = listener;

		getCity();

	}

	private void getCity() {

		final SystemCache sc = new SystemCache();
		final String cacheKey = Constants.CacheKey.REGION.getName();

		if (sc.containsKey(cacheKey)) {
			List<Region> regionlist = (ArrayList<Region>) sc.get(cacheKey);
			fillRegionTreeData(regionlist);
		} else {

			RequestParams p = new RequestParams();

			YhycRestClient.get(Constants.ReqUrl.REGION, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							final List<Region> regionlist = new ArrayList<Region>();

							try {
								JSONArray regionArray = (JSONArray) response
										.get(Constants.RESP_RESULT);
								int len = regionArray.length();

								for (int i = 0; i < len; i++) {
									Region reg = JSONUtil.fromJson(regionArray
											.get(i).toString(), Region.class);
									regionlist.add(reg);
								}

								sc.put(cacheKey, regionlist, 300);

								fillRegionTreeData(regionlist);
							} catch (JSONException e) {
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Log.i("city tree", throwable.getMessage());
						}
					});

		}

	}

	private void fillRegionTreeData(List<Region> regions) {

		treeData = new SparseArray<List<Region>>();

		for (Region reg : regions) {
			int pid = reg.getP();
			List<Region> list = treeData.get(pid);
			if (list == null) {
				list = new ArrayList<Region>();
				list.add(reg);
				treeData.put(pid, list);
			} else {
				treeData.get(pid).add(reg);
			}
		}
		initTreeView();
	}

	private void initTreeView() {

		groupListView = (ListView) findViewById(R.id.list_group);
		groupListView.setCacheColorHint(0);
		gAdapter = new GroupAdapter(treeData.get(parent_level));
		groupListView.setAdapter(gAdapter);

		childListView = (ListView) findViewById(R.id.list_child);
		cAdapter = new ChildAdapter(new ArrayList<Region>());
		childListView.setAdapter(cAdapter);
		List<Region> childCategoryList = treeData.get(((Region) treeData.get(
				parent_level).get(0)).getId());
		cAdapter.setChildrenList(childCategoryList);
		cAdapter.notifyDataSetChanged();

		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				gPosSelected = position;
				List<Region> childList = treeData.get(((Region) treeData.get(
						parent_level).get(position)).getId());
				cAdapter.setChildrenList(childList);
				cAdapter.notifyDataSetChanged();

				gAdapter.setmCurSelectPosition(position);
				gAdapter.notifyDataSetChanged();

			}
		});

		childListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listener.onSelected(cAdapter.getChildrenList().get(position),
						treeData.get(parent_level).get(gPosSelected));
			}
		});
	}

	private class ViewHolder {
		public ImageView imgView;
		public TextView textView;
	}

	class GroupAdapter extends BaseAdapter {

		private List<Region> topList;
		private ViewHolder mSelectHolder;
		private int mCurSelectPosition = 0;

		public GroupAdapter(List<Region> topList) {
			this.topList = topList;
		}

		public ViewHolder getmSelectHolder() {
			return mSelectHolder;
		}

		public void setmSelectHolder(ViewHolder mSelectHolder) {
			this.mSelectHolder = mSelectHolder;
		}

		public int getmCurSelectPosition() {
			return mCurSelectPosition;
		}

		public void setmCurSelectPosition(int mCurSelectPosition) {
			this.mCurSelectPosition = mCurSelectPosition;
		}

		@Override
		public int getCount() {
			if (topList != null) {
				return topList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (topList != null && topList.size() > position - 1) {
				return topList.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				LayoutInflater layoutInflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = layoutInflater.inflate(R.layout.tree_tier1, null);
				viewHolder = new ViewHolder();

				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.category_item_title);
				viewHolder.imgView = (ImageView) convertView
						.findViewById(R.id.img_tag);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (position == mCurSelectPosition) {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.address_child));
				updateItemUI(viewHolder.imgView, true);
			} else {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.white));
				updateItemUI(viewHolder.imgView, false);
			}
			Region city = topList.get(position);
			viewHolder.textView.setText(city.getName());
			return convertView;
		}
	}

	private void updateItemUI(ImageView imgView, boolean b) {
		if (b) {
			imgView.setVisibility(View.VISIBLE);
		} else {
			imgView.setVisibility(View.INVISIBLE);
		}
	}

	class ChildAdapter extends BaseAdapter {

		private List<Region> childrenList;

		public List<Region> getChildrenList() {
			return childrenList;
		}

		public void setChildrenList(List<Region> categoryList) {
			this.childrenList = categoryList;
		}

		public ChildAdapter(List<Region> childList) {
			super();
			this.childrenList = childList;
		}

		@Override
		public int getCount() {
			if (childrenList != null) {
				return childrenList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			if (childrenList != null && childrenList.size() > position - 1) {
				return childrenList.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater layoutInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = layoutInflater.inflate(R.layout.tree_tier2, null);
			}

			TextView childTextView = (TextView) view
					.findViewById(R.id.category_item_tv);
			Region childNode = childrenList.get(position);

			childTextView.setText(childNode.getName());
			if (childNode.getSts() == 1) {
				childTextView.setTextColor(resources
						.getColor(R.color.font_default));
			} else {
				childTextView.setTextColor(resources
						.getColor(R.color.font_note));
			}
			return view;
		}
	}

	public interface Listener {

		public void onSelected(Region list, Region parentList);

		public void onScroll(Region list);
	}
}
