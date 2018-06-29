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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.renderer.label.LabelCacheImpl.LabelRenderingMode;
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
    public void setUp() throws TransformException, FactoryException {
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
}
