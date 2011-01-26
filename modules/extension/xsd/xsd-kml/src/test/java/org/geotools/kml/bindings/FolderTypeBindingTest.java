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

import java.util.Collection;
import org.opengis.feature.simple.SimpleFeature;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;


public class FolderTypeBindingTest extends KMLTestSupport {
    public void testType() throws Exception {
        assertEquals(SimpleFeature.class, binding(KML.FolderType).getType());
    }

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.AFTER, binding(KML.FolderType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<Folder>" + "<name>folder</name>" + "<Placemark>" + "<Point>"
            + "<coordinates>0,0</coordinates>" + "</Point>" + "</Placemark>" + "</Folder>";
        buildDocument(xml);

        SimpleFeature document = (SimpleFeature) parse();
        assertEquals("folder", document.getAttribute("name"));

        Collection features = (Collection) document.getAttribute("Feature");
        assertEquals(1, features.size());
    }
}
