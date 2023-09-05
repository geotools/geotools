/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2022, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.GeometryTransformationVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Assert;
import org.junit.Test;

public class GeometryFilterVisitorTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testSimpleBuffer() {
        org.geotools.api.filter.expression.Expression geomTx =
                ff.function("buffer", ff.property("the_geom"), ff.literal(2));

        ReferencedEnvelope re = new ReferencedEnvelope(0, 2, 0, 2, null);

        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = (ReferencedEnvelope) geomTx.accept(visitor, re);

        ReferencedEnvelope expected = new ReferencedEnvelope(-2, 4, -2, 4, null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testChainBuffer() {
        // check buffer chaining
        Function innerBuffer = ff.function("buffer", ff.property("the_geom"), ff.literal(3));
        Function geomTx = ff.function("buffer", innerBuffer, ff.literal(2));

        ReferencedEnvelope re = new ReferencedEnvelope(0, 2, 0, 2, null);

        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = (ReferencedEnvelope) geomTx.accept(visitor, re);

        ReferencedEnvelope expected = new ReferencedEnvelope(-5, 7, -5, 7, null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testChainIntersection() {
        Function innerBuffer1 = ff.function("buffer", ff.property("the_geom"), ff.literal(3));
        Function innerBuffer2 = ff.function("buffer", ff.property("other_geom"), ff.literal(2));
        Function geomTx = ff.function("intersection", innerBuffer1, innerBuffer2);

        ReferencedEnvelope re = new ReferencedEnvelope(0, 2, 0, 2, null);

        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = (ReferencedEnvelope) geomTx.accept(visitor, re);

        ReferencedEnvelope expected = new ReferencedEnvelope(-3, 5, -3, 5, null);
        Assert.assertEquals(expected, result);
    }
}
