package com.aug3.yhyc.model;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.SparseArray;

import com.aug3.yhyc.api.Classification;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.util.SystemCache;
import com.aug3.yhyc.util.Tools;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Category {

	public interface CategoryCallback {
		public void callback(Category cateogry);
	}

	private SparseArray<Classification> categorylist;

	public Category(Context ctx, CategoryCallback cb) {
		initCategoryView(ctx, cb);
	}

	public String getCategoryName(int categoryID) {

		return categorylist.get(categoryID).getName();
	}

	private void initCategoryView(final Context ctx, final CategoryCallback cb) {

		final SystemCache sc = new SystemCache();
		final String cacheKey = Constants.CacheKey.CLASSIFICATION_MAP.getName();

		if (sc.containsKey(cacheKey)) {

			categorylist = (SparseArray<Classification>) sc.get(cacheKey);
			cb.callback(this);

		} else {
			RequestParams p = new RequestParams();

			YhycRestClient.get(Constants.ReqUrl.SHOP_GOODS_CLASSIFICATION, p,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							try {
								JSONArray cats = (JSONArray) response
										.get(Constants.RESP_RESULT);

								int len = cats.length();
								SparseArray<Classification> catMap = new SparseArray<Classification>();

								for (int i = 0; i < len; i++) {
									JSONObject cat = (JSONObject) cats.get(i);

									Classification classify = new Classification();
									classify.setCode(cat.getInt("code"));
									classify.setName(cat.getString("name"));
									classify.setPic(cat.getString("pic"));

									catMap.put(classify.getCode(), classify);
								}
								sc.put(cacheKey, catMap);

								categorylist = catMap;

								cb.callback(Category.this);

							} catch (JSONException e) {
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {

							Tools.ShowAPIError(ctx);
						}
					});
		}

	}

}
