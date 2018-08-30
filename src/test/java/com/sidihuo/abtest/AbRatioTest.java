package com.sidihuo.abtest;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sidihuo.abtest.pojo.AbExperiment;
import com.sidihuo.abtest.pojo.AbWhitelist;
import com.sidihuo.abtest.service.AbTestCacheService;

/**
 * 
 * @author XiaoGang
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AbRatioTest {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	static AbTestCacheService<AbExperiment> experimentService = new ExperimentServiceTest();
	static AbTestCacheService<Map<String, AbWhitelist>> whitelistService = new WhitelistServiceTest();
	static AbTest abTest = new AbTest(experimentService, 30, 2, whitelistService, 30, 2);

	@Before
	public void init() {
		logger.info("Junit AbRatioTest init finished");
	}

	@Test
	public void test() {

		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("A", 0);
		map.put("B", 0);
		map.put("C", 0);
		map.put("D", 0);

		int num = 10000;
		for (int i = 0; i < num; i++) {
			AbTestResponse abTestResponse = abTest.getAbTestResponse(ExperimentServiceTest.EXPERIMENT_ID_TEST,
					UUID.randomUUID().toString(), null);
			String version = abTestResponse.getVersion();
			map.put(version, map.get(version) + 1);
		}

		double numA = map.get("A");
		double numB = map.get("B");
		double numC = map.get("C");
		double numD = map.get("D");

		logger.info("验证了 分流比各版本各占25%流量");
		logger.info("\nA: " + numA / num + "\nB: " + numB / num

				+ "\nC: " + numC / num + "\nD: " + numD / num);

	}

}
