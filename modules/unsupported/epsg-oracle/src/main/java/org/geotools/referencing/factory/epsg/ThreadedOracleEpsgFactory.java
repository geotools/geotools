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

// J2SE dependencies
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

// Geotools dependencies
import org.geotools.util.logging.Logging;
import org.geotools.factory.Hints;
import org.geotools.referencing.factory.AbstractAuthorityFactory;

/**
 * Connection to the EPSG database in Oracle database engine using a JDBC datasource. The EPSG
 * database can be downloaded from <A HREF="http://www.epsg.org">http://www.epsg.org</A>.
 * It should have been imported into an Oracle database, which doesn't need to be on
 * the local machine.
 * <p>
 * <h3>Connection parameters</h3>
 * The preferred way to specify connection parameters is through the JNDI interface.
 * However, this datasource provides the following alternative as a convenience: if a
 * {@value #CONFIGURATION_FILE} file is found in current directory or in the user's home
 * directory, then the following properties are fetched. Note that the default value may change
 * in a future version if a public server becomes available.
 * <P>
 * <TABLE BORDER="1">
 * <TR>
 *   <TH>Property</TH>
 *   <TH>Type</TH>
 *   <TH>Description</TH>
 *   <TH>Geotools Default</TH>
 * </TR>
 * <TR>
 *   <TD>{@code serverName}</TD>
 *   <TD>String</TD>
 *   <TD>Oracle database server host name</TD>
 *   <TD>{@code localhost}</TD>
 * </TR>
 * <TR>
 *   <TD>{@code databaseName}</TD>
 *   <TD>String</TD>
 *   <TD>Oracle database name</TD>
 *   <TD>{@code EPSG}</TD>
 * </TR>
 * <TR>
 *   <TD>{@code schema}</TD>
 *   <TD>String</TD>
 *   <TD>The schema for the EPSG tables</TD>
 *   <TD></TD>
 * </TR>
 * <TR>
 *   <TD>{@code portNumber}</TD>
 *   <TD>int</TD>
 *   <TD>TCP port which the Oracle database server is listening on</TD>
 *   <TD>{@code 1521}</TD>
 * </TR>
 * <TR>
 *   <TD>{@code user}</TD>
 *   <TD>String</TD>
 *   <TD>User used to make database connections</TD>
 *   <TD>{@code GeoTools}</TD>
 * </TR>
 * <TR>
 *   <TD>{@code password}</TD>
 *   <TD>String</TD>
 *   <TD>Password used to make database connections</TD>
 *   <TD>{@code GeoTools}</TD></TR>
 * </TABLE>
 * <P>
 * The database version is given in the
 * {@linkplain org.opengis.metadata.citation.Citation#getEdition edition attribute}
 * of the {@linkplain org.opengis.referencing.AuthorityFactory#getAuthority authority}.
 * The Oracle database should be read only.
 * <P>
 * Just having this class accessible in the classpath, together with the registration in
 * the {@code META-INF/services/} directory, is sufficient to get a working EPSG authority
 * factory backed by this database. Vendors can create a copy of this class, modify it and
 * bundle it with their own distribution if they want to connect their users to an other
 * database.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Didier Richard
 * @author Martin Desruisseaux
 * @author Jody Garnett
 */
public class ThreadedOracleEpsgFactory extends ThreadedEpsgFactory {
    /**
     * The user configuration file. This class search first for the first file found in the
     * following directories:
     * <ul>
     *   <li>The current directory</li>
     *   <li>The user's home directory</li>
     * </ul>
     */
    public static final String CONFIGURATION_FILE = "EPSG-DataSource.properties";

    /**
     * The schema name, or {@code null} if none.
     */
    private String schema;

    /**
     * Creates a new instance of this factory.
     */
    public ThreadedOracleEpsgFactory() {
        this(null);
    }

    /**
     * Creates a new instance of this factory with the specified hints.
     * The priority is set to a lower value than the {@linkplain FactoryOnAccess}'s one
     * in order to give the priority to any "official" database installed locally by the
     * user, when available.
     */
    public ThreadedOracleEpsgFactory(final Hints hints) {
        super(hints, PRIORITY + 5);
    }

    /**
     * Loads the {@linkplain #CONFIGURATION_FILE configuration file}.
     */
    private static Properties load() {
        final Properties p = new Properties();
        File file = new File(CONFIGURATION_FILE);
        if (!file.isFile()) {
            file = new File(System.getProperty("user.home", "."), CONFIGURATION_FILE);
            if (!file.isFile()) {
                // Returns an empty set of properties.
                return p;
            }
        }
        try {
            final InputStream in = new FileInputStream(file);
            p.load(in);
            in.close();
        } catch (IOException exception) {
            Logging.unexpectedException("org.geotools.referencing.factory", DataSource.class,
                                        "<init>", exception);
            // Continue. We will try to work with whatever properties are available.
        }
        return p;
    }

    /**
     * Returns a data source for the PostgreSQL database.
     */
    protected DataSource createDataSource() throws SQLException {
        DataSource source = super.createDataSource();
        if( source != null ){
            return source;
        }
        final Properties p = load();
        int portNumber;
        try {
            portNumber = Integer.parseInt(p.getProperty("portNumber", "5432"));
        } catch (NumberFormatException exception) {
            portNumber = 5432;
            Logging.unexpectedException("org.geotools.referencing.factory", DataSource.class,
                                        "<init>", exception);
        }
        String serverName = p.getProperty("serverName", "localhost");
        String databaseName =  p.getProperty("databaseName", "EPSG");
        String user  = p.getProperty("user", "Geotools");
        String password =  p.getProperty("password", "Geotools");
        schema = p.getProperty("schema", null);
        
        return source;
    }

    /**
     * Returns the backing-store factory for Oracle syntax.
     *
     * @param  hints A map of hints, including the low-level factories to use for CRS creation.
     * @return The EPSG factory using Oracle syntax.
     * @throws SQLException if connection to the database failed.
     */
    protected AbstractAuthorityFactory createBackingStore(final Hints hints) throws SQLException {
        final Connection connection = getDataSource().getConnection();
        final FactoryUsingOracleSQL factory = new FactoryUsingOracleSQL(hints, connection);
        if (schema != null) {
            factory.setSchema(schema);
        }
        return factory;
    }
}
