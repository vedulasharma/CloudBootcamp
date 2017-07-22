package com.homedepot.fi.rv.storesettings.util;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;


@Configuration
public class RedisConfiguration {

	final static Logger LOGGER = LogManager.getLogger(RedisConfiguration.class);
	
	@Bean
	public JedisConnectionFactory connectionFactory() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		
		JSONObject vcapServices = null;
		JSONObject redisVCAP = null;
		
		try{
			vcapServices = getVcapServicesFromEnvironment();
		
			redisVCAP = getRedisVcapEntry(vcapServices);
		
			connectionFactory.setHostName(redisVCAP.getString("host"));
			connectionFactory.setPort(redisVCAP.getInt("port"));
			connectionFactory.setPassword(redisVCAP.getString("password"));
		}
		catch(IllegalStateException | JSONException ie){
			LOGGER.error(ie.getMessage(), ie);
		}
		return connectionFactory;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory());
		return redisTemplate;
	}

	private static JSONObject getVcapServicesFromEnvironment() {
		JSONObject vcapServices = null;

		Map<String, String> environment = System.getenv();
		String unconvertedVcapServices = null;

		if (environment.containsKey("VCAP_SERVICES")) {
			unconvertedVcapServices = environment.get("VCAP_SERVICES");
		}

		if (unconvertedVcapServices != null) {
			try {
				vcapServices = new JSONObject(unconvertedVcapServices);
			} catch (JSONException e) {
				throw new IllegalStateException("Unable to parse the VCAP_SERVICES environment variable. Database access may fail as a result");
			}
		} else {
			throw new IllegalStateException("The VCAP_SERVICES were not found in the environment. Connections will not be able to be made to the database.");
		}

		return vcapServices;
	}
	
	private static JSONObject getRedisVcapEntry(JSONObject vcapServices){
		
		JSONObject redisVCAP = null;
		JSONArray vcapServiceArray = null;
		
		try{
			//Get the p-redis entry
			vcapServiceArray = (JSONArray)vcapServices.get("p-redis");
			
			redisVCAP = (JSONObject)((JSONObject)vcapServiceArray.get(0)).get("credentials"); 
		}
		catch(JSONException je){
			throw new IllegalStateException("The p-redis entry was not found in VCAP Services.  The Redis Connection will not be established.");
		}
		
		return redisVCAP;
	}
}
