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
package org.geotools.gml2.bindings;

import org.geotools.gml2.GML;
import org.geotools.referencing.CRS;
import org.geotools.xml.Binding;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;


public class GMLAbstractGeometryTypeBindingTest extends GMLTestSupport {
    public void testType() {
        assertEquals(Geometry.class, binding(GML.AbstractGeometryType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GML.AbstractGeometryType).getExecutionMode());
    }

    public void testParse() throws Exception {
        GML2MockData.point(document, document);
        document.getDocumentElement().setAttribute("srsName", "EPSG:4326");

        Point p = (Point) parse();
        assertTrue(p.getUserData() instanceof CoordinateReferenceSystem);
    }

    public void testEncode() throws Exception {
        Point p = GML2MockData.point();
        p.setUserData(CRS.decode("EPSG:4326", true));

        Document doc = encode(p, GML.Point);
        assertEquals("http://www.opengis.net/gml/srs/epsg.xml#4326",
            doc.getDocumentElement().getAttribute("srsName"));
    }
}
