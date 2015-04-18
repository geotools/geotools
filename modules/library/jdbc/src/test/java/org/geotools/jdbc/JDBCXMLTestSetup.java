/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public abstract class JDBCXMLTestSetup extends JDBCDelegatingTestSetup {
    
    protected static final String FRAGMENT = "test-data/fragment.xml";
    protected static final String DOCUMENT = "test-data/document.xml";
    protected static final String LONGDOC = "test-data/longdoc.xml";
    
    protected JDBCXMLTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    @Override
    protected void setUpData() throws Exception {
        try {
            dropXMLTable();
        }
        catch( Exception e ) {}
        
        createXMLTable();
    }
    
    /**
     * Creates a table named 'xml' which has an id attribute and a XML attribute, 
     * with the following schema:
     * <p>
     * xml(id:Integer,xml:XML) 
     * </p>
     * <p>
     * The table has the following data, with the xml content coming from 
     * resources/org.geotools.data.jdbc:
     *
     *  1 | {fragment.xml}
     *  2 | {document.xml}
     *  3 | {longdoc.xml}
     * </p>
     *  </p>
     */
    protected abstract void createXMLTable() throws Exception;
    
    /**
     * Drops the 'xml' table.
     */
    protected abstract void dropXMLTable() throws Exception; 
    
    protected final String readXML(String resource) throws IOException {
        InputStream stream = JDBCXMLTestSetup.class.getResourceAsStream(resource);
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringWriter s = new StringWriter();
        BufferedWriter w = new BufferedWriter(s);
        
        String line = r.readLine();
        while (line != null) {
            w.write(line+"\n");
            line = r.readLine();
        }
        r.close();
        w.close();
        
        return s.toString();
    }
}