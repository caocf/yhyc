package com.aug3.yhyc.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.aug3.yhyc.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AsyncImageLoader {

	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.empty_photo)
			.showImageForEmptyUri(R.drawable.empty_photo)
			.showImageOnFail(R.drawable.empty_photo).cacheInMemory()
			.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).build();

	public static DisplayImageOptions options1 = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.empty_photo)
			.showImageForEmptyUri(R.drawable.empty_photo)
			.showImageOnFail(R.drawable.empty_photo).cacheInMemory()
			.cacheOnDisc().build();

	public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	private static ImageLoader getImageLoader() {
		return ImageLoader.getInstance();
	}

	static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public static void displayImage(String url, ImageView iv) {
		getImageLoader().displayImage(url, iv, options, animateFirstListener);
	}

	public static void displayImageWithoutRoundCorner(String url, ImageView iv) {
		getImageLoader().displayImage(url, iv, options1, animateFirstListener);
	}

}
