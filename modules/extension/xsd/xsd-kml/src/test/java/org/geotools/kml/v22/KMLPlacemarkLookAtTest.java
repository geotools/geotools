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
package org.geotools.kml.v22;

import java.util.List;
import org.geotools.xsd.Parser;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;

public class KMLPlacemarkLookAtTest extends KMLTestSupport {

    public void testParseDocument() throws Exception {
        Parser parser = new Parser(createConfiguration());
        SimpleFeature doc =
                (SimpleFeature) parser.parse(getClass().getResourceAsStream("geot5666.kml"));
        assertNotNull(doc);
        assertEquals("document", doc.getType().getTypeName());
        assertEquals("GEOT-5666", doc.getAttribute("name"));
        List features = (List) doc.getAttribute("Feature");
        assertEquals(1, features.size());
        SimpleFeature placemark = (SimpleFeature) features.get(0);

        assertEquals("Placemark with LookAt", placemark.getAttribute("name"));

        Point lookat = (Point) placemark.getAttribute("LookAt");
        assertEquals(149.1717, lookat.getX(), 0.0001);
        assertEquals(-35.3446, lookat.getY(), 0.0001);

        Point geometry = (Point) placemark.getDefaultGeometry();
        assertEquals(149.2884, geometry.getX(), 0.0001);
        assertEquals(-35.1779, geometry.getY(), 0.0001);
    }
}
