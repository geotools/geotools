package org.geotools.coverage.io.range.impl;

import java.util.Set;

import org.geotools.coverage.io.driver.CoverageIO;
import org.geotools.coverage.io.driver.Driver;
import org.junit.Test;

import junit.framework.Assert;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 */
public class TestDriverLookup extends Assert {
    
    @Test
    public void testDriver(){
        final Set<Driver> drivers = CoverageIO.getAvailableDrivers();
        assertFalse(drivers.isEmpty());
        assertFalse(drivers.isEmpty());
    }

}
