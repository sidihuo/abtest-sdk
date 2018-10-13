/**
 * 
 */
package com.sidihuo.abtest.service;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sidihuo.abtest.pojo.AbExperiment;
import com.sidihuo.abtest.pojo.AbWhitelist;

/**
 * @author XiaoGang
 *
 *         查询DB的服务
 */
public abstract class AbTestCacheService<POJO> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public final POJO select(String key) {
		POJO pojo = doSelect(key);
		logger.info("selected result: key={},value={}, Next begin valid ...", key, pojo);
		pojo = valid(pojo);
		if (pojo == null) {
			pojo = createInstance();
		}
		return pojo;
	}

	/**
	 * @param pojo
	 */
	protected POJO valid(POJO pojo) {
		if (pojo == null) {
			return pojo;
		}
		if (pojo instanceof AbExperiment) {
			AbExperiment abExperiment = (AbExperiment) pojo;
			boolean validAbExperiment = validAbExperiment(abExperiment);
			if (!validAbExperiment) {
				pojo = null;
			}
		} else if (pojo instanceof Map<?, ?>) {
			Map<?, ?> abWhitelists = (Map<?, ?>) pojo;
			boolean validAbWhitelists = validAbWhitelists(abWhitelists);
			if (!validAbWhitelists) {
				pojo = null;
			}
		} else {
			logger.warn("doSelect result pojo is illegal and set null,{}", pojo);
			pojo = null;
		}

		return pojo;
	}

	/**
	 * @param abWhitelists
	 */
	protected boolean validAbWhitelists(Map<?, ?> abWhitelists) {
		if (abWhitelists.size() == 0) {
			return true;
		}
		Set<?> keySet = abWhitelists.keySet();
		for (Object key : keySet) {
			if (!(key instanceof String)) {
				logger.warn("validAbWhitelists key illegal ={}, and set pojo null", JSON.toJSONString(abWhitelists));
				return false;
			}
			String clientID = (String) key;
			Object value = abWhitelists.get(clientID);
			if (!(value instanceof AbWhitelist)) {
				logger.warn("validAbWhitelists value illegal={}, and set pojo null", JSON.toJSONString(abWhitelists));
				return false;
			}
			AbWhitelist abWhitelist = (AbWhitelist) value;
			String clientIDValue = abWhitelist.getClientID();
			String experimentID = abWhitelist.getExperimentID();
			String version = abWhitelist.getVersion();
			if (!clientID.equals(clientIDValue) || clientIDValue == null || clientIDValue.equals("")
					|| experimentID == null || experimentID.equals("") || version == null || version.equals("")) {
				logger.warn("validAbWhitelists value para is empty={}, and set pojo null",
						JSON.toJSONString(abWhitelists));
				return false;
			}
		}
		return true;
	}

	/**
	 * @param abExperiment
	 */
	protected boolean validAbExperiment(AbExperiment abExperiment) {
		String ratioVersion25 = abExperiment.getRatioVersion25();
		String ratioVersion50 = abExperiment.getRatioVersion50();
		String ratioVersion75 = abExperiment.getRatioVersion75();
		String ratioVersion100 = abExperiment.getRatioVersion100();
		String experimentID = abExperiment.getExperimentID();
		if (experimentID == null || experimentID.equals("") || ratioVersion25 == null || ratioVersion25.equals("")
				|| ratioVersion50 == null || ratioVersion50.equals("") || ratioVersion75 == null
				|| ratioVersion75.equals("") || ratioVersion100 == null || ratioVersion100.equals("")) {
			logger.warn("validAbExperiment para empty={}, and set pojo null", abExperiment);
			return false;
		}
		if (ratioVersion25.length() != 5 || ratioVersion50.length() != 5 || ratioVersion75.length() != 5
				|| ratioVersion100.length() != 5) {
			logger.warn("validAbExperiment ratioVersion length illegal={}, and set pojo null", abExperiment);
			return false;
		}
		return true;
	}

	/**
	 * 防止缓存穿透，查询结果为null就新new一个对象（无任何属性）放进缓存,
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private POJO createInstance() {
		try {
			Object newInstance = getPOJOClass().newInstance();
			POJO pojo = (POJO) newInstance;
			logger.warn("select result is null, avoid cache breakdown,create newInstance={},className={}", pojo,
					pojo.getClass().getSimpleName());
			return pojo;
		} catch (Exception e) {
			logger.warn("select result is null, avoid cache breakdown,create newInstance faild ", e);
			return null;
		}
	}

	/**
	 * 缓存源数据的查询服务
	 * 
	 * @param key
	 * @return
	 */
	protected abstract POJO doSelect(String key);

	/**
	 * 
	 * @return POJO的类
	 */
	protected abstract Class<?> getPOJOClass();

}
