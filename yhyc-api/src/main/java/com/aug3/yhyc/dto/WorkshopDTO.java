package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.List;

import com.aug3.yhyc.valueobj.Shequ;

public class WorkshopDTO implements Serializable {

	private long id;

	private String name;

	private String owner;

	private String dist;

	private String addr;

	private String tel;

	private String start;

	private List<Shequ> shequ;

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

	public List<Shequ> getShequ() {
		return shequ;
	}

	public void setShequ(List<Shequ> shequ) {
		this.shequ = shequ;
	}

}
