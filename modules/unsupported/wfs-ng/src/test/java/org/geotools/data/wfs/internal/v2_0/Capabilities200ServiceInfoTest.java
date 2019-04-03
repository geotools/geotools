/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal.v2_0;

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import net.opengis.wfs20.WFSCapabilitiesType;
import org.geotools.wfs.v2_0.WFSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Capabilities200ServiceInfo}.
 *
 * @author Matthias Schulze (LDBV at ldbv dot bayern dot de)
 */
public class Capabilities200ServiceInfoTest {
    public static final String SERVER_URL =
            "http://laermkartierung1.eisenbahn-bundesamt.de/deegree/services/wfs?service=WFS&request=GetCapabilities"; // $NON-NLS-1$

    private Capabilities200ServiceInfo featureType;

    @Before
    public void setup() {
        try {
            URL getCapsUrl = new URL(SERVER_URL);
            WFSConfiguration configuration = new org.geotools.wfs.v2_0.WFSConfiguration();
            Parser parser = new Parser(configuration);
            WFSCapabilitiesType type = (WFSCapabilitiesType) parser.parse(getCapsUrl.openStream());
            featureType =
                    new Capabilities200ServiceInfo(
                            "http://schemas.opengis.net/wfs/2.0/wfs.xsd", getCapsUrl, type);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // One parameter, no view params, no mappings => no parameters
    @Test
    public void testGetDescription() {
        String descResult = featureType.getDescription();
        assertNotNull(descResult);
    }

    // One parameter, no view params, no mappings => no parameters
    @Test
    public void testGetTitle() {
        String descResult = featureType.getTitle();
        assertNotNull(descResult);
    }
}
