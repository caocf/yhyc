package com.aug3.yhyc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.yhyc.domain.DictDomain;
import com.aug3.yhyc.dto.CategoryDTO;
import com.aug3.yhyc.valueobj.Category;

@Path("/dict/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class DictService extends BaseService {

	private DictDomain dictDomain;

	public DictDomain getDictDomain() {
		return dictDomain;
	}

	public void setDictDomain(DictDomain dictDomain) {
		this.dictDomain = dictDomain;
	}

	@GET
	@Path("/category")
	public String listCategory(@Context HttpServletRequest request,
			@QueryParam("token") String token) {

		Map<Integer, List<Category>> categories = dictDomain.mapCategory();
		CategoryDTO dto = new CategoryDTO();
		dto.setCategories(categories);
		return buidResponseSuccess(dto);
	}

}
