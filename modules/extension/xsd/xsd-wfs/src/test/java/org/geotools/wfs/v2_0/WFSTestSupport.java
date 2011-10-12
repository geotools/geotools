package org.geotools.wfs.v2_0;

import org.geotools.xml.Configuration;
import org.geotools.xml.test.XMLTestSupport;

public class WFSTestSupport extends XMLTestSupport {

    @Override
    protected Configuration createConfiguration() {
        return new WFSConfiguration();
    }

}
