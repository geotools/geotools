package org.geotools.filter.v2_0;

import org.geotools.xml.Configuration;
import org.geotools.xml.test.XMLTestSupport;

public class FESTestSupport extends XMLTestSupport {

    @Override
    protected Configuration createConfiguration() {
        return new FESConfiguration();
    }

}
