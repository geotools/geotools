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
package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for rendering and reprojection
 * 
 * @author jandm
 * 
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.7.2/modules/library/render/src/test/java/org/geotools/renderer/lite/TileTest.java $
 */
public class TileTest {
    private static final long TIME = 4000;

    SimpleFeatureSource polyfs;

    SimpleFeatureSource linefs;

    ReferencedEnvelope leftTileBounds;

    ReferencedEnvelope rightTileBounds;

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "tilerect.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        polyfs = ds.getFeatureSource("tilerect");
        property = new File(TestData.getResource(this, "tilelines.properties").toURI());
        ds = new PropertyDataStore(property.getParentFile());
        linefs = ds.getFeatureSource("tilelines");

        leftTileBounds = new ReferencedEnvelope(0, 10, 0, 10, polyfs.getBounds()
                .getCoordinateReferenceSystem());
        rightTileBounds = new ReferencedEnvelope(10, 20, 0, 10, polyfs.getBounds()
                .getCoordinateReferenceSystem());

        // load font
        Font f = Font.createFont(Font.TRUETYPE_FONT, TestData.getResource(this, "recreate.ttf")
                .openStream());
        FontCache.getDefaultInstance().registerFont(f);

        // System.setProperty("org.geotools.test.interactive", "true");

    }

    @Test
    public void testFillAlignment() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillCross.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(polyfs, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        RendererBaseTest.showRender("FillAlignment", renderer, TIME, leftTileBounds,
                rightTileBounds);
    }

    @Test
    public void testStrokeAlignment() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "dotsStars.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(linefs, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        RendererBaseTest.showRender("StrokeAlignment", renderer, TIME, leftTileBounds,
                rightTileBounds);
    }

}
