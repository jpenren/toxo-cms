package es.toxo.cms.common.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Configuration {
	private static final String CONF = "/configuration.properties";
	private static final Properties PROPS = new Properties();
	private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
	
	private Configuration() {
	}
	
	static{
		try {
			PROPS.load(Configuration.class.getResourceAsStream(CONF));
		} catch (Exception e) {
			LOG.error("Error loading configuration", e);
		}
	}
	
	/**
	 * Gets the value of property
	 * @param key identifier
	 * @return property value
	 */
	public static String get(String key){
		return PROPS.getProperty(key);
	}
	
	/**
	 * Gets the value of property as int
	 * @param key identifier
	 * @param defaultValue value if property not exists or wrong value
	 * @return property value
	 */
	public static int get(String key, int defaultValue){
		try {
			return Integer.parseInt(PROPS.getProperty(key));
		} catch (Exception e) {
			LOG.error("Error loading property", e);
		}
		
		return defaultValue;
	}

}
