/**
 * 
 */
package com.sidihuo.abtest.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @author XiaoGang
 *
 *         本地缓存中内容失效的通知，异步，不影响性能
 *
 */
public class AbTestCacheRemovalListener implements RemovalListener<Object, Object> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void onRemoval(RemovalNotification<Object, Object> notification) {
		String removedNotification = String.format("key=%s,value=%s,reason=%s", notification.getKey(),
				notification.getValue(),
				notification.getCause());
		logger.info("cache removed-->{}", removedNotification);
	}

}
