package com.sidihuo.abtest;


import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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

	static AbTestCacheService<AbExperiment> experimentService = new ExperimentServiceTest();
	static AbTestCacheService<Map<String, AbWhitelist>> whitelistService = new WhitelistServiceTest();
	static AbTest abTest = new AbTest(experimentService, 30, 5, whitelistService, 30, 5);

	@Before
	public void init() {
		System.out.println(" junit test init finished");
	}



	@Test
	public void testNoExpNoWhite() {
		for (int i = 0; i < 2; i++) {
			AbTestResponse abTestResponse = abTest.getAbTestResponse(ExperimentServiceTest.EXPERIMENT_ID_TEST + "1",
					WhitelistServiceTest.CLIENT_ID_TEST + "1", null);
			System.err.println(i + "_" + abTestResponse);
		}
		System.out.println("==============================================================");
    }

	@Test
	public void testExpNoWhite() {
		for (int i = 0; i < 2; i++) {
		AbTestResponse abTestResponse = abTest.getAbTestResponse(ExperimentServiceTest.EXPERIMENT_ID_TEST + "5",
				WhitelistServiceTest.CLIENT_ID_TEST + "1", null);
			System.err.println(i + "_" + abTestResponse);
		}
		System.out.println("==============================================================");
	}

	@Test
	public void testErrorExpNoWhite() {
		for (int i = 0; i < 2; i++) {
		AbTestResponse abTestResponse = abTest.getAbTestResponse(ExperimentServiceTest.EXPERIMENT_ID_TEST + "6",
				WhitelistServiceTest.CLIENT_ID_TEST + "1", null);
			System.err.println(i + "_" + abTestResponse);
		}
		System.out.println("==============================================================");
	}


}
