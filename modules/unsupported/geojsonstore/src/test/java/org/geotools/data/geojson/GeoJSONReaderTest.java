/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import org.geotools.test.TestData;
import org.junit.Test;
import org.locationtech.jts.io.ParseException;

/** @author ian */
public class GeoJSONReaderTest {

    /**
     * Test method for {@link org.geotools.data.geojson.GeoJSONReader#getFeatures()}.
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testGetFeatures() throws IOException, ParseException {
        URL url = TestData.url(GeoJSONDataStore.class, "locations.json");
        GeoJSONReader reader = new GeoJSONReader(url);
        reader.getFeatures();
    }
}
