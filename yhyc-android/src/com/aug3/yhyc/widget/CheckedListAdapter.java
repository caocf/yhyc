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
import com.aug3.yhyc.model.ListItemChecked;
import com.aug3.yhyc.view.SimpleViewHolder;

public class CheckedListAdapter extends BaseAdapter {

	private ArrayList<ListItemChecked> listItemData;
	private Context context;

	public CheckedListAdapter(Context ctx, ArrayList<ListItemChecked> itemsdata) {
		this.listItemData = itemsdata;
		this.context = ctx;
	}

	public ArrayList<ListItemChecked> getListItemData() {
		return listItemData;
	}

	public void setListItemData(ArrayList<ListItemChecked> listItemData) {
		this.listItemData = listItemData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		SimpleViewHolder holder;
		if (convertView == null) {
			vi = LayoutInflater.from(context).inflate(
					R.layout.list_item_select, null);
			holder = new SimpleViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.listitem_title);
			holder.image = (ImageView) vi.findViewById(R.id.listitem_checked);

			vi.setTag(holder);
		} else {
			holder = (SimpleViewHolder) vi.getTag();
		}

		ListItemChecked itemdata = listItemData.get(position);
		holder.text.setText(itemdata.getName());
		holder.image.setVisibility(itemdata.isChecked() ? View.VISIBLE
				: View.INVISIBLE);

		return vi;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		if (listItemData == null)
			return null;
		return listItemData.get(position);
	}

	@Override
	public int getCount() {
		if (listItemData == null)
			return 0;
		return listItemData.size();
	}

}
