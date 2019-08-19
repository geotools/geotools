/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.ows.wmts.model;

import static org.geotools.ows.wmts.WMTSTestUtils.createCapabilities;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class WMTSServiceTest {

    public WMTSServiceTest() {}

    @Test
    public void testParser2() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("admin_ch.getcapa.xml");
        try {
            WMTSService service = (WMTSService) capabilities.getService();
            assertNotNull(service.get_abstract());
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            if ((e.getMessage() != null) && e.getMessage().indexOf("timed out") > 0) {
                // System.err.println("Unable to test - timed out: " + e);
            } else {
                throw (e);
            }
        }
    }
}
