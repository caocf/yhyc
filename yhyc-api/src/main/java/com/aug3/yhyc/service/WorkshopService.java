package com.aug3.yhyc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.sys.rs.response.RespType;
import com.aug3.yhyc.domain.WorkshopDomain;
import com.aug3.yhyc.dto.WorkshopDTO;

@Path("/shop/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class WorkshopService extends BaseService {

	private WorkshopDomain shopDomain;

	public WorkshopDomain getShopDomain() {
		return shopDomain;
	}

	public void setShopDomain(WorkshopDomain shopDomain) {
		this.shopDomain = shopDomain;
	}

	/**
	 * 获取社区对应的云仓网点信息
	 * 
	 * @param request
	 * @param token
	 * @param shequ
	 * @return
	 */
	@GET
	@Path("/list")
	public String getWorkshops(@Context HttpServletRequest request,
			@QueryParam("token") String token,
			@QueryParam("shequ") String shequ, @QueryParam("cat") String cat) {

		List<WorkshopDTO> workshops = shopDomain.queryWorkshops(
				Long.parseLong(shequ), Integer.parseInt(cat));
		return buidResponseResult(workshops, RespType.SUCCESS);
	}

}
