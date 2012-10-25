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

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.StyleFactory2;
import org.geotools.styling.UomOgcMapping;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;


public class UnitRescaleTest {

    private StyleFactory2 sf;
    private FilterFactory2 ff;
    
    @Before
    public void setUp() throws Exception {
        sf = (StyleFactory2) CommonFactoryFinder.getStyleFactory(null);
        ff = CommonFactoryFinder.getFilterFactory2(null);
    }
    
    @Test
    public void testUOMEncodingLineSymbolizer() throws Exception {
        LineSymbolizer lineSymbolizer = sf.createLineSymbolizer();
        lineSymbolizer.setUnitOfMeasure(UomOgcMapping.METRE.getUnit());
        lineSymbolizer.setStroke(sf.createStroke(ff.literal("#0000FF"), ff.literal("3")));
    
        Rule rule = sf.createRule();
        rule.symbolizers().add(lineSymbolizer);
        
        Rule[] rules = new Rule[] { rule };
        FeatureTypeStyle featureTypeStyle = sf.createFeatureTypeStyle(rules);
    
        List<FeatureTypeStyle> featureTypeStyles = new ArrayList<FeatureTypeStyle>();
        featureTypeStyles.add(featureTypeStyle);
        
        double dpi90 = 25.4 / 0.28;
        double widthAtDpi90 = 10714.286;
        Assert.assertEquals(widthAtDpi90, getStrokeWidth(featureTypeStyles, rules, dpi90), 0.1);
        Assert.assertEquals(widthAtDpi90 * 2, getStrokeWidth(featureTypeStyles, rules, dpi90 * 2), 0.5);
        Assert.assertEquals(widthAtDpi90 * 3, getStrokeWidth(featureTypeStyles, rules, dpi90 * 3), 0.5);
        Assert.assertEquals(widthAtDpi90 * 4, getStrokeWidth(featureTypeStyles, rules, dpi90 * 4), 0.5);        
        Assert.assertEquals(widthAtDpi90 * 5, getStrokeWidth(featureTypeStyles, rules, dpi90 * 5), 0.5);                
    }
    
    private double getStrokeWidth(List<FeatureTypeStyle> featureTypeStyles, Rule[] rules, double dpi) {
        ArrayList<LiteFeatureTypeStyle> lfts = new ArrayList<LiteFeatureTypeStyle>();
        for (FeatureTypeStyle fts : featureTypeStyles) {
            List<Rule> ruleList = new ArrayList<Rule>(Arrays.asList(rules));
            List<Rule> elseRuleList = new ArrayList<Rule>();
            LiteFeatureTypeStyle s = new LiteFeatureTypeStyle(null, ruleList, elseRuleList);
            lfts.add(s);
        }
        
        Map hints = new HashMap();
        hints.put("dpi", new Double(dpi));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.scaleDenominator = 1;
        renderer.setRendererHints(hints);
        renderer.applyUnitRescale(lfts);
        
        for (LiteFeatureTypeStyle s : lfts) {
            Rule r = s.ruleList[0];
            LineSymbolizer rescaledLineSymbolizer = (LineSymbolizer) r.getSymbolizers()[0];
            return rescaledLineSymbolizer.getStroke().getWidth().evaluate(null, Double.class);
        }   
        
        // this should not happen
        return -1;
    }
}