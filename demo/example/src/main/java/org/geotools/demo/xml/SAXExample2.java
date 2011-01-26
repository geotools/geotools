/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.xml;

import java.io.IOException;
import java.io.StringReader;

import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.geotools.gml.GMLHandlerJTS;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.vividsolutions.jts.geom.Geometry;

public class SAXExample2 {
	static String xml = "<Example xmlns:gml=\"http://www.opengis.net/gml\">"
+"<gml:Polygon srsName=\"http://www.opengis.net/gml/srs/EPSG#4326\">"
+"<gml:outerBoundaryIs>"
+"<gml:LinearRing>"
+"<gml:coordinates>0,0 0,10 10,10 10,0 0,0</gml:coordinates>"
+"</gml:LinearRing>"
+"</gml:outerBoundaryIs>"
+"</gml:Polygon>"
+"</Example>";
	
	public static void main(String args[]) throws Exception {
		StringReader reader = new StringReader( xml );
        InputSource input = new InputSource( reader );
        
        Callback result = parse( input );
        System.out.println( "got:"+result.getGeometry() );
	}
	
	public static Callback parse(InputSource input) throws IOException, SAXException {

	    // parse xml
	    XMLReader reader = XMLReaderFactory.createXMLReader();
	    
	    Callback callback = new Callback();
	    GMLFilterGeometry geometryCallback = new GMLFilterGeometry( callback );	    
	    GMLFilterDocument gmlCallback = new GMLFilterDocument( geometryCallback );	    
		reader.setContentHandler( gmlCallback );
	    reader.parse(input);
	    
	    return callback;
	}
	/**
	 * This class is called when the SAX parser has finished
	 * parsing a Filter.
	 */
	static class Callback extends DefaultHandler implements GMLHandlerJTS {
		Geometry geometry= null;

		public void geometry(Geometry geometry) {
			this.geometry = geometry; 
		}
		public Geometry getGeometry() {
			return geometry;
		}	
	}	
}
