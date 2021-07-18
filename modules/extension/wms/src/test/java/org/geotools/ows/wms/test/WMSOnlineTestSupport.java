/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.ows.wms.test;

import java.net.URL;
import java.util.Properties;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.test.OnlineTestCase;

/**
 * Set up for WMS Online tests
 *
 * @author ian
 */
public abstract class WMSOnlineTestSupport extends OnlineTestCase {

    protected URL serverURL;
    URL brokenURL;

    /** */
    public WMSOnlineTestSupport() {
        super();
    }

    @Override
    protected void setUpInternal() throws Exception {
        String kvp_prop = fixture.getProperty("local_geoserver");
        serverURL = new URL(kvp_prop);

        brokenURL = new URL("http://afjklda.com");
    }

    @Override
    protected String getFixtureId() {
        return "wms";
    }

    @Override
    protected Properties createExampleFixture() {
        Properties example = new Properties();
        example.put("local_geoserver", "http://localhost:8080/geoserver/wms?");
        example.put("remote_geoserver", "http://demo.geosolutions.com/geoserver/wms?");
        example.put(
                "esri_wms",
                "https://tigerweb.geo.census.gov/arcgis/services/TIGERweb/tigerWMS_ACS2015/MapServer/WMSServer?");

        return example;
    }

    @Override
    protected boolean isOnline() throws Exception {
        String kvp_prop = fixture.getProperty("local_geoserver");
        URL url = new URL(kvp_prop);
        WebMapServer wms = new WebMapServer(url);

        return wms.getCapabilities() != null;
    }
}
