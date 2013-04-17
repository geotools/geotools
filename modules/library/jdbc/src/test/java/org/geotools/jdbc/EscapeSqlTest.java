/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2013, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Collections;
import junit.framework.TestCase;
import org.geotools.factory.Hints;

/**
 * Check that SQL escape code generates the correct strings
 */
public class EscapeSqlTest extends TestCase  {
       
    public void testSqlEscaping() throws Exception {
        VirtualTable vt = new VirtualTable("test", "%param1%");
        vt.setEscapeSql(true);
        vt.addParameter(new VirtualTableParameter("param1", "default_value", new RegexpValidator(".*")));
        String singleQuote = vt.expandParameters(new Hints(
                Hints.VIRTUAL_TABLE_PARAMETERS, Collections.singletonMap("param1", "o'shea")));
        assertEquals("single quotes should be doubled", "o''shea", singleQuote);
        
        String doubleQuote = vt.expandParameters(new Hints(
                Hints.VIRTUAL_TABLE_PARAMETERS, Collections.singletonMap("param1", "If you hear a voice within you say \"you cannot paint,\" then by all means paint, and that voice will be silenced.")));
        assertEquals("double quotes should be doubled", "If you hear a voice within you say \"\"you cannot paint,\"\" then by all means paint, and that voice will be silenced.", 
                doubleQuote);
        
        String backslash = vt.expandParameters(new Hints(
                Hints.VIRTUAL_TABLE_PARAMETERS, Collections.singletonMap("param1", "abc\\n")));
        assertEquals("backslashes should be removed", "abcn", backslash);
    }
}
