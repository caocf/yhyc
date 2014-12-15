package com.aug3.yhyc;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.aug3.yhyc.fragment.FoodShowFragment;
import com.aug3.yhyc.fragment.HomeFragment;
import com.aug3.yhyc.fragment.MyaccFragment;
import com.aug3.yhyc.http.ServiceAgent;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.view.ConfirmDialog;

public class MainActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;

	private ImageView iv_shoppingcart;
	private TextView tv_notice;

	// 定义一个布局
	private LayoutInflater layoutInflater;

	// 定义数组来存放Fragment界面
	private Class fragmentArray[] = { HomeFragment.class,
			 MyaccFragment.class };//FoodShowFragment.class,

	// 定义数组来存放按钮图片
	private int mImageViewArray[] = { R.drawable.tab_home_btn,
			 R.drawable.tab_personal_btn };//R.drawable.tab_caipu_btn,

	private Resources resources;

	// Tab选项卡的文字
	private int mTextviewArray[] = { R.string.title_firstpage,
			 R.string.title_my };//R.string.title_love_food,

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		resources = getResources();

		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();

		int size = UserPreference.getInstance().getCart().size();
		if (size > 0) {
			tv_notice.setVisibility(View.VISIBLE);
			tv_notice.setText(String.valueOf(size));
		} else {
			tv_notice.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		layoutInflater = LayoutInflater.from(this);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(
					resources.getString(mTextviewArray[i])).setIndicator(
					getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			// 设置Tab按钮的背景
			// mTabHost.getTabWidget().getChildAt(i)
			// .setBackgroundResource(R.drawable.selector_tab_background);
		}

		// int i = getIntent().getIntExtra(Constants.DISPLAY_TAB_X, 0);

		// if (i == Constants.DISPLAY_TAB_MYSTORE) {
		// mTabHost.setCurrentTab(i);
		// }

		iv_shoppingcart = (ImageView) findViewById(R.id.main_shoppingcart);
		iv_shoppingcart.setOnClickListener(gotoShoppingCart());

		tv_notice = (TextView) findViewById(R.id.main_shoppingcart_notice);

	}

	private View.OnClickListener gotoShoppingCart() {

		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;

				intent = new Intent(MainActivity.this,
						ShoppingCartActivity.class);

				startActivity(intent);

				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);

			}
		};
	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.view_home_tab_item, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);

		return view;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exitAlert();

		}
		return true;
	}

	private void exitAlert() {
		final ConfirmDialog ad = new ConfirmDialog(this);

		ad.setTitle(R.string.label_tips);
		ad.setMessage(R.string.label_tip_confirm_to_exit);
		ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ad.dismiss();

				// 结束这个Activity
				MainActivity.this.finish();
				onDestroy();

			}
		});

		ad.setNegativeButton(R.string.btn_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});
	}

	@Override
	protected void onPause() {

		super.onPause();

		updatePrefs();

	}

	private void updatePrefs() {

		ServiceAgent.updateFav();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

}
