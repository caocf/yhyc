package com.aug3.yhyc.model;

import java.util.List;

public class WorkshopGroupItem extends GroupListItemBase {

	private List<Integer> cat;
	private String notice;

	public List<Integer> getCat() {
		return cat;
	}

	public void setCat(List<Integer> cat) {
		this.cat = cat;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}
