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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.MultiValuedFilter;
import org.opengis.filter.MultiValuedFilter.MatchAction;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * 
 * Test whether filters are compatible with multi-valued properties.
 * 
 * @author Niels Charlier, Curtin University of Technology
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/main/src/test/java/org/geotools/filter/MultiFilterTest.java $
 */
public class MultiFilterTest {
    
    private FilterFactory2 fac = CommonFactoryFinder.getFilterFactory2(null);
    
    @Test
    public void testFactoryAndGetter() {
        MultiValuedFilter filter;
        Expression expr1 = fac.property("foo");
        Expression expr2 = fac.property("bar");
        Expression expr3 = fac.property("noo");
        
        //test default is MatchAction.ANY
        filter = fac.bbox(expr1, new ReferencedEnvelope());
        assertEquals(filter.getMatchAction(), MatchAction.ANY);        
        filter = fac.beyond(expr1, expr2, 0.1, "");
        assertEquals(filter.getMatchAction(), MatchAction.ANY);        
        filter = fac.beyond(expr1, expr2, 0.1, "");
        assertEquals(filter.getMatchAction(), MatchAction.ANY);        
        filter = fac.contains(expr1, expr2);
        assertEquals(filter.getMatchAction(), MatchAction.ANY);        
        filter = fac.crosses(expr1, expr2);        
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.disjoint(expr1, expr2);        
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.dwithin(expr1, expr2, 0.1, "");        
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.equal(expr1, expr2);        
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.intersects(expr1, expr2);        
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.overlaps(expr1, expr2);        
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.touches(expr1, expr2);        
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.within(expr1, expr2);        
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        
        filter = fac.equal(expr1, expr2, false);
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.less(expr1, expr2, false);
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.greater(expr1, expr2, false);
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.lessOrEqual(expr1, expr2, false);
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.greaterOrEqual(expr1, expr2,false);
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.notEqual(expr1, expr2, false);
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        filter = fac.between(expr1, expr2, expr3);
        assertEquals(filter.getMatchAction(), MatchAction.ANY);  
        filter = fac.like(expr1, "");
        assertEquals(filter.getMatchAction(), MatchAction.ANY);
        
        //test custom match action
        filter = fac.bbox(expr1, new ReferencedEnvelope(), MatchAction.ONE);
        assertEquals(filter.getMatchAction(), MatchAction.ONE);        
        filter = fac.beyond(expr1, expr2, 0.1, "", MatchAction.ALL);
        assertEquals(filter.getMatchAction(), MatchAction.ALL);        
        filter = fac.beyond(expr1, expr2, 0.1, "", MatchAction.ONE);
        assertEquals(filter.getMatchAction(), MatchAction.ONE);        
        filter = fac.contains(expr1, expr2, MatchAction.ALL);
        assertEquals(filter.getMatchAction(), MatchAction.ALL);        
        filter = fac.crosses(expr1, expr2, MatchAction.ONE);        
        assertEquals(filter.getMatchAction(), MatchAction.ONE);
        filter = fac.disjoint(expr1, expr2, MatchAction.ALL);        
        assertEquals(filter.getMatchAction(), MatchAction.ALL);
        filter = fac.dwithin(expr1, expr2, 0.1, "", MatchAction.ONE);        
        assertEquals(filter.getMatchAction(), MatchAction.ONE);
        filter = fac.equal(expr1, expr2, MatchAction.ALL);        
        assertEquals(filter.getMatchAction(), MatchAction.ALL);
        filter = fac.intersects(expr1, expr2, MatchAction.ONE);        
        assertEquals(filter.getMatchAction(), MatchAction.ONE);
        filter = fac.overlaps(expr1, expr2, MatchAction.ALL);        
        assertEquals(filter.getMatchAction(), MatchAction.ALL);
        filter = fac.touches(expr1, expr2, MatchAction.ONE);        
        assertEquals(filter.getMatchAction(), MatchAction.ONE);
        filter = fac.within(expr1, expr2, MatchAction.ALL);        
        assertEquals(filter.getMatchAction(), MatchAction.ALL);
        
        filter = fac.equal(expr1, expr2, false, MatchAction.ALL);
        assertEquals(filter.getMatchAction(), MatchAction.ALL);
        filter = fac.less(expr1, expr2, false, MatchAction.ONE);
        assertEquals(filter.getMatchAction(), MatchAction.ONE);
        filter = fac.greater(expr1, expr2, false, MatchAction.ALL);
        assertEquals(filter.getMatchAction(), MatchAction.ALL);
        filter = fac.lessOrEqual(expr1, expr2, false, MatchAction.ONE);
        assertEquals(filter.getMatchAction(), MatchAction.ONE);
        filter = fac.greaterOrEqual(expr1, expr2,false, MatchAction.ALL);
        assertEquals(filter.getMatchAction(), MatchAction.ALL);
        filter = fac.notEqual(expr1, expr2, false, MatchAction.ONE);
        assertEquals(filter.getMatchAction(), MatchAction.ONE);        
        filter = fac.between(expr1, expr2, expr3, MatchAction.ALL);
        assertEquals(filter.getMatchAction(), MatchAction.ALL);        
        filter = fac.like(expr1, "", "*", "?", "\\", false, MatchAction.ONE);
        assertEquals(filter.getMatchAction(), MatchAction.ONE);
        
    }
    
    @Test
    public void testCompareStringOperators_Any() {
        Filter filter;
        List<String> list = new ArrayList<String>();
        
        list.add("foo-1");
        list.add("foo-2");
        list.add("foo-3");
        list.add("blah-blah");
        
        Expression e = new LiteralExpressionImpl(list);        
        
        Expression empty = new LiteralExpressionImpl(new ArrayList<String>());
        
        filter = fac.equals(e, new LiteralExpressionImpl("foo-2"));
        assertTrue(filter.evaluate(null));
        
        filter = fac.equals(new LiteralExpressionImpl("foo-1"), e);
        assertTrue(filter.evaluate(null));
        
        filter = fac.equals(e, new LiteralExpressionImpl("foo-3"));
        assertTrue(filter.evaluate(null));
        
        filter = fac.equals(new LiteralExpressionImpl("blah"), e);
        assertFalse(filter.evaluate(null));
        
        filter = fac.equals(empty, new LiteralExpressionImpl("blah"));
        assertFalse(filter.evaluate(null));
        
        filter = fac.equals(new LiteralExpressionImpl("blah"), empty);
        assertFalse(filter.evaluate(null));
        
        filter = fac.notEqual(e, new LiteralExpressionImpl("blah"));
        assertTrue(filter.evaluate(null));
        
        filter = fac.notEqual(new LiteralExpressionImpl("foo-2"), e);
        assertTrue(filter.evaluate(null));
        
        filter = fac.like(e, "foo*");
        assertTrue(filter.evaluate(null));
        
        filter = fac.like(e, "blah*");
        assertTrue(filter.evaluate(null));
        
        filter = fac.like(e, "foox*");
        assertFalse(filter.evaluate(null));
        
        filter = fac.like(empty, "*");
        assertFalse(filter.evaluate(null));
                
        //testing collection with collection comparison
        
        List<String> list2 = new ArrayList<String>();
        
        list2.add("foo-4");
        list2.add("foo-5");
        list2.add("foo-6");
        
        filter = fac.equals(e, new LiteralExpressionImpl(list2));
        assertFalse(filter.evaluate(null));
        
        filter = fac.notEqual(e, new LiteralExpressionImpl(list2));
        assertTrue(filter.evaluate(null));
        
        list2.add("blah-blah");
        
        filter = fac.equals(e, new LiteralExpressionImpl(list2));
        assertTrue(filter.evaluate(null));
        
        filter = fac.notEqual(e, new LiteralExpressionImpl(list2));
        assertTrue(filter.evaluate(null));
        
        list2 = new ArrayList<String>();
        list2.add("foo-1");
        list2.add("foo-2");
        list2.add("foo-3");
        list2.add("blah-blah");
        
        filter = fac.notEqual(e, new LiteralExpressionImpl(list2));
        assertTrue(filter.evaluate(null));        
                
        //testing non equals
        list = new ArrayList<String>();
        list.add("foo-1");
        list.add("foo-1");
        list.add("foo-1");
        e = new LiteralExpressionImpl(list);   
        
        filter = fac.notEqual(e, new LiteralExpressionImpl("foo-1"));
        assertFalse(filter.evaluate(null));
        
        list2 = new ArrayList<String>();
        list2.add("foo-1");
        list2.add("foo-1");
        
        filter = fac.notEqual(e, new LiteralExpressionImpl(list2));
        assertFalse(filter.evaluate(null));        
                
    }
    
    @Test
    public void testCompareStringOperators_All() {
        Filter filter;
        List<String> list = new ArrayList<String>();
        
        list.add("foo-1");
        list.add("foo-2");
        list.add("foo-3");
        
        List<String> list2 = new ArrayList<String>();
        
        list2.add("fOo-2");
        list2.add("foO-2");
        list2.add("Foo-2");
        
        Expression e1 = new LiteralExpressionImpl(list);        
        Expression e2 = new LiteralExpressionImpl(list2);        
                
        filter = fac.equal(e1, new LiteralExpressionImpl("foo-2"), false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.equal(new LiteralExpressionImpl("foo-2"), e2, false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.notEqual(e1, new LiteralExpressionImpl("blah"), false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.notEqual(new LiteralExpressionImpl("foo-2"), e1, false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));   
        
        filter = fac.like(e1, "foo-*", "*", "?", "\\", false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.like(e1, "*-1", "*", "?", "\\", false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
    }
    
    @Test
    public void testCompareStringOperators_One() {
        Filter filter;
        List<String> list = new ArrayList<String>();
        
        list.add("foo-1");
        list.add("foo-2");
        list.add("foo-3");
        
        List<String> list2 = new ArrayList<String>();
        
        list2.add("Foo-1");
        list2.add("fOo-2");
        list2.add("foO-2");
        list2.add("Foo-2");
        
        Expression e1 = new LiteralExpressionImpl(list);        
        Expression e2 = new LiteralExpressionImpl(list2);        
                
        filter = fac.equal(e1, new LiteralExpressionImpl("foo-2"), false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.equal(new LiteralExpressionImpl("foo-2"), e2, false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.notEqual(e1, new LiteralExpressionImpl("blah"), false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.notEqual(new LiteralExpressionImpl("foo-2"), e2, false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));   
        
        filter = fac.like(e1, "foo-*", "*", "?", "\\", false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.like(e1, "*-1", "*", "?", "\\", false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
    }
        
    @Test
    public void testCompareNumberOperators_Any() {
        Filter filter;
        List<Double> list = new ArrayList<Double>();
                
        list.add(35.2);
        list.add(202.3);        
        list.add(201.7);
        list.add(10000.5);
        
        Expression e = new LiteralExpressionImpl(list);        
        
        Expression empty = new LiteralExpressionImpl(new ArrayList<String>());
        
        filter = fac.equals(e, new LiteralExpressionImpl(201.7));
        assertTrue(filter.evaluate(null));
        
        filter = fac.equals(new LiteralExpressionImpl(202.3), e);
        assertTrue(filter.evaluate(null));
        
        filter = fac.notEqual(e, new LiteralExpressionImpl(10.7));
        assertTrue(filter.evaluate(null));
        
        filter = fac.notEqual(new LiteralExpressionImpl(888.8), e);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greater(e, new LiteralExpressionImpl(9999));
        assertTrue(filter.evaluate(null));
        
        filter = fac.greater(e, new LiteralExpressionImpl(99999));
        assertFalse(filter.evaluate(null));
        
        filter = fac.greater(new LiteralExpressionImpl(50), e);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greater(new LiteralExpressionImpl(34), e);
        assertFalse(filter.evaluate(null));
        
        filter = fac.less(e, new LiteralExpressionImpl(50));
        assertTrue(filter.evaluate(null));
        
        filter = fac.less(e, new LiteralExpressionImpl(34));
        assertFalse(filter.evaluate(null));
        
        filter = fac.less(new LiteralExpressionImpl(9999), e);
        assertTrue(filter.evaluate(null));
        
        filter = fac.less(new LiteralExpressionImpl(99999), e);
        assertFalse(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (e, new LiteralExpressionImpl(10000.5));
        assertTrue(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (e, new LiteralExpressionImpl(10001));
        assertFalse(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (new LiteralExpressionImpl(35.2), e);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (new LiteralExpressionImpl(35), e);
        assertFalse(filter.evaluate(null));
                
        filter = fac.lessOrEqual (e, new LiteralExpressionImpl(35.2));
        assertTrue(filter.evaluate(null));
        
        filter = fac.lessOrEqual (e, new LiteralExpressionImpl(35));
        assertFalse(filter.evaluate(null));
        
        filter = fac.lessOrEqual (new LiteralExpressionImpl(10000.5), e);
        assertTrue(filter.evaluate(null));
        
        filter = fac.lessOrEqual (new LiteralExpressionImpl(10001), e);
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(e, new LiteralExpressionImpl(34), new LiteralExpressionImpl(36));
        assertTrue(filter.evaluate(null));
        
        filter = fac.between(e, new LiteralExpressionImpl(200), new LiteralExpressionImpl(300));
        assertTrue(filter.evaluate(null));
        
        filter = fac.between(e, new LiteralExpressionImpl(36), new LiteralExpressionImpl(201));
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(e, new LiteralExpressionImpl(203), new LiteralExpressionImpl(9999));
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(new LiteralExpressionImpl(36), e, new LiteralExpressionImpl(37));
        assertTrue(filter.evaluate(null));
        
        filter = fac.between(new LiteralExpressionImpl(0), e, new LiteralExpressionImpl(100000));
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(new LiteralExpressionImpl(34), new LiteralExpressionImpl(0), e);
        assertTrue(filter.evaluate(null));
        
        filter = fac.between(new LiteralExpressionImpl(10001), new LiteralExpressionImpl(0), e);
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(empty, new LiteralExpressionImpl(0), new LiteralExpressionImpl(10001));
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(new LiteralExpressionImpl(10001), new LiteralExpressionImpl(0), empty);
        assertFalse(filter.evaluate(null));
               
    }
    
    @Test
    public void testCompareNumberOperators_All() {
        Filter filter;
        List<Double> list = new ArrayList<Double>();                
        list.add(35.2);
        list.add(202.3);        
        list.add(201.7);
        list.add(10000.5);
        
        List<Double> list2 = new ArrayList<Double>();        
        list2.add(35.2);
        list2.add(35.2);        
        list2.add(35.2);
        
        Expression e1 = new LiteralExpressionImpl(list);        
        Expression e2 = new LiteralExpressionImpl(list2);        
        
        filter = fac.equal(e1, new LiteralExpressionImpl(35.2), false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.equal(new LiteralExpressionImpl(35.2), e2, false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.notEqual(e1, new LiteralExpressionImpl(201.7), false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.notEqual(new LiteralExpressionImpl(55), e1, false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greater(e1, new LiteralExpressionImpl(9999), false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.greater(e1, new LiteralExpressionImpl(32), false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greater(new LiteralExpressionImpl(100000000), e1, false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greater(new LiteralExpressionImpl(700), e1, false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.less(e1, new LiteralExpressionImpl(100000000), false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.less(e1, new LiteralExpressionImpl(9999), false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.less(new LiteralExpressionImpl(32), e1, false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.less(new LiteralExpressionImpl(700), e1, false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (e1, new LiteralExpressionImpl(35.2), false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (e1, new LiteralExpressionImpl(201.7), false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (new LiteralExpressionImpl(10000.5), e1, false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (new LiteralExpressionImpl(201.7), e1, false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
                
        filter = fac.lessOrEqual (e1, new LiteralExpressionImpl(10000.5), false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.lessOrEqual (e1, new LiteralExpressionImpl(201.7), false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.lessOrEqual (new LiteralExpressionImpl(35.2), e1, false, MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        filter = fac.lessOrEqual (new LiteralExpressionImpl(201.7), e1, false, MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(e1, new LiteralExpressionImpl(34), new LiteralExpressionImpl(300), MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(e1, new LiteralExpressionImpl(34), new LiteralExpressionImpl(10000000), MatchAction.ALL);
        assertTrue(filter.evaluate(null));
    }
    
    @Test
    public void testCompareNumberOperators_One() {
        Filter filter;
        List<Double> list = new ArrayList<Double>();                
        list.add(35.2);
        list.add(202.3);        
        list.add(201.7);
        list.add(10000.5);
        
        List<Double> list2 = new ArrayList<Double>();        
        list2.add(35.2);
        list2.add(35.2);        
        list2.add(202.3);        
        
        Expression e1 = new LiteralExpressionImpl(list);        
        Expression e2 = new LiteralExpressionImpl(list2);        
         
        filter = fac.equal(e1, new LiteralExpressionImpl(35.2), false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.equal(new LiteralExpressionImpl(35.2), e2, false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.notEqual(e1, new LiteralExpressionImpl(35.2), false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.notEqual(new LiteralExpressionImpl(35.2), e2, false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greater(e1, new LiteralExpressionImpl(100), false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.greater(e1, new LiteralExpressionImpl(9000), false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greater(new LiteralExpressionImpl(40), e1, false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greater(new LiteralExpressionImpl(700), e1, false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.less(e1, new LiteralExpressionImpl(40), false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.less(e1, new LiteralExpressionImpl(700), false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.less(new LiteralExpressionImpl(9000), e1, false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.less(new LiteralExpressionImpl(100), e1, false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (e1, new LiteralExpressionImpl(10000.5), false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (e1, new LiteralExpressionImpl(201.7), false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (new LiteralExpressionImpl(35.2), e1, false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.greaterOrEqual (new LiteralExpressionImpl(10001), e1, false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
                
        filter = fac.lessOrEqual (e1, new LiteralExpressionImpl(35.2), false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.lessOrEqual (e1, new LiteralExpressionImpl(10001), false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.lessOrEqual (new LiteralExpressionImpl(10000.5), e1, false, MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.lessOrEqual (new LiteralExpressionImpl(201.7), e1, false, MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(e1, new LiteralExpressionImpl(34), new LiteralExpressionImpl(300), MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        filter = fac.between(e1, new LiteralExpressionImpl(34), new LiteralExpressionImpl(36), MatchAction.ONE);
        assertTrue(filter.evaluate(null));
    }
    
    @Test
    public void testGeometries_Any() {
        Filter filter;
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
       
        Coordinate[] coords1 = {
                new Coordinate(0, 0),
                new Coordinate(6, 0),
                new Coordinate(6, 7),
                new Coordinate(0, 7),
                new Coordinate(0, 0)};                
        Polygon geom1 = gf.createPolygon(gf.createLinearRing(coords1), new LinearRing[0]);
        
        Coordinate[] coords2 = new Coordinate[] {
                new Coordinate(2, 2),
                new Coordinate(6, 0),
                new Coordinate(6, 7),
                new Coordinate(0, 7),
                new Coordinate(2, 2)
        };              
        Polygon geom2 = gf.createPolygon(gf.createLinearRing(coords2), new LinearRing[0]);
        
        Coordinate[] coords3 = new Coordinate[] {
                new Coordinate(2, 2),
                new Coordinate(6, 0),
                new Coordinate(11, 12),
                new Coordinate(0, 7),
                new Coordinate(2, 2)
        };              
        Polygon geom3 = gf.createPolygon(gf.createLinearRing(coords3), new LinearRing[0]);
        
        Coordinate[] coords4 = new Coordinate[] {
                new Coordinate(20, 20),
                new Coordinate(60, 30),
                new Coordinate(110, 120),
                new Coordinate(30, 70),
                new Coordinate(20, 20)
        };              
        Polygon geom4 = gf.createPolygon(gf.createLinearRing(coords4), new LinearRing[0]);
                        
        List<Geometry> list = new ArrayList<Geometry>();
        list.add(geom4);
        
        filter = fac.overlaps(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1));
        assertFalse(filter.evaluate(null));
        filter = fac.overlaps(new LiteralExpressionImpl(geom1), new LiteralExpressionImpl(list));
        assertFalse(filter.evaluate(null));
        filter = fac.disjoint(new LiteralExpressionImpl(geom1), new LiteralExpressionImpl(list));
        assertTrue(filter.evaluate(null));
        
        list.add(geom3);
        filter = fac.overlaps(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1));
        assertTrue(filter.evaluate(null));
        filter = fac.overlaps(new LiteralExpressionImpl(geom1), new LiteralExpressionImpl(list));
        assertTrue(filter.evaluate(null));
                
        filter = fac.within(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1));
        assertFalse(filter.evaluate(null));
        
        list.add(geom2);
        filter = fac.within(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1));
        assertTrue(filter.evaluate(null));
        
        filter = fac.equal(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1));
        assertFalse(filter.evaluate(null));
        
        list.add(geom1);        
        filter = fac.equal(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1));
        assertTrue(filter.evaluate(null));
        
        //comparing lists with lists
        list = new ArrayList<Geometry>();
        list.add(geom3);
        list.add(geom2);        
        
        List<Geometry> list2 = new ArrayList<Geometry>();
        list2.add(geom1);
        list2.add(geom2);

        filter = fac.equal(new LiteralExpressionImpl(list), new LiteralExpressionImpl(list2));
        assertTrue(filter.evaluate(null));
        filter = fac.intersects(new LiteralExpressionImpl(list), new LiteralExpressionImpl(list2));
        assertTrue(filter.evaluate(null));
        
    }
    
    @Test
    public void testGeometries_One() {
        Filter filter;
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
       
        Coordinate[] coords1 = {
                new Coordinate(0, 0),
                new Coordinate(6, 0),
                new Coordinate(6, 7),
                new Coordinate(0, 7),
                new Coordinate(0, 0)};                
        Polygon geom1 = gf.createPolygon(gf.createLinearRing(coords1), new LinearRing[0]);
        
        Coordinate[] coords2 = new Coordinate[] {
                new Coordinate(2, 2),
                new Coordinate(6, 0),
                new Coordinate(6, 7),
                new Coordinate(0, 7),
                new Coordinate(2, 2)
        };              
        Polygon geom2 = gf.createPolygon(gf.createLinearRing(coords2), new LinearRing[0]);
        
        Coordinate[] coords3 = new Coordinate[] {
                new Coordinate(2, 2),
                new Coordinate(6, 0),
                new Coordinate(11, 12),
                new Coordinate(0, 7),
                new Coordinate(2, 2)
        };              
        Polygon geom3 = gf.createPolygon(gf.createLinearRing(coords3), new LinearRing[0]);
        
        Coordinate[] coords4 = new Coordinate[] {
                new Coordinate(20, 20),
                new Coordinate(60, 30),
                new Coordinate(110, 120),
                new Coordinate(30, 70),
                new Coordinate(20, 20)
        };              
        Polygon geom4 = gf.createPolygon(gf.createLinearRing(coords4), new LinearRing[0]);
                        
        List<Geometry> list = new ArrayList<Geometry>();
        list.add(geom4);
        
        filter = fac.equal(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom4), MatchAction.ONE);
        assertTrue(filter.evaluate(null));

        filter = fac.overlaps(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        filter = fac.disjoint(new LiteralExpressionImpl(geom1), new LiteralExpressionImpl(list), MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        list.add(geom3);
        
        filter = fac.equal(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom4), MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.overlaps(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ONE);
        assertTrue(filter.evaluate(null));
                
        filter = fac.within(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        list.add(geom2);
        filter = fac.within(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
        filter = fac.overlaps(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ONE);
        assertTrue(filter.evaluate(null));
                
        filter = fac.equal(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ONE);
        assertFalse(filter.evaluate(null));
        
        list.add(geom1);        
        filter = fac.equal(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ONE);
        assertTrue(filter.evaluate(null));
        
    }
    
    @Test
    public void testGeometries_All() {
        Filter filter;
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
       
        Coordinate[] coords1 = {
                new Coordinate(0, 0),
                new Coordinate(6, 0),
                new Coordinate(6, 7),
                new Coordinate(0, 7),
                new Coordinate(0, 0)};                
        Polygon geom1 = gf.createPolygon(gf.createLinearRing(coords1), new LinearRing[0]);
        
        Coordinate[] coords2 = new Coordinate[] {
                new Coordinate(2, 2),
                new Coordinate(6, 0),
                new Coordinate(6, 7),
                new Coordinate(0, 7),
                new Coordinate(2, 2)
        };              
        Polygon geom2 = gf.createPolygon(gf.createLinearRing(coords2), new LinearRing[0]);
        
        Coordinate[] coords3 = new Coordinate[] {
                new Coordinate(2, 2),
                new Coordinate(6, 0),
                new Coordinate(11, 12),
                new Coordinate(0, 7),
                new Coordinate(2, 2)
        };              
        Polygon geom3 = gf.createPolygon(gf.createLinearRing(coords3), new LinearRing[0]);
        
        Coordinate[] coords4 = new Coordinate[] {
                new Coordinate(20, 20),
                new Coordinate(60, 30),
                new Coordinate(110, 120),
                new Coordinate(30, 70),
                new Coordinate(20, 20)
        };              
        Polygon geom4 = gf.createPolygon(gf.createLinearRing(coords4), new LinearRing[0]);
                        
        List<Geometry> list = new ArrayList<Geometry>();
        list.add(geom4);
        
        filter = fac.equal(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom4), MatchAction.ALL);
        assertTrue(filter.evaluate(null));

        filter = fac.overlaps(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        filter = fac.disjoint(new LiteralExpressionImpl(geom1), new LiteralExpressionImpl(list), MatchAction.ALL);
        assertTrue(filter.evaluate(null));
        
        list.add(geom3);
        
        filter = fac.equal(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom4), MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        filter = fac.overlaps(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ALL);
        assertFalse(filter.evaluate(null));
                
        filter = fac.within(new LiteralExpressionImpl(list), new LiteralExpressionImpl(geom1), MatchAction.ALL);
        assertFalse(filter.evaluate(null));
        
        list.remove(geom4);
        list.add(geom2);
        
        filter = fac.intersects(new LiteralExpressionImpl(geom1), new LiteralExpressionImpl(list), MatchAction.ALL);
        assertTrue(filter.evaluate(null));
                              
        
    }

}
