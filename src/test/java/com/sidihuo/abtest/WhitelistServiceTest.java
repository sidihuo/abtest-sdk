/**
 * 
 */
package com.sidihuo.abtest;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.sidihuo.abtest.pojo.AbWhitelist;
import com.sidihuo.abtest.service.AbTestCacheService;

/**
 * @author XiaoGang
 * 
 *         实验白名单的数据源获取服务测试
 *
 */
public class WhitelistServiceTest extends AbTestCacheService<Map<String, AbWhitelist>> {

	/**
	 * 配置的白名单ID
	 */
	public final static String CLIENT_ID_TEST = "clientIDTest";

	@Override
	public Map<String, AbWhitelist> doSelect(String key) {
		Map<String, AbWhitelist> whitelists = null;
		if ((ExperimentServiceTest.EXPERIMENT_ID_TEST).equals(key)) {
			whitelists = new HashMap<String, AbWhitelist>();
			AbWhitelist abWhitelist = new AbWhitelist();
			abWhitelist.setClientID(CLIENT_ID_TEST);
			abWhitelist.setClientName("测试客户");
			abWhitelist.setExperimentID(key);
			abWhitelist.setVersion("A");// 指定此客户的版本号
			whitelists.put(abWhitelist.getClientID(), abWhitelist);
		}

		try {
			// 模拟耗时查询
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.info("开发者实现接口查询白名单的结果  key={},value={}", key, JSON.toJSONString(whitelists));
		return whitelists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sidihuo.abtest.service.AbTestCacheService#getPOJOClass()
	 */
	@Override
	protected Class<?> getPOJOClass() {
		return Map.class;
	}

}
