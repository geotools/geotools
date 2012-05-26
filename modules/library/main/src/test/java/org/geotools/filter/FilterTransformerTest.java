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
package org.geotools.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

import junit.framework.TestCase;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 * 
 * @source $URL$
 */
public class FilterTransformerTest extends TestCase {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    FilterTransformer transform = new FilterTransformer();

    @Override
    protected void setUp() throws Exception {
        // init xmlunit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("ogc", "http://www.opengis.net/ogc");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));    }
    
    public void testIdEncode() throws Exception {
        HashSet<FeatureId> set = new LinkedHashSet<FeatureId>();
        set.add(ff.featureId("FID.1"));
        set.add(ff.featureId("FID.2"));
        Filter filter = ff.id(set);

        String output = transform.transform(filter);
        assertNotNull("got xml", output);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ogc:FeatureId xmlns=\"http://www.opengis.net/ogc\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\" fid=\"FID.1\"/><ogc:FeatureId fid=\"FID.2\"/>";
        assertEquals("expected id filters", xml, output);
    }

    public void testEncodeLong() throws Exception {
        Filter filter = ff.greater(ff.property("MYATT"), ff.literal(50000000l));
        String output = transform.transform(filter);
        assertNotNull("got xml", output);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ogc:PropertyIsGreaterThan "
                + "xmlns=\"http://www.opengis.net/ogc\" xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\">"
                + "<ogc:PropertyName>MYATT</ogc:PropertyName>"
                + "<ogc:Literal>50000000</ogc:Literal></ogc:PropertyIsGreaterThan>";
        assertEquals(xml, output);
    }

    public void testEncodeSRSNameLonLat() throws Exception {
        // create a georeferenced geometry
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        Geometry point = new WKTReader().read("POINT(10 0)");
        point.setUserData(wgs84);

        // a filter on top of it
        Filter filter = ff.overlaps(ff.property("geom"), ff.literal(point));
        String output = transform.transform(filter);
        Document doc = XMLUnit.buildControlDocument(output);
        XMLAssert.assertXpathEvaluatesTo("EPSG:4326", "//gml:Point/@srsName", doc);
    }
    
    public void testEncodeSRSNameLatLon() throws Exception {
        // create a georeferenced geometry
        CoordinateReferenceSystem wgs84 = CRS.decode("urn:ogc:def:crs:EPSG::4326");
        Geometry point = new WKTReader().read("POINT(10 0)");
        point.setUserData(wgs84);

        // a filter on top of it
        Filter filter = ff.overlaps(ff.property("geom"), ff.literal(point));
        String output = transform.transform(filter);
        Document doc = XMLUnit.buildControlDocument(output);
        XMLAssert.assertXpathEvaluatesTo("urn:ogc:def:crs:EPSG::4326", "//gml:Point/@srsName", doc);
    }
}
