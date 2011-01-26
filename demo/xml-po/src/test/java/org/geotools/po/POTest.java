/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.po;

import java.io.InputStream;

import junit.framework.TestCase;

import org.geotools.po.bindings.POConfiguration;
import org.geotools.xml.Parser;

/**
 * Test case which tests the parsing of a sample instance document from 
 * the purchase order schema, po.xsd.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class POTest extends TestCase {

	public void test() throws Exception {
		
		//load the xml file
		InputStream input = getClass().getResourceAsStream( "po.xml" );
		
		//create the configuration
		POConfiguration configuration = new POConfiguration();
		
		//parse the instance document
		Parser parser = new Parser( configuration );
		PurchaseOrderType po = (PurchaseOrderType) parser.parse( input );
		
		assertNotNull( po );
	}
}
