/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.ecql;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.filter.function.FilterFunction_buffer;
import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLRelGeoOpTest;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.DistanceBufferOperator;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Relation geo operation 
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 *
 * @source $URL$
 */
public class ECQLRelGeoOpTest extends CQLRelGeoOpTest {
    protected static final FilterFactory FILTER_FACTORY = CommonFactoryFinder.getFilterFactory((Hints) null);

    public ECQLRelGeoOpTest(){
        super(Language.ECQL);
    }
    
    @Test
    public void functionDwithinGeometry() throws Exception{
        Filter resultFilter;

        // DWITHIN
        resultFilter = CompilerUtil.parseFilter(this.language,
                "DWITHIN(buffer(the_geom,5), POINT(1 2), 10, kilometers)");

        Assert.assertTrue(resultFilter instanceof DistanceBufferOperator);

        DistanceBufferOperator distOp = (DistanceBufferOperator) resultFilter;
        
        Assert.assertTrue(distOp.getExpression1() instanceof FilterFunction_buffer);
        
        Assert.assertTrue(distOp.getExpression2() instanceof Literal);

    }
   
    @Test
    public void functionDwithinFunction() throws Exception{
        Filter filter;

        // DWITHIN
        filter = CompilerUtil.parseFilter(this.language,
                "DWITHIN(buffer(the_geom,5), buffer(the_geom,2), 10, kilometers)");

        Assert.assertTrue(filter instanceof DistanceBufferOperator);

        DistanceBufferOperator distOp = (DistanceBufferOperator) filter;
        
        Assert.assertTrue(distOp.getExpression1() instanceof FilterFunction_buffer);
        
        Assert.assertTrue(distOp.getExpression2() instanceof FilterFunction_buffer);

    }
    @Test
    public void geometryDwithinGeometry() throws Exception{
        Filter resultFilter;

        // DWITHIN
        resultFilter = CompilerUtil.parseFilter(this.language,
                "DWITHIN(POINT(5 7), POINT(1 2), 10, kilometers)");

        Assert.assertTrue(resultFilter instanceof DistanceBufferOperator);

    }

    @Test
    public void geometryBeyondGeometry() throws Exception {
        
        Filter resultFilter = CompilerUtil.parseFilter(language,
                "BEYOND(POINT(5.0 7.0), POINT(1.0 2.0), 10.0, kilometers)");
        
        Assert.assertTrue(resultFilter instanceof Beyond);
        Beyond beyondFilter = (Beyond) resultFilter;
        
        Literal literal1 = (Literal) beyondFilter.getExpression1();
        Assert.assertTrue(literal1.getValue() instanceof Geometry);
        
        Literal literal2 = (Literal) beyondFilter.getExpression2();
        Assert.assertTrue(literal2.getValue() instanceof Geometry);
    }

    @Test
    public void functionBeyondFunction() throws Exception {
        
        Filter resultFilter = CompilerUtil.parseFilter(language,
                "BEYOND(buffer(geom1,3), buffer(geom2, 4), 10.0, kilometers)");
        
        Assert.assertTrue(resultFilter instanceof Beyond);
        Beyond beyondFilter = (Beyond) resultFilter;
        
        Expression expr1 = beyondFilter.getExpression1();
        Assert.assertTrue(expr1 instanceof FilterFunction_buffer);
        
        Expression expr2 =  beyondFilter.getExpression2();
        Assert.assertTrue(expr2 instanceof FilterFunction_buffer);
    }
   

}
