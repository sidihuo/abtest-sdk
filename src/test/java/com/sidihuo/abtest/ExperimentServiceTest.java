/**
 * 
 */
package com.sidihuo.abtest;

import com.sidihuo.abtest.pojo.AbExperiment;
import com.sidihuo.abtest.service.AbTestCacheService;

/**
 * @author XiaoGang
 * 
 *         实验配置的数据源获取服务测试
 *
 */
public class ExperimentServiceTest extends AbTestCacheService<AbExperiment> {

	/**
	 * 正常配置的实验ID
	 */
	public final static String EXPERIMENT_ID_TEST = "experimentIDTest";

	/**
	 * 不合法配置的实验ID
	 */
	public final static String EXPERIMENT_ID_TEST_ILLEGAL = "experimentIDTestIllegal";

	@Override
	public AbExperiment doSelect(String key) {
		AbExperiment abExperiment = null;
		if (EXPERIMENT_ID_TEST.equals(key)) {
			abExperiment = new AbExperiment();
			abExperiment.setExperimentID(key);
			abExperiment.setExperimentName("测试实验Test");
			// 分流比各版本各占25%流量
			abExperiment.setRatioVersion25("AAAAA");
			abExperiment.setRatioVersion50("BBBBB");
			abExperiment.setRatioVersion75("CCCCC");
			abExperiment.setRatioVersion100("DDDDD");
		} else if (EXPERIMENT_ID_TEST_ILLEGAL.equals(key)) {
			abExperiment = new AbExperiment();
			abExperiment.setExperimentID(key);
			abExperiment.setExperimentName("测试实验-不合法");
			abExperiment.setRatioVersion25("ABCDAZZZZZZZZ");// 超过5位,不合法
			abExperiment.setRatioVersion50("BCDAB");
			abExperiment.setRatioVersion75("CDABC");
			abExperiment.setRatioVersion100("DABCD");
		}

		try {
			// 模拟耗时查询
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.info("开发者实现接口查询的实验结果  key={},value={}", key, abExperiment);
		return abExperiment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see AbTestCacheService#getPOJOClass()
	 */
	@Override
	protected Class<?> getPOJOClass() {
		return AbExperiment.class;
	}

}
