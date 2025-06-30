/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import net.opengis.wmts.v_1.CapabilitiesType;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.test.TestData;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xsd.Parser;

/** @author Emanuele Tajariol (etj AT geo-solutions DOT it) */
public class WMTSTestUtils {

    public static WMTSCapabilities createCapabilities(String capFile) throws Exception {
        File getCaps = TestData.file(null, capFile);
        assertNotNull(getCaps);

        Parser parser = new Parser(new WMTSConfiguration());

        Object object = parser.parse(new FileReader(getCaps, StandardCharsets.UTF_8));
        assertTrue("Capabilities failed to parse " + object.getClass(), object instanceof CapabilitiesType);

        WMTSCapabilities capabilities = new WMTSCapabilities((CapabilitiesType) object);
        return capabilities;
    }
}
