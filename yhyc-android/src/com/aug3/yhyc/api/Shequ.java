package com.aug3.yhyc.api;

import java.io.Serializable;

public class Shequ implements Serializable {

	private long id;
	private String name;
	// ƴ������ĸ
	private String py;

	// ����
	private int city;
	// ����
	private int dist;

	private String addr;

	// ��γ��
	private long lat;
	private long lgt;

	// ����
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
