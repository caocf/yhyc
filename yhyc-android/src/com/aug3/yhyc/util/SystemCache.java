package com.aug3.yhyc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SystemCache implements a system-wide/class-loader-level cache. Data stored in
 * one instance of this cache is available to all other SystemCache instances in
 * the system.
 * 
 * It is a kind of LRUCache and absolutely a JVM cache (does not resolve
 * cross-JVM cache).
 * 
 * You need to take care of your objects, so as not to flood your memory.
 * 
 * You can set the TTL(time to live) of the object to guarantee the stored
 * object is the latest status. If expired, you need to get object from other
 * resources and put back to the cache as distributed cache does.
 * 
 * It is always used for caching dictionary data that does not changed in a long
 * time, and we suggest you cache your object list in one key, that is
 * {key->dictMap}.
 * 
 * 
 * For example, the following code snippet:
 * 
 * <code>
 *   SystemCache<String,String> cache1 = new SystemCache<String,String>();
 *   SystemCache<String,String> cache2 = new SystemCache<String,String>();
 *   cache1.put("theKey","theValue");
 *   System.out.println("value is " + cache2.get("theKey"));
 * </code>
 * 
 * would print "value is theValue."
 * 
 * @author Roger.xia
 **/
public class SystemCache {

	private static Map<Object, TTLEntry> theCache;

	private static AtomicLong hit = new AtomicLong(0);
	private static AtomicLong req = new AtomicLong(0);

	public static long getHit() {
		return hit.get();
	}

	public static long getReq() {
		return req.get();
	}

	static {
		Map<Object, TTLEntry> cache = new LRUCache<Object, TTLEntry>(100);
		theCache = new ConcurrentHashMap<Object, TTLEntry>(cache);
	}

	public Object get(Object key) {
		TTLEntry ttlObj = theCache.get(key);
		req.incrementAndGet();
		if (ttlObj == null) {
			return null;
		} else if (ttlObj.getTtl() != 0
				&& (System.currentTimeMillis() / 1000 - ttlObj.getBorn()) > ttlObj
						.getTtl()) {
			theCache.remove(key);
			return null;
		} else {
			hit.incrementAndGet();
			return ttlObj.getObj();
		}
	}

	public Object put(Object key, Object value) {
		return this.put(key, value, 0);
	}

	public Object put(Object key, Object value, long secondsToExpire) {

		TTLEntry ttlObj = new TTLEntry();
		if (secondsToExpire != 0) {
			ttlObj.setBorn(System.currentTimeMillis() / 1000);
			ttlObj.setTtl(secondsToExpire);
		}
		ttlObj.setObj(value);
		return theCache.put(key, ttlObj);
	}

	public Object remove(Object key) {
		return theCache.remove(key);
	}

	public boolean containsKey(Object key) {
		boolean hasKey = theCache.containsKey(key);
		if (hasKey) {
			TTLEntry ttlObj = theCache.get(key);
			if (ttlObj.getTtl() != 0
					&& (System.currentTimeMillis() / 1000 - ttlObj.getBorn()) > ttlObj
							.getTtl()) {
				theCache.remove(key);
				hasKey = false;
			}
		}
		return hasKey;
	}

	public void flush() {
		theCache.clear();

	}

	class TTLEntry {
		long born = 0;
		long ttl = 0;
		Object obj;

		public long getBorn() {
			return born;
		}

		public void setBorn(long born) {
			this.born = born;
		}

		public long getTtl() {
			return ttl;
		}

		public void setTtl(long ttl) {
			this.ttl = ttl;
		}

		public Object getObj() {
			return obj;
		}

		public void setObj(Object obj) {
			this.obj = obj;
		}

	}
}
