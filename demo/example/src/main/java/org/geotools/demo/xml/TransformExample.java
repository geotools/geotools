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

import org.geotools.filter.FilterTransformer;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.filter.Filter;

public class TransformExample {

	public static void main( String args[]) throws Exception {
		Filter filter = CQL.toFilter("name = 'fred'");
		System.out.println( filter );
		
		FilterTransformer transform = new FilterTransformer();
		transform.setIndentation(2);
		
		String xml = transform.transform( filter );
		System.out.println( xml );
	}
}

