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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

import junit.framework.TestCase;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.xml.Parser;
import org.opengis.style.ExternalGraphic;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 *
 * @source $URL$
 */
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
    
    public void testParseGraphicFill() throws Exception {
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse("../graphicFill.xml");
        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        PolygonSymbolizer ps = (PolygonSymbolizer) layer.getStyles()[0].featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        Graphic graphicFill = ps.getFill().getGraphicFill();
        assertNotNull(graphicFill);
        ExternalGraphic eg = (ExternalGraphic) graphicFill.graphicalSymbols().get(0);
        assertEquals(new URI("http://maps.google.com/mapfiles/kml/pal2/icon4.png"), eg.getOnlineResource().getLinkage());
        
    }
    
    Object parse(String filename) throws Exception {
        SLDConfiguration sld = new SLDConfiguration();
        InputStream location = getClass().getResourceAsStream(filename);
        return new Parser(sld).parse(location);
    }
    
    public void testParseSldWithExternalEntities() throws Exception {
        // this SLD file references as external entity a file on the local filesystem
        String file = "../example-textsymbolizer-externalentities.xml";
        
        Parser parser = new Parser(new SLDConfiguration());
        
        try {
            InputStream location = getClass().getResourceAsStream(file);
            parser.parse(location);
            fail("parsing should fail with a FileNotFoundException because the parser try to access a file that doesn't exist");
        } catch (FileNotFoundException e) {
        }
        
        // set an entity resolver to prevent access to the local file system 
        parser.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource();
            }      
        });
        
        try {
            InputStream location = getClass().getResourceAsStream(file);
            parser.parse(location);
            fail("parsing should fail with a MalformedURLException because the EntityResolver blocked entity resolution");
        } catch (MalformedURLException e) {
        }        
    }
}
