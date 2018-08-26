/**
 * 
 */
package com.sidihuo.abtest.pojo;

/**
 * @author XiaoGang
 *
 */
public class VersionResult extends BasePOJO {

	/**
	 * 白名单客户与否
	 */
	private boolean whitelistClient;

	private String version;

	/**
	 * @return the whitelistClient
	 */
	public boolean isWhitelistClient() {
		return whitelistClient;
	}

	/**
	 * @param whitelistClient
	 *            the whitelistClient to set
	 */
	public void setWhitelistClient(boolean whitelistClient) {
		this.whitelistClient = whitelistClient;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
