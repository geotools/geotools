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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCXMLTestSetup;

public class PostgisXMLTestSetup extends JDBCXMLTestSetup {
    public PostgisXMLTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    @Override
    protected void createXMLTable() throws Exception {
        run( "CREATE TABLE xml (id Integer PRIMARY KEY, xml XML)");
        
        run( "INSERT INTO xml VALUES (" +
                "1, XMLPARSE (CONTENT E'" + readXML(FRAGMENT).replace("\n", "\\n")+"') )");
        
        run( "INSERT INTO xml VALUES (" +
                "2, XMLPARSE (DOCUMENT E'" + readXML(DOCUMENT).replace("\n", "\\n")+"') )");
        
        run( "INSERT INTO xml VALUES (" +
                "3, XMLPARSE (DOCUMENT E'" + readXML(LONGDOC).replace("\n", "\\n")+"') )");
    }
    
    @Override
    protected void dropXMLTable() throws Exception {
        runSafe("DROP TABLE xml");
    }
}
