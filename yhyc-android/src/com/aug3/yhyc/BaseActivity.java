package com.aug3.yhyc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.aug3.yhyc.base.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseActivity extends Activity {

	protected Resources resources;

	protected SharedPreferences sp;

	protected String currency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		resources = getResources();

		currency = resources.getString(R.string.label_rmb);

		sp = getSharedPreferences(Constants.SharedPrefs.YHYC,
				Context.MODE_PRIVATE);

		ActivityMgr.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		ActivityMgr.getInstance().delete(this);
		super.onDestroy();
	}

	public View.OnClickListener goBack = new OnClickListener() {
		@Override
		public void onClick(View v) {
			closeWindow();
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			closeWindow();
		}
		return true;
	}

	public void closeWindow() {
		finish();
		myOverridePendingTransition();
	}

	public void gotoActivity(Intent intent) {
		startActivity(intent);
		myOverridePendingTransition();
	}

	public void myOverridePendingTransition() {
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	// //////////////////////////////
	// universal image loader usage
	// //////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.imglistview_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_clear_memory_cache:
			ImageLoader.getInstance().clearMemoryCache();
			return true;
		case R.id.item_clear_disc_cache:
			ImageLoader.getInstance().clearDiscCache();
			return true;
		default:
			return false;
		}
	}

	public void bindClearEventToEditText(final Button clearBtn,
			final EditText editText) {

		clearBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(editText.getText())) {
					editText.setText("");
					clearBtn.setVisibility(View.INVISIBLE);
				}

			}

		});

		editText.addTextChangedListener(new TextWatcher() {

			// 缓存上一次文本框内是否为空
			private boolean isnull = true;

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					if (!isnull) {
						isnull = true;
					}
				} else {
					if (isnull) {
						clearBtn.setVisibility(View.VISIBLE);
						isnull = false;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			/**
			 * 随着文本框内容改变动态改变列表内容
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		});

		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (!TextUtils.isEmpty(editText.getText())) {
						clearBtn.setVisibility(View.VISIBLE);
					}
				} else {
					clearBtn.setVisibility(View.INVISIBLE);
				}

			}
		});

	}

	public void gotoItemList(Context context, long workshop, String shopName,
			String notice, int category) {

		Intent intent = new Intent(context, ItemListActivity.class);

		intent.putExtra(Constants.Extra.WORKSHOP, workshop);
		intent.putExtra(Constants.Extra.SHOP_NAME, shopName);
		intent.putExtra(Constants.Extra.SHOP_NOTICE, notice);
		intent.putExtra(Constants.Extra.PRODUCT_CAT_ID, category);
		gotoActivity(intent);
	}

	public void gotoWorkshop(Context context, long workshop) {

		Intent i = new Intent(context, WorkshopActivity.class);
		i.putExtra(Constants.Extra.WORKSHOP, workshop);
		gotoActivity(i);
	}

}
