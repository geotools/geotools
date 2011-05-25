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
package org.geotools.referencing.factory.epsg;

// J2SE dependencies
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.sql.DataSource;

// Geotools dependencies
import org.geotools.factory.Hints;


/**
 * An EPSG factory suitable for Oracle SQL syntax.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author John Grange
 *
 * @todo Since this class is constructed through the service provider API rather than directly
 *       instantiated by the user, we need some way to pass the schema information to this class.
 *       one possible approach is to set the schema in preferences. Maybe a better was is to look
 *       for a place in the Oracle {@link javax.sql.DataSource} for that.
 */
public class OracleDialectEpsgFactory extends AnsiDialectEpsgFactory {
    /**
     * The pattern to use for removing <code>" as "</code> elements from the SQL statements.
     */
    private final Pattern pattern = Pattern.compile("\\sAS\\s");

    /**
     * Constructs an authority factory using the specified connection.
     *
     * @param userHints  The underlying factories used for objects creation.
     * @param connection The connection to the underlying EPSG database.
     */
    public OracleDialectEpsgFactory(final Hints      userHints,
                                    final Connection connection)
    {
        super(userHints, connection);
    }

    /**
     * Constructs an authority factory using the specified datasource.
     *
     * @param userHints  The underlying factories used for objects creation.
     * @param datasource The datasource of the underlying EPSG database.
     */
    public OracleDialectEpsgFactory(final Hints      userHints,
                                    final DataSource datasource)
    {
        super(userHints, datasource);
    }

    
    /**
     * Constructs an authority factory using the specified connection to an EPSG database
     * and a database schema. If the database schema is not supplied, or it is null
     * or an empty string, then the tables are assumed to be in the same schema as
     * the user which is being used to connect to the database.  You <strong>MUST</strong>
     * ensure that the connecting user has permissions to select from all the tables in the
     * epsg user schema.
     *
     * @param userHints  The underlying factories used for objects creation.
     * @param connection The connection to the underlying EPSG database.
     * @param epsgSchema The database schema in which the epsg tables are stored (optional).
     */
    public OracleDialectEpsgFactory(final Hints      userHints,
                                    final Connection connection,
                                    final String     epsgSchema)
    {
        super(userHints, connection);
        adaptTableNames(epsgSchema);
    }

    /**
     * Modifies the given SQL string to be suitable for an Oracle databases.
     * This removes {@code " AS "} elements from the SQL statements as
     * these don't work in oracle.
     *
     * @param statement The statement in MS-Access syntax.
     * @return The SQL statement to use, suitable for an Oracle database.
     */
    protected String adaptSQL(final String statement) {
        return pattern.matcher(super.adaptSQL(statement)).replaceAll(" ");
    }

    /**
     * If we have been supplied with a non null {@code epsgSchema},
     * prepend the schema to all the table names.
     *
     * @param epsgSchema The database schema in which the epsg tables are stored (optional).
     */
    private void adaptTableNames(String epsgSchema) {
        if (epsgSchema != null) {
            epsgSchema = epsgSchema.trim();
            if (epsgSchema.length() != 0) {
                for (final Iterator it=map.entrySet().iterator(); it.hasNext();) {
                    final Map.Entry  entry = (Map.Entry) it.next();
                    final String tableName = (String) entry.getValue();
                    /**
                     * Update the map, prepending the schema name to the table name
                     * so long as the value is a table name and not a field. This
                     * algorithm assumes that all old table names start with "epsg_".
                     */
                    if (tableName.startsWith("epsg_")) {
                        entry.setValue(epsgSchema + '.' + tableName);
                    }
                }
            }
        }
    }
}
