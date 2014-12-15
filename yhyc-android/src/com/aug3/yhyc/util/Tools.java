package com.aug3.yhyc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.aug3.yhyc.R;
import com.aug3.yhyc.base.Constants;

public class Tools {

	public static final SimpleDateFormat iso_timestamp_formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm", Locale.CHINA);

	public static boolean isLetter(String charString) {
		return charString.matches("^[a-zA-Z]*");
	}

	public static File getDirectoryDCIM() {
		return Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	}

	public static void ShowTip(Context context, int resID) {
		Toast toast = Toast.makeText(context, resID, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 20);
		toast.show();
	}

	public static void ShowTip(Context context, String tip) {
		Toast toast = Toast.makeText(context, tip, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 20);
		toast.show();
	}

	public static void ShowLongTip(Context context, String tip) {
		Toast toast = Toast.makeText(context, tip, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 20);
		toast.show();
	}

	public static void ShowLongTip(Context context, int resID) {
		Toast toast = Toast.makeText(context, resID, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 20);
		toast.show();
	}

	public static void ShowAPIError(Context context) {
		Toast toast = Toast.makeText(context, R.string.label_toast_api_error,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 20);
		toast.show();
	}

	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;

			left = 0;
			top = 0;
			right = width;
			bottom = width;

			height = width;

			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;

			float clip = (width - height) / 2;

			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;

			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

		return output;
	}

	public static void saveBitmap(String fn, Bitmap bitmap) {
		File f = new File(fn);
		try {
			f.createNewFile();
		} catch (IOException e1) {
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
		}

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		try {
			fos.flush();
		} catch (IOException e) {
		}
		try {
			fos.close();
		} catch (IOException e) {
		}

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static Intent getPhotoZoomIntent(Uri uri, int aspectX, int aspectY,
			int outputX, int outputY) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);
		return intent;
	}

	public static Intent getPhotoZoomIntent(Uri uri) {
		return getPhotoZoomIntent(uri, 1, 1, 300, 300);

	}

	/**
	 * 获取联系人电话
	 * 
	 * @param cursor
	 * @return
	 */
	public static String getContactPhone(Context context, Cursor cursor) {

		int phoneColumn = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String result = "";
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人电话的cursor
			Cursor phone = context.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
							+ contactId, null, null);
			if (phone.moveToFirst()) {
				for (; !phone.isAfterLast(); phone.moveToNext()) {
					int index = phone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex = phone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type = phone.getInt(typeindex);
					String phoneNumber = phone.getString(index);
					result = phoneNumber;
					switch (phone_type) {
					case 2:
						result = phoneNumber;
						break;

					default:
						break;
					}
				}
				if (!phone.isClosed()) {
					phone.close();
				}
			}
		}
		return result;
	}

	public static List<Long> transfer2ListLong(String commaSeparated) {
		String[] array = TextUtils.split(commaSeparated, Constants.COMMA);
		List<Long> list = new ArrayList<Long>();
		for (String s : array) {
			list.add(Long.parseLong(s));
		}
		return list;
	}

	public static void setImageResource(ImageView iv, String pic,
			ScaleType scaleType) {

		if (!TextUtils.isEmpty(pic)) {
			if (pic.contains("ad001")) {
				iv.setBackgroundResource(R.drawable.ad001);
			} else if (pic.contains("ad002")) {
				iv.setBackgroundResource(R.drawable.ad002);
			} else if (pic.contains("ad003")) {
				iv.setBackgroundResource(R.drawable.ad003);
			} else {
				AsyncImageLoader.displayImageWithoutRoundCorner(pic, iv);
			}
			if (scaleType != null) {
				iv.setScaleType(scaleType);
			}
		}

	}

}
