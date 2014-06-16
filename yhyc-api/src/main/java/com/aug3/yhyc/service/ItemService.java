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
import com.aug3.yhyc.valueobj.Item;

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
	// TODO
	// @AccessTrace
	// @AccessToken
	public boolean newItem(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("uid") long uid,
			@FormParam("item") String item) {

		JSONUtil.fromJson(item, Item.class);
		return false;
	}

	@GET
	@Path("/list")
	public String listItems(@Context HttpServletRequest request,
			@QueryParam("token") String token,
			@QueryParam("workshop") long workshop) {

		if (workshop == 0) {
			return buidResponseResult("invlid param workshop",
					RespType.INVALID_PARAMETERS);
		}
		List<Item> items = itemDomain.findItemsByWorkshop(workshop);
		return buidResponseSuccess(items);
	}

	@GET
	@Path("/filter")
	public String filterItems(@Context HttpServletRequest request,
			@QueryParam("token") String token,
			@QueryParam("workshop") long workshop, @QueryParam("cat") int cat, @QueryParam("type") int type) {

		if (workshop == 0) {
			return buidResponseResult("invlid param workshop",
					RespType.INVALID_PARAMETERS);
		}
		List<Item> items = itemDomain.filterItems(workshop, cat, type);
		return buidResponseSuccess(items);
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
	public String showItem(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("item") long item) {

		ProductItem result = itemDomain.findItemByID(item);
		return buidResponseSuccess(result);
	}

	@GET
	@Path("/comments")
	public String listComments(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("item") long item,
			@QueryParam("pn") int pn) {

		if (pn == 0) {
			pn = 1;
		}
		CommentDTO comments = itemDomain.findCommentsByItem(item, pn);
		return buidResponseSuccess(comments);
	}

	@POST
	@Path("/comment/new")
	public void newComment(@Context HttpServletRequest request,
			@FormParam("token") String token,
			@FormParam("comments") String comments) {

		itemDomain.newComments(JSONUtil.fromJson(comments, CommentReq.class));
	}

	@GET
	@Path("/groupbyshop")
	public String groupItemsByShop(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("items") String items) {

		if (StringUtils.isBlank(items)) {
			return buidResponseResult("invlid param items",
					RespType.INVALID_PARAMETERS);
		}
		
		List<ShopItem> shopitems = itemDomain
				.groupItemsByShop(transfer2Long(items));
		return buidResponseSuccess(shopitems);
	}

	@GET
	@Path("/myfav")
	public String fetchFavorite(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid) {

		List<ShopItem> fav = itemDomain.fetchFavorite(uid);
		return buidResponseSuccess(fav);
	}

	@GET
	@Path("/shoppingcart")
	public String fetchShoppingCart(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid) {

		List<ShopItem> cart = itemDomain.fetchShoppingCart(uid);
		return buidResponseSuccess(cart);
	}

}
