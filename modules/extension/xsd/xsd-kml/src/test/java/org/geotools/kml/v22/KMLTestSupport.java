package org.geotools.kml.v22;

import org.geotools.xsd.Configuration;
import org.geotools.xsd.test.XMLTestSupport;

public abstract class KMLTestSupport extends XMLTestSupport {

    @Override
    protected Configuration createConfiguration() {
        return new KMLConfiguration();
    }
}
