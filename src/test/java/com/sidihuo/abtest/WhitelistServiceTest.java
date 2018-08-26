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
 */
public class WhitelistServiceTest extends AbTestCacheService<Map<String, AbWhitelist>> {


	public final static String CLIENT_ID_TEST = "clientIDTest";

	@Override
	public Map<String, AbWhitelist> doSelect(String key) {
		Map<String, AbWhitelist> whitelists = null;
		if ((ExperimentServiceTest.EXPERIMENT_ID_TEST + "5").equals(key)) {
			whitelists = new HashMap<String, AbWhitelist>();
			AbWhitelist abWhitelist = new AbWhitelist();
			abWhitelist.setClientID(CLIENT_ID_TEST + "5");
			abWhitelist.setClientName("测试客户5");
			abWhitelist.setExperimentID(key);
			abWhitelist.setVersion("A");
			whitelists.put(abWhitelist.getClientID(), abWhitelist);
		}

		try {
			// 耗时查询
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.info("开发者查询白名单结果  key={},value={}", key, JSON.toJSONString(whitelists));
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
