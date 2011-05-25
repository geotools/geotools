/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.geotools.test.TestData;
import org.geotools.xml.schema.Schema;

/**
 * @author dzwiers
 *
 *
 * @source $URL$
 */
public class SchemaMergeTest extends TestCase {

    protected SAXParser parser;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        parser = spf.newSAXParser();
    }
    
	public void testMergeSchema(){
		// will load a doc that includes two schema docs which duplicate definitions
		

        File f;
        try {
            f = TestData.file(this,"merge.xsd");
	        URI u = f.toURI();
	        XSISAXHandler contentHandler = new XSISAXHandler(u);
	        XSISAXHandler.setLogLevel(Level.WARNING);

	        try {
	            parser.parse(f, contentHandler);
	        } catch (Exception e) {
	            e.printStackTrace();
	            fail(e.toString());
	        }

	        try{
	            assertNotNull("Schema missing", contentHandler.getSchema());
	            System.out.println(contentHandler.getSchema());
	            

		        Schema schema = contentHandler.getSchema();
		        
		        assertTrue("Should only have 2 elements, had "+schema.getElements().length,schema.getElements().length == 2);
		        assertTrue("Should only have 1 complexType, had "+schema.getComplexTypes().length,schema.getComplexTypes().length == 1);
		        
	        } catch (Exception e) {
	            e.printStackTrace();
	            fail(e.toString());
	        }
        } catch (IOException e1) {
            e1.printStackTrace();
            fail(e1.toString());
        }
	}
}
