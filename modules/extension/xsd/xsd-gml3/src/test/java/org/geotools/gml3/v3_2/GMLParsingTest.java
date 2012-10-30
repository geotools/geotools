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
package org.geotools.gml3.v3_2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.eclipse.xsd.XSDSchema;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.CRS;
import org.geotools.xml.Parser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class GMLParsingTest extends TestCase {

    public void testGML() throws Exception {
        XSDSchema gml = GML.getInstance().getSchema();
        assertFalse( gml.getTypeDefinitions().isEmpty() );
    }
    
    public void testParseFeatureCollection() throws Exception {
        File schema = File.createTempFile("test", "xsd");
        schema.deleteOnExit();
        FileUtils.copyURLToFile(getClass().getResource("test.xsd"), schema);
        
        Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
            getClass().getResourceAsStream( "test.xml" )   
        );
        URL schemaURL = DataUtilities.fileToURL( schema.getAbsoluteFile() );        
        dom.getDocumentElement().setAttribute( "xsi:schemaLocation", "http://www.geotools.org/test " + schemaURL.getFile() );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform( 
            new DOMSource( dom ), new StreamResult( out ) );
        
        GMLConfiguration config = new GMLConfiguration();
        Parser p = new Parser( config );
        Object o = p.parse( new ByteArrayInputStream( out.toByteArray() ) );
        assertTrue( o instanceof FeatureCollection );
        
        FeatureCollection features = (FeatureCollection) o;
        assertEquals( 3, features.size() );
        
        FeatureIterator fi = features.features();
        try {
            for ( int i = 0; i < 3; i++ ) {
                assertTrue( fi.hasNext() );
                
                SimpleFeature f = (SimpleFeature) fi.next();
                assertTrue( f.getDefaultGeometry() instanceof Point );
            
                Point point = (Point) f.getDefaultGeometry();
                assertEquals( i/1d, point.getX(), 0.1 );
                assertEquals( i/1d, point.getX(), 0.1 );
                
                assertEquals( i, f.getAttribute( "count" ) );
            }
        }
        finally {
            fi.close();
        }
    }

    /**
     * Parse an srsName from a gml:Point.
     * 
     * @param srsName the srsName attribute on the gml:Point
     * @return the parsed CoordinateReferenceSystem
     */
    private static CoordinateReferenceSystem parsePointSrsname(String srsName) {
        Parser parser = new Parser(new GMLConfiguration());
        String text = "<gml:Point " //
                + "xmlns:gml=\"http://www.opengis.net/gml/3.2\" " //
                + "srsName=\"" + srsName + "\">" //
                + "<gml:pos>1 2</gml:pos>" //
                + "</gml:Point>";
        try {
            Point point = (Point) parser.parse(new StringReader(text));
            return (CoordinateReferenceSystem) point.getUserData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test parsing of an srsName in EPSG code format.
     */
    public void testParseEpsgSrsname() throws Exception {
        assertEquals(CRS.decode("EPSG:4326"), parsePointSrsname("EPSG:4326"));
    }

    /**
     * Test parsing of an srsName in OGC HTTP URL format.
     */
    public void testParseOgcHttpUrlSrsname() throws Exception {
        assertEquals(CRS.decode("EPSG:4326"),
                parsePointSrsname("http://www.opengis.net/gml/srs/epsg.xml#4326"));
    }

    /**
     * Test parsing of an srsName in OGC URN Experimental format.
     */
    public void testParseOgcUrnExperimentalSrsname() throws Exception {
        assertEquals(CRS.decode("EPSG:4326"), parsePointSrsname("urn:x-ogc:def:crs:EPSG::4326"));
    }

    /**
     * Test parsing of an srsName in OGC URN format.
     */
    public void testParseOgcUrnSrsname() throws Exception {
        assertEquals(CRS.decode("EPSG:4326"), parsePointSrsname("urn:ogc:def:crs:EPSG::4326"));
    }

    /**
     * Test parsing of an srsName in OGC HTTP URI format.
     */
    public void testParseOgcHttpUriSrsname() throws Exception {
        assertEquals(CRS.decode("EPSG:4326"),
                parsePointSrsname("http://www.opengis.net/def/crs/EPSG/0/4326"));
    }

}
