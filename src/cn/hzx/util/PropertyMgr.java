package cn.hzx.util;

import java.util.Properties;

public class PropertyMgr {
	static Properties props = new Properties();
	static {
		try {
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//private PropertyMgr() {};
	public static String getProperty(String key) {
		return props.getProperty(key);
	}
}
