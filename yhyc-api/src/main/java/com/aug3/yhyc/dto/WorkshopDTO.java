package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.List;

import com.aug3.yhyc.valueobj.Shequ;

public class WorkshopDTO implements Serializable {

	private long id;

	private String name;

	private long owner;

	private String who;

	private String pic;

	private String addr;

	private String tel;

	private String start;

	private String notice;

	private List<Integer> cat;

	private List<Shequ> shequ;

	// advertisement url
	private String ad;

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

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
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

	public List<Integer> getCat() {
		return cat;
	}

	public void setCat(List<Integer> cat) {
		this.cat = cat;
	}

	public List<Shequ> getShequ() {
		return shequ;
	}

	public void setShequ(List<Shequ> shequ) {
		this.shequ = shequ;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

}
