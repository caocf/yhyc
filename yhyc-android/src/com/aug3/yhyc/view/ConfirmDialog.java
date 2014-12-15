package com.aug3.yhyc.view;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.aug3.yhyc.R;

public class ConfirmDialog {

	Context context;
	android.app.AlertDialog ad;
	TextView titleView;
	TextView messageView;
	Window window = null;

	public ConfirmDialog(Context context) {
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		window = ad.getWindow();
		window.setContentView(R.layout.dialog_confirm);
		titleView = (TextView) window.findViewById(R.id.confirmdialog_title);
		messageView = (TextView) window
				.findViewById(R.id.confirmdialog_message);
	}

	public void setTitle(int resId) {
		titleView.setText(resId);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setMessage(int resId) {
		messageView.setText(resId);
	}

	public void setMessage(String message) {
		messageView.setText(message);
	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(int textResId,
			final View.OnClickListener listener) {

		Button button = (Button) window.findViewById(R.id.confirmdialog_btn_ok);
		button.setText(textResId);
		button.setOnClickListener(listener);
	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(int textResId,
			final View.OnClickListener listener) {

		Button button = (Button) window
				.findViewById(R.id.confirmdialog_btn_cancel);
		button.setText(textResId);
		button.setOnClickListener(listener);

	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}
}
