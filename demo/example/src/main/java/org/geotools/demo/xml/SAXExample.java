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

import org.geotools.filter.FilterFilter;
import org.geotools.filter.FilterHandler;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.opengis.filter.Filter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXExample {
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
		StringReader reader = new StringReader( xml );
        InputSource input = new InputSource( reader );
        
        Filter filter = parse( input );
        System.out.println( "got:"+filter );
	}
	
	public static Filter parse(InputSource input) throws IOException, SAXException {
	    SimpleFilterHandler simpleFilterHandler = new SimpleFilterHandler();
	    FilterFilter filterFilter = new FilterFilter(simpleFilterHandler, null);
	    GMLFilterGeometry filterGeometry = new GMLFilterGeometry(filterFilter);
	    GMLFilterDocument filterDocument = new GMLFilterDocument(filterGeometry);

	    // parse xml
	    XMLReader reader = XMLReaderFactory.createXMLReader();
	    reader.setContentHandler(filterDocument);
	    reader.parse(input);
	    
	    return simpleFilterHandler.getFilter();
	}
	/**
	 * This class is called when the SAX parser has finished
	 * parsing a Filter.
	 */
	static class SimpleFilterHandler extends DefaultHandler implements FilterHandler {
	  private Filter filter;
	  public void filter(Filter filter) {
	    this.filter = filter;
	  }
	  public Filter getFilter() {
	    return filter;
	  }
	}	
}
