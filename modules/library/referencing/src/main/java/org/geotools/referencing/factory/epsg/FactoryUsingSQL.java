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
package org.geotools.referencing.factory.epsg;

// J2SE dependencies and extensions
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

// Geotools dependencies
import org.geotools.factory.Hints;


/**
 * Please use {@link AccessDialectEpsgFactory}.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 *
 * @deprecated Please use {@link AccessDialectEpsgFactory}.
 */
public class FactoryUsingSQL extends DirectEpsgFactory {
    /**
     * Constructs an authority factory using the specified connection.
     *
     * @param userHints The underlying factories used for objects creation.
     * @param connection The connection to the underlying EPSG database.
     *
     * @since 2.2
     */
    public FactoryUsingSQL(final Hints userHints, final Connection connection) {
        super(userHints, connection );
    }
    
    /**
     * Constructs an authority factory using the specified connection.
     *
     * @param userHints The underlying factories used for objects creation.
     * @param dataSource The connection to the underlying EPSG database.
     *
     * @since 2.5
     */
    public FactoryUsingSQL(final Hints userHints, final DataSource dataSource) {
        super(userHints, dataSource);
    }

    /**
     * Invoked when a new {@link PreparedStatement} is about to be created from a SQL string.
     * 
     * Since the <A HREF="http://www.epsg.org">EPSG database</A> is available mainly in MS-Access
     * format, and this is the target of our super class, we have no work to do here.
     *
     * @param  statement The statement in MS-Access syntax.
     * @return The SQL statement to use. This implementation returns the string unchanged.
     */
    protected String adaptSQL(final String statement) {
        return statement;
    }
}
