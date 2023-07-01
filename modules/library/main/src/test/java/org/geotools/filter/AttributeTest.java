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

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

/**
 * Tests for Attribute Expressions
 *
 * @author James Macgill
 */
public class AttributeTest {
    SimpleFeatureType schema = null;

    public SimpleFeature[] sampleFeatures() throws Exception {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.add("value", Integer.class);
        ftb.add("geometry", Geometry.class);
        ftb.add("name", String.class);
        ftb.setName("test");
        schema = ftb.buildFeatureType();

        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        SimpleFeature[] f = new SimpleFeature[3];
        f[0] =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {
                            Integer.valueOf(12), gf.createGeometryCollection(null), "first"
                        },
                        null);
        f[1] =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {
                            Integer.valueOf(3), gf.createGeometryCollection(null), "second"
                        },
                        null);
        f[2] =
                SimpleFeatureBuilder.build(
                        schema,
                        new Object[] {
                            Integer.valueOf(15), gf.createGeometryCollection(null), "third"
                        },
                        null);

        return f;
    }

    @Test
    public void testTypeMissmatch() throws Exception {
        SimpleFeature[] f = sampleFeatures();

        // the following are intentionaly backwards
        AttributeExpressionImpl e1 = new AttributeExpressionImpl(schema, "value");
        AttributeExpressionImpl e2 = new AttributeExpressionImpl(schema, "name");
        boolean pass = false;
        Object value = e1.evaluate(f[0]);

        if (value instanceof Integer) {
            pass = true;
        }

        Assert.assertTrue("String expresion returned an Integer", pass);
        pass = false;

        value = e2.evaluate(f[0]);

        if (value instanceof String) {
            pass = true;
        }

        Assert.assertTrue("Integer expresion returned a String", pass);
    }

    @Test
    public void testSetupAndExtraction() throws Exception {
        // this should move out to a more configurable system run from scripts
        // but we can start with a set of hard coded tests
        SimpleFeature[] f = sampleFeatures();

        AttributeExpressionImpl e1 = new AttributeExpressionImpl(schema, "value");
        AttributeExpressionImpl e2 = new AttributeExpressionImpl(schema, "name");

        Assert.assertEquals(12d, ((Integer) e1.evaluate(f[0])).doubleValue(), 0);
        Assert.assertEquals(3d, ((Integer) e1.evaluate(f[1])).doubleValue(), 0);
        Assert.assertEquals(15d, ((Integer) e1.evaluate(f[2])).doubleValue(), 0);
        Assert.assertEquals("first", (String) e2.evaluate(f[0]));
        Assert.assertEquals("second", (String) e2.evaluate(f[1]));
    }
}
