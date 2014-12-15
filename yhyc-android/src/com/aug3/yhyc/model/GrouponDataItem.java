package com.aug3.yhyc.model;

public class GrouponDataItem {

	public String title;

	public int imageId;

	// promotion price
	public double pp;

	// market price
	public double mp;

	public GrouponDataItem(String title, int imageId, double pp, double mp) {

		this.title = title;
		this.imageId = imageId;
		this.pp = pp;
		this.mp = mp;
	}

}
