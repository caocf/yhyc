package com.aug3.yhyc.model;

import java.util.List;

public class WorkshopListItem {

	public long id;
	public String title;
	public String subtitle;
	public String notice;
	public String phone;
	public List<Integer> cat;

	// change this to pic string and access from http
	public String image;

	public WorkshopListItem(long id, String title, String subtitle,
			String image, String notice, List<Integer> cat, String phone) {
		super();
		this.id = id;
		this.title = title;
		this.subtitle = subtitle;
		this.image = image;
		this.notice = notice;
		this.cat = cat;
		this.phone = phone;
	}

}
