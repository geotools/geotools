/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.style.StyleFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.UomOgcMapping;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnitRescaleTest {

    private StyleFactory sf;
    private FilterFactory ff;

    @Before
    public void setUp() throws Exception {
        sf = CommonFactoryFinder.getStyleFactory(null);
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    @Test
    public void testUOMEncodingLineSymbolizer() throws Exception {
        LineSymbolizerImpl lineSymbolizer = (LineSymbolizerImpl) sf.createLineSymbolizer();
        lineSymbolizer.setUnitOfMeasure(UomOgcMapping.METRE.getUnit());
        lineSymbolizer.setStroke(sf.createStroke(ff.literal("#0000FF"), ff.literal("3")));

        RuleImpl rule = (RuleImpl) sf.createRule();
        rule.symbolizers().add(lineSymbolizer);

        RuleImpl[] rules = {rule};
        FeatureTypeStyleImpl featureTypeStyle = (FeatureTypeStyleImpl) sf.createFeatureTypeStyle(rules);

        List<FeatureTypeStyleImpl> featureTypeStyles = new ArrayList<>();
        featureTypeStyles.add(featureTypeStyle);

        double dpi90 = 25.4 / 0.28;
        double widthAtDpi90 = 10714.286;
        Assert.assertEquals(widthAtDpi90, getStrokeWidth(featureTypeStyles, rules, dpi90), 0.1);
        Assert.assertEquals(
                widthAtDpi90 * 2, getStrokeWidth(featureTypeStyles, rules, dpi90 * 2), 0.5);
        Assert.assertEquals(
                widthAtDpi90 * 3, getStrokeWidth(featureTypeStyles, rules, dpi90 * 3), 0.5);
        Assert.assertEquals(
                widthAtDpi90 * 4, getStrokeWidth(featureTypeStyles, rules, dpi90 * 4), 0.5);
        Assert.assertEquals(
                widthAtDpi90 * 5, getStrokeWidth(featureTypeStyles, rules, dpi90 * 5), 0.5);
    }

    private double getStrokeWidth(
            List<FeatureTypeStyleImpl> featureTypeStyles, RuleImpl[] rules, double dpi) {
        ArrayList<LiteFeatureTypeStyle> lfts = new ArrayList<>();
        for (FeatureTypeStyleImpl fts : featureTypeStyles) {
            List<RuleImpl> ruleList = new ArrayList<>(Arrays.asList(rules));
            List<RuleImpl> elseRuleList = new ArrayList<>();
            LiteFeatureTypeStyle s =
                    new LiteFeatureTypeStyle(
                            null, null, ruleList, elseRuleList, fts.getTransformation());
            lfts.add(s);
        }

        Map<Object, Object> hints = new HashMap<>();
        hints.put("dpi", Double.valueOf(dpi));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.scaleDenominator = 1;
        renderer.setRendererHints(hints);
        renderer.applyUnitRescale(lfts);

        for (LiteFeatureTypeStyle s : lfts) {
            RuleImpl r = s.ruleList[0];
            LineSymbolizerImpl rescaledLineSymbolizer = (LineSymbolizerImpl) r.symbolizers().get(0);
            return rescaledLineSymbolizer.getStroke().getWidth().evaluate(null, Double.class);
        }

        // this should not happen
        return -1;
    }
}
