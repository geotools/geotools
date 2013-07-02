package org.geotools.coverage.io.driver;

import java.util.Set;

import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.impl.CoverageIO;
import org.junit.Test;

import junit.framework.Assert;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 *
 *
 *
 * @source $URL$
 */
public class TestDriverLookup extends Assert {
    
    @Test
    public void testDriver(){
        final Set<Driver> drivers = CoverageIO.getAvailableDrivers();
        assertFalse(drivers.isEmpty());
    }

}
