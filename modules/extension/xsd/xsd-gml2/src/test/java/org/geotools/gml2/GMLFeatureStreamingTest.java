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
import org.geotools.xsd.StreamingParser;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.geotools.api.feature.simple.SimpleFeature;

public class GMLFeatureStreamingTest {
    @Test
    public void testStreamByXpath() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("feature.xml")) {
            String xpath = "/featureMember/TestFeature";

            StreamingParser parser = new StreamingParser(new TestConfiguration(), in, xpath);
            makeAssertions(parser);
        }
    }

    @Test
    public void testStreamByElementName() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("feature.xml")) {

            StreamingParser parser =
                    new StreamingParser(
                            new TestConfiguration(), in, new QName(GML.NAMESPACE, "featureMember"));
            makeAssertions(parser);
        }
    }

    private void makeAssertions(StreamingParser parser) {
        for (int i = 0; i < 3; i++) {
            SimpleFeature f = (SimpleFeature) parser.parse();
            Assert.assertNotNull(f);

            Assert.assertEquals(i + "", f.getID());
            Assert.assertEquals(i, ((Point) f.getDefaultGeometry()).getX(), 0d);
            Assert.assertEquals(i, ((Point) f.getDefaultGeometry()).getY(), 0d);
            Assert.assertEquals(i, ((Integer) f.getAttribute("count")).intValue());
        }

        Assert.assertNull(parser.parse());
    }
}
