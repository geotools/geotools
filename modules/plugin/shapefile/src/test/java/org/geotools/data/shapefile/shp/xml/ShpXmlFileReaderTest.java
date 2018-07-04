/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.shp.xml;

import static org.junit.Assert.*;

import java.net.URL;
import org.geotools.TestData;
import org.geotools.data.shapefile.TestCaseSupport;
import org.geotools.data.shapefile.files.ShpFiles;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

/** @source $URL$ */
public class ShpXmlFileReaderTest {
    ShpXmlFileReader reader;

    @Before
    public void setUp() throws Exception {
        URL example = TestData.url(TestCaseSupport.class, "example.shp.xml");
        ShpFiles shpFiles = new ShpFiles(example);

        reader = new ShpXmlFileReader(shpFiles);
    }

    @Test
    public void testBBox() {
        Metadata meta = reader.parse();
        assertNotNull("meta", meta);
        IdInfo idInfo = meta.getIdinfo();
        assertNotNull("idInfo", idInfo);
        Envelope bounding = idInfo.getBounding();
        assertNotNull(bounding);
        assertEquals(-180.0, bounding.getMinX(), 0.00001);
        assertEquals(180.0, bounding.getMaxX(), 0.00001);
        assertEquals(-90.0, bounding.getMinY(), 0.00001);
        assertEquals(90.0, bounding.getMaxY(), 0.00001);
    }
}
