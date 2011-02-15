package org.geotools.coverage.io.driver;

import org.geotools.coverage.io.driver.DefaultDriver;
import org.geotools.coverage.io.driver.Driver;
import org.geotools.factory.Hints;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 */
public class TestDriver extends DefaultDriver implements Driver {

    @Override
    public boolean isAvailable() {
        return true;
    }

    protected TestDriver() {
        super("test driver", "test driver", "test driver",new Hints());
    }


}
