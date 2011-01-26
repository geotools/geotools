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

import java.net.URL;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.TestCaseSupport;

import com.vividsolutions.jts.geom.Envelope;

public class ShpXmlFileReaderTest extends TestCase {
    ShpXmlFileReader reader;

    protected void setUp() throws Exception {
        super.setUp();
        URL example = TestData.url(TestCaseSupport.class, "example.shp.xml");
        ShpFiles shpFiles = new ShpFiles(example);

        reader = new ShpXmlFileReader(shpFiles);
    }

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
