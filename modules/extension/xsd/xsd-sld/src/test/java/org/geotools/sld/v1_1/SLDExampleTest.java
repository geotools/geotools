/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.sld.v1_1;

import junit.framework.TestCase;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.xml.Parser;

public class SLDExampleTest extends TestCase {

    public void testParseSLD() throws Exception {
        /*
        <StyledLayerDescriptor version="1.1.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld" 
        xmlns:ogc="http://www.opengis.net/ogc" xmlns:se="http://www.opengis.net/se" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <NamedLayer>
                        <se:Name>OCEANSEA_1M:Foundation</se:Name>
                        <UserStyle>
                                <se:Name>GEOSYM</se:Name>
                                <IsDefault>1</IsDefault>
                                <se:FeatureTypeStyle>
                                        <se:FeatureTypeName>Foundation</se:FeatureTypeName>
                                        <se:Rule>
                                                <se:Name>main</se:Name>
                                                <se:PolygonSymbolizer uom="http://www.opengis.net/sld/units/pixel">
                                                        <se:Name>MySymbol</se:Name>
                                                        <se:Description>
                                                                <se:Title>Example Symbol</se:Title>
                                                                <se:Abstract>This is just a simple example.</se:Abstract>
                                                        </se:Description>
                                                        <se:Geometry>
                                                                <ogc:PropertyName>GEOMETRY</ogc:PropertyName>
                                                        </se:Geometry>
                                                        <se:Fill>
                                                                <se:SvgParameter name="fill">#96C3F5</se:SvgParameter>
                                                        </se:Fill>
                                                </se:PolygonSymbolizer>
                                        </se:Rule>
                                </se:FeatureTypeStyle>
                        </UserStyle>
                </NamedLayer>
        </StyledLayerDescriptor>*/

        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse("example-sld.xml");
        assertEquals(1, sld.getStyledLayers().length);
        
        NamedLayer l = (NamedLayer) sld.getStyledLayers()[0];
        assertEquals("OCEANSEA_1M:Foundation", l.getName());
        
        assertEquals(1, l.getStyles().length);
        Style s = l.getStyles()[0];
        assertEquals("GEOSYM", s.getName());
        assertTrue(s.isDefault());
        
        assertEquals(1, s.getFeatureTypeStyles().length);
        FeatureTypeStyle fts = s.getFeatureTypeStyles()[0];
        
        assertEquals("Foundation", fts.getFeatureTypeName());
        assertEquals(1, fts.rules().size());
        
        Rule r = fts.rules().get(0);
        assertEquals("main", r.getName());
        assertEquals(1, r.symbolizers().size());
        
        PolygonSymbolizer sym = (PolygonSymbolizer) r.symbolizers().get(0);
    }
    
    Object parse(String filename) throws Exception {
        SLDConfiguration sld = new SLDConfiguration();
        return new Parser(sld).parse(getClass().getResourceAsStream(filename));
    }
}
