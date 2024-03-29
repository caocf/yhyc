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
import com.aug3.yhyc.base.RespType;
import com.aug3.yhyc.domain.OrderDomain;
import com.aug3.yhyc.domain.OrderStatus;
import com.aug3.yhyc.domain.UserDomain;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.Orders;
import com.aug3.yhyc.interceptors.annotation.AccessTrace;
import com.aug3.yhyc.valueobj.User;

@Path("/order/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class OrderService extends BaseService {

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
	@AccessTrace
	// @AccessToken
	public String newOrder(@Context HttpServletRequest request,
			@FormParam("uid") long uid, @FormParam("order") String order) {

		final Orders orders = JSONUtil.fromJson(order, Orders.class);
		if (uid == 0 && orders.getUid() == 0) {
			User user = new User();
			user.setName(orders.getDelivery().getRecip());
			user.setMobi(orders.getDelivery().getMobi());
			long u = userDomain.registerTempUser(user);
			orders.setUid(u);
		}
		List<Long> orderids = orderDomain.newOrder(orders);

		return buildResponseSuccess(orderids);
	}

	@GET
	@Path("/list")
	public String listOrders(@Context HttpServletRequest request,
			@QueryParam("uid") long uid, @QueryParam("status") int status) {

		List<Order> orders = orderDomain.listOrders(uid, status);

		return buildResponseSuccess(orders);
	}

	@GET
	@Path("/listbyids")
	public String listOrdersByID(@Context HttpServletRequest request,
			@QueryParam("id") String id, @QueryParam("status") int status) {

		if (StringUtils.isBlank(id)) {
			return buildResponseResult("invlid param id",
					RespType.INVALID_PARAMETERS);
		}

		List<Order> orders = orderDomain.listOrdersByID(transfer2Long(id),
				status);

		return buildResponseSuccess(orders);
	}

	@GET
	@Path("/show")
	public String showOrder(@Context HttpServletRequest request,
			@QueryParam("id") long id) {

		Order order = orderDomain.showOrder(id);
		return buildResponseSuccess(order);
	}

	/**
	 * 
	 * @param request
	 * @param token
	 * @param workshop
	 * @return
	 */
	@GET
	@Path("/sales")
	public String listSalesOrders(@Context HttpServletRequest request,
			@QueryParam("workshop") long workshop,
			@QueryParam("status") int status) {

		List<Order> orders = orderDomain.listOrdersByWorkshop(workshop, status);
		return buildResponseSuccess(orders);
	}

	@GET
	@Path("/sales/stats")
	public String newOrderItemsStats(@Context HttpServletRequest request,
			@QueryParam("workshop") long workshop) {

		List<ItemDTO> sales = orderDomain.newOrderItemsStats(workshop);
		return buildResponseSuccess(sales);
	}

	@GET
	@Path("/deliver")
	public String deliverOrder(@Context HttpServletRequest request,
			@QueryParam("order") long order) {

		int n = orderDomain.editOrderStatus(order,
				OrderStatus.DELIVERING.getValue());

		return buildResponseSuccess(n);
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
			@QueryParam("order") long order, @QueryParam("status") int status) {

		int n = orderDomain.editOrderStatus(order, status);

		return buildResponseSuccess(n);
	}

	/**
	 * not used for now, do it on client
	 * 
	 * @param request
	 * @param uid
	 * @param orderid
	 * @return
	 */
	@GET
	@Path("/addtocart")
	public String addOrderItems2Cart(@Context HttpServletRequest request,
			@QueryParam("uid") long uid, @QueryParam("orderid") long orderid) {

		if (uid == 0 || orderid == 0) {
			return buildResponseResult("invlid param uid/orderid",
					RespType.INVALID_PARAMETERS);
		}

		orderDomain.addOrderItems2Cart(uid, orderid);

		return buildResponseSuccess(RespType.SUCCESS);
	}

}
