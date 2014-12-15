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
		// 设置WebView属性，能够执行JavaScript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.requestFocus();
		//webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 取消滚动条

		// 加载URL内容
		webview.loadUrl(url);
		// 设置web视图客户端
		webview.setWebViewClient(new MyWebViewClient());
	}

	/**
	 * 设置回退
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * web视图客户端
	 */
	public class MyWebViewClient extends WebViewClient {
		
		public boolean shouldOverviewUrlLoading(WebView view, String url) {
			
			// 设置点击网页里面的链接还是在当前的webview里跳转
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				android.net.http.SslError error) {
			
			// 设置webview处理https请求
			handler.proceed();
		}

		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			
			// 加载页面报错时的处理
			Tools.ShowTip(WebViewActivity.this, "Oh no! " + description);
		}
	}
}
