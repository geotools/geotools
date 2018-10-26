/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style.svg;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.TransformedShape;
import org.geotools.renderer.lite.StreamingRenderer;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

public class SVGMarkFactoryTest {

    private FilterFactory ff;

    @Before
    public void setUp() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    @Test
    public void testSimpleSquare() throws Exception {
        SVGMarkFactory mf = new SVGMarkFactory();
        URL url = StreamingRenderer.class.getResource("test-data/square16.svg");
        assertNotNull(url);
        // first call, non cached path
        Shape shape = mf.getShape(null, ff.literal(url), null);
        assertNotNull(shape);
        final TransformedShape expected =
                new TransformedShape(
                        new Rectangle2D.Float(0, 0, 1, 1),
                        new AffineTransform(1, 0, 0, -1, -0.5, 0.5));
        assertEquals(expected, shape);

        // second call, hopefully using the cached path
        shape = mf.getShape(null, ff.literal(url), null);
        assertNotNull(shape);
        assertEquals(expected, shape);
    }

    @Test
    public void testTwoSquares() throws Exception {
        SVGMarkFactory mf = new SVGMarkFactory();
        URL url = StreamingRenderer.class.getResource("test-data/twoSquares.svg");
        assertNotNull(url);
        // first call, non cached path
        Shape shape = mf.getShape(null, ff.literal(url), null);
        assertNotNull(shape);
        GeneralPath gp = new GeneralPath(new Rectangle2D.Double(0, 0, 0.5, 0.5));
        gp.append(new Rectangle2D.Double(0.5, 0.5, 0.5, 0.5), false);
        Shape expected = new TransformedShape(gp, new AffineTransform(1, 0, 0, -1, -0.5, 0.5));
        assertEquals(expected, shape);

        // second call, hopefully using the cached path
        shape = mf.getShape(null, ff.literal(url), null);
        assertNotNull(shape);
        assertEquals(expected, shape);
    }

    private void assertEquals(Shape shape1, Shape shape2) {
        PathIterator it1 = shape1.getPathIterator(null);
        PathIterator it2 = shape2.getPathIterator(null);
        double[] d1 = new double[6];
        double[] d2 = new double[6];
        boolean done = it1.isDone() && it2.isDone();
        while (!done) {
            if (it1.isDone() != it2.isDone()) {
                fail("The two path iterators are not done at the same time");
            }
            int seg1 = it1.currentSegment(d1);
            int seg2 = it2.currentSegment(d2);
            if (seg1 != seg2) {
                fail("The two path iterators are not returning the same point type");
            }
            if (!Arrays.equals(d1, d2)) {
                fail(
                        "The two path iterators are not returning the same coordinates\n"
                                + Arrays.toString(d1)
                                + "\n"
                                + Arrays.toString(d2));
            }
            it1.next();
            it2.next();
            done = it1.isDone() && it2.isDone();
        }
    }
}
