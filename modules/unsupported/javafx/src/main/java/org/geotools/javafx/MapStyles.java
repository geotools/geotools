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

package org.geotools.javafx;

import java.awt.*;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

/** Created by Jochen Saalfeld <jochen.saalfeld@intevation.de> on 2/16/17. */
public class MapStyles {

    private Color selectedColor;
    private Color outlineColor;
    private Color fillColor;
    private float outlineWidth;
    private float fillTransparacy;
    private StyleBuilder sb;
    private StyleFactory sf;
    private FilterFactory2 ff;
    private String geometryAttributeName;
    private float strokeTransparacy;
    private static final Color OUTLINE_COLOR = Color.BLACK;
    private static final Color SELECTED_COLOUR = Color.YELLOW;
    private static final Color FILL_COLOR = Color.CYAN;
    private static final Float OUTLINE_WIDTH = 0.3f;
    private static final Float FILL_TRANSPARACY = 0.4f;
    private static final Float STROKE_TRANSPARACY = 0.8f;

    public MapStyles(
            Color selectedColor,
            Color outlineColor,
            Color fillColor,
            float outlineWidth,
            float fillTransparacy,
            float strokeTransparacy,
            String geometryAttributeName) {
        this.sb = new StyleBuilder();
        this.sf = CommonFactoryFinder.getStyleFactory(null);
        this.ff = CommonFactoryFinder.getFilterFactory2(null);
        this.selectedColor = selectedColor;
        this.outlineColor = outlineColor;
        this.fillColor = fillColor;
        this.outlineWidth = outlineWidth;
        this.fillTransparacy = fillTransparacy;
        this.geometryAttributeName = geometryAttributeName;
        this.strokeTransparacy = strokeTransparacy;
    }

    public MapStyles(String geometryAttributeName) {
        this(
                SELECTED_COLOUR,
                OUTLINE_COLOR,
                FILL_COLOR,
                OUTLINE_WIDTH,
                FILL_TRANSPARACY,
                STROKE_TRANSPARACY,
                geometryAttributeName);
    }

    public Style createSelectedStyle(FeatureId ids) {
        Rule selectedRule = createRule(selectedColor, selectedColor);
        selectedRule.setFilter(ff.id(ids));

        Rule otherRule = createRule(outlineColor, fillColor);
        otherRule.setElseFilter(true);

        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.rules().add(selectedRule);
        fts.rules().add(otherRule);

        Style style = sf.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    private Rule createRule(Color outlineColor, Color fillColor) {
        Symbolizer symbolizer = null;
        Fill fill = null;
        Stroke stroke = sf.createStroke(ff.literal(outlineColor), ff.literal(outlineWidth));

        fill = sf.createFill(ff.literal(fillColor), ff.literal(fillTransparacy));
        symbolizer = sf.createPolygonSymbolizer(stroke, fill, geometryAttributeName);

        Rule rule = sf.createRule();
        rule.symbolizers().add(symbolizer);
        return rule;
    }

    public Style createPolygonStyle() {
        Fill fill = sf.createFill(ff.literal(fillColor), ff.literal(fillTransparacy));
        Stroke stroke =
                sf.createStroke(
                        ff.literal(outlineColor),
                        ff.literal(outlineWidth),
                        ff.literal(strokeTransparacy));
        PolygonSymbolizer polygonSymbolizer = sf.createPolygonSymbolizer(stroke, fill, null);
        return this.sb.createStyle(polygonSymbolizer);
    }
}
