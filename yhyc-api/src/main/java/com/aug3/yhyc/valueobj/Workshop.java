package com.aug3.yhyc.valueobj;

import java.util.List;

public class Workshop {

	private long id;

	private String name;

	// 店铺图片
	private String pic;

	// 营业执照或组织机构代码
	private String regi;

	// 保证金
	private double margin;

	// 服务打分
	private double service;

	// 送货速度打分
	private double speed;

	// 商品质量
	private double quality;

	private String owner;

	private String idcard;

	private String city;

	private String dist;

	private String addr;

	private String tel;

	private String start;

	private List<Long> shequ;

	private List<Integer> cat;

	// 状态 : 1 - 正常 0 - 整装待发
	private String sts;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getRegi() {
		return regi;
	}

	public void setRegi(String regi) {
		this.regi = regi;
	}

	public double getMargin() {
		return margin;
	}

	public void setMargin(double margin) {
		this.margin = margin;
	}

	public double getService() {
		return service;
	}

	public void setService(double service) {
		this.service = service;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getQuality() {
		return quality;
	}

	public void setQuality(double quality) {
		this.quality = quality;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public List<Long> getShequ() {
		return shequ;
	}

	public void setShequ(List<Long> shequ) {
		this.shequ = shequ;
	}

	public List<Integer> getCat() {
		return cat;
	}

	public void setCat(List<Integer> cat) {
		this.cat = cat;
	}

	public String getSts() {
		return sts;
	}

	public void setSts(String sts) {
		this.sts = sts;
	}

}
