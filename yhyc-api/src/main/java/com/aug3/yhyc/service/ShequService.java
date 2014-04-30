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
import com.aug3.yhyc.domain.ShequDomain;
import com.aug3.yhyc.dto.WorkshopDTO;
import com.aug3.yhyc.valueobj.Shequ;

@Path("/shequ/")
@XmlRootElement()
public class ShequService extends BaseService {

	private ShequDomain shequDomain;

	public ShequDomain getShequDomain() {
		return shequDomain;
	}

	public void setShequDomain(ShequDomain shequDomain) {
		this.shequDomain = shequDomain;
	}

	/**
	 * 查询社区
	 * 
	 * @param request
	 * @param token
	 * @param q
	 * @return
	 */
	@Produces("application/json")
	@GET
	@Path("/query")
	public String queryShequ(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("city") String city, @QueryParam("q") String q) {

		List<Shequ> result = shequDomain.queryShequ(city, q);
		return buidResponseResult(result, RespType.SUCCESS);
	}

	/**
	 * 获取社区对应的云仓网点信息
	 * 
	 * @param request
	 * @param token
	 * @param shequ
	 * @return
	 */
	@Produces("application/json")
	@GET
	@Path("/workshops")
	public String getWorkshops(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("shequ") String shequ) {

		List<WorkshopDTO> workshops = shequDomain.queryWorkshops(shequ);
		return buidResponseResult(workshops, RespType.SUCCESS);
	}

}
