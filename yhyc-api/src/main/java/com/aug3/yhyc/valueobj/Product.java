package com.aug3.yhyc.valueobj;

import java.util.List;

public class Product {

	// 分类号+流水号
	private long id;

	private String name;

	// 类目
	private String cat;

	// tags
	private List<Integer> tags;

	// 推荐
	private List<Long> ref;

	// 橱窗图片
	private String pic;

	// 产地
	private String orig;

	// 品牌
	private String brand;

	// 描述
	private List<Intro> desc;

	// 状态
	private int sts;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public List<Integer> getTags() {
		return tags;
	}

	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}

	public List<Long> getRef() {
		return ref;
	}

	public void setRef(List<Long> ref) {
		this.ref = ref;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getOrig() {
		return orig;
	}

	public void setOrig(String orig) {
		this.orig = orig;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public List<Intro> getDesc() {
		return desc;
	}

	public void setDesc(List<Intro> desc) {
		this.desc = desc;
	}

	public int getSts() {
		return sts;
	}

	public void setSts(int sts) {
		this.sts = sts;
	}

	public class Intro {
		// 图片描述
		private String exp;
		// 图片url
		private String pic;

		public String getExp() {
			return exp;
		}

		public void setExp(String exp) {
			this.exp = exp;
		}

		public String getPic() {
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

	}

	public enum PTag {
		DIET(1), ;

		private int code;

		private PTag(int code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}
	}

}
