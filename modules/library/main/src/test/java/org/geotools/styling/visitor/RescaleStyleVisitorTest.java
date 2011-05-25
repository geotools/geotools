/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Font;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.FilterFactory2;


/**
 * Unit test for RescaleStyleVisitor.
 *
 * @author Jody Garnett (Refractions Research Inc)
 *
 * @source $URL$
 */
public class RescaleStyleVisitorTest extends TestCase {
    StyleBuilder sb;
    StyleFactory sf;
    FilterFactory2 ff;
    
    RescaleStyleVisitor visitor;
    double scale;
    
    public RescaleStyleVisitorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    	sf = CommonFactoryFinder.getStyleFactory(null);
        ff = CommonFactoryFinder.getFilterFactory2(null);
        sb = new StyleBuilder(sf, ff);
        scale = 2.0;
        visitor = new RescaleStyleVisitor( scale );
    }
    
    public void testStyleDuplication() throws IllegalFilterException {
    	//create a style
    	Style oldStyle = sb.createStyle("FTSName", sf.createPolygonSymbolizer());
    	oldStyle.getFeatureTypeStyles()[0].setSemanticTypeIdentifiers(new String[] {"simple", "generic:geometry"});
    	//duplicate it
    	oldStyle.accept(visitor);
    	Style newStyle = (Style) visitor.getCopy();
    	
    	//compare it
    	assertNotNull(newStyle);
    }

    public void testStyle() throws Exception {
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.setFeatureTypeName("feature-type-1");

        FeatureTypeStyle fts2 = fts2();

        Style style = sf.getDefaultStyle();
        style.addFeatureTypeStyle(fts);
        style.addFeatureTypeStyle(fts2);

        style.accept( visitor );        
        Style copy = (Style) visitor.getCopy();
        
        Style notEq = sf.getDefaultStyle();

        fts2 = fts2();
        notEq.addFeatureTypeStyle(fts2);
    }

    private FeatureTypeStyle fts2() {
        FeatureTypeStyle fts2 = sf.createFeatureTypeStyle();
        Rule rule = sf.createRule();
        fts2.addRule(rule);
        fts2.setFeatureTypeName("feature-type-2");

        return fts2;
    }

    public void testRule() throws Exception {
        Symbolizer symb1 = sf.createLineSymbolizer(sf
                .getDefaultStroke(), "geometry");

        Symbolizer symb2 = sf.createPolygonSymbolizer(sf
                .getDefaultStroke(), sf.getDefaultFill(), "shape");

        RasterSymbolizer symb3 = sf.createRasterSymbolizer();

        Rule rule = sf.createRule();
        rule.setSymbolizers(new Symbolizer[] { symb1, symb2, symb3 });

        rule.accept(visitor);
        Rule clone = (Rule) visitor.getCopy();

        assertNotNull( clone );
    }
    
    public void testStroke() throws Exception {
        Stroke original = sb.createStroke(Color.RED, 2, new float[] {5, 10});
        original.accept(visitor);
        Stroke clone = (Stroke) visitor.getCopy();
        
        assertEquals(4.0d, clone.getWidth().evaluate(null));
        assertEquals(10.0f, clone.getDashArray()[0]);
        assertEquals(20.0f, clone.getDashArray()[1]);
    }
    
    public void testTextSymbolizer() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "label");
        ts.getOptions().put(TextSymbolizer.MAX_DISPLACEMENT_KEY, "10");
        
        ts.accept(visitor);
        TextSymbolizer clone = (TextSymbolizer) visitor.getCopy();
        assertEquals("20", clone.getOptions().get(TextSymbolizer.MAX_DISPLACEMENT_KEY));
    }
    
}
