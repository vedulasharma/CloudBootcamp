package com.homedepot.fi.rv.storesettings.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.homedepot.fi.rv.storesettings.AppConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = AppConfig.class)
@ActiveProfiles("int-test")
public class StoreSettingsResourceTest {

	private static Logger LOGGER = LogManager.getLogger(StoreSettingsResourceTest.class);
	@Test
	public void testSuccess(){
		
		LOGGER.error("Test Log Message");
		Assert.assertTrue(true);
	}
}
