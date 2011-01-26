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
package org.geotools.gml2;

import java.io.InputStream;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.geotools.xml.StreamingParser;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Point;


public class GMLFeatureStreamingTest extends TestCase {
    public void testStreamByXpath() throws Exception {
        InputStream in = getClass().getResourceAsStream("feature.xml");
        String xpath = "/featureMember/TestFeature";

        StreamingParser parser = new StreamingParser(new TestConfiguration(), in, xpath);
        makeAssertions(parser);

        in.close();
    }

    public void testStreamByElementName() throws Exception {
        InputStream in = getClass().getResourceAsStream("feature.xml");

        StreamingParser parser = new StreamingParser(new TestConfiguration(), in,
                new QName(GML.NAMESPACE, "featureMember"));
        makeAssertions(parser);

        in.close();
    }

    private void makeAssertions(StreamingParser parser) {
        for (int i = 0; i < 3; i++) {
            SimpleFeature f = (SimpleFeature) parser.parse();
            assertNotNull(f);

            assertEquals(i + "", f.getID());
            assertEquals(i, ((Point) f.getDefaultGeometry()).getX(), 0d);
            assertEquals(i, ((Point) f.getDefaultGeometry()).getY(), 0d);
            assertEquals(i, ((Integer) f.getAttribute("count")).intValue());
        }

        assertNull(parser.parse());
    }
}
