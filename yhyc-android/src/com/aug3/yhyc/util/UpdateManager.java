package com.aug3.yhyc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.aug3.yhyc.R;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.view.ConfirmDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UpdateManager {

	private ProgressDialog pBar;
	private Context context;

	private UpdateCallBack cb;

	public UpdateManager(Context context, UpdateCallBack cb) {
		this.context = context;
		this.cb = cb;

	}

	public interface UpdateCallBack {
		public abstract void doAction();
	}

	public void checkUpdate() {

		final int ver = UserPreference.getInstance().getVer();
		final String curVerName = UserPreference.getInstance().getVerName();

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.VERSION, ver);

		YhycRestClient.get(Constants.ReqUrl.APP_INFO, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {

							JSONObject app = (JSONObject) response
									.get(Constants.RESP_RESULT);

							if (app != null) {

								int verCode = app.getInt("verCode");
								String verName = app.getString("verName");
								String url = app.getString("url");
								String changelog = app.getString("changelog");

								if (verCode > 0) {

									if (ver != -1 && verCode > ver) {

										if (!TextUtils.isEmpty(url)) {
											doNewVersionUpdate(curVerName,
													verName, url, changelog);
											return;
										}

									}
								}
							}

							cb.doAction();

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						Tools.ShowAPIError(context);

					}
				});

	}

	private void doNewVersionUpdate(String curVersionName,
			final String verName, final String url, final String changelog) {

		final ConfirmDialog ad = new ConfirmDialog(context);

		ad.setTitle(R.string.label_tips);
		ad.setMessage(new StringBuffer()
				.append("当前版本:")
				.append(curVersionName)
				.append(", 发现新版本:")
				.append(verName)
				.append("，是否更新？")
				.append(TextUtils.isEmpty(changelog) ? "" : "新增修改：\n【"
						+ changelog + "】").toString());

		ad.setPositiveButton(R.string.btn_OK, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();

				pBar = new ProgressDialog(context);
				pBar.setTitle("正在下载");
				pBar.setMessage("请稍候...\n如下载或安装出现问题，请卸载应用，重新安装");
				pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				downFile(url);

			}
		});

		ad.setNegativeButton(R.string.btn_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
				cb.doAction();
			}
		});
	}

	private void downFile(final String url) {

		pBar.show();

		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;

				FileOutputStream fileOutputStream = null;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					// long length = entity.getContentLength();
					InputStream is = entity.getContent();

					String fname = url.substring(url.lastIndexOf("/") + 1);
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								fname);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						// int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							// count += ch;
							// if (length > 0) {
							// }
						}
					}
					fileOutputStream.flush();

					updateAfterDownload(fname);
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				} finally {
					if (fileOutputStream != null) {
						try {
							fileOutputStream.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}.start();
	}

	private void updateAfterDownload(final String fname) {
		mHandler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update(fname);
			}
		});
	}

	private void update(final String fname) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), fname)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	private Handler mHandler = new Handler();

}
