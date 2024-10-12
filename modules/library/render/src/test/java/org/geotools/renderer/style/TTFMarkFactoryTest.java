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
package org.geotools.renderer.style;

import java.awt.Shape;
import org.geotools.api.filter.FilterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TTFMarkFactoryTest {
    private TTFMarkFactory ttf;
    private FilterFactory ff;

    @Before
    public void setUp() throws Exception {
        ttf = new TTFMarkFactory();
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    /**
     * Checks various malformed ttf paths, some that should be ignored, some that we should complain
     * about
     */
    @Test
    public void testInvalidPaths() throws Exception {
        Assert.assertNull(ttf.getShape(null, ff.literal("font://invalid"), null));
        try {
            ttf.getShape(null, ff.literal("ttf://invalid"), null);
            Assert.fail("Should have throw an exception, invalid path");
        } catch (IllegalArgumentException e) {
        }
        try {
            ttf.getShape(null, ff.literal("ttf://missingFont#56"), null);
            Assert.fail("Should have throw an exception, invalid font");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("missingFont"));
        }
        try {
            ttf.getShape(null, ff.literal("ttf://Serif#blah"), null);
            Assert.fail("Should have throw an exception, invalid char number");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("blah"));
        }
    }

    /** Checks valid paths */
    @Test
    public void testValidPathSimpleNumber() throws Exception {
        Shape shape = ttf.getShape(null, ff.literal("ttf://Serif#56"), null);
        Assert.assertNotNull(shape);
        Assert.assertTrue(shape.getBounds2D().getWidth() <= 1);
        Assert.assertTrue(shape.getBounds2D().getHeight() <= 1);
    }

    /** Checks valid paths */
    @Test
    public void testValidPathHex() throws Exception {
        Shape shape = ttf.getShape(null, ff.literal("ttf://Serif#0xF054"), null);
        Assert.assertNotNull(shape);
        Assert.assertTrue(shape.getBounds2D().getWidth() <= 1);
        Assert.assertTrue(shape.getBounds2D().getHeight() <= 1);
    }

    /** Checks valid paths */
    @Test
    public void testValidPathUnicode() throws Exception {
        Shape shape = ttf.getShape(null, ff.literal("ttf://Serif#U+0041"), null);
        Assert.assertNotNull(shape);
        Assert.assertTrue(shape.getBounds2D().getWidth() <= 1);
        Assert.assertTrue(shape.getBounds2D().getHeight() <= 1);
    }

    /** Checks valid paths */
    @Test
    public void testLocalFont() throws Exception {
        String fontPath = TestData.getResource(StreamingRenderer.class, "recreate.ttf")
                .toURI()
                .toString();
        Shape shape = ttf.getShape(null, ff.literal("ttf://" + fontPath + "#U+0021"), null);
        Assert.assertNotNull(shape);
        Assert.assertTrue(shape.getBounds2D().getWidth() <= 1);
        Assert.assertTrue(shape.getBounds2D().getHeight() <= 1);
    }
}
