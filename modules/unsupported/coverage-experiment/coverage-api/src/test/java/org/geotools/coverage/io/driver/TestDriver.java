package org.geotools.coverage.io.driver;

import java.util.EnumSet;

import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.impl.DefaultDriver;
import org.geotools.factory.Hints;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @source $URL$
 */
public class TestDriver extends DefaultDriver implements Driver {

    @Override
    public boolean isAvailable() {
        return true;
    }

    public TestDriver() {
        super("test driver", "test driver", "test driver",EnumSet.of(DriverCapabilities.CONNECT), new Hints());
    }


}
