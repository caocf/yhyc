package com.aug3.yhyc.model;

public class GalleryInfo {

	public String title;
	public String url;
	public int drawable;
	public boolean isSelect;

	public GalleryInfo(int drawable, String title, boolean isSelected) {
		this.title = title;
		this.drawable = drawable;
		this.isSelect = isSelected;
	}

	public GalleryInfo(String url, String title, boolean isSelected) {
		this.title = title;
		this.url = url;
		this.isSelect = isSelected;
	}
}