/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.api.style.FeatureTypeStyle.MAX_SCALE_DENOMINATOR;
import static org.geotools.api.style.FeatureTypeStyle.MIN_SCALE_DENOMINATOR;
import static org.geotools.api.style.FeatureTypeStyle.VENDOR_OPTION_INCLUSION;
import static org.geotools.renderer.lite.RendererBaseTest.assertPixel;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

/** Checks that rendering never changes the style it is given, whatever the scale and the thread. */
public class StreamingRendererStyleTest {

    /** Lines at about 1:5,000,000 on a 300 pixel map: blue rule. */
    private static final ReferencedEnvelope NEAR = new ReferencedEnvelope(-2, 2, -2, 2, DefaultGeographicCRS.WGS84);

    /** Lines at about 1:50,000,000 on a 300 pixel map: red rule. */
    private static final ReferencedEnvelope FAR = new ReferencedEnvelope(-20, 20, -20, 20, DefaultGeographicCRS.WGS84);

    private static final double SWITCH_SCALE = 1e7;

    private PropertyDataStore store;
    private Style style;

    @Before
    public void setUp() throws Exception {
        File data =
                new File(TestData.getResource(this, "genericLines.properties").toURI());
        store = new PropertyDataStore(data.getParentFile());
        StyleBuilder sb = new StyleBuilder();
        Rule near = sb.createRule(sb.createLineSymbolizer(Color.BLUE, 3));
        near.setMaxScaleDenominator(SWITCH_SCALE);
        Rule far = sb.createRule(sb.createLineSymbolizer(Color.RED, 3));
        far.setMinScaleDenominator(SWITCH_SCALE);
        // never drawn on a map, and never applied as the other rules have no filter
        Rule legendOnly = sb.createRule(sb.createLineSymbolizer(Color.GREEN, 3));
        legendOnly.getOptions().put(VENDOR_OPTION_INCLUSION, "legendOnly");
        Rule elseRule = sb.createRule(sb.createLineSymbolizer(Color.YELLOW, 3));
        elseRule.setElseFilter(true);
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("genericLines", new Rule[] {near, far, legendOnly, elseRule});
        // wide enough to change no image, but the copies take these ranges on rules without their own
        fts.getOptions().put(MIN_SCALE_DENOMINATOR, "1");
        fts.getOptions().put(MAX_SCALE_DENOMINATOR, "1e12");
        style = sb.createStyle();
        style.featureTypeStyles().add(fts);
    }

    /** After rendering at two scales the style equals a copy taken before. */
    @Test
    public void testStyleNotModified() throws Exception {
        DuplicatingStyleVisitor duplicator = new DuplicatingStyleVisitor();
        style.accept(duplicator);
        Style before = (Style) duplicator.getCopy();

        render(NEAR);
        render(FAR);

        assertEquals(before, style);
    }

    /** Two threads drawing the same style at different scales get the same images as one thread alone. */
    @Test
    public void testConcurrentScales() throws Exception {
        BufferedImage near = render(NEAR);
        BufferedImage far = render(FAR);
        // the left line of the square, x = -1, sits at column 75 near and at column 142 far
        assertPixel(near, 75, 150, Color.BLUE);
        assertPixel(far, 142, 150, Color.RED);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Future<BufferedImage>> nearImages = new ArrayList<>();
            List<Future<BufferedImage>> farImages = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                nearImages.add(executor.submit(() -> render(NEAR)));
                farImages.add(executor.submit(() -> render(FAR)));
            }
            for (int i = 0; i < 20; i++) {
                assertSamePixels(near, nearImages.get(i).get());
                assertSamePixels(far, farImages.get(i).get());
            }
        } finally {
            executor.shutdown();
        }
    }

    private BufferedImage render(ReferencedEnvelope bounds) throws Exception {
        MapContent map = new MapContent();
        try {
            map.addLayer(new FeatureLayer(store.getFeatureSource("genericLines"), style));
            StreamingRenderer renderer = new StreamingRenderer();
            renderer.setMapContent(map);
            return RendererBaseTest.renderImage(renderer, bounds, null);
        } finally {
            map.dispose();
        }
    }

    private static void assertSamePixels(BufferedImage expected, BufferedImage actual) {
        int[] expectedPixels =
                expected.getRGB(0, 0, expected.getWidth(), expected.getHeight(), null, 0, expected.getWidth());
        int[] actualPixels = actual.getRGB(0, 0, actual.getWidth(), actual.getHeight(), null, 0, actual.getWidth());
        assertNotEquals(0, countNonWhite(expectedPixels));
        assertArrayEquals(expectedPixels, actualPixels);
    }

    private static long countNonWhite(int[] pixels) {
        long count = 0;
        for (int p : pixels) if (p != Color.WHITE.getRGB()) count++;
        return count;
    }
}
