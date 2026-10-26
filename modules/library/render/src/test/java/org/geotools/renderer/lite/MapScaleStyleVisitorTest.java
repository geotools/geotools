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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Rule;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.Symbolizer;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.StyleBuilder;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.mockito.MockedStatic;

public class MapScaleStyleVisitorTest {

    private static final double SCALE = 50_000;

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    private static final StyleFactory SF = CommonFactoryFinder.getStyleFactory();

    private final StyleBuilder sb = new StyleBuilder();

    /** Only the rules drawn at the map scale are copied, the others never reach the copy code. */
    @Test
    public void testRulesOutOfScale() {
        Rule inScale = rule("inScale", 0, Double.POSITIVE_INFINITY);
        Rule tooSmall = rule("tooSmall", 0, 1000);
        Rule tooLarge = rule("tooLarge", 1_000_000, Double.POSITIVE_INFINITY);
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("Feature", new Rule[] {inScale, tooSmall, tooLarge});

        List<Symbolizer> copied = new ArrayList<>();
        MapScaleStyleVisitor visitor = new MapScaleStyleVisitor(SF, FF, null, SCALE) {
            @Override
            protected Symbolizer copy(Symbolizer symbolizer) {
                copied.add(symbolizer);
                return super.copy(symbolizer);
            }
        };
        FeatureTypeStyle copy = visitor.copyForMap(fts);

        assertEquals(List.of("inScale"), ruleNames(copy));
        assertEquals(inScale.symbolizers(), copied);
    }

    /** A feature type style without rules in scale gives no copy. */
    @Test
    public void testNoRuleInScale() {
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("Feature", rule("tooSmall", 0, 1000));
        assertNull(new MapScaleStyleVisitor(SF, FF, null, SCALE).copyForMap(fts));
    }

    /** Rules without a scale range take the one of the feature type style vendor options. */
    @Test
    public void testFeatureTypeStyleScale() {
        Rule noScale = rule("noScale", 0, Double.POSITIVE_INFINITY);
        Rule ownScale = rule("ownScale", 10_000, 100_000);
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("Feature", new Rule[] {noScale, ownScale});
        fts.getOptions().put(MIN_SCALE_DENOMINATOR, "40000");
        fts.getOptions().put(MAX_SCALE_DENOMINATOR, "60000");

        FeatureTypeStyle copy = new MapScaleStyleVisitor(SF, FF, null, SCALE).copyForMap(fts);
        assertEquals(List.of("noScale", "ownScale"), ruleNames(copy));
        assertEquals(40_000, copy.rules().get(0).getMinScaleDenominator(), 0);
        assertEquals(60_000, copy.rules().get(0).getMaxScaleDenominator(), 0);
        assertEquals(10_000, copy.rules().get(1).getMinScaleDenominator(), 0);
        assertEquals(100_000, copy.rules().get(1).getMaxScaleDenominator(), 0);

        // at 1:20,000 the feature type style range excludes the rule without its own range
        assertEquals(List.of("ownScale"), ruleNames(new MapScaleStyleVisitor(SF, FF, null, 20_000).copyForMap(fts)));
    }

    /** The inclusion vendor option drops what is meant for legends only, at every level. */
    @Test
    public void testInclusion() {
        Rule mapOnly = rule("mapOnly", 0, Double.POSITIVE_INFINITY);
        mapOnly.getOptions().put(VENDOR_OPTION_INCLUSION, "mapOnly");
        Rule legendOnly = rule("legendOnly", 0, Double.POSITIVE_INFINITY);
        legendOnly.getOptions().put(VENDOR_OPTION_INCLUSION, "legendOnly");
        Rule mixed = rule("mixed", 0, Double.POSITIVE_INFINITY);
        Symbolizer legendSymbolizer = sb.createPointSymbolizer();
        legendSymbolizer.getOptions().put(VENDOR_OPTION_INCLUSION, "legendOnly");
        mixed.symbolizers().add(legendSymbolizer);
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("Feature", new Rule[] {mapOnly, legendOnly, mixed});

        FeatureTypeStyle copy = new MapScaleStyleVisitor(SF, FF, null, SCALE).copyForMap(fts);
        assertEquals(List.of("mapOnly", "mixed"), ruleNames(copy));
        assertEquals(1, copy.rules().get(1).symbolizers().size());

        fts.getOptions().put(VENDOR_OPTION_INCLUSION, "legendOnly");
        assertNull(new MapScaleStyleVisitor(SF, FF, null, SCALE).copyForMap(fts));
    }

    /** The rendering transformation is copied as it is, without simplification. */
    @Test
    public void testTransformation() {
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("Feature", rule("rule", 0, Double.POSITIVE_INFINITY));
        Expression transformation = FF.function("strConcat", FF.literal("a"), FF.literal("b"));
        fts.setTransformation(transformation);

        FeatureTypeStyle copy = new MapScaleStyleVisitor(SF, FF, null, SCALE).copyForMap(fts);
        assertEquals(transformation, copy.getTransformation());
        assertNotSame(transformation, copy.getTransformation());
    }

    /** Copying text symbolizers and marks makes no factory lookup, which copies the default hints. */
    @Test
    public void testNoFactoryLookup() {
        Rule rule = sb.createRule(sb.createTextSymbolizer());
        rule.symbolizers().add(sb.createPointSymbolizer(sb.createGraphic(null, sb.createMark("circle"), null)));
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("Feature", rule);

        try (MockedStatic<CommonFactoryFinder> finder = mockStatic(CommonFactoryFinder.class, CALLS_REAL_METHODS)) {
            new MapScaleStyleVisitor(SF, FF, null, SCALE).copyForMap(fts);
            // any() also matches the null hints most callers pass
            finder.verify(() -> CommonFactoryFinder.getFilterFactory((Hints) any()), never());
            finder.verify(() -> CommonFactoryFinder.getFilterFactory(), never());
            finder.verify(() -> CommonFactoryFinder.getStyleFactory((Hints) any()), never());
            finder.verify(() -> CommonFactoryFinder.getStyleFactory(), never());
        }
    }

    private Rule rule(String name, double minScale, double maxScale) {
        Rule rule = sb.createRule(sb.createLineSymbolizer());
        rule.setName(name);
        rule.setMinScaleDenominator(minScale);
        rule.setMaxScaleDenominator(maxScale);
        return rule;
    }

    private static List<String> ruleNames(FeatureTypeStyle fts) {
        return fts.rules().stream().map(Rule::getName).toList();
    }
}
