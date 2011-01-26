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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.geotools.factory.CommonFactoryFinder;

import junit.framework.TestCase;

public class SLDParserTest extends TestCase {

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
    
    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    
    public void testBasic() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input());
        Style[] styles = parser.readXML();
        assertStyles(styles);
    }
    
    public void testMultipleParse() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input());
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
    
    void assertStyles(Style[] styles) {
        assertEquals(1, styles.length);
        assertEquals("style", styles[0].getName());
    }
    
    InputStream input() {
        return new ByteArrayInputStream(SLD.getBytes());
    }
}
