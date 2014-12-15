package com.aug3.yhyc;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;

public class WebViewActivity extends BaseActivity {

	private ActionBarView ab;

	/** Called when the activity is first created. */
	private WebView webview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		Bundle bundle = getIntent().getExtras();
		int title = bundle.getInt(Constants.Extra.TITLE, 0);
		String url = bundle.getString(Constants.Extra.URL);

		ab = (ActionBarView) findViewById(R.id.actionbar_back);
		if (title == 0) {
			ab.setTitleText(R.string.app_name);
		} else {
			ab.setTitleText(title);
		}

		webview = (WebView) findViewById(R.id.webview);
		// ����WebView���ԣ��ܹ�ִ��JavaScript�ű�
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.requestFocus();
		//webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // ȡ��������

		// ����URL����
		webview.loadUrl(url);
		// ����web��ͼ�ͻ���
		webview.setWebViewClient(new MyWebViewClient());
	}

	/**
	 * ���û���
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * web��ͼ�ͻ���
	 */
	public class MyWebViewClient extends WebViewClient {
		
		public boolean shouldOverviewUrlLoading(WebView view, String url) {
			
			// ���õ����ҳ��������ӻ����ڵ�ǰ��webview����ת
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				android.net.http.SslError error) {
			
			// ����webview����https����
			handler.proceed();
		}

		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			
			// ����ҳ�汨��ʱ�Ĵ���
			Tools.ShowTip(WebViewActivity.this, "Oh no! " + description);
		}
	}
}
