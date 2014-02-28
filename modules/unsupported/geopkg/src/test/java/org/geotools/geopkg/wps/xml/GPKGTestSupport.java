package org.geotools.geopkg.wps.xml;


import org.geotools.geopkg.wps.xml.GPKGConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.test.XMLTestSupport;

/**
 * Base test class for the http://www.opengis.net/gpkg schema.
 *
 * @generated
 */
public class GPKGTestSupport extends XMLTestSupport {

    protected Configuration createConfiguration() {
       return new GPKGConfiguration();
    }
  
} 