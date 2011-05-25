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

package org.geotools.data.postgis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Use simple SQL to ensure test fixture is working.
 * 
 * <p>
 * 
 * See {@link AbstractRegfuncPostgisOnlineTestCase} for a description of this test fixture.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class SimpleRegfuncPostgisOnlineTest extends AbstractRegfuncPostgisOnlineTestCase {

    /**
     * Test that there are four rows in the test table.
     * 
     * @throws Exception
     */
    public void testCountRows() throws Exception {
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT count(1) FROM "
                        + TEST_TABLE_NAME);
                // ensure we have a result
                assertTrue(resultSet.next());
                // expect four rows
                assertEquals(4, resultSet.getInt(1));
                // ensure no more results
                assertFalse(resultSet.next());
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Test that there are two rows with description containing the word basalt (case insensitive).
     * 
     * @throws Exception
     */
    public void testFunctionCall() throws Exception {
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery( //
                        "SELECT count(1) FROM " //
                                + TEST_TABLE_NAME //
                                + " WHERE " //
                                + TEST_FUNCTION_NAME //
                                + "(" //
                                + DESCRIPTION_COLUMN_NAME //
                                + ", 'basalt') = 1");
                // ensure we have a result
                assertTrue(resultSet.next());
                // expect two rows containing the word basalt (case-insensitive)
                assertEquals(2, resultSet.getInt(1));
                // ensure no more results
                assertFalse(resultSet.next());
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
