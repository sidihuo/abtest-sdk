/**
 * 
 */
package com.sidihuo.abtest;

import com.sidihuo.abtest.pojo.AbExperiment;
import com.sidihuo.abtest.service.AbTestCacheService;

/**
 * @author XiaoGang
 *
 */
public class ExperimentServiceTest extends AbTestCacheService<AbExperiment> {


	public final static String EXPERIMENT_ID_TEST = "experimentIDTest";

	@Override
	public AbExperiment doSelect(String key) {
		AbExperiment abExperiment = null;
		if ((EXPERIMENT_ID_TEST + "5").equals(key)) {
			abExperiment = new AbExperiment();
			abExperiment.setExperimentID(key);
			abExperiment.setExperimentName("测试实验5");
			abExperiment.setRatioVersion25("ABCDA");
			abExperiment.setRatioVersion50("BCDAB");
			abExperiment.setRatioVersion75("CDABC");
			abExperiment.setRatioVersion100("DABCD");
		} else if ((EXPERIMENT_ID_TEST + "6").equals(key)) {
			abExperiment = new AbExperiment();
			abExperiment.setExperimentID(key);
			abExperiment.setExperimentName("测试实验6");
			abExperiment.setRatioVersion25("ABCDAWWWWW");
			abExperiment.setRatioVersion50("BCDAB");
			abExperiment.setRatioVersion75("CDABC");
			abExperiment.setRatioVersion100("DABCD");
		}

		try {
			// 耗时查询
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.info("开发者查询实验结果  key={},value={}", key, abExperiment);
		return abExperiment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sidihuo.abtest.service.AbTestCacheService#getPOJOClass()
	 */
	@Override
	protected Class<?> getPOJOClass() {
		return AbExperiment.class;
	}

}
