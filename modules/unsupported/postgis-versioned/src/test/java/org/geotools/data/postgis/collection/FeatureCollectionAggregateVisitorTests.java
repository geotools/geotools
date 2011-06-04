/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis.collection;

import org.geotools.data.jdbc.JDBCFeatureCollection;
import org.geotools.data.postgis.AbstractPostgisDataTestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.AverageVisitor;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.SumVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.FunctionExpression;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

public class FeatureCollectionAggregateVisitorTests extends AbstractPostgisDataTestCase {
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    JDBCFeatureCollection roads;
    JDBCFeatureCollection rivers;
    PropertyName roadsId;
    PropertyName riverFlow;

    public FeatureCollectionAggregateVisitorTests(String test) {
        super(test);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        roads = (JDBCFeatureCollection) data.getFeatureSource("road").getFeatures();
        rivers = (JDBCFeatureCollection) data.getFeatureSource("river").getFeatures();
        roadsId = ff.property("id");
        riverFlow = ff.property("flow");
    }
    
    public void testSum() throws Exception {
        // test against integer data
        SumVisitor sumVisitor = new SumVisitor(roadsId);
        roads.accepts(sumVisitor, null);
        assertTrue(roads.isOptimized); //the optimization was used
        assertEquals(6, sumVisitor.getResult().toInt());
        
        // test complex expression
        Literal one = ff.literal(1);
        Add add = ff.add(roadsId, one);
        SumVisitor sumInt = new SumVisitor(add);
        roads.accepts(sumInt, null);
        assertTrue(roads.isOptimized);
        assertEquals(9, sumInt.getResult().toInt());
        
        // test against float
        Add addFloat = ff.add(riverFlow, one);
        SumVisitor sumFloat = new SumVisitor(addFloat);
        rivers.accepts(sumFloat, null);
        assertTrue(rivers.isOptimized);
        assertEquals(9.5, sumFloat.getResult().toDouble(), 0.0);
    }
    
    public void testCount() throws Exception {
      // test count 
      CountVisitor countVisitor = new CountVisitor();
      roads.accepts(countVisitor, null);
      assertTrue(roads.isOptimized); //the optimization was used
      assertEquals(3, countVisitor.getResult().toInt());
    }
    
    public void testMinMax() throws Exception {
        MinVisitor minVisitor = new MinVisitor(roadsId);
        roads.accepts(minVisitor, null);
        assertTrue(roads.isOptimized); //the postgis optimization was used
        assertEquals(1, minVisitor.getResult().toInt());

        MaxVisitor maxVisitor = new MaxVisitor(roadsId);
        roads.accepts(maxVisitor, null);
        assertTrue(roads.isOptimized); //the postgis optimization was used
        assertEquals(3, maxVisitor.getResult().toInt());
    }
    
    public void testAverage() throws Exception {
        AverageVisitor averageVisitor = new AverageVisitor(roadsId);
        roads.accepts(averageVisitor, null);
        assertTrue(roads.isOptimized); //the postgis optimization was used
        assertEquals(2, averageVisitor.getResult().toInt());
    }

    public void testUnique() throws Exception {
        UniqueVisitor uniqueVisitor = new UniqueVisitor(roadsId);
        roads.accepts(uniqueVisitor, null);
        assertTrue(roads.isOptimized); //the postgis optimization was used
        assertEquals(3, uniqueVisitor.getResult().toSet().size());
    }
    
    public void testSumExpression() throws Exception {
        FunctionExpression expr = (FunctionExpression) ff.function("Collection_Sum", ff.property("id"));
        int result = ((Number) expr.evaluate(roads)).intValue();
        assertTrue(roads.isOptimized);
        assertEquals(6, result);
    }
   
}
