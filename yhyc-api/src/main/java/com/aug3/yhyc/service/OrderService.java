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

import org.apache.log4j.Logger;

import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.domain.OrderDomain;
import com.aug3.yhyc.domain.UserDomain;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.Orders;
import com.aug3.yhyc.valueobj.User;

@Path("/order/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class OrderService extends BaseService {

	private static final Logger LOG = Logger.getLogger(OrderService.class);

	private OrderDomain orderDomain;

	private UserDomain userDomain;

	public OrderDomain getOrderDomain() {
		return orderDomain;
	}

	public void setOrderDomain(OrderDomain orderDomain) {
		this.orderDomain = orderDomain;
	}

	public UserDomain getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(UserDomain userDomain) {
		this.userDomain = userDomain;
	}

	@POST
	@Path("/new")
	// @AccessTrace
	// @AccessToken
	public String newOrder(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("uid") long uid,
			@FormParam("order") String order) {
		LOG.info(order);
		Orders orders = JSONUtil.fromJson(order, Orders.class);
		if (uid == 0 && orders.getUid() == 0) {
			User user = new User();
			user.setName(orders.getDelivery().getRecip());
			user.setMobi(orders.getDelivery().getMobi());
			long u = userDomain.registerTempUser(user);
			orders.setUid(u);
		}
		orderDomain.newOrder(orders);
		return buidResponseSuccess("success!");
	}

	@GET
	@Path("/list")
	public String listOrders(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid,
			@QueryParam("status") int status) {

		List<Order> orders = orderDomain.listOrders(uid, status);

		return buidResponseSuccess(orders);
	}

	/**
	 * 获取销售订单列表
	 * 
	 * @param request
	 * @param token
	 * @param workshop
	 * @return
	 */
	@GET
	@Path("/sales")
	public String listSalesOrders(@Context HttpServletRequest request,
			@QueryParam("token") String token,
			@QueryParam("workshop") long workshop,
			@QueryParam("status") int status) {

		List<Order> orders = orderDomain.listOrdersByWorkshop(workshop, status);
		return buidResponseSuccess(orders);
	}

	@GET
	@Path("/show")
	public String showOrder(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("id") long id) {

		Order order = orderDomain.showOrder(id);
		return buidResponseSuccess(order);
	}

	@GET
	@Path("/deliver")
	public String deliverOrder(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("id") long id) {

		orderDomain.deliverOrder(id);
		return buidResponseSuccess("success");
	}

	/**
	 * change status, e.g. cancel, delivering, finished
	 * 
	 * @param request
	 * @param token
	 * @param orderid
	 * @param status
	 * @return
	 */
	@GET
	@Path("/status")
	public String editStatus(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("order") long order,
			@QueryParam("status") int status) {

		int n = orderDomain.editOrderStatus(order, status);

		return buidResponseSuccess(n);
	}

}
