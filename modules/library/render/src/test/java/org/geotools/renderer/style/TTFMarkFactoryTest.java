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

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.test.TestData;
import org.opengis.filter.FilterFactory;

public class TTFMarkFactoryTest extends TestCase {
    private TTFMarkFactory ttf;
    private FilterFactory ff;

    @Override
    protected void setUp() throws Exception {
        ttf = new TTFMarkFactory();
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    /**
     * Checks various malformed ttf paths, some that should be ignored, some that we should
     * complain about
     * @throws Exception
     */
    public void testInvalidPaths() throws Exception {
        assertNull(ttf.getShape(null, ff.literal("font://invalid"), null));
        try {
            ttf.getShape(null, ff.literal("ttf://invalid"), null);
            fail("Should have throw an exception, invalid path");
        } catch(IllegalArgumentException e) {
        }
        try {
            ttf.getShape(null, ff.literal("ttf://missingFont#56"), null);
            fail("Should have throw an exception, invalid font");
        } catch(IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("missingFont"));
        }
        try {
            ttf.getShape(null, ff.literal("ttf://Serif#blah"), null);
            fail("Should have throw an exception, invalid char number");
        } catch(IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("blah"));
        }
    }
    
    /**
     * Checks valid paths
     * @throws Exception
     */
    public void testValidPathSimpleNumber() throws Exception {
        Shape shape = ttf.getShape(null, ff.literal("ttf://Serif#56"), null);
        assertNotNull(shape);
        assertTrue(shape.getBounds2D().getWidth() <= 1);
        assertTrue(shape.getBounds2D().getHeight() <= 1);
    }
    
    /**
     * Checks valid paths
     * @throws Exception
     */
    public void testValidPathHex() throws Exception {
        Shape shape = ttf.getShape(null, ff.literal("ttf://Serif#0xF054"), null);
        assertNotNull(shape);
        assertTrue(shape.getBounds2D().getWidth() <= 1);
        assertTrue(shape.getBounds2D().getHeight() <= 1);
    }
    
    /**
     * Checks valid paths
     * @throws Exception
     */
    public void testValidPathUnicode() throws Exception {
        Shape shape = ttf.getShape(null, ff.literal("ttf://Serif#U+0041"), null);
        assertNotNull(shape);
        assertTrue(shape.getBounds2D().getWidth() <= 1);
        assertTrue(shape.getBounds2D().getHeight() <= 1);
    }
    
    /**
     * Checks valid paths
     * @throws Exception
     */
    public void testLocalFont() throws Exception {
        String fontPath = TestData.getResource(StreamingRenderer.class, "recreate.ttf").toURI().toString();
        Shape shape = ttf.getShape(null, ff.literal("ttf://" +  fontPath + "#U+0021"), null);
        assertNotNull(shape);
        assertTrue(shape.getBounds2D().getWidth() <= 1);
        assertTrue(shape.getBounds2D().getHeight() <= 1);
    }
}
