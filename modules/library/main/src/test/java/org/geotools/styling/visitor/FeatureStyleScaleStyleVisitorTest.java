/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.visitor;

import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.styling.StyleBuilder;
import org.junit.Assert;
import org.junit.Test;

public class FeatureStyleScaleStyleVisitorTest {

    @Test
    public void testMaxScaleDenominator() {
        StyleBuilder sb = new StyleBuilder();
        Style style = sb.createStyle();
        Rule rule = sb.createRule();
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("featureType", rule);
        fts.getOptions().put(FeatureTypeStyle.MAX_SCALE_DENOMINATOR, "1000");
        fts.getOptions().put(FeatureTypeStyle.MIN_SCALE_DENOMINATOR, "10");
        style.featureTypeStyles().add(fts);
        FeatureStyleScaleStyleVisitor visitor = new FeatureStyleScaleStyleVisitor();
        visitor.visit(style);
        Style copiedStyle = (Style) visitor.getCopy();
        Rule copiedRule = copiedStyle.featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(1000d, copiedRule.getMaxScaleDenominator(), 1e-10);
        Assert.assertEquals(10d, copiedRule.getMinScaleDenominator(), 1e-10);
    }

    @Test
    public void testNoScaleDenominator() {
        StyleBuilder sb = new StyleBuilder();
        Style style = sb.createStyle();
        Rule rule = sb.createRule();
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("featureType", rule);
        style.featureTypeStyles().add(fts);
        FeatureStyleScaleStyleVisitor visitor = new FeatureStyleScaleStyleVisitor();
        visitor.visit(style);
        Style copiedStyle = (Style) visitor.getCopy();
        Rule copiedRule = copiedStyle.featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, copiedRule.getMaxScaleDenominator(), 1e-10);
        Assert.assertEquals(0d, copiedRule.getMinScaleDenominator(), 1e-10);
    }

    @Test
    public void testRuleScaleDenominator() {
        StyleBuilder sb = new StyleBuilder();
        Style style = sb.createStyle();
        Rule rule = sb.createRule();
        rule.setMinScaleDenominator(100.0d);
        rule.setMaxScaleDenominator(5000.0d);
        FeatureTypeStyle fts = sb.createFeatureTypeStyle("featureType", rule);
        fts.getOptions().put(FeatureTypeStyle.MAX_SCALE_DENOMINATOR, "1000");
        fts.getOptions().put(FeatureTypeStyle.MIN_SCALE_DENOMINATOR, "10");
        style.featureTypeStyles().add(fts);
        FeatureStyleScaleStyleVisitor visitor = new FeatureStyleScaleStyleVisitor();
        visitor.visit(style);
        Style copiedStyle = (Style) visitor.getCopy();
        Rule copiedRule = copiedStyle.featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(5000.0d, copiedRule.getMaxScaleDenominator(), 1e-10);
        Assert.assertEquals(100.0d, copiedRule.getMinScaleDenominator(), 1e-10);
    }
}
