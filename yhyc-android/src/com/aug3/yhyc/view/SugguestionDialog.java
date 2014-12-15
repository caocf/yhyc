package com.aug3.yhyc.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.aug3.yhyc.R;

public class SugguestionDialog {

	Context context;
	// android.app.AlertDialog ad;
	Dialog ad;

	public EditText edit_name, edit_mobi, edit_content;

	Window window = null;

	public String getUserName() {
		return edit_name.getText() != null ? edit_name.getText().toString()
				: "";
	}

	public String getUserMobi() {
		return edit_mobi.getText() != null ? edit_mobi.getText().toString()
				: "";
	}

	public String getUserContent() {
		return edit_content.getText() != null ? edit_content.getText()
				.toString() : "";
	}

	public SugguestionDialog(Context context) {
		this.context = context;
		// ad = new android.app.AlertDialog.Builder(context).create();
		ad = new Dialog(context, R.style.dialog);
		ad.show();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		window = ad.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0));
		window.setContentView(R.layout.dialog_sugguestion);

		edit_name = (EditText) window.findViewById(R.id.user_name);
		edit_mobi = (EditText) window.findViewById(R.id.user_mobi);
		edit_content = (EditText) window.findViewById(R.id.user_sugguestion);

	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(int textResId,
			final View.OnClickListener listener) {

		Button button = (Button) window.findViewById(R.id.sugguestion_ok_btn);
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
				.findViewById(R.id.sugguestion_cancel_btn);
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
