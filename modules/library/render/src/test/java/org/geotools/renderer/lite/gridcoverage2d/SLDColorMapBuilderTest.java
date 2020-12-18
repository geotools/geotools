/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import static org.junit.Assert.assertEquals;

import it.geosolutions.jaiext.classifier.LinearColorMap;
import it.geosolutions.jaiext.classifier.LinearColorMap.LinearColorMapType;
import it.geosolutions.jaiext.classifier.LinearColorMapElement;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ColorMapEntryImpl;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.css.CssParser;
import org.geotools.styling.css.CssTranslator;
import org.geotools.styling.css.Stylesheet;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.FeatureTypeStyle;
import org.opengis.style.Rule;
import org.opengis.style.Style;

public class SLDColorMapBuilderTest {
    private SLDColorMapBuilder builder;
    private ColorMapEntry entry;
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    Map<String, Object> envValues;

    @Before
    public void setUp() {
        builder = new SLDColorMapBuilder();
        builder.setNumberColorMapEntries(1);
        builder.setLinearColorMapType(LinearColorMapType.TYPE_VALUES);

        entry = new ColorMapEntryImpl();
        entry.setColor(ff.literal("#000000"));
        entry.setQuantity(ff.literal(1.0));
        entry.setOpacity(ff.literal(1.0));
        entry.setLabel("label");

        envValues = new HashMap<>();
    }

    @Test
    public void testDynamicQuantity() {
        entry.setQuantity(ff.literal("${6.0+4.0}"));

        builder.addColorMapEntry(entry);
        LinearColorMap colorMap = builder.buildLinearColorMap();
        LinearColorMapElement[] domainElements = colorMap.getDomainElements();
        assertEquals(1, domainElements.length);
        assertEquals(10.0, domainElements[0].getInputMinimum(), 0.0);
        assertEquals(10.0, domainElements[0].getInputMaximum(), 0.0);
    }

    @Test
    public void testDynamicQuantityEnv() {
        EnvFunction.setLocalValue("quantity", 10.0);
        try {
            entry.setQuantity(ff.literal("${env('quantity')}"));

            builder.addColorMapEntry(entry);
            LinearColorMap colorMap = builder.buildLinearColorMap();
            LinearColorMapElement[] domainElements = colorMap.getDomainElements();
            assertEquals(1, domainElements.length);
            assertEquals(10.0, domainElements[0].getInputMinimum(), 0.0);
            assertEquals(10.0, domainElements[0].getInputMaximum(), 0.0);
        } finally {
            EnvFunction.clearLocalValues();
        }
    }

    @Test
    public void testDynamicColor() {
        entry.setColor(ff.literal("${strConcat('#FF','0000')}"));

        builder.addColorMapEntry(entry);
        LinearColorMap colorMap = builder.buildLinearColorMap();
        LinearColorMapElement[] domainElements = colorMap.getDomainElements();
        assertEquals(1, domainElements.length);
        assertEquals(1, domainElements[0].getColors().length);
        assertEquals(255, domainElements[0].getColors()[0].getRed());
        assertEquals(0, domainElements[0].getColors()[0].getGreen());
        assertEquals(0, domainElements[0].getColors()[0].getBlue());
    }

    @Test
    public void testDynamicColorEnv() {
        EnvFunction.setLocalValue("color", "#FF0000");
        try {
            entry.setColor(ff.literal("${env('color')}"));

            builder.addColorMapEntry(entry);
            LinearColorMap colorMap = builder.buildLinearColorMap();
            LinearColorMapElement[] domainElements = colorMap.getDomainElements();
            assertEquals(1, domainElements.length);
            assertEquals(1, domainElements[0].getColors().length);
            assertEquals(255, domainElements[0].getColors()[0].getRed());
            assertEquals(0, domainElements[0].getColors()[0].getGreen());
            assertEquals(0, domainElements[0].getColors()[0].getBlue());
        } finally {
            EnvFunction.clearLocalValues();
        }
    }

    @Test
    public void testDynamicOpacityEnv() {
        EnvFunction.setLocalValue("opacity", 0.5);
        try {
            entry.setOpacity(ff.literal("${env('opacity')}"));

            builder.addColorMapEntry(entry);
            LinearColorMap colorMap = builder.buildLinearColorMap();
            LinearColorMapElement[] domainElements = colorMap.getDomainElements();
            assertEquals(1, domainElements.length);
            assertEquals(1, domainElements[0].getColors().length);
            assertEquals(128, domainElements[0].getColors()[0].getAlpha());
        } finally {
            EnvFunction.clearLocalValues();
        }
    }

    @Test
    public void testDynamicOpacity() {
        entry.setOpacity(ff.literal("${0.25*2}"));

        builder.addColorMapEntry(entry);
        LinearColorMap colorMap = builder.buildLinearColorMap();
        LinearColorMapElement[] domainElements = colorMap.getDomainElements();
        assertEquals(1, domainElements.length);
        assertEquals(1, domainElements[0].getColors().length);
        assertEquals(128, domainElements[0].getColors()[0].getAlpha());
    }

    @Test
    public void testDynamicColorMapEntryFromCss() throws Exception {
        String css =
                "* { raster-channels: 'auto'; raster-color-map: color-map-entry("
                        + "[env('color','#FF0000')], "
                        + "[env('min', 1) + 2 * (env('max',10) - env('min',1)) / 3],"
                        + "[env('customOpacity', 1)])  ;}";
        LinearColorMap colorMap = buildColorMapFromCss(css);
        assertColorMap(colorMap, 255, 0, 0, 7, 255);

        EnvFunction.setGlobalValue("min", 10);
        EnvFunction.setGlobalValue("max", 40);
        EnvFunction.setGlobalValue("customOpacity", 0.5);
        EnvFunction.setGlobalValue("color", "#0000FF");

        colorMap = buildColorMapFromCss(css);
        assertColorMap(colorMap, 0, 0, 255, 30, 128);
    }

    private LinearColorMap buildColorMapFromCss(String css) {
        Stylesheet ss = CssParser.parse(css);
        CssTranslator translator = new CssTranslator();
        Style style = translator.translate(ss);
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        Rule rule = fts.rules().get(0);
        RasterSymbolizer rs = (RasterSymbolizer) rule.symbolizers().get(0);
        ColorMap cm = rs.getColorMap();
        SLDColorMapBuilder builder = new SLDColorMapBuilder("test");
        builder.setNumberColorMapEntries(1);
        builder.setLinearColorMapType(LinearColorMapType.TYPE_VALUES);
        builder.addColorMapEntry(cm.getColorMapEntry(0));
        return builder.buildLinearColorMap();
    }

    private void assertColorMap(
            LinearColorMap colorMap, int red, int green, int blue, int quantity, int opacity) {
        LinearColorMapElement element = colorMap.get(0);
        Color[] colors = element.getColors();
        assertEquals(1, colors.length);
        Color color = colors[0];
        assertEquals(red, color.getRed());
        assertEquals(green, color.getGreen());
        assertEquals(blue, color.getBlue());
        assertEquals(opacity, color.getAlpha());
        assertEquals(quantity, element.getRange().getMin().intValue());
    }
}
