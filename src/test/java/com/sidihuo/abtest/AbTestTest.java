package com.sidihuo.abtest;


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
public class AbTestTest {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	static AbTestCacheService<AbExperiment> experimentService = new ExperimentServiceTest();
	static AbTestCacheService<Map<String, AbWhitelist>> whitelistService = new WhitelistServiceTest();
	static AbTest abTest = new AbTest(experimentService, 30, 2, whitelistService, 30, 2);

	@Before
	public void init() {
		logger.info("Junit AbTestTest init finished");
	}

	@Test
	public void test() {

		 testNoExpNoWhite();

		 testExpNoWhite();

		 testErrorExpNoWhite();

		 testNoExps();
	}

	public void testNoExpNoWhite() {
		logger.info("\n 测试无试验号+无白名单");
		for (int i = 0; i < 2; i++) {
			AbTestResponse abTestResponse = abTest.getAbTestResponse(ExperimentServiceTest.EXPERIMENT_ID_TEST + "00001",
					WhitelistServiceTest.CLIENT_ID_TEST + "0001", null);
			logger.info("-------------->" + i + "_" + abTestResponse);
		}
		logger.info("\n 测试无试验号+无白名单========================================end \n");
    }

	public void testExpNoWhite() {
		logger.info("\n 测试有试验号+无白名单");
		for (int i = 0; i < 2; i++) {
			AbTestResponse abTestResponse = abTest.getAbTestResponse(ExperimentServiceTest.EXPERIMENT_ID_TEST,
					WhitelistServiceTest.CLIENT_ID_TEST + "0001", null);
			logger.info("-------------->" + i + "_" + abTestResponse);
		}
		logger.info("\n 测试有试验号+无白名单========================================end \n");
	}

	public void testErrorExpNoWhite() {
		logger.info("\n 测试有试验号(不合法)+无白名单");
		for (int i = 0; i < 2; i++) {
			AbTestResponse abTestResponse = abTest.getAbTestResponse(ExperimentServiceTest.EXPERIMENT_ID_TEST_ILLEGAL,
					WhitelistServiceTest.CLIENT_ID_TEST + "0001", null);
			logger.info("-------------->" + i + "_" + abTestResponse);
		}
		logger.info("\n 测试有试验号(不合法)+无白名单========================================end \n");
	}

	public void testNoExps() {
		logger.info("\n 测试随机试验号攻击");
		for (int i = 0; i < 10; i++) {
			AbTestResponse abTestResponse = abTest.getAbTestResponse(UUID.randomUUID().toString(),
					UUID.randomUUID().toString(), null);
			logger.info("-------------->" + i + "_" + abTestResponse);
		}
		// 如果使用随机试验号攻击，会导致本地缓存数量达到上限，正常实验缓存也会消失
		logger.info("\n 测试随机试验号攻击========================================end \n");
	}


}
