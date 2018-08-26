/**
 * 
 */
package com.sidihuo.abtest.cache;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import com.sidihuo.abtest.service.AbTestCacheService;

/**
 * 
 * @author XiaoGang
 * 
 *         Guava本地缓存
 * @param <POJO>
 *            缓存的对象内容
 * @param <Service>
 *            缓存内容的查询服务(开发者自己实现),缓存的数据源获取服务
 */
public class AbTestCache<POJO, Service extends AbTestCacheService<POJO>> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 从DB中查询数据的服务
	 */
	private Service service;

	/**
	 * 从DB查询的数据在缓存中存活时间，秒
	 */
	private long expireTime;

	/**
	 * 缓存最大个数
	 */
	private long maximumSize;

	/**
	 * 异步监听缓存失效移除
	 */
	private RemovalListener<? super Object, ? super Object> asyncRemovalListener = RemovalListeners
			.asynchronous(new AbTestCacheRemovalListener(), Executors.newSingleThreadExecutor());

	/**
	 * 本地缓存
	 */
	private LoadingCache<String, POJO> cache;


	/**
	 * 
	 * @param service
	 *            从DB中查询的服务
	 * @param expireTime
	 *            从DB查询的数据在缓存中存活时间，秒
	 * @param maximumSize
	 *            缓存最大个数
	 */
	public AbTestCache(Service service, long expireTime, long maximumSize) {
		super();
		this.service = service;
		this.expireTime = expireTime;
		this.maximumSize = maximumSize;

		this.cache = createCache();

		logger.info("create cache finished");
	}

	private LoadingCache<String, POJO> createCache() {
		LoadingCache<String, POJO> cache = CacheBuilder.newBuilder()

				.removalListener(asyncRemovalListener)

				.maximumSize(maximumSize)

				// 到期时候并不会直接从内存剔除，到期后再次调用会发现已过期，会重新load，此时才剔除换新的
				.expireAfterWrite(expireTime, TimeUnit.SECONDS)

				.build(createCacheLoader());

		return cache;
	}

	private CacheLoader<String, POJO> createCacheLoader() {

		return new CacheLoader<String, POJO>() {
			@Override
			public POJO load(String key) throws Exception {
				POJO select = service.select(key);
				logger.info("no cache or expired, NEW LOAD --> key={},value={}", key, select);
				return select;
			}
		};
	}

	/**
	 * @param key
	 * @return the cache
	 * 
	 *         如果缓存中没有，先查询，结果放入缓存，最后从缓存取
	 */
	public POJO getFromCache(String key) {
		try {
			POJO pojo = cache.get(key);
			logger.info("getFromCache key={},value={}", key, pojo);
			return pojo;
		} catch (Exception e) {
			logger.warn("getFromCache faild,key={},reason={}", key, e.getMessage());
		}
		return null;
	}

}
