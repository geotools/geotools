/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.kml;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.opengis.feature.simple.SimpleFeature;
import org.w3c.dom.Document;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.kml.bindings.DocumentTypeBinding;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Symbolizer;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.geotools.xml.StreamingParser;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;


public class KMLParsingTest extends TestCase {
    
   public void testParse() throws Exception {
        Parser parser = new Parser(new KMLConfiguration());
        SimpleFeature f = (SimpleFeature) parser.parse(getClass().getResourceAsStream("states.kml"));
        assertNotNull(f);

        assertEquals("topp:states", f.getAttribute("name"));

        Collection placemarks = (Collection) f.getAttribute("Feature");
        assertEquals(49, placemarks.size());
    }

    public void testStream() throws Exception {
        StreamingParser parser = new StreamingParser(new KMLConfiguration(),
                getClass().getResourceAsStream("states.kml"), KML.Placemark);
        int count = 0;
        SimpleFeature f = null;

        while ((f = (SimpleFeature) parser.parse()) != null) {
            FeatureTypeStyle style = (FeatureTypeStyle) f.getAttribute("Style");
            assertNotNull(style);

            Symbolizer[] syms = style.getRules()[0].getSymbolizers();
            assertEquals(3, syms.length);

            count++;
        }

        assertEquals(49, count);
    }
    
    public void testEncodeFeatureCollection() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "foo" );
        tb.add( "name", String.class );
        tb.add( "description", String.class );
        tb.add( "geometry", Geometry.class );
        
        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder sb = new SimpleFeatureBuilder( tb.buildFeatureType() );
        FeatureCollection features = new DefaultFeatureCollection(null,null);
        
        sb.add( "one" );
        sb.add( "the first feature");
        sb.add( gf.createPoint( new Coordinate(1, 1) ) ) ;
        features.add( sb.buildFeature("1"));
        
        sb.add( "two" );
        sb.add( "the second feature");
        sb.add( gf.createPoint( new Coordinate(2, 2) ) ) ;
        features.add( sb.buildFeature("2"));
        
        Encoder encoder = new Encoder(new KMLConfiguration());
        encoder.setIndenting(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode(features, KML.kml, out );
        System.out.println( new String( out.toByteArray() ));
        
        DocumentBuilder db = 
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document d = db.parse( new ByteArrayInputStream( out.toByteArray() ) );
        assertEquals( "kml:kml", d.getDocumentElement().getNodeName() );
        assertEquals( 2, d.getElementsByTagName( "kml:Placemark").getLength() );
        
    }
    
    public void XtestEncodeFeature() throws Exception { 
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "foo" );
        tb.add( "name", String.class );
        tb.add( "description", String.class );
        tb.add( "geometry", Geometry.class );
        
        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder sb = new SimpleFeatureBuilder( tb.buildFeatureType() );
        ArrayList features = new ArrayList();
        
        sb.add( "one" );
        sb.add( "the first feature");
        sb.add( gf.createPoint( new Coordinate(1, 1) ) ) ;
        features.add( sb.buildFeature("1"));
        
        sb.add( "two" );
        sb.add( "the second feature");
        sb.add( gf.createPoint( new Coordinate(2, 2) ) ) ;
        features.add( sb.buildFeature("2"));
        
        sb = new SimpleFeatureBuilder(DocumentTypeBinding.featureType);
        sb.set( "Feature", features );
        SimpleFeature f = sb.buildFeature("kml");
        
        Encoder encoder = new Encoder(new KMLConfiguration());
        encoder.setIndenting(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode(f, KML.kml, out );
        
        DocumentBuilder db = 
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document d = db.parse( new ByteArrayInputStream( out.toByteArray() ) );
        assertEquals( "kml:kml", d.getDocumentElement().getNodeName() );
        assertEquals( 2, d.getElementsByTagName( "kml:Placemark").getLength() );
    }
}
