package com.aug3.yhyc.dto;

import java.util.List;

public class ProductDTO {

	// 分类号+流水号
	private long id;

	private String name;

	// tags
	private List<String> tags;

	// 推荐
	private List<ItemDTO> ref;

	// 橱窗图片
	private String pic;

	// 描述
	private String desc;

	private String cooked;

	private String cpic;

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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<ItemDTO> getRef() {
		return ref;
	}

	public void setRef(List<ItemDTO> ref) {
		this.ref = ref;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCooked() {
		return cooked;
	}

	public void setCooked(String cooked) {
		this.cooked = cooked;
	}

	public String getCpic() {
		return cpic;
	}

	public void setCpic(String cpic) {
		this.cpic = cpic;
	}

}
