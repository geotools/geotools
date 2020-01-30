/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2016 , Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.label;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.renderer.label.LabelCacheImpl.LabelRenderingMode;
import org.geotools.renderer.lite.RendererBaseTest;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.TextStyle2D;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mockito;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

public class LabelPainterTest {

    private static GeometryFactory geometryFactory = new GeometryFactory();
    private static StyleFactory styleFactory = new StyleFactoryImpl();
    private Graphics2D graphics;
    private TextStyle2D style;
    private TextSymbolizer symbolizer;
    LiteShape2 shape;

    @Before
    public void setUp()
            throws TransformException, FactoryException, IOException, FontFormatException {
        graphics = Mockito.mock(Graphics2D.class);
        Mockito.when(graphics.getFontRenderContext())
                .thenReturn(
                        new FontRenderContext(
                                new AffineTransform(),
                                RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
                                RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT));
        style = new TextStyle2D();
        style.setFont(new Font("Serif", Font.PLAIN, 10));
        shape =
                new LiteShape2(
                        geometryFactory.createPoint(new Coordinate(10, 10)),
                        ProjectiveTransform.create(new AffineTransform()),
                        null,
                        false);
        symbolizer = styleFactory.createTextSymbolizer();

        RendererBaseTest.setupVeraFonts();
    }

    @Test
    public void testEmptyLinesInLabel() {
        LabelPainter painter = new LabelPainter(graphics, LabelRenderingMode.STRING);
        LabelCacheItem labelItem =
                new LabelCacheItem("LAYERID", style, shape, "line1\n\nline2", symbolizer);
        labelItem.setAutoWrap(0);
        painter.setLabel(labelItem);
        assertEquals(3, painter.getLineCount());
    }

    @Test
    public void testEmptyLinesInLabelWithAutoWrap() {
        LabelPainter painter = new LabelPainter(graphics, LabelRenderingMode.STRING);
        LabelCacheItem labelItem =
                new LabelCacheItem("LAYERID", style, shape, "line1\n\nline2", symbolizer);
        labelItem.setAutoWrap(100);
        painter.setLabel(labelItem);
        assertEquals(3, painter.getLineCount());
    }

    @Test
    public void testOnlyNewlines() {
        LabelPainter painter = new LabelPainter(graphics, LabelRenderingMode.STRING);
        LabelCacheItem labelItem = new LabelCacheItem("LAYERID", style, shape, "\n\n", symbolizer);
        labelItem.setAutoWrap(100);
        painter.setLabel(labelItem);
        // emtpy label
        assertEquals(0, painter.getLineCount());
    }

    @Test
    public void testGetLastLineHeightOnlyNewLines() {
        LabelPainter painter = new LabelPainter(graphics, LabelRenderingMode.STRING);
        LabelCacheItem labelItem = new LabelCacheItem("LAYERID", style, shape, "\n\n", symbolizer);
        labelItem.setAutoWrap(100);
        painter.setLabel(labelItem);
        // should default to 0 with no lines to paint
        assertTrue(painter.getLineHeightForAnchorY(0) == 0.0);
    }

    @Test
    public void testGetLastLineHeightLabelWithAutoWrap() {
        LabelPainter painter = new LabelPainter(graphics, LabelRenderingMode.STRING);
        LabelCacheItem labelItem =
                new LabelCacheItem("LAYERID", style, shape, "line1\n\nline2", symbolizer);
        labelItem.setAutoWrap(100);
        painter.setLabel(labelItem);
        // should not default to 0
        assertTrue(painter.getLineHeightForAnchorY(0) > 0.0);

        // should get line height of first line
        assertTrue(painter.lines.get(0).getLineHeight() == painter.getLineHeightForAnchorY(1));

        // should get line height of last line
        assertTrue(
                painter.lines.get(painter.getLineCount() - 1).getLineHeight()
                        == painter.getLineHeightForAnchorY(1));
    }

    @Test
    public void testFullLabelBoundsNativeSize() throws Exception {
        TextStyle2D style = new TextStyle2D();
        style.setFont(new Font("Bitstream Vera Sans", Font.PLAIN, 10));

        LabelPainter painter = new LabelPainter(graphics, LabelRenderingMode.STRING);
        LabelCacheItem labelItem = new LabelCacheItem("LAYERID", style, shape, "line1", symbolizer);
        painter.setLabel(labelItem);

        double tolerance = 2; // account for JDK/OS specific differences despite the same font

        // check bounds with no graphic
        Rectangle2D lBounds = painter.getFullLabelBounds();
        assertEquals(0, lBounds.getMinX(), tolerance);
        assertEquals(-7.5, lBounds.getMinY(), tolerance);
        assertEquals(22, lBounds.getWidth(), tolerance);
        assertEquals(8, lBounds.getHeight(), tolerance);

        // set a graphic
        MarkStyle2D mark = new MarkStyle2D();
        mark.setShape(new Rectangle2D.Double(-.5, -.5, 1., 1.));
        mark.setSize(20);
        style.setGraphic(mark);
        painter.setLabel(labelItem);

        // check bounds with graphic (expands height)
        Rectangle2D lgBounds = painter.getFullLabelBounds();
        assertEquals(0, lgBounds.getMinX(), tolerance);
        assertEquals(-13.5, lgBounds.getMinY(), tolerance);
        assertEquals(22, lgBounds.getWidth(), tolerance);
        assertEquals(20, lgBounds.getHeight(), tolerance);

        // stretch graphics with margin
        labelItem.setGraphicsResize(LabelCacheItem.GraphicResize.STRETCH);
        labelItem.setGraphicMargin(new int[] {5, 5, 5, 5});
        painter.setLabel(labelItem);

        Rectangle2D lgsBounds = painter.getFullLabelBounds();
        assertEquals(-5, lgsBounds.getMinX(), tolerance);
        assertEquals(-12.5, lgsBounds.getMinY(), tolerance);
        assertEquals(32, lgsBounds.getWidth(), tolerance);
        assertEquals(18, lgsBounds.getHeight(), tolerance);

        // same as above but grow proportionally instead
        labelItem.setGraphicsResize(LabelCacheItem.GraphicResize.PROPORTIONAL);
        labelItem.setGraphicMargin(null);
        painter.setLabel(labelItem);

        Rectangle2D lgpBounds = painter.getFullLabelBounds();
        assertEquals(0, lgpBounds.getMinX(), tolerance);
        assertEquals(-14, lgpBounds.getMinY(), tolerance);
        assertEquals(22, lgpBounds.getWidth(), tolerance);
        assertEquals(22, lgpBounds.getHeight(), tolerance);
    }
}
