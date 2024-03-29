package com.aug3.yhyc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.yhyc.domain.DictDomain;
import com.aug3.yhyc.dto.CategoryDTO;
import com.aug3.yhyc.dto.DeliveryTime;
import com.aug3.yhyc.dto.SystemSettings;
import com.aug3.yhyc.valueobj.Category;
import com.aug3.yhyc.valueobj.Region;
import com.aug3.yhyc.valueobj.Tag;

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
	public String listCategory(@Context HttpServletRequest request) {

		Map<Integer, List<Category>> categories = dictDomain.mapCategory();
		CategoryDTO dto = new CategoryDTO();
		dto.setCategories(categories);
		return buildResponseSuccess(dto);
	}

	@GET
	@Path("/region")
	public String listRegion(@Context HttpServletRequest request) {

		List<Region> result = dictDomain.listRegion();
		return buildResponseSuccess(result);
	}

	@GET
	@Path("/tags")
	public String listTags(@Context HttpServletRequest request) {

		List<Tag> result = dictDomain.listTags();
		return buildResponseSuccess(result);
	}

	@GET
	@Path("/deliverytime")
	public String deliverytime(@Context HttpServletRequest request) {

		DeliveryTime dt = dictDomain.deliverytime();
		return buildResponseSuccess(dt);
	}

	@GET
	@Path("/systemsettings")
	public String getSystemSettings(@Context HttpServletRequest request) {

		SystemSettings ss = dictDomain.getSystemSettings();
		return buildResponseSuccess(ss);
	}

}
