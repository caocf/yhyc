package com.aug3.yhyc.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.domain.StatDomain;
import com.aug3.yhyc.dto.ShopStats;

@Path("/stats/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class StatService extends BaseService {

	private StatDomain statDomain;

	public StatDomain getStatDomain() {
		return statDomain;
	}

	public void setStatDomain(StatDomain statDomain) {
		this.statDomain = statDomain;
	}

	@GET
	@Path("/shop")
	public String shopStatistics(@Context HttpServletRequest request,
			@QueryParam("uid") long uid, @QueryParam("workshop") long workshop,
			@QueryParam("freq") int freq) {

		ShopStats daily = statDomain.getShopDailyStats(uid, workshop,
				Constants.ORDER_STATUS_FINISHED, freq);

		return buildResponseSuccess(daily);
	}

}
