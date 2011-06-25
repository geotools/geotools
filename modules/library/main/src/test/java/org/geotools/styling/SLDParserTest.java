/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.style.GraphicalSymbol;

public class SLDParserTest {

    public static String SLD = 
        "<StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" version=\"1.0.0\">"+
        " <NamedLayer>"+
        "  <Name>layer</Name>"+
        "  <UserStyle>"+
        "   <Name>style</Name>"+
        "   <FeatureTypeStyle>"+
        "    <Rule>"+
        "     <PolygonSymbolizer>"+
        "      <Fill>"+
        "       <CssParameter name=\"fill\">#FF0000</CssParameter>"+
        "      </Fill>"+
        "     </PolygonSymbolizer>"+
        "    </Rule>"+
        "   </FeatureTypeStyle>"+
        "  </UserStyle>"+
        " </NamedLayer>"+
        "</StyledLayerDescriptor>";
    
    static String SLD_DEFAULT_POINT = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
        "<StyledLayerDescriptor version=\"1.0.0\" \n" + 
        "        xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" \n" + 
        "        xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" \n" + 
        "        xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n" + 
        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
        "    <UserStyle>\n" + 
        "        <Name>Default Styler</Name>\n" + 
        "        <Title>Default Styler</Title>\n" + 
        "        <Abstract></Abstract>\n" + 
        "        <FeatureTypeStyle>\n" + 
        "            <FeatureTypeName>Feature</FeatureTypeName>\n" + 
        "            <Rule>\n" + 
        "                <PointSymbolizer>\n" + 
        "                    <Graphic>\n" + 
        "                    </Graphic>\n" + 
        "                </PointSymbolizer>\n" + 
        "            </Rule>\n" + 
        "        </FeatureTypeStyle>\n" + 
        "    </UserStyle>\n" + 
        "</StyledLayerDescriptor>";
    
    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    
    @Test
    public void testBasic() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input(SLD));
        Style[] styles = parser.readXML();
        assertStyles(styles);
    }

    @Test
    public void testMultipleParse() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input(SLD));
        Style[] styles = parser.readXML();
        assertStyles(styles);
        
        styles = parser.readDOM();
        assertStyles(styles);
        
        try {
            parser.readXML();
            fail("Parsing again Should have thrown exception");
        }
        catch(Exception e) {}
    }
    
    @Test
    public void testDefaultPoint() throws Exception {
        // fixes for GEOS-3111 broke default point symbsolizer handling
        SLDParser parser = new SLDParser(styleFactory, input(SLD_DEFAULT_POINT));
        Style[] styles = parser.readXML();
        
        assertEquals(1, styles.length);
        List<FeatureTypeStyle> fts = styles[0].featureTypeStyles();
        assertEquals(1, fts.size());
        List<Rule> rules = fts.get(0).rules();
        assertEquals(1, rules.size());
        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        assertEquals(1, symbolizers.size());
        PointSymbolizer ps = (PointSymbolizer) symbolizers.get(0);
        // here we would have had two instead of one
        List<GraphicalSymbol> graphicalSymbols = ps.getGraphic().graphicalSymbols();
        assertEquals(1, graphicalSymbols.size());
        Mark mark = (Mark) graphicalSymbols.get(0);
        assertEquals(mark, CommonFactoryFinder.getStyleFactory(null).createMark());
    }
    
    void assertStyles(Style[] styles) {
        assertEquals(1, styles.length);
        assertEquals("style", styles[0].getName());
    }
    
    InputStream input(String sld) {
        return new ByteArrayInputStream(sld.getBytes());
    }
}
