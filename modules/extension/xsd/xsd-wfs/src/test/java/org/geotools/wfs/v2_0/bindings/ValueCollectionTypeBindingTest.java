/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import net.opengis.wfs20.ValueCollectionType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml3.v3_2.GML;
import org.geotools.wfs.PropertyValueCollection;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.opengis.feature.simple.SimpleFeatureType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

public class ValueCollectionTypeBindingTest extends WFSTestSupport {

    static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        registerNamespaceMapping("gt", "http://geotools.org");
    }
    public void testNothing(){
        // just to prevent build failure
    }
    public void testEncode() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("feature");
        tb.setNamespaceURI("http://geotools.org");
        tb.add("geom", Point.class);
        tb.add("str", String.class);
        tb.add("int", Integer.class);
        tb.add("date", Date.class);

        SimpleFeatureType featureType = tb.buildFeatureType();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureType);

        DefaultFeatureCollection features = new DefaultFeatureCollection(null,null);
        
        b.add(new WKTReader().read("POINT(0 0)"));
        b.add("zero");
        b.add("0");
        b.add(DATE_FORMAT.parse("2011-06-20 00:00:00"));
        features.add(b.buildFeature(null));
        
        b.add(new WKTReader().read("POINT(1 1)"));
        b.add("one");
        b.add("1");
        b.add(DATE_FORMAT.parse("2011-06-20 11:11:11"));
        features.add(b.buildFeature(null));

        ValueCollectionType vc = Wfs20Factory.eINSTANCE.createValueCollectionType();
        PropertyValueCollection valueCollection = new PropertyValueCollection(features, featureType.getDescriptor("geom"));
        vc.getMember().add(valueCollection);

        Document doc = encode(vc, WFS.ValueCollection);
        assertEquals("wfs:ValueCollection", doc.getDocumentElement().getNodeName());
        assertEquals(2, getElementsByQName(doc, WFS.member).getLength());
        
        NodeList geoms = getElementsByQName(doc, new QName("http://geotools.org", "geom")); 
        assertEquals(2, geoms.getLength());

        assertNotNull(getElementByQName((Element)geoms.item(0), GML.Point));
        assertNotNull(getElementByQName((Element)geoms.item(1), GML.Point));
        
        vc = Wfs20Factory.eINSTANCE.createValueCollectionType();
        vc.getMember().add(new PropertyValueCollection(features, featureType.getDescriptor("str")));

        doc = encode(vc, WFS.ValueCollection);
        assertEquals("wfs:ValueCollection", doc.getDocumentElement().getNodeName());
        assertEquals(2, getElementsByQName(doc, WFS.member).getLength());
        
        NodeList strs = getElementsByQName(doc, new QName("http://geotools.org", "str")); 
        assertEquals(2, strs.getLength());

        Set vals = new HashSet(Arrays.asList("zero", "one"));
        vals.remove(strs.item(0).getFirstChild().getNodeValue());
        vals.remove(strs.item(1).getFirstChild().getNodeValue());
        assertTrue(vals.isEmpty());
        
        vc = Wfs20Factory.eINSTANCE.createValueCollectionType();
        vc.getMember().add(new PropertyValueCollection(features, featureType.getDescriptor("date")));

        doc = encode(vc, WFS.ValueCollection);
        assertEquals("wfs:ValueCollection", doc.getDocumentElement().getNodeName());
        assertEquals(2, getElementsByQName(doc, WFS.member).getLength());
        
        //print(doc);
        NodeList dates = getElementsByQName(doc, new QName("http://geotools.org", "date")); 
        assertEquals(2, dates.getLength());
        
    }
}
