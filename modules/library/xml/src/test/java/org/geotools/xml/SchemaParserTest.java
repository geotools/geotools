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

import org.geotools.TestData;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 * @
 *
 * @author dzwiers www.refractions.net
 * @source $URL$
 */
public class SchemaParserTest extends TestCase {
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

    public void testMail(){
        runit("xml/mails.xsd");
    }

    public void testWFS(){
        runit("xml/wfs/WFS-basic.xsd");
    }

    public void testGMLFeature(){
        runit("xml/gml/feature.xsd");
    }

    public void testGMLGeometry(){
        runit("xml/gml/geometry.xsd");
    }

    public void testGMLXLinks(){
        runit("xml/gml/xlinks.xsd");
    }

    private void runit(String path){
        File f;
        try {
            f = TestData.copy(this,path);
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
                //System.out.println(contentHandler.getSchema());
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
