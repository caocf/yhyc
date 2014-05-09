package com.aug3.yhyc.valueobj;

import java.util.List;

public class Workshop {

	private long id;

	private String name;

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
