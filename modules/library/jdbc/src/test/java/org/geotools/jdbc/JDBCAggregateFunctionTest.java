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
package org.geotools.jdbc;

import java.util.Set;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.SumVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

public abstract class JDBCAggregateFunctionTest extends JDBCTestSupport {

    boolean visited = false;
  
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        visited = false;
    }
    
    class MySumVisitor extends SumVisitor {

        public MySumVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }
        
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
        
    }
    
    public void testSum() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("doubleProperty") );
        
        SumVisitor v = new MySumVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals( 3.3, v.getResult().toDouble(), 0.01 );
    }
    
    public void testSumWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("doubleProperty") );
        
        SumVisitor v = new MySumVisitor(p);
        
        Filter f = ff.less( ff.property( aname("doubleProperty") ), ff.literal(2) );
        Query q = new DefaultQuery( tname("ft1"), f);
        dataStore.getFeatureSource( tname("ft1")).accepts( q, v, null);
        assertFalse(visited);
        assertEquals( 1.1, v.getResult().toDouble(), 0.01 );
    }
    
    public void testSumWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("doubleProperty") );
        
        SumVisitor v = new MySumVisitor(p);
        
        DefaultQuery q = new DefaultQuery( tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);
        
        dataStore.getFeatureSource( tname("ft1")).accepts( q, v, null);
        assertFalse(visited);
        assertEquals( 1.1, v.getResult().toDouble(), 0.01 );
    }
    
    class MyMaxVisitor extends MaxVisitor {

        public MyMaxVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }
        
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
        
    }
    
    public void testMax() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("doubleProperty") );
        
        MaxVisitor v = new MyMaxVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals( 2.2, v.getResult().toDouble(), 0.01 );
    }
    
    public void testMaxWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("doubleProperty") );
        
        MaxVisitor v = new MyMaxVisitor(p);
        
        Filter f = ff.less( ff.property( aname("doubleProperty") ), ff.literal(2) );
        Query q = new DefaultQuery( tname("ft1"), f);
        dataStore.getFeatureSource( tname("ft1")).accepts( q, v, null);
        assertFalse(visited);
        assertEquals( 1.1, v.getResult().toDouble(), 0.01 );
    }
    
    public void testMaxWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("doubleProperty") );
        
        MaxVisitor v = new MyMaxVisitor(p);
        
        DefaultQuery q = new DefaultQuery( tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);
        dataStore.getFeatureSource( tname("ft1")).accepts( q, v, null);
        
        assertFalse(visited);
        assertEquals( 1.1, v.getResult().toDouble(), 0.01 );
    }
    
    class MyMinVisitor extends MinVisitor {

        public MyMinVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }
        
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
        
    }
    
    public void testMin() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("doubleProperty") );
        
        MinVisitor v = new MyMinVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals( 0.0, v.getResult().toDouble(), 0.01 );
    }
    
    public void testMinWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("doubleProperty") );
        
        MinVisitor v = new MyMinVisitor(p);
        
        Filter f = ff.greater( ff.property( aname("doubleProperty") ), ff.literal(1) );
        Query q = new DefaultQuery( tname("ft1"), f);
        dataStore.getFeatureSource( tname("ft1")).accepts( q, v, null);
        assertFalse(visited);
        assertEquals( 1.1, v.getResult().toDouble(), 0.01 );
    }
    
    public void testMinWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("doubleProperty") );
        
        MinVisitor v = new MyMinVisitor(p);
        
        DefaultQuery q = new DefaultQuery( tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);
        dataStore.getFeatureSource( tname("ft1")).accepts( q, v, null);
        
        assertFalse(visited);
        assertEquals( 0.0, v.getResult().toDouble(), 0.01 );
    }
    
    class MyUniqueVisitor extends UniqueVisitor {

        public MyUniqueVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }
        
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }
        
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
        
    }
    
    public void testUnique() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("stringProperty") );
        
        UniqueVisitor v = new MyUniqueVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(3, result.size());
        assertTrue(result.contains("zero"));
        assertTrue(result.contains("one"));
        assertTrue(result.contains("two"));
    }
    
    public void testUniqueWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("stringProperty") );
        
        UniqueVisitor v = new MyUniqueVisitor(p);
        Filter f = ff.greater( ff.property( aname("doubleProperty") ), ff.literal(1) );
        Query q = new Query( tname("ft1"), f);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(2, result.size());
        assertTrue(result.contains("one"));
        assertTrue(result.contains("two"));
    }
    
    public void testUniqueWithLimitOffset() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property( aname("stringProperty") );
        
        UniqueVisitor v = new MyUniqueVisitor(p);
        Query q = new Query( tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(2, result.size());
    }
}
