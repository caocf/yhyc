package com.aug3.yhyc.valueobj;

public class Item {

	// workshopid + 分类号+流水号
	private long id;

	private String name;

	// 类目
	private String category;

	// 库存
	private long inventory;

	// 规格
	private String spec;

	// 橱窗图片
	private String pic;

	// 卖家id
	private String sid;

	// 收藏人数
	private long fav;

	// 卖出数
	private long sales;

	// 促销价
	private double pp;

	// 市场价
	private double mp;

	// 积分
	private int ac;

	// 产地
	private String origin;

	// 品牌
	private String brand;

	// 描述
	private String desc;

	// 图片组
	private String url;

	// 状态
	private int status;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getInventory() {
		return inventory;
	}

	public void setInventory(long inventory) {
		this.inventory = inventory;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public long getFav() {
		return fav;
	}

	public void setFav(long fav) {
		this.fav = fav;
	}

	public long getSales() {
		return sales;
	}

	public void setSales(long sales) {
		this.sales = sales;
	}

	public double getMp() {
		return mp;
	}

	public void setMp(double mp) {
		this.mp = mp;
	}

	public double getPp() {
		return pp;
	}

	public void setPp(double pp) {
		this.pp = pp;
	}

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
