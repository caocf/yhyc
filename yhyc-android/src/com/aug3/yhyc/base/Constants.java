package com.aug3.yhyc.base;

public class Constants {

	private Constants() {
	}

	public static final String COMMA = ",";

	public static final String DISPLAY_TAB_X = "displaytabx";

	// 取图片 请求码
	public static final int IMAGE_REQUEST_CODE = 0;
	public static final int CAMERA_REQUEST_CODE = 1;
	// 图片缩放完成后, 保存裁剪之后的图片数据
	public static final int RESULT_REQUEST_CODE = 2;

	// universal image loader
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static final int TTL_IN_SECONDS = 60 * 30;

	public static enum CacheKey {
		ITEM("item_{#placeholder}"), SHEQU_CAT_SHOPS(
				"shequ_shops_{#placeholder}"), SHOP_ITEMS(
				"shopitems_{#placeholder}"), REGION("region_dict"), DELIVERY_TIME(
				"delivery_time"), SHEQU_SHOPS("shops_{#placeholder}"), CLASSIFICATION(
				"goods_classification"), CLASSIFICATION_MAP(
				"map_classification"), SHIP_AGREEMENT("ship_agreement");

		private String key;

		private CacheKey(String key) {
			this.key = key;
		}

		public String getName() {
			return key;
		}

		public String getName(String placeholder) {
			return key.replace("{#placeholder}", placeholder);
		}
	}

	public static class SharedPrefs {

		public static final String fnAvatar = "avatar_{uid}.png";

		public static final String YHYC = "yhyc_userprefs";

		public final static String KEY_USERNAME = "U_N";
		public final static String KEY_USERMOBI = "U_M";
		public final static String KEY_USERID = "U_ID";

		public final static String KEY_TOKEN = "token";

		public static final String MY_SHEQUID = "myshequID";
		public static final String MY_SHEQU = "myshequ";
		public static final String MY_SHEQU_LIST = "myshequlist";
		public static final String MY_ORDER_LIST = "myorderlist";

		public static final String RECIP = "recip";
		public static final String ADDR = "addr";
		public static final String MOBILE = "mobi";

		public static final String FIRST_PREF = "first_pref";
	}

	public static class Extra {

		public static final String IMAGES = "com.aug3.yhyc.IMAGES";
		public static final String IMAGE_POSITION = "com.aug3.yhyc.IMAGE_POSITION";

		public static final String ORDER_LIST = "com.aug3.yhyc.orderlist";

		public static final String ITEMID = "com.aug3.yhyc.itemid";
		public static final String ITEM = "com.aug3.yhyc.item";

		public static final String WORKSHOP = "com.aug3.yhyc.workshop.id";
		public static final String SHOP_NAME = "com.aug3.yhyc.workshop.name";
		public static final String SHOP_NOTICE = "com.aug3.yhyc.workshop.notice";

		public static final String PRODUCT_CAT_ID = "com.aug3.yhyc.category.id";
		public static final String CATEGORYTEXT = "com.aug3.yhyc.category";

		public static final String DISTRICT = "com.aug3.yhyc.shequ.city";
		public static final String SHEQU = "com.aug3.yhyc.shequ.id";

		public static final String ID = "com.aug3.yhyc.id";
		public static final String TITLE = "com.aug3.yhyc.title";
		public static final String URL = "com.aug3.yhyc.url";

		public static final String MOBILE = "com.aug3.yhyc.user.mobile";
		public static final String MOBILE_BIND = "com.aug3.yhyc.user.mobile.bind";

		public static final String AC = "com.aug3.yhyc.item.ac";

	}

	public static final String RESP_CODE = "code";
	public static final String RESP_RESULT = "message";

	public static class ReqParam {

		public static final String UUID = "uuid";

		public static final String ID = "id";

		public static final String TOKEN = "token";

		public static final String USER = "user";

		public static final String MOBI = "mobi";

		public static final String VERIFY_CODE = "verifyCode";

		public static final String USERID = "uid";

		public static final String VERSION = "ver";

		public static final String ORDER = "order";

		public static final String ORDERID = "orderid";

		public static final String ITEM = "item";

		public static final String ITEMS = "items";

		public static final String WORKSHOP = "workshop";

		public static final String CAT = "cat";

		public static final String STATUS = "status";

		public static final String FIELD = "field";

		public static final String TYPE = "type";

		public static final String BUCKET = "bucket";

		public static final String COMMENTS = "comments";

		public static final String PAGE = "pn";

		public static final String NAME = "name";
	}

	public static class ReqUrl {

		public static final String APP_INFO = "/app/welcome";

		public static final String SHOP_DEFAULT = "/shop/default";

		public static final String SHOP_GOODS_CLASSIFICATION = "/shop/goods/classification";

		public static final String SHOP_LIST = "/shop/list";

		public static final String SHOP_REQUEST = "/shop/request";

		public static final String SHOP_INFO = "/shop/show";

		public static final String ORDER_LIST = "/order/list";

		public static final String ORDER_LIST_BYID = "/order/listbyids";

		public static final String ORDER_STATUS = "/order/status";

		public static final String ORDER_SHOW = "/order/show";

		public static final String ORDER_NEW = "/order/new";

		public static final String ITEM_MYFAV = "/item/myfav";

		public static final String ITEM_LIST = "/item/filter";

		public static final String ITEM_SHOW = "/item/show";

		public static final String ITEM_SHOPPINGCART = "/item/shoppingcart";

		public static final String ITEM_GROUPBY_SHOP = "/item/groupbyshop";

		public static final String ITEM_COMMENT = "/item/comments";

		public static final String ITEM_COMMENT_NEW = "/item/comment/new";

		public static final String USER_VERIFY = "/user/mobile/verify";

		public static final String USER_BIND = "/user/mobile/bind";

		public static final String USER_VERIFICATION = "/user/mobile/verification";

		public static final String USER_NEW_TEMP = "/user/new/temp";

		public static final String USER_SUGGUEST = "/user/sugguest";

		public static final String USER_PREFS = "/user/prefs";

		public static final String USER_PREFS_UPDATE = "/user/prefs/update";

		public static final String USER_NAME = "/user/name";

		public static final String USER_NOTIFY = "/user/notify";
		
		public static final String USER_MESSAGES = "/user/messages";
		
		public static final String USER_MESSAGE = "/user/message";
		
		public static final String USER_MESSAGE_COMMENT = "/user/message/comment";

		public static final String USER_TAGS = "/user/tags";

		public static final String WEB_STORE_LIST = "/webstore/list";

		public static final String WEB_STORE_IEXCHG = "/webstore/iexchg";

		public static final String UPTOKEN = "/qiniu/uptoken";

		public static final String CATEGORY = "/dict/category";

		public static final String REGION = "/dict/region";

		public static final String TAGS = "/dict/tags";

		public static final String DELIVERY_TIME = "/dict/deliverytime";

		public static final String SYSTEM_SETTINGS = "/dict/systemsettings";

	}

}
