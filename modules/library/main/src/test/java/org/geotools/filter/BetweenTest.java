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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.logging.Logger;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.PropertyIsBetween;

/**
 * tests for between filters.
 *
 * @author James Macgill
 */
public class BetweenTest {
    /** Standard logging instance */
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(BetweenTest.class);

    @Test
    public void testContains() throws Exception {
        // this should move out to a more configurable system run from scripts
        // but we can start with a set of hard coded tests
        IsBetweenImpl a = new IsBetweenImpl(null, null, null);

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setCRS(null);
        ftb.add("value", Integer.class);
        ftb.add("geometry", Geometry.class);
        ftb.setName("testSchema");
        SimpleFeatureType schema = ftb.buildFeatureType();

        a.setExpression1(new LiteralExpressionImpl(Double.valueOf(5)));
        a.setExpression2(new LiteralExpressionImpl(Double.valueOf(15)));
        a.setExpression(new AttributeExpressionImpl(schema, "value"));

        // FlatFeatureFactory fFac = new FlatFeatureFactory(schema);
        LOGGER.fine("geometry is " + schema.getDescriptor("geometry"));
        LOGGER.fine("value is " + schema.getDescriptor("value"));
        LOGGER.fine("schema has value in it ? " + (schema.getDescriptor("value") != null));

        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        SimpleFeature f1 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {Integer.valueOf(12), gf.createPoint(new Coordinate(12, 12))},
                        null);
        SimpleFeature f2 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {Integer.valueOf(3), gf.createPoint(new Coordinate(3, 3))},
                        null);
        SimpleFeature f3 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {Integer.valueOf(15), gf.createPoint(new Coordinate(15, 15))},
                        null);
        SimpleFeature f4 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {Integer.valueOf(5), gf.createPoint(new Coordinate(5, 5))},
                        null);
        SimpleFeature f5 =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {Integer.valueOf(30), gf.createPoint(new Coordinate(30, 30))},
                        null);

        assertEquals(true, a.evaluate(f1)); // in between
        assertEquals(false, a.evaluate(f2)); // too small
        assertEquals(true, a.evaluate(f3)); // max value
        assertEquals(true, a.evaluate(f4)); // min value
        assertEquals(false, a.evaluate(f5)); // too large
    }

    @Test
    public void testEquals() throws Exception {
        org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        PropertyIsBetween f1 = ff.between(ff.property("abc"), ff.literal(10), ff.literal(20));
        PropertyIsBetween f2 = ff.between(ff.property("efg"), ff.literal(10), ff.literal(20));
        PropertyIsBetween f3 = ff.between(ff.property("abc"), ff.literal(10), ff.literal(20));

        assertEquals(f1, f3);
        assertFalse(f1.equals(f2));
    }
}
