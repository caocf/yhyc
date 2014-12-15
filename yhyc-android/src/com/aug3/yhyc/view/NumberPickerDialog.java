package com.aug3.yhyc.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.aug3.yhyc.R;

public class NumberPickerDialog {

	Context context;
	android.app.AlertDialog ad;
	Button btn_sub, btn_add;
	EditText edit_number;
	Window window = null;

	public int getEditNumber() {
		String num = edit_number.getText().toString();
		if (TextUtils.isEmpty(num)) {
			return 1;
		} else {
			return Integer.parseInt(num);
		}
	}

	public NumberPickerDialog(Context context) {
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		window = ad.getWindow();
		window.setContentView(R.layout.dialog_number_picker);
		btn_sub = (Button) window.findViewById(R.id.numberpicker_sub);
		btn_add = (Button) window.findViewById(R.id.numberpicker_add);
		edit_number = (EditText) window.findViewById(R.id.numberpicker_number);

		btn_sub.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String num = edit_number.getText().toString();

				int number = 1;
				if (TextUtils.isEmpty(num)) {
					edit_number.setText("1");
				} else {
					number = Integer.parseInt(num);
				}

				if (number > 1) {

					edit_number.setText(String.valueOf(number - 1));

				}

			}
		});

		btn_add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String num = edit_number.getText().toString();

				int number = 1;
				if (TextUtils.isEmpty(num)) {
					edit_number.setText("1");
				} else {
					number = Integer.parseInt(num);
				}

				if (number < 100) {

					edit_number.setText(String.valueOf(number + 1));

				}

			}
		});
	}

	public void setInitialNumber(int num) {
		edit_number.setText(String.valueOf(num));
	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(int textResId,
			final View.OnClickListener listener) {

		Button button = (Button) window.findViewById(R.id.numberpicker_ok_btn);
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
				.findViewById(R.id.numberpicker_cancel_btn);
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
