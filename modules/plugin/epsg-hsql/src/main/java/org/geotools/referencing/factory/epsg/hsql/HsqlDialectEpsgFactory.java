/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg.hsql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.referencing.factory.epsg.AnsiDialectEpsgFactory;
import org.geotools.util.factory.Hints;

/**
 * Adapts SQL statements for HSQL. The HSQL database engine doesn't understand the parenthesis in
 * (INNER JOIN ... ON) statements for the "BursaWolfParameters" query. Unfortunately, those
 * parenthesis are required by MS-Access. We need to removes them programmatically here.
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class HsqlDialectEpsgFactory extends AnsiDialectEpsgFactory {
    /**
     * The regular expression pattern for searching the "FROM (" clause. This is the pattern for the
     * opening parenthesis.
     */
    private static final Pattern OPENING_PATTERN =
            Pattern.compile("\\s+FROM\\s*\\(", Pattern.CASE_INSENSITIVE);

    /** Constructs the factory for the given connection to the HSQL database. */
    public HsqlDialectEpsgFactory(final Hints hints) throws SQLException {
        super(hints, HsqlEpsgDatabase.createDataSource());
    }

    /** Constructs the factory for the given connection to the HSQL database. */
    public HsqlDialectEpsgFactory(final Hints hints, final javax.sql.DataSource dataSource) {
        super(hints, dataSource);
    }

    /** If the query contains a "FROM (" expression, remove the parenthesis. */
    public String adaptSQL(String query) {
        query = super.adaptSQL(query);
        final Matcher matcher = OPENING_PATTERN.matcher(query);
        if (matcher.find()) {
            final int opening = matcher.end() - 1;
            final int length = query.length();
            int closing = opening;
            for (int count = 0; ; closing++) {
                if (closing >= length) {
                    // Should never happen with well formed SQL statement.
                    // If it happen anyway, don't change anything and let
                    // the HSQL driver produces a "syntax error" message.
                    return query;
                }
                switch (query.charAt(closing)) {
                    case '(':
                        count++;
                        break;
                    case ')':
                        count--;
                        break;
                    default:
                        continue;
                }
                if (count == 0) {
                    break;
                }
            }
            query =
                    query.substring(0, opening)
                            + query.substring(opening + 1, closing)
                            + query.substring(closing + 1);
        }
        return query;
    }

    /**
     * Shutdown the HSQL database engine. This method is invoked automatically at JVM shutdown time
     * just before to close the connection.
     */
    protected void shutdown(final boolean active) throws SQLException {
        if (active) {
            try (Statement statement = getConnection().createStatement()) {
                statement.execute("SHUTDOWN");
            }
        }
        super.shutdown(active);
    }
}
