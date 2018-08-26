/**
 * 
 */
package com.sidihuo.abtest.pojo;

import com.alibaba.fastjson.JSON;

/**
 * @author XiaoGang
 *
 */
public class BasePOJO {
	
	/* json字符串
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
