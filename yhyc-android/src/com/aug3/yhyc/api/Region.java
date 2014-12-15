package com.aug3.yhyc.api;

import java.io.Serializable;

public class Region implements Serializable {

	private int id;
	private String name;
	private int p;
	private int level;
	private int sts;
	private long shequ;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSts() {
		return sts;
	}

	public void setSts(int sts) {
		this.sts = sts;
	}

	public long getShequ() {
		return shequ;
	}

	public void setShequ(long shequ) {
		this.shequ = shequ;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Region && ((Region) o).getId() == id;
	}

}
