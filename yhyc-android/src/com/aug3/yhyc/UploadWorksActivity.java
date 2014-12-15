package com.aug3.yhyc;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.ActionBarView;
import com.aug3.yhyc.view.PicPickerDialog;

public class UploadWorksActivity extends BaseActivity {

	private ActionBarView ab;

	private EditText edit_title, edit_ingredients, edit_author;

	private ImageView iv_works;

	private TextView tv_author;

	private Button btn_upload;

	private File works_pic = new File(Tools.getDirectoryDCIM(),
			"yhyc_works.png");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_works);

		initActionBar();

		initView();
	}

	private void initActionBar() {
		ab = (ActionBarView) findViewById(R.id.actionbar_back);
		ab.setTitleText(R.string.title_upload_works);

	}

	private void initView() {

		edit_title = (EditText) findViewById(R.id.works_title);
		edit_ingredients = (EditText) findViewById(R.id.works_ingredients);
		edit_author = (EditText) findViewById(R.id.works_author_edit);
		tv_author = (TextView) findViewById(R.id.works_author_tv);

		iv_works = (ImageView) findViewById(R.id.works_iv);
		iv_works.setOnClickListener(selectPicture);

		btn_upload = (Button) findViewById(R.id.works_upload_btn);
		btn_upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				upload();

			}
		});

	}

	private void upload() {

		String title = edit_title.getText().toString();
		String ingredients = edit_ingredients.getText().toString();
		String author = tv_author.getText().toString();

		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(ingredients)
				|| TextUtils.isEmpty(author)) {
			Tools.ShowTip(this, R.string.label_toast_fillin_all);
		}

		// TODO
	}

	private View.OnClickListener selectPicture = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			final PicPickerDialog ad = new PicPickerDialog(
					UploadWorksActivity.this);

			ad.setTitle(R.string.label_select_pic);
			ad.setLoadPicListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ad.dismiss();

					Intent intentFromGallery = new Intent();
					intentFromGallery.setType("image/*"); // 设置文件类型
					intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intentFromGallery,
							Constants.IMAGE_REQUEST_CODE);

				}
			});

			ad.setCameraListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					ad.dismiss();

					Intent intentFromCapture = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					// 判断存储卡是否可以用，可用进行存储
					if (Tools.hasSdcard()) {

						intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(works_pic));
					}

					startActivityForResult(intentFromCapture,
							Constants.CAMERA_REQUEST_CODE);

				}
			});
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case Constants.IMAGE_REQUEST_CODE:
				startActivityForResult(getPhotoZoomIntent(data.getData()),
						Constants.RESULT_REQUEST_CODE);
				break;
			case Constants.CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					startActivityForResult(
							getPhotoZoomIntent(Uri.fromFile(works_pic)),
							Constants.RESULT_REQUEST_CODE);
				} else {
					Tools.ShowLongTip(UploadWorksActivity.this,
							R.string.label_storage_not_found);
				}
				break;
			case Constants.RESULT_REQUEST_CODE: // 图片缩放完成后, 保存裁剪之后的图片数据
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap photo = extras.getParcelable("data");
						Drawable drawable = new BitmapDrawable(
								this.getResources(), photo);
						iv_works.setBackground(drawable);
						// TODO save pic
					}
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Intent getPhotoZoomIntent(Uri uri) {
		return Tools.getPhotoZoomIntent(uri, 3, 2, 600, 400);
	}

}
