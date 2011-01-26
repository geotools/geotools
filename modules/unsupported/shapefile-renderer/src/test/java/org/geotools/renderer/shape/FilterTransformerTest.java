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
package org.geotools.renderer.shape;

import java.awt.geom.AffineTransform;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Or;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class FilterTransformerTest extends TestCase {


    private static final String DEFAULT = "4326";
	private FilterTransformer filterTransformer;
    private final FilterFactory filterFactory=CommonFactoryFinder.getFilterFactory(null);
    protected void setUp() throws Exception {
        super.setUp();
        AffineTransform t=AffineTransform.getTranslateInstance(10,10);
        DefaultMathTransformFactory fac=new DefaultMathTransformFactory();
        MathTransform mt = fac.createAffineTransform(new GeneralMatrix(t));
        filterTransformer=new FilterTransformer(mt);
    }
    
    public void testVisitBBoxExpression() throws Exception {
        Envelope envelope = new Envelope(0,10,0,10);
        GeometryFactory geometryFactory=new GeometryFactory();
        Literal lit = filterFactory.literal(geometryFactory.toGeometry(envelope));
        lit = (Literal) lit.accept(filterTransformer, null);
        assertTrue(geometryFactory.toGeometry(new Envelope(10,20,10,20)).equals((Geometry)lit.getValue()));
    }
    
    public void testVisitCompareFilter() throws Exception {
        BBOX filter = filterFactory.bbox("geom", 0, 0, 10, 10, DEFAULT);
        
        BBOX result = (BBOX) filter.accept(filterTransformer, null);
        assertEquals(10.0, result.getMinX(),0.000001);
        assertEquals(10.0, result.getMinY(),0.000001);
        assertEquals(20.0, result.getMaxX(),0.000001);
        assertEquals(20.0, result.getMaxY(),0.000001);
    }
    
    public void testVisitORFilter() throws Exception {
        BBOX bbox1 = filterFactory.bbox("geom", 0, 0, 10, 10, DEFAULT);
        BBOX bbox2 = filterFactory.bbox("geom", 10, 10, 20, 20, DEFAULT);
        
        Or or = filterFactory.or(bbox1, bbox2);
        // TEST filter.None because it was a bug before
        or = (Or) or.accept(filterTransformer, null);

        bbox1=(BBOX) or.getChildren().get(0);
        bbox2=(BBOX) or.getChildren().get(1);
        
        assertEquals(10.0, bbox1.getMinX(),0.000001);
        assertEquals(10.0, bbox1.getMinY(),0.000001);
        assertEquals(20.0, bbox1.getMaxX(),0.000001);
        assertEquals(20.0, bbox1.getMaxY(),0.000001);

        assertEquals(20.0, bbox2.getMinX(),0.000001);
        assertEquals(20.0, bbox2.getMinY(),0.000001);
        assertEquals(30.0, bbox2.getMaxX(),0.000001);
        assertEquals(30.0, bbox2.getMaxY(),0.000001);
    }

}
