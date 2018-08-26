/**
 * 
 */
package com.sidihuo.abtest.util;

import com.sidihuo.abtest.exception.AbTestException;
import com.sidihuo.abtest.pojo.AbExperiment;

/**
 * @author XiaoGang
 *
 */
public class VersionUtil {

	/**
	 * 二分法找模所在区间
	 * 
	 * @param mod
	 * @param abExperiment
	 * @return
	 */
	public static String getVersion(int mod, AbExperiment abExperiment) {
		if (mod >= 0 && mod < 50) {// [0,50)
			return getVersion50(mod, abExperiment);
		} else if (mod >= 50 && mod < 100) {// [50,100)
			return getVersion100(mod, abExperiment);
		} else {
			throw new AbTestException("illegal mod=" + mod);
		}
	}


	/**
	 * [50,100)
	 * 
	 * @param mod
	 * @param abExperiment
	 * @return
	 */
	private static String getVersion100(int mod, AbExperiment abExperiment) {
		if (mod >= 50 && mod < 75) {// [50,75)
			return getVersion(mod - 50, abExperiment.getRatioVersion75());
		} else {// [75,100)
			return getVersion(mod - 75, abExperiment.getRatioVersion100());
		}
	}

	/**
	 * [0,50)
	 * 
	 * @param mod
	 * @param abExperiment
	 * @return
	 */
	private static String getVersion50(int mod, AbExperiment abExperiment) {
		if (mod >= 0 && mod < 25) {// [0,25)
			return getVersion(mod, abExperiment.getRatioVersion25());
		} else {// [25,50)
			return getVersion(mod - 25, abExperiment.getRatioVersion50());
		}
	}

	/**
	 * 二分法
	 * 
	 * @param mod
	 *            大于等于0 ， 小于25
	 * @param versions
	 *            5个字符，ABCD版本号
	 * @return
	 */
	private static String getVersion(int mod, String versions) {
		char version;
		if (mod >= 0 && mod < 15) {

			if (mod >= 0 && mod < 5) {// [0,5)
				version = versions.charAt(0);
			} else if (mod >= 5 && mod < 10) {// [5,10)
				version = versions.charAt(1);
			} else {// [10,15)
				version = versions.charAt(2);
			}

		} else {

			if (mod >= 15 && mod < 20) {// [15,20)
				version = versions.charAt(3);
			} else {// [20,25)
				version = versions.charAt(4);
			}

		}

		return String.valueOf(version);
	}

}
