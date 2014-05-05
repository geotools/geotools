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
 *    
 *    Created on 20 June 2002, 18:53
 */
package org.geotools.filter;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.PropertyIsBetween;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * tests for between filters.
 *
 * @author James Macgill
 *
 *
 * @source $URL$
 */
public class BetweenTest extends TestCase {
    /** Standard logging instance */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter");

    public BetweenTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(BetweenTest.class);

        return suite;
    }

    public void testContains() throws Exception {
        //this should move out to a more configurable system run from scripts
        //but we can start with a set of hard coded tests
        BetweenFilterImpl a = new BetweenFilterImpl();

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setCRS(null);
        ftb.add("value", Integer.class);
        ftb.add("geometry", Geometry.class);
        ftb.setName("testSchema");
        SimpleFeatureType schema = ftb.buildFeatureType();

        a.setExpression1(new LiteralExpressionImpl(new Double(5)));
        a.setExpression2(new LiteralExpressionImpl(new Double(15)));
        a.setExpression(new AttributeExpressionImpl(schema, "value"));

        //FlatFeatureFactory fFac = new FlatFeatureFactory(schema);
        LOGGER.fine("geometry is " + schema.getDescriptor("geometry"));
        LOGGER.fine("value is " + schema.getDescriptor("value"));
        LOGGER.fine("schema has value in it ? "
            + (schema.getDescriptor("value") != null));

        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        SimpleFeature f1 = SimpleFeatureBuilder.build(schema,new Object[] {
                    new Integer(12), gf.createPoint(new Coordinate(12,12))
                }, null);
        SimpleFeature f2 = SimpleFeatureBuilder.build(schema, new Object[] {
                    new Integer(3), gf.createPoint(new Coordinate(3,3))
                }, null);
        SimpleFeature f3 = SimpleFeatureBuilder.build(schema, new Object[] {
                    new Integer(15), gf.createPoint(new Coordinate(15,15))
                }, null);
        SimpleFeature f4 = SimpleFeatureBuilder.build(schema, new Object[] {
                    new Integer(5), gf.createPoint(new Coordinate(5,5))
                }, null);
        SimpleFeature f5 = SimpleFeatureBuilder.build(schema, new Object[] {
                    new Integer(30), gf.createPoint(new Coordinate(30,30))
                }, null);

        assertEquals(true, a.evaluate(f1)); // in between
        assertEquals(false, a.evaluate(f2)); // too small
        assertEquals(true, a.evaluate(f3)); // max value
        assertEquals(true, a.evaluate(f4)); // min value
        assertEquals(false, a.evaluate(f5)); // too large
    }

    public void testEquals() throws Exception {
        org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        PropertyIsBetween f1 = ff.between(ff.property("abc"), ff.literal(10), ff.literal(20));
        PropertyIsBetween f2 = ff.between(ff.property("efg"), ff.literal(10), ff.literal(20));
        PropertyIsBetween f3 = ff.between(ff.property("abc"), ff.literal(10), ff.literal(20));

        assertEquals(f1, f3);
        assertFalse(f1.equals(f2));
    }
}
