/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc.fidmapper;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Types;

import junit.framework.TestCase;

public class AutoIncrementFIDMapperTest extends TestCase {

    /**
     * A weak test to make sure that AutoIncrementFIDMapper supports multiple
     * (common) data types for its primary key column.
     */
    public void testGetPKAttributes() throws IOException {
        AutoIncrementFIDMapper mapper = new AutoIncrementFIDMapper("column_1", Types.INTEGER);
        Object[] attr = mapper.getPKAttributes("12345");
        assertEquals(Integer.class, attr[0].getClass());
                
        mapper = new AutoIncrementFIDMapper("column_2", Types.BIGINT);
        attr = mapper.getPKAttributes("1234567");
        assertEquals(BigInteger.class, attr[0].getClass());
        
        mapper = new AutoIncrementFIDMapper("column_3", Types.SMALLINT);
        attr = mapper.getPKAttributes("123");
        assertEquals(Integer.class, attr[0].getClass());
        
        mapper = new AutoIncrementFIDMapper("column_4", Types.TINYINT);
        attr = mapper.getPKAttributes("1");
        assertEquals(Integer.class, attr[0].getClass());
        
        mapper = new AutoIncrementFIDMapper("column_5", Types.VARCHAR);
        attr = mapper.getPKAttributes("UNIQUE_FID");
        assertEquals(String.class, attr[0].getClass());
    }
    
}
