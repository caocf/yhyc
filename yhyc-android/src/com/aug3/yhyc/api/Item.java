package com.aug3.yhyc.api;

public class Item {

	// workshopid + �����+��ˮ��
	private long id;

	private String name;

	// ��Ӧ��Ʒ
	private long pid;

	// pic name
	private String pic;

	private int act;

	// workshop id
	private long sid;

	// ���
	private long invt;

	// ���
	private String spec;

	// �ղ�����
	private long fav;

	// ������
	private long sales;

	// ������
	private double pp;

	// �г���
	private double mp;

	// ����
	private int ac;

	// ״̬
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

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getAct() {
		return act;
	}

	public void setAct(int act) {
		this.act = act;
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public long getInvt() {
		return invt;
	}

	public void setInvt(long invt) {
		this.invt = invt;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

	public double getPp() {
		return pp;
	}

	public void setPp(double pp) {
		this.pp = pp;
	}

	public double getMp() {
		return mp;
	}

	public void setMp(double mp) {
		this.mp = mp;
	}

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public int getSts() {
		return sts;
	}

	public void setSts(int sts) {
		this.sts = sts;
	}

}
