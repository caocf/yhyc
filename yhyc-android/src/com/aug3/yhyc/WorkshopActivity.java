package com.aug3.yhyc;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.api.WorkshopDTO;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.Category;
import com.aug3.yhyc.model.Category.CategoryCallback;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.util.JSONUtil;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.AutoScaleViewGroup;
import com.aug3.yhyc.view.ConfirmDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WorkshopActivity extends BaseActivity {

	private long workshop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workshop);

		findViewById(R.id.actionbar_back).setOnClickListener(goBack);

		((TextView) findViewById(R.id.actionbar_title))
				.setText(R.string.label_title_shop_detail);

		Bundle bundle = getIntent().getExtras();
		workshop = bundle.getLong(Constants.Extra.WORKSHOP);

		initShopInfo();

	}

	private void initShopInfo() {

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.WORKSHOP, workshop);

		YhycRestClient.get(Constants.ReqUrl.SHOP_INFO, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						WorkshopDTO shop = null;
						try {
							shop = JSONUtil.fromJson(
									response.getString(Constants.RESP_RESULT),
									WorkshopDTO.class);
						} catch (JSONException e) {
						}

						if (shop != null) {
							showShopInfo(shop);
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Tools.ShowAPIError(WorkshopActivity.this);
					}
				});
	}

	private void showShopInfo(WorkshopDTO shop) {

		String adUrl = shop.getAd();
		if (!TextUtils.isEmpty(adUrl)) {
			AsyncImageLoader.displayImageWithoutRoundCorner(adUrl,
					(ImageView) findViewById(R.id.workshop_ad));
		}

		String pic = shop.getPic();
		if (!TextUtils.isEmpty(pic)) {
			AsyncImageLoader.displayImageWithoutRoundCorner(pic,
					(ImageView) findViewById(R.id.workshop_pic));
		}

		((TextView) findViewById(R.id.workshop_name)).setText(shop.getName());
		((TextView) findViewById(R.id.workshop_address))
				.setText(shop.getAddr());

		((TextView) findViewById(R.id.workshop_notice)).setText(shop
				.getNotice());

		new Category(WorkshopActivity.this, new BusinessTagsDisplay(shop));

		initActionStack(shop.getTel());
	}

	private class BusinessTagsDisplay implements CategoryCallback {

		private WorkshopDTO shop;

		public BusinessTagsDisplay(final WorkshopDTO shop) {
			this.shop = shop;
		}

		@Override
		public void callback(Category category) {

			List<Integer> tags = shop.getCat();

			if (tags != null && !tags.isEmpty()) {
				findViewById(R.id.workshop_tags_tv).setVisibility(View.VISIBLE);
				AutoScaleViewGroup tagslayout = (AutoScaleViewGroup) findViewById(R.id.workshop_tags);
				TextView tv_tag = null;

				for (int tag : tags) {
					tv_tag = new TextView(WorkshopActivity.this);
					tv_tag.setText(category.getCategoryName(tag));
					tv_tag.setTextColor(resources.getColor(R.color.white));
					tv_tag.setBackgroundResource(R.drawable.btn_submit_shape);
					tv_tag.setPadding(6, 2, 6, 2);
					final int cat = tag;
					tv_tag.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							gotoItemList(WorkshopActivity.this, shop.getId(),
									shop.getName(), shop.getNotice(), cat);
						}
					});
					tagslayout.addView(tv_tag);
				}
			} else {
				findViewById(R.id.workshop_tags_tv).setVisibility(View.GONE);
			}
		}

	}

	private void initActionStack(String phone) {

		Button btn = (Button) findViewById(R.id.workshop_dial);
		if (TextUtils.isEmpty(phone)) {
			btn.setVisibility(View.GONE);
		} else {
			if (phone.length() > 8) {
				phone = phone.replace(phone.substring(3, 7), "****");
			}
			btn.setText(phone);
			btn.setOnClickListener(callShopOwner(phone));
		}
	}

	private View.OnClickListener callShopOwner(final String phone) {

		return new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				confirmToCall(phone);

			}
		};

	}

	private void confirmToCall(final String phone) {

		final ConfirmDialog ad = new ConfirmDialog(this);

		ad.setTitle(R.string.label_tips);
		ad.setMessage(R.string.label_tip_confirm_to_call_shop_keeper);
		ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ad.dismiss();

				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phone));
				startActivity(intent);

			}
		});

		ad.setNegativeButton(R.string.btn_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});
	}

}
