/**
 * 
 */
package com.sidihuo.abtest.pojo;

/**
 * @author XiaoGang
 *
 */
public class AbExperiment extends BasePOJO {
	
	/**
	 * 试验号，唯一
	 */
	private String experimentID;
	
	/**
	 * 试验名
	 */
	private String experimentName;
	
	
	/**
	 * 试验配置分流取模0~24对应的版本号（必須5位字符）
	 */
	private String ratioVersion25;
	
	/**
	 * 试验配置分流取模25~49对应的版本号（必須5位字符）
	 */
	private String ratioVersion50;
	
	/**
	 * 试验配置分流取模50~74对应的版本号（必須5位字符）
	 */
	private String ratioVersion75;
	
	/**
	 * 试验配置分流取模75~99对应的版本号（必須5位字符）
	 */
	private String ratioVersion100;
	

	/**
	 * @return the experimentID
	 */
	public String getExperimentID() {
		return experimentID;
	}

	/**
	 * @param experimentID the experimentID to set
	 */
	public void setExperimentID(String experimentID) {
		this.experimentID = experimentID;
	}

	/**
	 * @return the experimentName
	 */
	public String getExperimentName() {
		return experimentName;
	}

	/**
	 * @param experimentName the experimentName to set
	 */
	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}


	/**
	 * @return the ratioVersion25
	 */
	public String getRatioVersion25() {
		return ratioVersion25;
	}

	/**
	 * @param ratioVersion25
	 *            the ratioVersion25 to set 试验配置分流取模0~24对应的版本号（必須5位字符）
	 */
	public void setRatioVersion25(String ratioVersion25) {
		this.ratioVersion25 = ratioVersion25;
	}

	/**
	 * @return the ratioVersion50
	 */
	public String getRatioVersion50() {
		return ratioVersion50;
	}

	/**
	 * @param ratioVersion50
	 *            the ratioVersion50 to set 试验配置分流取模25~49对应的版本号（必須5位字符）
	 */
	public void setRatioVersion50(String ratioVersion50) {
		this.ratioVersion50 = ratioVersion50;
	}

	/**
	 * @return the ratioVersion75
	 */
	public String getRatioVersion75() {
		return ratioVersion75;
	}

	/**
	 * @param ratioVersion75
	 *            the ratioVersion75 to set 试验配置分流取模50~74对应的版本号（必須5位字符）
	 */
	public void setRatioVersion75(String ratioVersion75) {
		this.ratioVersion75 = ratioVersion75;
	}

	/**
	 * @return the ratioVersion100
	 */
	public String getRatioVersion100() {
		return ratioVersion100;
	}

	/**
	 * @param ratioVersion100
	 *            the ratioVersion100 to set 试验配置分流取模75~99对应的版本号（必須5位字符）
	 */
	public void setRatioVersion100(String ratioVersion100) {
		this.ratioVersion100 = ratioVersion100;
	}


}
