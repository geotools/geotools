/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005. All rights reserved.
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
 *
 */
package org.geotools.data.db2;


/**
 * Exercise DB2DataStore.
 *
 * @author David Adler - IBM Corporation
 * @source $URL$
 */
public class DB2DataStoreOnlineTest extends AbstractDB2OnlineTestCase {
       public void testTableSchema() throws Exception {
    	String tableSchema = getDataStore().getTableSchema();
        assertEquals(tableSchema, "Test");
    }

    public void testEntity() throws Exception {
        DB2DataStore dataStore = getDataStore();
        String[] typeNames = dataStore.getTypeNames();
        int foundCount = 0;

        for (int i = 0; i < typeNames.length; i++) {
            if (typeNames[i].equals("Places")) {
                foundCount++;
            }

            if (typeNames[i].equals("Roads")) {
                foundCount++;
            }
        }

        assertTrue(foundCount == 2);
    }
}
