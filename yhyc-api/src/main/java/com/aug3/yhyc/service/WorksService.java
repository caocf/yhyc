package com.aug3.yhyc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.domain.WorksDomain;
import com.aug3.yhyc.valueobj.RecipeWizard;
import com.aug3.yhyc.valueobj.Works;

@Path("/works/")
@XmlRootElement()
@Produces("application/json")
public class WorksService extends BaseService {

	private WorksDomain worksDomain;

	public WorksDomain getWorksDomain() {
		return worksDomain;
	}

	public void setWorksDomain(WorksDomain worksDomain) {
		this.worksDomain = worksDomain;
	}

	@POST
	@Path("/new")
	// @AccessTrace
	// @AccessToken
	public String newWorks(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("works") String works) {
		worksDomain.newWorks(JSONUtil.fromJson(works, Works.class));
		return buidResponseSuccess("success!");
	}

	@POST
	@Path("/steps")
	// @AccessTrace
	// @AccessToken
	public String uploadSteps(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("uid") long uid,
			@FormParam("id") long id, @FormParam("") RecipeWizard wizard) {
		worksDomain.uploadSteps(uid, id, wizard);
		return buidResponseSuccess("success!");
	}

	@POST
	@Path("/update")
	// @AccessTrace
	// @AccessToken
	public String updateWorks(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("works") String works) {
		worksDomain.updateWorks(JSONUtil.fromJson(works, Works.class));
		return buidResponseSuccess("success!");
	}

	@GET
	@Path("/delete")
	// @AccessTrace
	// @AccessToken
	public String deleteWorks(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid,
			@QueryParam("id") long id, @QueryParam("admin") String admin) {
		if (StringUtils.isBlank("admin")) {
			admin = "FALSE";
		}
		worksDomain.deleteWorks(uid, id,
				Boolean.getBoolean(admin.toUpperCase()));
		return buidResponseSuccess("delete success!");
	}

	@GET
	@Path("/list")
	public String listWorks(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("pn") String pn) {

		List<Works> works = worksDomain.listWorks(getPageNo(pn));

		return this.buidResponseSuccess(works);
	}

	@GET
	@Path("/show")
	public String showOrder(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("id") long id) {

		Works works = worksDomain.showWorks(id);
		return this.buidResponseSuccess(works);
	}

	@GET
	@Path("/my")
	public String myWorks(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid,
			@QueryParam("pn") String pn) {

		List<Works> works = worksDomain.listWorksByUser(uid, getPageNo(pn));
		return this.buidResponseSuccess(works);
	}

	@GET
	@Path("/myfav")
	public String myFavWorks(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid,
			@QueryParam("pn") String pn) {

		List<Works> works = worksDomain.listFavWorksByUser(uid, getPageNo(pn));
		return this.buidResponseSuccess(works);
	}

}
