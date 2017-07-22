package com.homedepot.fi.rv.storesettings.resource;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StoreSettingsResource {

	private static final Logger LOGGER = LogManager.getLogger(StoreSettingsResource.class);
	private static final String DEFAULT_STORE = "9999";
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@RequestMapping(value = "/store-settings/{storeNumber}", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody ResponseEntity<String> getStoreSettings(@PathVariable String storeNumber) {

		String storeSettings = null;
		
		try{
			ValueOperations<String, String> values = redisTemplate.opsForValue();
			
			storeSettings = values.get(storeNumber);
			
			if(storeSettings == null){
				//Get the default store.
				storeSettings = values.get(DEFAULT_STORE);
			}
			
			if(storeSettings == null){
				LOGGER.error("Unable to retrieve the default store settings for store number " + storeNumber);
				return new ResponseEntity<String>("Unable to retrieve the default store settings for store number " + storeNumber, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	
			return new ResponseEntity<>(storeSettings,HttpStatus.OK);
		}
		catch(Exception e){
			LOGGER.error("Unable to retrieve store settings for store number " + storeNumber);
			return new ResponseEntity<String>("Unable to retrieve store settings for store number " + storeNumber, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/reroute/{storeNumber}", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody ResponseEntity<String> reroute(@PathVariable String storeNumber) {

		String storeSettings = null;
		
		try{
			ValueOperations<String, String> values = redisTemplate.opsForValue();
			
			storeSettings = values.get(storeNumber);
			
			if(storeSettings == null){
				//Get the default store.
				storeSettings = values.get(DEFAULT_STORE);
			}
			
			if(storeSettings == null){
				LOGGER.error("Unable to retrieve the default store settings for store number " + storeNumber);
				return new ResponseEntity<String>("Unable to retrieve the default store settings for store number " + storeNumber, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			JSONObject settingsJSON = new JSONObject(storeSettings);
			
			String receivingURL = settingsJSON.getString("receivingUrl");
	
			URI url = new URI(receivingURL);
		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setLocation(url);
		    return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}
		catch(Exception e){
			LOGGER.error("Unable to retrieve store settings for store number " + storeNumber);
			return new ResponseEntity<String>("Unable to retrieve store settings for store number " + storeNumber, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/store-settings/{storeNumber}", method = RequestMethod.POST, produces = {"application/json"})
	public @ResponseBody ResponseEntity<String> updateStoreSettings(@PathVariable String storeNumber, 
																    @RequestBody String storeSettings) {

		try{
			ValueOperations<String, String> values = redisTemplate.opsForValue();
			
			values.set(storeNumber, storeSettings);
	
			return new ResponseEntity<>(storeSettings,HttpStatus.OK);
		}
		catch(Exception e){
			LOGGER.error("Unable to set store settings for store number " + storeNumber);
			return new ResponseEntity<String>("Unable to set store settings for store number " + storeNumber, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
