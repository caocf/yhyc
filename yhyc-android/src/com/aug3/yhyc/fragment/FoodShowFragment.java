package com.aug3.yhyc.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.GrouponShowActivity;
import com.aug3.yhyc.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FoodShowFragment extends BaseFragment {

	private View view;

	private static TextView tv_title;

	private PullToRefreshListView mPullToRefreshListView;

	private ArrayList<ListItemData> listItemData = null;

	private String[] mStrings = { "Abbaye de Belloc",
			"Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu",
			"Airag", "Airedale", "Aisy Cendre", "Allgauer Emmentaler" };

	int[] icons = { R.drawable.icon_personal, R.drawable.my_order,
			R.drawable.my_favorite, };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_food_show, container, false);

		initActionBar();

		initFoodshow();

		return view;
	}

	protected void initActionBar() {

		tv_title = (TextView) view.findViewById(R.id.title_text);
		tv_title.setText(R.string.title_love_food);
		view.findViewById(R.id.actionbar_title_expand).setVisibility(
				View.INVISIBLE);

	}

	private void initFoodshow() {
		mPullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.foodshow_list);
		mPullToRefreshListView.setMode(Mode.BOTH);
		ILoadingLayout startLabels = mPullToRefreshListView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("品质生活，从此开始");
		startLabels.setReleaseLabel("品质生活，从此开始");

		listItemData = new ArrayList<ListItemData>();
		for (int i = 0; i < 3; i++) {
			ListItemData itemdata = new ListItemData();
			itemdata.pic = "";
			itemdata.title = "红烧狮子头";
			listItemData.add(itemdata);
		}
		mPullToRefreshListView.setAdapter(listAdapter);

		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.i("pull001", "onPullDownToRefresh");
						// pushDirection = 1;
						new FetchDataTask().execute();

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.i("pull001", "onPullUpToRefresh");
						// pushDirection = -1;
						new FetchDataTask().execute();
						refreshView.post(new Runnable() {
							@Override
							public void run() {
								mPullToRefreshListView.onRefreshComplete();
							}
						});

					}
				});

	}

	private class FetchDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				;
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// 进度条读完需要加载的内容
			// 下面那条是给适配器中的数值增加内容
			ListItemData itemdata = new ListItemData();
			itemdata.pic = "";
			itemdata.title = "红烧鱼";
			listItemData.add(itemdata);

			listAdapter.notifyDataSetChanged();

			mPullToRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}

	}

	private BaseAdapter listAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			ViewHolder holder;
			if (convertView == null) {
				vi = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_foodshow, null);
				holder = new ViewHolder();
				holder.iv_pic = (ImageView) vi.findViewById(R.id.show_iv);
				holder.tv_title = (TextView) vi
						.findViewById(R.id.product_title);

				vi.setTag(holder);
			} else {
				holder = (ViewHolder) vi.getTag();
			}
			ListItemData itemdata = listItemData.get(position);

			holder.tv_title.setText(itemdata.title);

			holder.iv_pic.setOnClickListener(gotoDetailShow());

			return vi;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return listItemData.get(position);
		}

		@Override
		public int getCount() {
			return listItemData.size();
		}
	};

	private View.OnClickListener gotoDetailShow() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(), GrouponShowActivity.class);
				gotoActivity(getActivity(), i);

			}
		};
	}

	private class ViewHolder {
		public ImageView iv_pic;
		public TextView tv_title;
		public TextView tv_pp;
		public TextView tv_mp;
		public TextView tv_countdown;
	}

	private class ListItemData {

		public String pic;
		public String title;
		public double pp;
		public double mp;

	}

}
