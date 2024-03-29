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

	private long owner;

	private List<Long> emp;

	private String idcard;

	private int city;

	private int dist;

	private String addr;

	private String tel;

	private String start;

	private List<Long> shequ;

	private List<Integer> cat;

	private String notice;

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

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public List<Long> getEmp() {
		return emp;
	}

	public void setEmp(List<Long> emp) {
		this.emp = emp;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
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

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}
