package com.aug3.yhyc.widget;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aug3.yhyc.R;
import com.aug3.yhyc.model.GalleryInfo;
import com.aug3.yhyc.util.AsyncImageLoader;
import com.aug3.yhyc.view.SimpleViewHolder;

public class ImageAdapter extends BaseAdapter {

	private ArrayList<GalleryInfo> list;
	private LayoutInflater inflater;

	public ImageAdapter(Context context, ArrayList<GalleryInfo> list) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		SimpleViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gallery_item, null);
			holder = new SimpleViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.tv_image_title);
			holder.image = (ImageView) convertView
					.findViewById(R.id.iv_image_drawable);
			convertView.setTag(holder);
		} else {
			holder = (SimpleViewHolder) convertView.getTag();
		}

		GalleryInfo i = list.get(position);
		holder.text.setText(i.title);
		AsyncImageLoader.displayImage(i.url, holder.image);

		// if (list.get(position).isSelect) {// 被选中的选项加选中的背景框
		// holder.image.setBackgroundResource(R.drawable.gallery_select);
		// } else {// 未被选中的选项设置背景透明
		// holder.image.setBackgroundDrawable(null);
		// }

		return convertView;
	}

	public void changeStatus(int select) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).isSelect = false;
		}

		list.get(select).isSelect = true;
	}

}
