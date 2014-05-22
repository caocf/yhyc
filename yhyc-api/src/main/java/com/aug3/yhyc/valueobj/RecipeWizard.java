package com.aug3.yhyc.valueobj;

import java.io.Serializable;

public class RecipeWizard implements Serializable, Comparable {

	private int seq;
	private String pic;
	private String desc;

	public RecipeWizard(int seq, String pic, String desc) {
		this.seq = seq;
		this.pic = pic;
		this.desc = desc;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int compareTo(Object o) {
		RecipeWizard wiz = (RecipeWizard) o;
		return this.seq - wiz.getSeq();
	}

}
