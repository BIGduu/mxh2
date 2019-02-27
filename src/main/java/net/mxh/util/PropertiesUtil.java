package net.mxh.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	public static String getPropertiesKey(String key) {
        Properties pps = new Properties();
        try {
        	InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
            pps.load(in);
            String value = pps.getProperty(key);
            return value.trim();
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
}
