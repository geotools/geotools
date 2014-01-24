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
package org.geotools.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

/**
 * Executes a chain of {@link PrimaryKeyFinder} in the order they are defined
 * 
 * @author Andrea Aime - OpenGeo
 * 
 *
 *
 * @source $URL$
 */
public class CompositePrimaryKeyFinder extends PrimaryKeyFinder {
    
    private static final Logger LOGGER = Logging.getLogger(CompositePrimaryKeyFinder.class);

    List<PrimaryKeyFinder> finders;

    public CompositePrimaryKeyFinder(PrimaryKeyFinder... finders) {
        this.finders = Arrays.asList(finders);
    }

    @Override
    public PrimaryKey getPrimaryKey(JDBCDataStore store, String schema, String table, Connection cx)
            throws SQLException {
        for (PrimaryKeyFinder finder : finders) {
            try {
                PrimaryKey pk = finder.getPrimaryKey(store, schema, table, cx);
                if (pk != null)
                    return pk;
            } catch(SQLException e) {
                LOGGER.log(Level.WARNING, "Failure occurred while looking up the primary "
                        + "key with finder: " + finder, e);
            }
        }

        return null;
    }

}
