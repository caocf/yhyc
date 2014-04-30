package com.aug3.yhyc.valueobj;

import java.io.Serializable;

public class Shequ implements Serializable {

	private long id;
	private String name;
	// 拼音首字母
	private String py;

	// 城市
	private String city;
	// 区域
	private String dist;
	private String addr;

	// 经纬度
	private long lat;
	private long lgt;

	// 描述
	private String desc;

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

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
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

	public long getLat() {
		return lat;
	}

	public void setLat(long lat) {
		this.lat = lat;
	}

	public long getLgt() {
		return lgt;
	}

	public void setLgt(long lgt) {
		this.lgt = lgt;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
