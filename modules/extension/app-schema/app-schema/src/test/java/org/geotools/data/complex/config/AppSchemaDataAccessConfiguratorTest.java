package org.geotools.data.complex.config;

import org.geotools.data.complex.AppSchemaDataAccessRegistry;
import org.geotools.test.AppSchemaTestSupport;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * This is to test app-schema joining configuration. Joining should be on by
 * default.
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * 
 */
public class AppSchemaDataAccessConfiguratorTest extends AppSchemaTestSupport {

	@Test
	public void testJoiningDefault() {
		// not set in the app-schema properties
		// joining should be on by default
		AppSchemaDataAccessRegistry.clearAppSchemaProperties();
		boolean joining = AppSchemaDataAccessConfigurator.isJoining();
		assertTrue(joining);
	}

	@Test
	public void testJoiningFalse() {
		// test joining set to false
		AppSchemaDataAccessRegistry.getAppSchemaProperties().setProperty(
				AppSchemaDataAccessConfigurator.PROPERTY_JOINING, "false");
		boolean joining = AppSchemaDataAccessConfigurator.isJoining();
		assertFalse(joining);
		AppSchemaDataAccessRegistry.clearAppSchemaProperties();
	}

	@Test
	public void testJoiningTrue() {
		// test joining set to true
		AppSchemaDataAccessRegistry.getAppSchemaProperties().setProperty(
				AppSchemaDataAccessConfigurator.PROPERTY_JOINING, "true");
		boolean joining = AppSchemaDataAccessConfigurator.isJoining();
		assertTrue(joining);
		AppSchemaDataAccessRegistry.clearAppSchemaProperties();
	}

}