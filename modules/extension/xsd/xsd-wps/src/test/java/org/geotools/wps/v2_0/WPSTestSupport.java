package org.geotools.wps.v2_0;

import org.geotools.xsd.Configuration;
import org.geotools.xsd.test.XMLTestSupport;

/**
 * Base test class for the http://www.opengis.net/wps/2.0 schema.
 *
 * @generated
 */
public class WPSTestSupport extends XMLTestSupport {

    @Override
    protected Configuration createConfiguration() {
        return new WPSConfiguration();
    }
}
