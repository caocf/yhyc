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

import com.aug3.sys.rs.response.RespType;
import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.domain.WorksDomain;
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
	@Path("/upload")
	// @AccessTrace
	// @AccessToken
	public String uploadWorks(@Context HttpServletRequest request, @FormParam("token") String token,
			@FormParam("works") String works) {
		worksDomain.uploadWorks(JSONUtil.fromJson(works, Works.class));
		return buidResponseResult("success!", RespType.SUCCESS);
	}

	@POST
	@Path("/update")
	// @AccessTrace
	// @AccessToken
	public String updateWorks(@Context HttpServletRequest request, @FormParam("token") String token,
			@FormParam("works") String works) {
		worksDomain.updateWorks(JSONUtil.fromJson(works, Works.class));
		return buidResponseResult("success!", RespType.SUCCESS);
	}

	@GET
	@Path("/delete")
	// @AccessTrace
	// @AccessToken
	public String deleteWorks(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("uid") String uid, @QueryParam("id") String id, @QueryParam("admin") String admin) {
		if (StringUtils.isBlank("admin")) {
			admin = "FALSE";
		}
		worksDomain.deleteWorks(Long.parseLong(uid), Long.parseLong(id), Boolean.getBoolean(admin.toUpperCase()));
		return buidResponseResult("delete success!", RespType.SUCCESS);
	}

	@GET
	@Path("/list")
	public String listWorks(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("pn") String pn) {

		List<Works> works = worksDomain.listWorks(getPageNo(pn));

		return this.buidResponseResult(works, RespType.SUCCESS);
	}

	@GET
	@Path("/show")
	public String showOrder(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("id") String id) {

		Works works = worksDomain.showWorks(Long.parseLong(id));
		return this.buidResponseResult(works, RespType.SUCCESS);
	}

	@GET
	@Path("/my")
	public String myWorks(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("uid") String uid, @QueryParam("pn") String pn) {

		List<Works> works = worksDomain.listWorksByUser(Long.parseLong(uid), getPageNo(pn));
		return this.buidResponseResult(works, RespType.SUCCESS);
	}

	@GET
	@Path("/myfav")
	public String myFavWorks(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("uid") String uid, @QueryParam("pn") String pn) {

		List<Works> works = worksDomain.listFavWorksByUser(Long.parseLong(uid), getPageNo(pn));
		return this.buidResponseResult(works, RespType.SUCCESS);
	}

}
