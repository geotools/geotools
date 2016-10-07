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

import java.awt.RenderingHints.Key;
import java.net.URL;
import java.util.HashMap;

import javax.swing.Icon;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.xml.NullEntityResolver;
import org.geotools.xml.PreventLocalEntityResolver;
import org.opengis.filter.FilterFactory;

/**
 * 
 *
 * @source $URL$
 */
public class SVGGraphicFactoryTest extends TestCase {

    private FilterFactory ff;

    @Override
    protected void setUp() throws Exception {
        
        ff = CommonFactoryFinder.getFilterFactory(null);
    }
    
    public void testNull() throws Exception {
        SVGGraphicFactory svg = new SVGGraphicFactory();
        assertNull(svg.getIcon(null, ff.literal("http://www.nowhere.com"), null, 20));
    }
    
    public void testInvalidPaths() throws Exception {
        SVGGraphicFactory svg = new SVGGraphicFactory();
        assertNull(svg.getIcon(null, ff.literal("http://www.nowhere.com"), "image/svg+not!", 20));
        try {
            svg.getIcon(null, ff.literal("ThisIsNotAUrl"), "image/svg", 20);
            fail("Should have throw an exception, invalid url");
        } catch(IllegalArgumentException e) {
        }
    }
    
    public void testLocalURL() throws Exception {
        SVGGraphicFactory svg = new SVGGraphicFactory();
        URL url = SVGGraphicFactory.class.getResource("gradient.svg");
        assertNotNull(url);
        // first call, non cached path
        Icon icon = svg.getIcon(null, ff.literal(url), "image/svg", 20);
        assertNotNull(icon);
        assertEquals(20, icon.getIconHeight());
        // check caching is working
        assertTrue(SVGGraphicFactory.glyphCache.containsKey(url.toString()));
        
        // second call, hopefully using the cached path
        icon = svg.getIcon(null, ff.literal(url), "image/svg", 20);
        assertNotNull(icon);
        assertEquals(20, icon.getIconHeight());
    }
    
    public void testLocalURLXEE() throws Exception {
        // disable references to entity stored on local file
        HashMap<Key, Object> hints = new HashMap<>();
        hints.put(Hints.ENTITY_RESOLVER, PreventLocalEntityResolver.INSTANCE );
        SVGGraphicFactory svg = new SVGGraphicFactory(hints);
        try {
            URL url = SVGGraphicFactory.class.getResource("attack.svg");
            Icon icon = svg.getIcon(null, ff.literal(url), "image/svg", 20);
            fail("expected entity resolution exception");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity resolution disallowed for file:///etc/passwd");
        }
        
        // now enable references to entity stored on local file 
        hints = new HashMap<>();
        hints.put(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE );
        svg = new SVGGraphicFactory(hints); // disable safety protection
        
        URL url = SVGGraphicFactory.class.getResource("attack.svg");
        Icon icon = svg.getIcon(null, ff.literal(url), "image/svg", 20);
        assertNotNull(icon);
    }

    public void testNaturalSize() throws Exception {
        SVGGraphicFactory svg = new SVGGraphicFactory();
        URL url = SVGGraphicFactory.class.getResource("gradient.svg");
        assertNotNull(url);
        // first call, non cached path
        Icon icon = svg.getIcon(null, ff.literal(url), "image/svg", -1);
        assertNotNull(icon);
        assertEquals(500, icon.getIconHeight());
    }
    
    public void testSizeWithPixels() throws Exception {
        SVGGraphicFactory svg = new SVGGraphicFactory();
        URL url = SVGGraphicFactory.class.getResource("gradient-pixels.svg");
        assertNotNull(url);
        // first call, non cached path
        Icon icon = svg.getIcon(null, ff.literal(url), "image/svg", -1);
        assertNotNull(icon);
        assertEquals(500, icon.getIconHeight());
    }
}
