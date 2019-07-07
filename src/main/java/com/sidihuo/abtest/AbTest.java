/**
 * 
 */
package com.sidihuo.abtest;

import java.util.Map;

import com.sidihuo.abtest.cache.AbTestCache;
import com.sidihuo.abtest.exception.AbTestException;
import com.sidihuo.abtest.pojo.AbExperiment;
import com.sidihuo.abtest.pojo.AbWhitelist;
import com.sidihuo.abtest.pojo.VersionResult;
import com.sidihuo.abtest.service.AbTestCacheService;
import com.sidihuo.abtest.util.ABTestUtil;
import com.sidihuo.abtest.util.VersionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author XiaoGang
 * 
 *         1.使用 AbTest-sdk 要在spring中注入此类（注意构造函数注入），必须以单例存在
 * 
 *         2.调用AbTest服务的入口
 * 
 *         3.用途：1)配合pv uv 分析最优版本；2)灰度发布;
 * 
 *         4.一个实验，最小流量比颗粒度为5%，如果需要更小粒度，可以两（多）个实现嵌套配合实现；
 */
public class AbTest {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 调用AbTest服务成功的返回码
	 */
	public static final String RESPONSE_CODE_SUCCESS = "000000";

	/**
	 * 调用AbTest服务,客户ID是白名单时的responseDescription
	 */
	public static final String RESPONSE_DESCRIPTION_WHITELIST = "whitelist client";

	/**
	 * 本地缓存：实验；key=expID,value=exp
	 */
	private AbTestCache<AbExperiment, AbTestCacheService<AbExperiment>> experimentCache;

	/**
	 * 本地缓存：白名单；key=expID,value=Map(Map中key=clientID,value=白名单)
	 */
	private AbTestCache<Map<String, AbWhitelist>, AbTestCacheService<Map<String, AbWhitelist>>> whitelistCache;

	/**
	 * 
	 * @param experimentService
	 *            查询DB实验的服务，结果放进本地缓存，开发者要自己实现
	 * @param experimentExpireTime
	 *            从DB查询的数据在缓存中存活时间，秒
	 * @param experimentMaximumSize
	 *            缓存最大实验个数
	 * @param whitelistService
	 *            查询DB白名单的服务，结果放进本地缓存，开发者要自己实现
	 * @param whitelistExpireTime
	 *            从DB查询的数据在缓存中存活时间，秒
	 * @param whitelistMaximumSize
	 *            缓存最大实验个数 (一个实验对应一个Map白名单缓存，查询白名单是先查询此实验的所有白名单，在查询这些白名单包含客户ID与否)
	 */
	public AbTest(AbTestCacheService<AbExperiment> experimentService, long experimentExpireTime,
                  long experimentMaximumSize, AbTestCacheService<Map<String, AbWhitelist>> whitelistService,
                  long whitelistExpireTime, long whitelistMaximumSize) {
		super();
		experimentCache = new AbTestCache<AbExperiment, AbTestCacheService<AbExperiment>>(experimentService,
				experimentExpireTime, experimentMaximumSize);
		whitelistCache = new AbTestCache<Map<String, AbWhitelist>, AbTestCacheService<Map<String, AbWhitelist>>>(
				whitelistService, whitelistExpireTime, whitelistMaximumSize);

		logger.info("\n"

				+ "*******************************************************************\n"
				+ "**                AbTest construct success                       **\n"
				+ "*******************************************************************");
	}

	/**
	 * AbTest请求调用入口
	 * 
	 * @param experimentID
	 *            必填
	 * @param clientID
	 *            必填
	 * @param domain
	 *            非必填；相同实验分流比配置，不同domain的实验结果不会有相互影响；
	 *            (比如使用同一个实验，domainA是APP端访问，domainB是PC端访问，虽然使用同一个实验，但是分流结果互不干扰)
	 *            也可用于分层分域设计实验;
	 * @return
	 */
	public AbTestResponse getAbTestResponse(String experimentID, String clientID, String domain) {
		long startTime = System.currentTimeMillis();
		AbTestResponse abTestResponse = new AbTestResponse();
		try {
			VersionResult versionResult = getVersion(experimentID, clientID, domain);
			String version = versionResult.getVersion();
			abTestResponse.setVersion(version);
			abTestResponse.setResponseCode(RESPONSE_CODE_SUCCESS);
			abTestResponse.setResponseMsg("success");
			boolean whitelistClient = versionResult.isWhitelistClient();
			abTestResponse.setResponseDescription(whitelistClient ? RESPONSE_DESCRIPTION_WHITELIST : "");
		} catch (AbTestException e) {
			abTestResponse.setResponseCode("100000");
			abTestResponse.setResponseMsg("abtest error");
			abTestResponse.setResponseDescription(e.getMessage());
			logger.warn("AbTest AbTestException-->" + e.getMessage());
		} catch (Exception e) {
			abTestResponse.setResponseCode("200000");
			abTestResponse.setResponseMsg("system error");
			abTestResponse.setResponseDescription(e.getMessage());
			logger.warn("AbTest Exception", e);
		}
		long timeConsume = System.currentTimeMillis() - startTime;
		logger.info("AbTest Request: experimentID={},clientID={},domain={};\nAbTest★★★Response={};\nTimeConsume={}ms",
				experimentID, clientID, domain, abTestResponse, timeConsume);
		return abTestResponse;
	}

	/**
	 * 
	 * @param experimentID
	 * @param clientID
	 * @param domain
	 * @return
	 */
	private VersionResult getVersion(String experimentID, String clientID, String domain) {
		// 校验必传参数
		if (experimentID == null || experimentID.equals("") || clientID == null || clientID.equals("")) {
			throw new AbTestException("experimentID and clientID can not be empty");
		}
		// 校验实验是否存在
		AbExperiment abExperiment = getExperimentConfig(experimentID);
		if (abExperiment == null || abExperiment.getExperimentID() == null) {
			throw new AbTestException("experimentID can not be found or experiment config is illegal");
		}
		// 开始分流
		VersionResult versionResult = new VersionResult();
		// 是否在白名单中
		String version = getVersionInWhitelist(experimentID, clientID);
		if (version != null) {
			versionResult.setWhitelistClient(true);
			versionResult.setVersion(version);
			return versionResult;
		}
		// 开始分流算法
		versionResult.setWhitelistClient(false);
		String domainValue = domain == null ? "" : domain;
		String hashString = experimentID + clientID + domainValue;
		String encodeMD5 = ABTestUtil.encodeMD5(hashString);
		int hashAndModulo = ABTestUtil.hashAndModulo(encodeMD5, 100);
		version = VersionUtil.getVersion(hashAndModulo, abExperiment);
		versionResult.setVersion(version);
		return versionResult;
	}



	/**
	 * @param experimentID
	 * @param clientID
	 * @return
	 */
	private String getVersionInWhitelist(String experimentID, String clientID) {
		Map<String, AbWhitelist> whitelists = getWhitelists(experimentID);
		if (whitelists == null) {
			return null;
		}
		AbWhitelist abWhitelist = whitelists.get(clientID);
		if (abWhitelist == null) {
			return null;
		}
		String version = abWhitelist.getVersion();
		return version;
	}

	private AbExperiment getExperimentConfig(String experimentID) {
		AbExperiment abExperiment = experimentCache.getFromCache(experimentID);
		return abExperiment;
	}

	private Map<String, AbWhitelist> getWhitelists(String experimentID) {
		Map<String, AbWhitelist> abWhitelists = whitelistCache.getFromCache(experimentID);
		return abWhitelists;
	}

}
