/**
 * 
 */
package com.sidihuo.abtest.pojo;

/**
 * @author XiaoGang
 *
 */
public class AbWhitelist extends BasePOJO{
	
	/**
	 * 试验号
	 */
	private String experimentID;
	
	/**
	 * 客戶号
	 */
	private String clientID;
	
	/**
	 * 客戶名
	 */
	private String clientName;
	
	/**
	 * 指定的试验号
	 */
	private String version;
	

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
	 * @return the clientID
	 */
	public String getClientID() {
		return clientID;
	}

	/**
	 * @param clientID the clientID to set
	 */
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	
}
