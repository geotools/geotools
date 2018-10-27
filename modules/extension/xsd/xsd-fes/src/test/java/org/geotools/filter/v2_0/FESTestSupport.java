package org.geotools.filter.v2_0;

import org.geotools.xsd.Configuration;
import org.geotools.xsd.test.XMLTestSupport;

public class FESTestSupport extends XMLTestSupport {

    @Override
    protected Configuration createConfiguration() {
        return new FESConfiguration();
    }
}
