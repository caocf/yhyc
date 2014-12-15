package com.aug3.yhyc;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aug3.yhyc.model.GrouponDataItem;
import com.aug3.yhyc.view.ActionBarView;

public class DailyPromotionActivity extends BaseActivity {

	private ActionBarView ab;

	private GridView gridView;

	private List<GrouponDataItem> grouponDataItems;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_promotion);

		ab = (ActionBarView) findViewById(R.id.title_view);
		ab.setTitleText(R.string.title_cookbook_groupon);

		initGroupon();

	}

	@SuppressLint("NewApi")
	protected void initGroupon() {

		// TODO
		grouponDataItems = new ArrayList<GrouponDataItem>();
		grouponDataItems.add(new GrouponDataItem("水果",
				R.drawable.category_fruit, 1, 2));
		grouponDataItems.add(new GrouponDataItem("水果",
				R.drawable.category_fruit, 1, 2));

		grouponDataItems.add(new GrouponDataItem("蔬菜",
				R.drawable.category_vegetable, 1, 2));
		grouponDataItems.add(new GrouponDataItem("海鲜", R.drawable.category_sea,
				5, 12));
		grouponDataItems.add(new GrouponDataItem("海鲜", R.drawable.category_sea,
				5, 12));
		grouponDataItems.add(new GrouponDataItem("海鲜", R.drawable.category_sea,
				5, 12));
		grouponDataItems.add(new GrouponDataItem("肉类",
				R.drawable.category_bread, 10, 20));
		grouponDataItems.add(new GrouponDataItem("肉类",
				R.drawable.category_bread, 10, 20));
		grouponDataItems.add(new GrouponDataItem("肉类",
				R.drawable.category_bread, 10, 20));

		BaseAdapter gridAdapter = new BaseAdapter() {

			@Override
			public int getCount() {
				return grouponDataItems.size();
			}

			@Override
			public Object getItem(int pos) {
				return grouponDataItems.get(pos);
			}

			@Override
			public long getItemId(int pos) {
				return pos;
			}

			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {

				View v = convertView;
				GrouponItemViewHolder holder;

				if (convertView == null) {
					v = getLayoutInflater().inflate(R.layout.groupon_item,
							parent, false);
					v.setBackgroundColor(getResources().getColor(R.color.white));

					holder = new GrouponItemViewHolder();
					holder.image = (ImageView) v
							.findViewById(R.id.groupon_item_image);
					holder.tv_title = (TextView) v
							.findViewById(R.id.groupon_item_title);
					holder.tv_pp = (TextView) v
							.findViewById(R.id.groupon_item_pp);
					holder.tv_mp = (TextView) v
							.findViewById(R.id.groupon_item_mp);
					holder.btn_order = (Button) v
							.findViewById(R.id.groupon_item_order);

					v.setTag(holder);
				} else {
					holder = (GrouponItemViewHolder) v.getTag();
				}

				GrouponDataItem vi = grouponDataItems.get(pos);

				holder.image.setImageResource(vi.imageId);
				holder.tv_title.setText(vi.title);
				holder.tv_pp.setText("￥" + vi.pp);
				holder.tv_mp.setText("￥" + vi.mp);
				holder.tv_mp.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				holder.btn_order.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(DailyPromotionActivity.this,
								R.string.info_addtocart_success,
								Toast.LENGTH_SHORT).show();

					}
				});

				return v;
			}

		};
		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setAdapter(gridAdapter);

	}

	public class GrouponItemViewHolder {

		public ImageView image;
		public TextView tv_title;
		public TextView tv_pp;
		public TextView tv_mp;
		public Button btn_order;

	}

}
