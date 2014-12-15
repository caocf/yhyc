package com.aug3.yhyc.model;

import java.util.ArrayList;
import java.util.List;

public class MyShequList {

	private List<Shequ> shequlist;

	public MyShequList(String myshequlist) {

		shequlist = new ArrayList<Shequ>();

		if (myshequlist != null && myshequlist.length() > 0) {
			String[] shequArray = myshequlist.split(";");
			for (String s : shequArray) {
				String[] sq = s.split(":");
				if (sq.length == 2)
					shequlist.add(new Shequ(Long.parseLong(sq[0]), sq[1]));
			}
		}
	}

	public List<Shequ> getShequlist() {
		return shequlist;
	}

	public void add(long id, String name) {
		shequlist.add(0, new Shequ(id, name));
		if (shequlist.size() > 5) {
			shequlist.remove(5);
		}
	}

	public void adjust(long id, String name) {
		shequlist.remove(new Shequ(id, name));
		add(id, name);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Shequ sq : shequlist) {
			sb.append(sq.toString()).append(";");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public class Shequ {

		private long id;
		private String name;

		public Shequ(long id, String name) {
			this.id = id;
			this.name = name;
		}

		public String toString() {
			return id + ":" + name;
		}

		@Override
		public boolean equals(Object shequ) {

			if (shequ instanceof Shequ) {
				Shequ sq = (Shequ) shequ;
				return this.id == sq.id && this.name.equals(sq.getName());

			} else
				return false;

		}

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

	}

}
