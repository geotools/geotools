/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;


public class FilterSplitterTest extends TestCase {
    protected FilterFactory ff;
    protected Filter bb;
    protected Filter bb2;
    protected Filter att;
    protected Filter bso;
    protected Envelope bbenv;
    protected Envelope bb2env;
    protected Envelope bsoenv;
    
    protected void setUp() {
        ff = new FilterFactoryImpl();
        bb = ff.bbox("geom", 0, 10, 1000, 1100, "srs");
        bbenv = new Envelope(0, 1000, 10, 1100);
        bb2 = ff.bbox("geom", 500, 510, 1500, 1600, "srs");
        bb2env = new Envelope(500, 1500, 510, 1600);
        att = ff.like(ff.property("dummydata"), "Id: 1*");

        Polygon p = createPolygon();
        bso = ((FilterFactoryImpl) ff).intersects(ff.property("the_geom"), ff.literal(p));
        bsoenv = p.getEnvelopeInternal();
    }

    protected static Polygon createPolygon() {
        Coordinate[] coords = new Coordinate[] {
                new Coordinate(200, 210), new Coordinate(200, 500), new Coordinate(2000, 2100),
                new Coordinate(500, 510), new Coordinate(200, 210)
            };
        CoordinateArraySequence seq = new CoordinateArraySequence(coords);
        LinearRing ls = new LinearRing(seq, new GeometryFactory());
        Polygon ret = new Polygon(ls, null, new GeometryFactory());

        return ret;
    }

    public static Test suite() {
        return new TestSuite(FilterSplitterTest.class);
    }

    public void testBBoxFilter() {
        BBoxFilterSplitter splitter = new BBoxFilterSplitter();
        bb.accept(splitter, null);
        assertEquals(bbenv, splitter.getEnvelope());

        Filter result = splitter.getFilterPre();
        assertEquals(bb, result);
    }

    public void testAndFilter() {
        BBoxFilterSplitter splitter = new BBoxFilterSplitter();
        Filter and = ff.and(bb, att);
        and.accept(splitter, null);       
        assertEquals(bbenv, splitter.getEnvelope());
        assertEquals(bb, splitter.getFilterPre());
        assertEquals(and, splitter.getFilterPost());
        
        
        and = ff.and(bb, bb2);
        splitter = new BBoxFilterSplitter();
        and.accept(splitter, null);
        Envelope i = new Envelope(bbenv);
        i.expandToInclude(bb2env);
        Envelope s = splitter.getEnvelope();
        assertEquals(i, s);
        assertEquals(and, splitter.getFilterPost());
    }

    public void testOrFilter() {
        BBoxFilterSplitter splitter = new BBoxFilterSplitter();
        
        //tests or with an geometry and attribute -> should include everything
        Filter or = ff.or(bb, att);
        or.accept(splitter, null);
        assertNotNull(splitter.getEnvelope());
        assertEquals(Filter.INCLUDE, splitter.getFilterPre());
        assertEquals(or, splitter.getFilterPost());
        
        //tests or with a two geometry filters
        or = ff.or(bb, bb2);
        splitter = new BBoxFilterSplitter();
        or.accept(splitter, null);
        bbenv.expandToInclude(bb2env);
        assertEquals(bbenv, splitter.getEnvelope());
        //assertEquals(or, splitter.getFilterPost());
        assertEquals(Filter.INCLUDE, splitter.getFilterPost());
    }

    public void testNotFilter() {
        BBoxFilterSplitter splitter = new BBoxFilterSplitter();
        Filter not = ff.not(bb);
        not.accept(splitter, null);
        assertEquals(Filter.INCLUDE, splitter.getFilterPre());
        assertEquals(not, splitter.getFilterPost());
        
        //tests:  bb2 AND (NOT bb)
        Filter and = ff.and(bb2, not);
        splitter = new BBoxFilterSplitter();
        and.accept(splitter, null);

        assertEquals(bb2, splitter.getFilterPre());
        assertEquals(and, splitter.getFilterPost());
        
    }

    public void testBinarySpatialOperator() {
        BBoxFilterSplitter splitter = new BBoxFilterSplitter();
        Filter and = ff.and(bb, bso);
        and.accept(splitter, null);
        Envelope testenv = bbenv;
        testenv.expandToInclude(bsoenv);
        assertEquals(testenv, splitter.getEnvelope());
        assertEquals(and, splitter.getFilterPost());
        
        SimpleFeature sf = createTestFeature();
        assertTrue(splitter.getFilterPre().evaluate(sf));
    }

    public void testAndManyFilters() {
        BBoxFilterSplitter splitter = new BBoxFilterSplitter();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(bb);
        filters.add(bb2);
        filters.add(att);
        filters.add(bso);

        Filter and = ff.and(filters);
        and.accept(splitter, null);
        Envelope tmp = new Envelope(bbenv);
        tmp.expandToInclude(bb2env);
        tmp.expandToInclude(bsoenv);
        assertEquals(tmp, splitter.getEnvelope());
        assertEquals(and, splitter.getFilterPost());
        
        Filter bbox = ff.bbox("geom", 500, 210, 2000, 200, "srs");
        
        Coordinate[] coords = new Coordinate[] {
                new Coordinate(205, 455), new Coordinate(205, 500), 
                new Coordinate(210, 500),
                new Coordinate(210, 455), new Coordinate(205, 455)
            };
        CoordinateArraySequence seq = new CoordinateArraySequence(coords);
        LinearRing ls = new LinearRing(seq, new GeometryFactory());
        Polygon ret = new Polygon(ls, null, new GeometryFactory());
        Filter intersects = ((FilterFactoryImpl) ff).intersects(ff.property("geom"), ff.literal(ret));

        and = ff.and(intersects, bbox);
        splitter = new BBoxFilterSplitter();
        and.accept(splitter, null);
        
        tmp = new Envelope(500, 2000, 200, 210);
        tmp.expandToInclude(ret.getEnvelopeInternal());
        assertEquals(ff.bbox("geom", tmp.getMinX(), tmp.getMinY(), tmp.getMaxX(), tmp.getMaxY(), "srs"), splitter.getFilterPre());
        assertEquals(and, splitter.getFilterPost());
        
        
        
    }

    public void testOrManyFilters() {
        BBoxFilterSplitter splitter = new BBoxFilterSplitter();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(bb);
        filters.add(bb2);
        filters.add(att);
        filters.add(bso);

        Filter or = ff.or(filters);
        or.accept(splitter, null);

        Envelope e = new Envelope(bbenv);
        e.expandToInclude(bb2env);
        e.expandToInclude(bsoenv);
        assertEquals(e, splitter.getEnvelope());
        assertEquals(or, splitter.getFilterPost());
    }

    public void testOrAndAndManyFilters() {
        Envelope testenv = new Envelope(bb2env);
        testenv.expandToInclude(bbenv);
        Filter testfilter = ff.bbox("geom", testenv.getMinX(), testenv.getMinY(), testenv.getMaxX(), testenv.getMaxY(), "srs");
    	
        //tests:
    	//(bbox or bbox) and attribute
        BBoxFilterSplitter splitter = new BBoxFilterSplitter();
    	Filter or = ff.or(bb, bb2);
    	Filter and = ff.and(or, att);
    	and.accept(splitter, null);
    	assertNotNull(splitter.getEnvelope());
    	assertEquals(testfilter, splitter.getFilterPre());
    	assertEquals(and, splitter.getFilterPost());
    	
    	//tests:
    	//(bbox and bbox) or attribute
    	splitter = new BBoxFilterSplitter();
    	and = ff.and(bb, bb2);
    	or = ff.or(and, att);
    	or.accept(splitter, null);
    	assertNotNull(splitter.getEnvelope());
    	assertEquals(Filter.INCLUDE, splitter.getFilterPre());
    	assertEquals(or, splitter.getFilterPost());
    	
    	//tests:
    	//(bbox or bbox) and bbox
    	splitter = new BBoxFilterSplitter();
    	or = ff.or(bb, bb2);
    	and = ff.and(or, bb);
    	and.accept(splitter, null);
    	assertNotNull(splitter.getEnvelope());
    	assertEquals(testfilter, splitter.getFilterPre());
    	assertEquals(and, splitter.getFilterPost());
    	
    	//tests:
    	//(bbox or attribute) and bbox
    	splitter = new BBoxFilterSplitter();
    	or = ff.or(bb, att);
    	and = ff.and(or, bb);
    	and.accept(splitter, null);
    	assertNotNull(splitter.getEnvelope());
    	assertEquals(bb, splitter.getFilterPre());
    	assertEquals(and, splitter.getFilterPost());
    	
    	//tests:
    	//(bbox and attribute) or bbox2
    	splitter = new BBoxFilterSplitter();
    	and = ff.and(bb, att);
    	or = ff.or(and, bb2);
    	or.accept(splitter, null);
    	assertNotNull(splitter.getEnvelope());
    	assertEquals(testfilter, splitter.getFilterPre());
    	assertEquals(or, splitter.getFilterPost());
    	
    	//tests:
    	//(bbox and attribute) or (bb2 and attribute);
    	splitter = new BBoxFilterSplitter();
    	Filter and1 = ff.and(bb, att);
    	Filter and2 = ff.and(bb2, att);
    	or = ff.or(and1, and2);
    	or.accept(splitter, null);
    	assertNotNull(splitter.getEnvelope());
    	assertEquals(testfilter, splitter.getFilterPre());
    	assertEquals(or, splitter.getFilterPost());
    	
    	//tests:
    	//(bbox or attribute) and (bb2 or attribute);
    	splitter = new BBoxFilterSplitter();
    	Filter or1 = ff.or(bb, att);
    	Filter or2 = ff.or(bb2, att);
    	and = ff.and(or1, or2);
    	and.accept(splitter, null);
    	assertNotNull(splitter.getEnvelope());
    	assertEquals(Filter.INCLUDE, splitter.getFilterPre());
    	assertEquals(and, splitter.getFilterPost());
    }
    
    /**
     * Tests filters that contain include filters
     */
    public void testIncludeFilter(){
    	BBoxFilterSplitter splitter = new BBoxFilterSplitter();
    	List<Filter> filters = new ArrayList<Filter>();    	
    	Filter includeFilter = Filter.INCLUDE;
    	
    	includeFilter.accept(splitter, null);
    	assertEquals(Filter.INCLUDE, splitter.getFilterPre());
    	assertEquals(Filter.INCLUDE, splitter.getFilterPost());
    	
    	//test include with another geometry filter using or
    	splitter = new BBoxFilterSplitter();
    	filters.add(includeFilter);
    	filters.add(bb2);
    	filters.add(bb);
        Filter or = ff.or(filters);
        or.accept(splitter, null);
        assertEquals(Filter.INCLUDE, splitter.getFilterPre());
        assertEquals(Filter.INCLUDE, splitter.getFilterPost());
        
        //test include with another geometry filter and attribute filter using or
    	splitter = new BBoxFilterSplitter();
    	filters = new ArrayList<Filter>();
    	filters.add(includeFilter);
    	filters.add(bb2);
    	filters.add(bb);
    	filters.add(att);
        or = ff.or(filters);
        or.accept(splitter, null);
        assertEquals(Filter.INCLUDE, splitter.getFilterPre());
        assertEquals(or, splitter.getFilterPost());        
        
        //test include with another geometry filter using and
    	splitter = new BBoxFilterSplitter();
    	filters = new ArrayList<Filter>();
    	filters.add(includeFilter);
    	filters.add(bb);
        Filter and = ff.and(filters);
        and.accept(splitter, null);
        assertEquals(bb, splitter.getFilterPre());
        assertEquals(and, splitter.getFilterPost());		//if pre incorporates all geometries then we need nothing post
        
        //test include with another geometry filter using and with attribute 
    	splitter = new BBoxFilterSplitter();
    	filters = new ArrayList<Filter>();
    	filters.add(includeFilter);
    	filters.add(bb);
    	filters.add(att);
        and = ff.and(filters);
        and.accept(splitter, null);
        assertEquals(bb, splitter.getFilterPre());
        assertEquals(and, splitter.getFilterPost());
    }
    /**
     * Tests filters that contain exclude filters
     */
    public void testExcludeFilter(){
    	BBoxFilterSplitter splitter = new BBoxFilterSplitter();
    	List<Filter> filters = new ArrayList<Filter>();    	
    	Filter excludeFilter = Filter.EXCLUDE;
    	
    	excludeFilter.accept(splitter, null);
    	assertEquals(Filter.EXCLUDE, splitter.getFilterPre());
    	assertEquals(Filter.INCLUDE, splitter.getFilterPost());
    	
    	//test include with another geometry filter using or
    	splitter = new BBoxFilterSplitter();
    	filters.add(excludeFilter);
    	filters.add(bb2);
    	filters.add(bb);
        Filter or = ff.or(filters);
        or.accept(splitter, null);
 
        Envelope testenv = new Envelope(bb2env);
        testenv.expandToInclude(bbenv);
        Filter testfilter = ff.bbox("geom", testenv.getMinX(), testenv.getMinY(), testenv.getMaxX(), testenv.getMaxY(), "srs");
        assertEquals(testfilter, splitter.getFilterPre());
        assertEquals(Filter.INCLUDE, splitter.getFilterPost());
        
        //add exclude filter last
        splitter = new BBoxFilterSplitter();
        filters = new ArrayList<Filter>();
        filters.add(bb);
        filters.add(bb2);
        filters.add(excludeFilter);
        or = ff.or(filters);
        or.accept(splitter, null);
        assertEquals(testfilter, splitter.getFilterPre());
        assertEquals(Filter.INCLUDE, splitter.getFilterPost());
        
        //test include with another geometry filter and attribute filter using or
    	splitter = new BBoxFilterSplitter();
    	filters = new ArrayList<Filter>();
    	filters.add(bb2);
    	filters.add(excludeFilter);
    	filters.add(bb);
    	filters.add(att);
        or = ff.or(filters);
        or.accept(splitter, null);
        assertEquals(testfilter, splitter.getFilterPre());
        assertEquals(or, splitter.getFilterPost());        
        
        //test include with another geometry filter using and
    	splitter = new BBoxFilterSplitter();
    	filters = new ArrayList<Filter>();
    	filters.add(excludeFilter);
    	filters.add(bb);
        Filter and = ff.and(filters);
        and.accept(splitter, null);
        assertEquals(Filter.EXCLUDE, splitter.getFilterPre());
        assertEquals(Filter.INCLUDE, splitter.getFilterPost());		//if pre incorporates all geometries then we need nothing post
        
        //test include with another geometry filter using and with attribute 
    	splitter = new BBoxFilterSplitter();
    	filters = new ArrayList<Filter>();
    	filters.add(excludeFilter);
    	filters.add(bb);
    	filters.add(att);
        and = ff.and(filters);
        and.accept(splitter, null);
        assertEquals(Filter.EXCLUDE, splitter.getFilterPre());
        assertEquals(Filter.INCLUDE, splitter.getFilterPost());
    }
    
    /**
     * Tests filter that contains bost exclude and include filters
     */
    public void testIncludeExcludeFilter(){
    	List<Filter> filters = new ArrayList<Filter>();    	
    	Filter excludeFilter = Filter.EXCLUDE;
    	Filter includeFilter = Filter.INCLUDE;
    	
    	filters.add(bb);
    	filters.add(excludeFilter);
    	filters.add(bb2);
    	filters.add(bb);
    	filters.add(includeFilter);
    	filters.add(bb);
    	filters.add(att);
    	
    	Filter or = ff.or(filters);
    	BBoxFilterSplitter splitter = new BBoxFilterSplitter();
    	or.accept(splitter, null);
    	assertEquals(includeFilter, splitter.getFilterPre());
    	assertEquals(or, splitter.getFilterPost());
    	
    	splitter = new BBoxFilterSplitter();
    	Filter and = ff.and(filters);
    	and.accept(splitter, null);
    	assertEquals(excludeFilter, splitter.getFilterPre());
    	assertEquals(Filter.INCLUDE, splitter.getFilterPost());	
    }
    
    private SimpleFeature createTestFeature(){
    	
    	SimpleFeatureTypeBuilder st = new SimpleFeatureTypeBuilder();
    	st.setName("My Feature Type");
    	st.add("the_geom", Geometry.class, DefaultEngineeringCRS.CARTESIAN_2D);
    	
    	SimpleFeatureType type;
    	try{
    		type = st.buildFeatureType();
    	}catch (Exception ex){
    		ex.printStackTrace();
    		return null;
    	}
    	SimpleFeatureBuilder sb = new SimpleFeatureBuilder(type);
    	SimpleFeature sf = sb.buildFeature("001");
    	sf.setDefaultGeometry(createPolygon());
    	return sf;
    }
}
