/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import org.geotools.ows.wmts.WMTSSpecification.GetCapsRequest;
import org.junit.Test;

/** @author ian */
public class WMTSSpecificationTest {

    /**
     * Test for GEOT-6677 *
     *
     * @throws MalformedURLException
     */
    @Test
    public void testProcessKey() throws MalformedURLException {

        String[] keys = {"api-key", "request"};
        String[] expected = {"api-key", "REQUEST"};
        for (int i = 0; i < keys.length; i++) {
            String obs = WMTSSpecification.processKey(keys[i]);
            assertEquals(expected[i], obs);
        }

        URL url = new URL("http://test.example.com/geoserver/wms?tk=42");
        WMTSSpecification.GetCapsRequest caps = new GetCapsRequest(url);
        URL out = caps.getFinalURL();
        assertTrue(out.toString().contains("tk"));
    }
}
