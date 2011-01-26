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
package org.geotools.data.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.data.DataSourceException;


/**
 * Provides a sequence for generating unique FID keys.  Selects the max value of the fid column
 * then increments it with each successive call to getNext().
 * 
 * <p>
 * This may break if other tools are modifying the database at the same time. But this is a wider
 * issue that should be dealt with in a Geotools standard way.  When that way is determined this
 * class can probably disappear. This is also part of the reason why this class is only package
 * visible, since this method of generating fids should not be used by other data sources.
 * </p>
 *
 * @author Sean Geoghegan, Defence Science and Technology Organisation
 * @author $Author: seangeo $
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT: This assumes that the DB does not manage the PK itself.  I need to work out a way
 *       to check that is does and then defer to the DB in that case.
 */
class FIDSequence {
    /** Holds the current FID.  THis is incremented for each getNext() call */
    private int current;

    /**
     * Creates a FID sequence that generates unique FID keys for adding features to the table.
     *
     * @param conn The Database Connection
     * @param tablename The table
     * @param fidColumn The FID column
     *
     * @throws DataSourceException If an error occurs determining the max fid.
     */
    FIDSequence(Connection conn, String tablename, String fidColumn) throws DataSourceException {
        ResultSet resultSet = null;

        try {
            String query = "SELECT max(" + fidColumn + ") FROM " + tablename;

            Statement statement = conn.createStatement();

            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                Number number = (Number) resultSet.getObject(1);

                current = number.intValue();
            } else {
                throw new DataSourceException("Could not get max for " + fidColumn);
            }
        } catch (SQLException e) {
            throw new DataSourceException("SQL Error occured when " + "generating unique key", e);
        } catch (ClassCastException e) {
            throw new DataSourceException("Error casting fid column " + "to a number", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e1) {
                // dont need to do anything here - just closing the result set
            }
        }
    }

    /**
     * Gets the next unique FID key.
     *
     * @return The next key in the sequence.
     */
    int getNext() {
        current += 1;

        return current;
    }
}
