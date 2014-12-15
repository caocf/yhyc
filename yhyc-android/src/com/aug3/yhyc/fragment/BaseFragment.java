package com.aug3.yhyc.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.aug3.yhyc.ItemDetailActivity;
import com.aug3.yhyc.WorkshopListActivity;
import com.aug3.yhyc.base.Constants;

public abstract class BaseFragment extends Fragment {

	public void gotoActivity(FragmentActivity activity, Intent intent) {

		activity.startActivity(intent);
		activity.overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);

	}

	public void startItemDetailActivity(FragmentActivity activity, long itemid) {

		Intent intent = new Intent(activity, ItemDetailActivity.class);

		intent.putExtra(Constants.Extra.ITEMID, itemid);

		gotoActivity(activity, intent);
	}

	public void startWorkshopListActivity(FragmentActivity activity, long shequ, int catID,
			String categoryText) {

		Intent intent = new Intent(activity, WorkshopListActivity.class);

		intent.putExtra(Constants.Extra.SHEQU, shequ);
		intent.putExtra(Constants.Extra.PRODUCT_CAT_ID, catID);
		intent.putExtra(Constants.Extra.CATEGORYTEXT, categoryText);

		gotoActivity(activity, intent);
	}

}
