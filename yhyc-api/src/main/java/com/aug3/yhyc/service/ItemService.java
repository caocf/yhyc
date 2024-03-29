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
import com.aug3.yhyc.domain.ItemDomain;
import com.aug3.yhyc.dto.CommentDTO;
import com.aug3.yhyc.dto.CommentReq;
import com.aug3.yhyc.dto.ProductItem;
import com.aug3.yhyc.dto.ShopItem;
import com.aug3.yhyc.interceptors.annotation.AccessTrace;
import com.aug3.yhyc.valueobj.Item;
import com.aug3.yhyc.valueobj.Product;

@Path("/item/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class ItemService extends BaseService {

	private ItemDomain itemDomain;

	public ItemDomain getItemDomain() {
		return itemDomain;
	}

	public void setItemDomain(ItemDomain itemDomain) {
		this.itemDomain = itemDomain;
	}

	@POST
	@Path("/new")
	@AccessTrace
	// @AccessToken
	public String newItem(@Context HttpServletRequest request,
			@FormParam("item") String item) {

		if (StringUtils.isBlank(item)) {
			return buildResponseResult("invlid param items",
					RespType.INVALID_PARAMETERS);
		}

		Item i = JSONUtil.fromJson(item, Item.class);

		long itemid = itemDomain.newItem(i);

		return buildResponseSuccess(itemid);
	}

	@POST
	@Path("/update")
	// @AccessTrace
	// @AccessToken
	public String updateItem(@Context HttpServletRequest request,
			@FormParam("workshop") long workshop, @FormParam("item") String item) {

		boolean boo = itemDomain.updateItem(workshop,
				JSONUtil.fromJson(item, Item.class));
		if (boo) {
			return buildResponseSuccess("OK");
		} else {
			return buildResponseResult("Failed", RespType.FAILED);
		}
	}

	@GET
	@Path("/list")
	public String listItems(@Context HttpServletRequest request,
			@QueryParam("workshop") long workshop) {

		if (workshop == 0) {
			return buildResponseResult("invlid param workshop",
					RespType.INVALID_PARAMETERS);
		}
		List<Item> items = itemDomain.findItemsByWorkshop(workshop);
		return buildResponseSuccess(items);
	}

	@GET
	@Path("/filter")
	@AccessTrace
	public String filterItems(@Context HttpServletRequest request,
			@QueryParam("workshop") long workshop, @QueryParam("cat") int cat,
			@QueryParam("type") int type) {

		if (workshop == 0) {
			return buildResponseResult("invlid param workshop",
					RespType.INVALID_PARAMETERS);
		}
		List<Item> items = itemDomain.filterItems(workshop, cat, type, false);
		return buildResponseSuccess(items);
	}

	@GET
	@Path("/manage")
	public String manageItems(@Context HttpServletRequest request,
			@QueryParam("workshop") long workshop, @QueryParam("cat") int cat,
			@QueryParam("type") int type) {

		if (workshop == 0) {
			return buildResponseResult("invlid param workshop",
					RespType.INVALID_PARAMETERS);
		}
		List<Item> items = itemDomain.filterItems(workshop, cat, type, true);
		return buildResponseSuccess(items);
	}

	/**
	 * show items, multiple items separated by comma
	 * 
	 * @param request
	 * @param token
	 * @param item
	 * @return
	 */
	@GET
	@Path("/show")
	@AccessTrace
	public String showItem(@Context HttpServletRequest request,
			@QueryParam("item") long item) {

		try {
			ProductItem result = itemDomain.findItemByID(item);
			return buildResponseSuccess(result);
		} catch (Exception e) {
			return buildResponseResult(e.getMessage(), RespType.FAILED);
		}
	}

	@GET
	@Path("/comments")
	public String listComments(@Context HttpServletRequest request,
			@QueryParam("item") long item, @QueryParam("pn") int pn) {

		if (pn == 0) {
			pn = 1;
		}
		CommentDTO comments = itemDomain.findCommentsByItem(item, pn);
		return buildResponseSuccess(comments);
	}

	@POST
	@Path("/comment/new")
	public void newComment(@Context HttpServletRequest request,
			@FormParam("comments") String comments) {

		itemDomain.newComments(JSONUtil.fromJson(comments, CommentReq.class));
	}

	@GET
	@Path("/groupbyshop")
	public String groupItemsByShop(@Context HttpServletRequest request,
			@QueryParam("items") String items) {

		if (StringUtils.isBlank(items)) {
			return buildResponseResult("invlid param items",
					RespType.INVALID_PARAMETERS);
		}

		List<ShopItem> shopitems = itemDomain
				.groupItemsByShop(transfer2Long(items));
		return buildResponseSuccess(shopitems);
	}

	@GET
	@Path("/myfav")
	public String fetchFavorite(@Context HttpServletRequest request,
			@QueryParam("uid") long uid) {

		List<ShopItem> fav = itemDomain.fetchFavorite(uid);
		return buildResponseSuccess(fav);
	}

	@GET
	@Path("/shoppingcart")
	public String fetchShoppingCart(@Context HttpServletRequest request,
			@QueryParam("uid") long uid) {

		List<ShopItem> cart = itemDomain.fetchShoppingCart(uid);
		return buildResponseSuccess(cart);
	}

	@GET
	@Path("/products")
	public String listProducts(@Context HttpServletRequest request,
			@QueryParam("workshop") long workshop, @QueryParam("cat") int cat) {

		if (workshop == 0) {
			return buildResponseResult("invlid param workshop",
					RespType.INVALID_PARAMETERS);
		}
		List<Product> items = itemDomain.listProducts(workshop, cat);

		return buildResponseSuccess(items);
	}

}
