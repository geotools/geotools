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

import java.io.Reader;
import java.io.StringReader;

import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.opengis.filter.Filter;
import org.xml.sax.InputSource;

public class XXXExample {

	static String xml = "<Filter xmlns:gml=\"http://www.opengis.net/gml\">"
		+"  <Overlaps>"
		+"    <PropertyName>testGeometry</PropertyName>"
		+"<gml:Polygon srsName=\"http://www.opengis.net/gml/srs/EPSG#4326\">"
		+"<gml:outerBoundaryIs>"
		+"<gml:LinearRing>"
		+"<gml:coordinates>0,0 0,10 10,10 10,0 0,0</gml:coordinates>"
		+"</gml:LinearRing>"
		+"</gml:outerBoundaryIs>"
		+"</gml:Polygon>"
		+"  </Overlaps>"
		+"</Filter>";
	
	public static void main(String args[]) throws Exception {
		//the xml instance document above
		Reader reader = new StringReader( xml );
		InputSource input = new InputSource( reader );
        
		//create the parser with the filter 1.0 configuration
		Configuration configuration = new org.geotools.filter.v1_0.OGCConfiguration();
		Parser parser = new Parser( configuration );
		
		//parse
		Filter filter = (Filter) parser.parse( input );
		System.out.println( "got:"+filter );
	}
}
