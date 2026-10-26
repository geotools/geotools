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
import static org.geotools.api.style.FeatureTypeStyle.RenderingSelectionOptions.MAPONLY;
import static org.geotools.api.style.FeatureTypeStyle.RenderingSelectionOptions.NORMAL;
import static org.geotools.api.style.FeatureTypeStyle.VENDOR_OPTION_INCLUSION;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Rule;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.Symbolizer;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;

/**
 * Copies a feature type style for a map at one scale. The copy holds only the rules the map draws, and rules the map
 * does not draw are not copied at all. Rules without a scale range take the one of their feature type style. Elements
 * meant for legends only are left out, and filters and expressions are simplified.
 *
 * <p>It combines the work of {@code MapRenderingSelectorStyleVisitor}, {@code FeatureStyleScaleStyleVisitor} and
 * {@link SimplifyingStyleVisitor}.
 */
class MapScaleStyleVisitor extends SimplifyingStyleVisitor {

    private static final Logger LOGGER = Logging.getLogger(MapScaleStyleVisitor.class);

    private final double scaleDenominator;

    private double ftsMinScale;
    private double ftsMaxScale = Double.POSITIVE_INFINITY;

    MapScaleStyleVisitor(
            StyleFactory styleFactory, FilterFactory filterFactory, FeatureType schema, double scaleDenominator) {
        super(styleFactory, filterFactory, schema);
        this.scaleDenominator = scaleDenominator;
    }

    /** Returns the copy of the feature type style, or {@code null} when the map draws none of its rules. */
    FeatureTypeStyle copyForMap(FeatureTypeStyle fts) {
        fts.accept(this);
        return pages.isEmpty() ? null : (FeatureTypeStyle) pages.pop();
    }

    @Override
    public void visit(FeatureTypeStyle fts) {
        if (!isDrawnOnMap(fts.getOptions())) return;
        ftsMinScale = readScaleDenominator(fts, MIN_SCALE_DENOMINATOR, 0d);
        ftsMaxScale = readScaleDenominator(fts, MAX_SCALE_DENOMINATOR, Double.POSITIVE_INFINITY);
        if (fts.rules().stream().noneMatch(this::isDrawn)) return;
        super.visit(fts);

        // copy the transformation so requests do not share it, unsimplified as in SimplifyingStyleVisitor
        Expression transformation = fts.getTransformation();
        if (transformation != null) {
            FeatureTypeStyle copy = (FeatureTypeStyle) pages.peek();
            DuplicatingFilterVisitor duplicator = new DuplicatingFilterVisitor(ff);
            copy.setTransformation((Expression) transformation.accept(duplicator, null));
        }
    }

    /** Returns the scale denominator in the option, or the default when the option is missing, blank or invalid. */
    private static double readScaleDenominator(FeatureTypeStyle fts, String optionKey, double defaultValue) {
        String value = fts.getOptions().get(optionKey);
        if (value == null || value.isBlank()) return defaultValue;
        Double scale = Converters.convert(value.trim(), Double.class);
        if (scale != null) return scale;
        LOGGER.log(
                Level.WARNING,
                "{0} option present but invalid in FeatureTypeStyle options: \"{1}\". Using default {2}.",
                new Object[] {optionKey, value, defaultValue});
        return defaultValue;
    }

    @Override
    public void visit(Rule rule) {
        if (!isDrawn(rule)) return;
        super.visit(rule);
        Rule copy = (Rule) pages.peek();
        copy.setMinScaleDenominator(getMinScale(rule));
        copy.setMaxScaleDenominator(getMaxScale(rule));
    }

    @Override
    protected Symbolizer copy(Symbolizer symbolizer) {
        if (symbolizer == null || !isDrawnOnMap(symbolizer.getOptions())) return null;
        return super.copy(symbolizer);
    }

    private boolean isDrawn(Rule rule) {
        return rule != null
                && isDrawnOnMap(rule.getOptions())
                && StreamingRenderer.isWithInScale(getMinScale(rule), getMaxScale(rule), scaleDenominator);
    }

    /** Returns the rule's own minimum scale, or the one of its feature type style when the rule has none. */
    private double getMinScale(Rule rule) {
        return rule.getMinScaleDenominator() == 0d ? ftsMinScale : rule.getMinScaleDenominator();
    }

    /** Returns the rule's own maximum scale, or the one of its feature type style when the rule has none. */
    private double getMaxScale(Rule rule) {
        return rule.getMaxScaleDenominator() == Double.POSITIVE_INFINITY ? ftsMaxScale : rule.getMaxScaleDenominator();
    }

    /** Applies the {@code inclusion} vendor option as {@code MapRenderingSelectorStyleVisitor} does. */
    private static boolean isDrawnOnMap(Map<String, String> options) {
        String inclusion = options == null ? null : options.get(VENDOR_OPTION_INCLUSION);
        return inclusion == null
                || NORMAL.name().equalsIgnoreCase(inclusion)
                || MAPONLY.name().equalsIgnoreCase(inclusion);
    }
}
