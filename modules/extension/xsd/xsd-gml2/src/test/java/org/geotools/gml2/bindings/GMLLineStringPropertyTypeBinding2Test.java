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
import org.geotools.xsd.Binding;
import org.locationtech.jts.geom.LineString;
import org.w3c.dom.Document;

public class GMLLineStringPropertyTypeBinding2Test extends GMLTestSupport {
    public void testType() {
        assertEquals(LineString.class, binding(GML.LineStringPropertyType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GML.LineStringPropertyType).getExecutionMode());
    }

    public void testParse() throws Exception {
        GML2MockData.lineStringProperty(document, document);

        LineString line = (LineString) parse();
        assertNotNull(line);
    }

    public void testEncode() throws Exception {
        Document doc = encode(GML2MockData.lineString(), GML.lineStringProperty);

        assertEquals(
                1,
                doc.getElementsByTagNameNS(GML.NAMESPACE, GML.LineString.getLocalPart())
                        .getLength());
    }
}
