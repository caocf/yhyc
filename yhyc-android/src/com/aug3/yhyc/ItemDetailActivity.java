package com.aug3.yhyc;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.fragment.ItemCommentFragment;
import com.aug3.yhyc.fragment.ShowItemFragment;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.view.MyPagerAdapter;

public class ItemDetailActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private MyPagerAdapter pagerAdapter;

	private ArrayList<Fragment> fragmentsList;
	private TextView tvTabProductinfo, tvTabComment;
	private int currIndex = 0;

	private ImageView iv_fav;
	private boolean fav;

	private Resources resources;

	public TextView shoppingcart_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);

		ActivityMgr.getInstance().addActivity(this);

		resources = getResources();

		initFavIcon();

		InitTextView();

		InitViewPager();

	}

	@Override
	public void onResume() {
		super.onResume();
		shoppingcart_number.setText(String.valueOf(UserPreference.getInstance()
				.getCart().size()));
	}

	@SuppressLint("NewApi")
	private void initFavIcon() {

		((ImageView) findViewById(R.id.actionbar_item_back))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					}
				});

		Bundle bundle = this.getIntent().getExtras();
		final long itemid = bundle.getLong(Constants.Extra.ITEMID);

		iv_fav = (ImageView) findViewById(R.id.actionbar_item_fav);

		if (UserPreference.getInstance().getFav().contains(itemid)) {
			iv_fav.setImageResource(R.drawable.ic_heart_h);
			fav = true;
		} else {
			iv_fav.setImageResource(R.drawable.ic_heart);
			fav = false;
		}

		iv_fav.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!fav) {
					iv_fav.setImageResource(R.drawable.ic_heart_h);
					UserPreference.getInstance().getFav().add(itemid);
				} else {
					iv_fav.setImageResource(R.drawable.ic_heart);
					UserPreference.getInstance().getFav().remove(itemid);
				}
				UserPreference.getInstance().setFavUpdated(true);

				fav = !fav;

			}
		});
	}

	private void InitTextView() {

		findViewById(R.id.actionbar_shoppingcart).setOnClickListener(
				gotoShoppingcart);

		shoppingcart_number = ((TextView) findViewById(R.id.actionbar_shoppingcart_number));

		tvTabProductinfo = (TextView) findViewById(R.id.tv_tab_product_info);
		tvTabComment = (TextView) findViewById(R.id.tv_tab_product_comment);

		tvTabProductinfo.setOnClickListener(new TabOnClickListener(0));
		tvTabComment.setOnClickListener(new TabOnClickListener(1));

	}

	private View.OnClickListener gotoShoppingcart = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ItemDetailActivity.this,
					ShoppingCartActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);

		}
	};

	private void InitViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.productdetail_pager);

		fragmentsList = new ArrayList<Fragment>();
		fragmentsList.add(new ShowItemFragment());
		fragmentsList.add(new ItemCommentFragment());

		pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
				fragmentsList);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(onPageChgListener);
	}

	public class TabOnClickListener implements View.OnClickListener {
		private int index = 0;

		public TabOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(index);
		}
	};

	private ViewPager.OnPageChangeListener onPageChgListener = new ViewPager.OnPageChangeListener() {

		@SuppressLint("NewApi")
		@Override
		public void onPageSelected(int pos) {
			switch (pos) {
			case 0:
				if (currIndex == 1) {
					tvTabComment.setTextColor(resources
							.getColor(R.color.font_head));
					tvTabComment
							.setBackgroundResource(R.drawable.btn_right_tab_nor_shape);
				}
				tvTabProductinfo
						.setTextColor(resources.getColor(R.color.white));
				tvTabProductinfo
						.setBackgroundResource(R.drawable.btn_left_tab_sel_shape);
				break;
			case 1:
				if (currIndex == 0) {
					tvTabProductinfo.setTextColor(resources
							.getColor(R.color.font_head));
					tvTabProductinfo
							.setBackgroundResource(R.drawable.btn_left_tab_nor_shape);
				}
				tvTabComment.setTextColor(resources.getColor(R.color.white));
				tvTabComment
						.setBackgroundResource(R.drawable.btn_right_tab_sel_shape);
				break;
			}
			currIndex = pos;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

}
