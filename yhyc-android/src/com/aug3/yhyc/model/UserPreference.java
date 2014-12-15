package com.aug3.yhyc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * this class is used for recording what use access last time
 * 
 * @author roger
 * 
 */
public final class UserPreference {

	private String uuid;

	private int ver;

	private String verName;

	// last accessed workshop
	private long workshop;

	private long uid;

	private String token;

	private String mobile;

	private String userName;

	private int ac;

	private boolean hasAvatar;

	private boolean cartUpdated;

	private Set<Long> cart = new HashSet<Long>();

	private Map<Long, Integer> cartItemsMap = new HashMap<Long, Integer>();

	private boolean favUpdated;

	private Set<Long> fav = new HashSet<Long>();

	private List<Long> orders = new ArrayList<Long>();

	private Map<String, String> urls;

	private static final UserPreference instance = new UserPreference();

	// can not initialize this class
	private UserPreference() {

	}

	public static UserPreference getInstance() {
		return instance;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public String getVerName() {
		return verName;
	}

	public void setVerName(String verName) {
		this.verName = verName;
	}

	public long getWorkshop() {
		return workshop;
	}

	public void setWorkshop(long workshop) {
		this.workshop = workshop;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public boolean isHasAvatar() {
		return hasAvatar;
	}

	public void setHasAvatar(boolean hasAvatar) {
		this.hasAvatar = hasAvatar;
	}

	public Set<Long> getCart() {
		return cart;
	}

	public void setCart(Set<Long> cart) {
		this.cart = cart;
	}

	public Map<Long, Integer> getCartItemsMap() {
		return cartItemsMap;
	}

	public void setCartItemsMap(Map<Long, Integer> cartItemsMap) {
		this.cartItemsMap = cartItemsMap;
	}

	public void increaseNumberOfItemInCart(long itemid, int inc) {
		if (cartItemsMap.containsKey(itemid)) {
			cartItemsMap.put(itemid, cartItemsMap.get(itemid) + inc);
		} else {
			cartItemsMap.put(itemid, inc);
		}
	}

	public int getNumberOfItemInCart(long itemid) {
		if (cartItemsMap.containsKey(itemid)) {
			return cartItemsMap.get(itemid);
		} else {
			return 1;
		}
	}

	public void setNumberOfItemInCart(long itemid, int num) {
		if (num > 1) {
			cartItemsMap.put(itemid, num);
		} else {
			cartItemsMap.put(itemid, 1);
		}
	}

	public Set<Long> getFav() {
		return fav;
	}

	public void setFav(Set<Long> fav) {
		this.fav = fav;
	}

	public boolean isCartUpdated() {
		return cartUpdated;
	}

	public void setCartUpdated(boolean cartUpdated) {
		this.cartUpdated = cartUpdated;
	}

	public boolean isFavUpdated() {
		return favUpdated;
	}

	public void setFavUpdated(boolean favUpdated) {
		this.favUpdated = favUpdated;
	}

	public List<Long> getOrders() {
		return orders;
	}

	public void setOrders(List<Long> orders) {
		this.orders = orders;
	}

	public Map<String, String> getUrls() {
		return urls;
	}

	public void setUrls(Map<String, String> urls) {
		this.urls = urls;
	}

}
