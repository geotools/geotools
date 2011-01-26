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
package org.geotools.kml.bindings;

import org.opengis.feature.simple.SimpleFeature;
import org.geotools.feature.FeatureCollection;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;


public class KmlTypeBindingTest extends KMLTestSupport {
    public void testType() throws Exception {
        assertEquals(FeatureCollection.class, binding(KML.KmlType).getType());
    }

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.OVERRIDE, binding(KML.KmlType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<kml>" + "<Document>" + "<name>document</name>" + "</Document>" + "</kml>";
        buildDocument(xml);

        SimpleFeature document = (SimpleFeature) parse();
        assertEquals("document", document.getAttribute("name"));
    }
}
