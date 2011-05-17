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

package org.geotools.data.sfs;

import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Intersects;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;


public class SFSFilterVisitorTest extends TestCase {
    private static final String URL = "http://localhost:8082/simplefeatureservice/";
    private static final String URL_LAYER_ASIA = URL+"/layerAsia";
    private static final String PROPERTY_NAME = "aProperty";
    private static final String PROPERTY_VALUE = "342";
    private static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);

    public void testBBOX() throws Exception {
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        BBOX bbox = FF.bbox("prop0", 0, 0, 10, 10, "EPSG:4326");
        visitor.visit(bbox, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?bbox=0.0,0.0,10.0,10.0", URLDecoder.decode(builder.toString(),"UTF-8"));
    }
    
    /* This test was added */
    public void testDWithin() throws Exception {
    	
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        /* Testing for point geometry*/
        GeometryFactory gf = new GeometryFactory();
        Point p = gf.createPoint(new Coordinate(100.1, 0.1));

        DWithin dws = FF.dwithin(FF.property("Point"), FF.literal(p), 10, "");
        visitor.visit(dws, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?epsg=&lat=0.1&lon=100.1&tolerance=10.0", URLDecoder.decode(builder.toString(),"UTF-8"));
    }
    
    /* Test for point geometry but it can be tested for other also */
    public void testIntersect() throws Exception {
    	
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        /* Testing for point geometry*/
        GeometryFactory gf = new GeometryFactory();
        Point p = gf.createPoint(new Coordinate(100.1, 0.1));
        
        Intersects intr = FF.intersects(FF.property("Point"), FF.literal(p));
        visitor.visit(intr, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?geometry={\"type\":\"Point\",\"coordinates\":[100.1,0.1]}", URLDecoder.decode(builder.toString(),"UTF-8"));
    }

    public void testPropertyIsEqualTo() throws Exception {
    	
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        PropertyIsEqualTo filter = FF.equals(FF.property(PROPERTY_NAME), FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?" + PROPERTY_NAME + "__eq=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME,URLDecoder.decode(builder.toString(),"UTF-8"));
    }

    public void testPropertyIsNotEqualTo() throws Exception {
    	
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        PropertyIsNotEqualTo filter = FF.notEqual(FF.property(PROPERTY_NAME),
                FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?" + PROPERTY_NAME + "__ne=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, URLDecoder.decode(builder.toString(),"UTF-8"));
    }

    public void testPropertyIsGreaterThan() throws Exception {
    
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        PropertyIsGreaterThan filter = FF.greater(FF.property(PROPERTY_NAME),
                FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?" + PROPERTY_NAME + "__gt=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, URLDecoder.decode(builder.toString(),"UTF-8"));
    }

    public void testPropertyIsGreaterThanOrEqualTo() throws Exception {
    	
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        PropertyIsGreaterThanOrEqualTo filter = FF.greaterOrEqual(FF.property(PROPERTY_NAME),
                FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?" + PROPERTY_NAME + "__gte=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, URLDecoder.decode(builder.toString(),"UTF-8"));
    }

    public void testPropertyIsLessThan() throws Exception {
    	
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        PropertyIsLessThan filter = FF.less(FF.property(PROPERTY_NAME), FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?" + PROPERTY_NAME + "__lt=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, URLDecoder.decode(builder.toString(),"UTF-8"));
    }

    public void testPropertyIsLessThanOrEqualTo() throws Exception {
    	
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        PropertyIsLessThanOrEqualTo filter = FF.lessOrEqual(FF.property(PROPERTY_NAME),
                FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?" + PROPERTY_NAME + "__lte=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, URLDecoder.decode(builder.toString(),"UTF-8"));
    }

    public void testPropertyIsLike() throws Exception {
        StringBuilder builder = new StringBuilder(URL_LAYER_ASIA);
        SFSFilterVisitor visitor = new SFSFilterVisitor(true);
        PropertyIsLike filter = FF.like(FF.property(PROPERTY_NAME), PROPERTY_VALUE, "%", "_", "\\", true);
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        assertEquals(URL_LAYER_ASIA + "?" + PROPERTY_NAME + "__like=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, URLDecoder.decode(builder.toString(),"UTF-8"));
    }
    
    public void testHints() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("CFCC", String.class);
        tb.add("NAME", String.class);
        tb.add("the_geom", MultiLineString.class);
        tb.setName("layerAsia");
        SimpleFeatureType ft = tb.buildFeatureType();
        
        
        Query q = new DefaultQuery("layerAsia");
        q.setFilter(FF.less(FF.property(PROPERTY_NAME), FF.literal(PROPERTY_VALUE)));
        Map<String, String> vtParams = new LinkedHashMap<String, String>();
        vtParams.put("first", "a=b");
        vtParams.put("second", "a%b");
        q.setHints(new Hints(Hints.VIRTUAL_TABLE_PARAMETERS, vtParams));
        
        String result = SFSDataStoreUtil.encodeQuery(q, ft);
        assertEquals("aProperty__lt=342&queryable=aProperty&hints=first:a%3Db;second:a%25b", result);
    }
    

}
