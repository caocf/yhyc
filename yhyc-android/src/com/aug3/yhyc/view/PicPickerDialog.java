package com.aug3.yhyc.view;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aug3.yhyc.R;

public class PicPickerDialog {

	Context context;
	android.app.AlertDialog ad;
	TextView titleView;
	ImageButton btn_loadpic, btn_camera;
	Window window = null;

	public PicPickerDialog(Context context) {
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();
		// �ؼ������������,ʹ��window.setContentView,�滻�����Ի��򴰿ڵĲ���
		window = ad.getWindow();
		window.setContentView(R.layout.dialog_pic_picker);
		titleView = (TextView) window.findViewById(R.id.picdialog_title);

	}

	public void setLoadPicListener(final View.OnClickListener listener) {
		btn_loadpic = (ImageButton) window.findViewById(R.id.picdialog_loadpic);
		btn_loadpic.setOnClickListener(listener);

	}

	public void setCameraListener(final View.OnClickListener listener) {
		btn_camera = (ImageButton) window.findViewById(R.id.picdialog_camera);
		btn_camera.setOnClickListener(listener);
	}

	public void setTitle(int resId) {
		titleView.setText(resId);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	/**
	 * �رնԻ���
	 */
	public void dismiss() {
		ad.dismiss();
	}
}
