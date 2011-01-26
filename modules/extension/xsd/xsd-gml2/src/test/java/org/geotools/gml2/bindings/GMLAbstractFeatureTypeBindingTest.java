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
import org.geotools.gml2.TEST;
import org.geotools.gml2.TestConfiguration;
import org.geotools.xml.Binding;
import org.geotools.xml.Configuration;
import org.opengis.feature.simple.SimpleFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Point;


public class GMLAbstractFeatureTypeBindingTest extends GMLTestSupport {
    protected Configuration createConfiguration() {
        return new TestConfiguration();
    }

    protected void registerNamespaces(Element root) {
        super.registerNamespaces(root);
        root.setAttribute("xmlns:test", TEST.NAMESPACE);
    }

    public void testType() {
        assertEquals(SimpleFeature.class, binding(GML.AbstractFeatureType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(GML.AbstractFeatureType).getExecutionMode());
    }

    public void testParse() throws Exception {
        Element feature = GML2MockData.feature(document, document);
        feature.setAttributeNS(GML.NAMESPACE, "fid", "fid.1");

        SimpleFeature f = (SimpleFeature) parse();
        assertNotNull(feature);

        assertEquals("fid.1", f.getID());

        Point p = (Point) f.getDefaultGeometry();
        assertNotNull(p);
        assertEquals(1.0, p.getX(), 0d);
        assertEquals(2.0, p.getY(), 0d);

        Integer i = (Integer) f.getAttribute("count");
        assertNotNull(i);
        assertEquals(1, i.intValue());
    }

    public void testEncode() throws Exception {
        Document dom = encode(GML2MockData.feature(), TEST.TestFeature);
        print(dom);
        
        assertEquals(1, dom.getElementsByTagName("gml:boundedBy").getLength());
        assertEquals(1, dom.getElementsByTagName("test:geom").getLength());
        assertEquals(1, dom.getElementsByTagName("test:count").getLength());
        assertEquals(1, dom.getElementsByTagName("test:date").getLength());
    }
}
