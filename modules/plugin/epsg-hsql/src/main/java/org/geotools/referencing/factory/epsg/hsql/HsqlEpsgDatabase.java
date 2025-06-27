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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.geotools.api.referencing.FactoryException;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.hsqldb.jdbc.JDBCDataSource;

/**
 * This utility class knows everything there is to know about the care and feeding of our pet EPSG database. This
 * utility class is used to hold logic previously associated with our own custom DataSource.
 *
 * <p>The EPSG database can be downloaded from <A HREF="http://www.epsg.org">http://www.epsg.org</A>. The SQL scripts
 * (modified for the HSQL syntax as <A HREF="doc-files/HSQL.html">explained here</A>) are bundled into this plugin. The
 * database version is given in the {@linkplain org.geotools.api.metadata.citation.Citation#getEdition edition
 * attribute} of the {@linkplain org.geotools.api.referencing.AuthorityFactory#getAuthority authority}. The HSQL
 * database is read only.
 *
 * <p>
 *
 * @since 2.4
 * @version $Id$
 * @author Jody Garnett
 * @todo This class is used only by {@link HSQLDataSource}, which is deprecated.
 */
public class HsqlEpsgDatabase {
    /** The key for fetching the database directory from {@linkplain System#getProperty(String) system properties}. */
    public static final String DIRECTORY_KEY = "EPSG-HSQL.directory";

    /** The database name. */
    public static final String DATABASE_NAME = "EPSG";

    /**
     * Creates a DataSource that is set up and ready to go.
     *
     * <p>This method pays attention to the system property "EPSG-HSQL.directory" and makes use of the default database
     * name "EPSG".
     */
    public static javax.sql.DataSource createDataSource() throws SQLException {
        return createDataSource(getDirectory());
    }

    public static javax.sql.DataSource createDataSource(Hints hints) throws FactoryException {
        try {
            return createDataSource(getDirectory());
        } catch (SQLException e) {
            throw new FactoryException(e);
        }
    }

    public static javax.sql.DataSource createDataSource(File directory) throws SQLException {
        JDBCDataSource dataSource = new JDBCDataSource();
        /*
         * Constructs the full path to the HSQL database. Note: we do not use
         * File.toURI() because HSQL doesn't seem to expect an encoded URL (e.g.
         * "%20" instead of spaces).
         */
        final StringBuilder url = new StringBuilder("jdbc:hsqldb:file:");
        final String path = directory.getAbsolutePath().replace(File.separatorChar, '/');
        if (path.length() == 0 || path.charAt(0) != '/') {
            url.append('/');
        }
        url.append(path);
        if (url.charAt(url.length() - 1) != '/') {
            url.append('/');
        }
        url.append(HsqlEpsgDatabase.DATABASE_NAME);
        dataSource.setDatabase(url.toString());
        /*
         * If the temporary directory do not exists or can't be created, lets
         * the 'database' attribute unset. If the user do not set it explicitly
         * (for example through JNDI), an exception will be thrown when
         * 'getConnection()' will be invoked.
         */
        dataSource.setUser("SA"); // System administrator. No password.
        if (!dataExists(dataSource)) {
            generateData(dataSource);
            try {
                forceReadOnly(directory);
            } catch (IOException file) {
                throw (SQLException) new SQLException("Can't read the SQL script.").initCause(file);
                // TODO: inline cause when we will be allowed to target Java 6.
            }
        }
        return dataSource;
    }

    /**
     * HSQL has created automatically an empty database. We need to populate it. Executes the SQL scripts bundled in the
     * JAR. In theory, each line contains a full SQL statement. For this plugin however, we have compressed "INSERT
     * INTO" statements using Compactor class in this package.
     */
    private static void generateData(javax.sql.DataSource dataSource) throws SQLException {
        Logging.getLogger(HsqlEpsgDatabase.class).config("Creating cached EPSG database."); // TODO: localize
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        HsqlEpsgDatabase.class.getResourceAsStream("EPSG.sql"), StandardCharsets.ISO_8859_1))) {
            StringBuilder insertStatement = null;
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                final int length = line.length();
                if (length != 0) {
                    if (line.startsWith("INSERT INTO")) {
                        /*
                         * We are about to insert many rows into a single table.
                         * The row values appear in next lines; the current line
                         * should stop right after the VALUES keyword.
                         */
                        insertStatement = new StringBuilder(line);
                        continue;
                    }
                    if (insertStatement != null) {
                        /*
                         * We are about to insert a row. Prepend the "INSERT
                         * INTO" statement and check if we will have more rows
                         * to insert after this one.
                         */
                        final int values = insertStatement.length();
                        insertStatement.append(line);
                        final boolean hasMore = line.charAt(length - 1) == ',';
                        if (hasMore) {
                            insertStatement.setLength(insertStatement.length() - 1);
                        }
                        line = insertStatement.toString();
                        insertStatement.setLength(values);
                        if (!hasMore) {
                            insertStatement = null;
                        }
                    }
                    statement.execute(line);
                }
            }
        } catch (IOException exception) {
            SQLException e = new SQLException("Can't read the SQL script."); // TODO: localize
            e.initCause(exception); // TODO: inline cause when we will be allowed to target Java 6.
            throw e;
        }
    }

    private static void forceReadOnly(File directory) throws IOException {
        final File file = new File(directory, HsqlEpsgDatabase.DATABASE_NAME + ".properties");
        final Properties properties = new Properties();
        try (InputStream propertyIn = new FileInputStream(file)) {

            properties.load(propertyIn);
        }
        properties.put("readonly", "true");
        try (OutputStream out = new FileOutputStream(file)) {
            properties.store(out, "EPSG database on HSQL");
        }
    }

    /**
     * Returns the default directory for the EPSG database. If the {@value #DIRECTORY_KEY}
     * {@linkplain System#getProperty(String) system property} is defined and contains the name of a directory with a
     * valid {@linkplain File#getParent parent}, then the {@value #DATABASE_NAME} database will be saved in that
     * directory. Otherwise, a temporary directory will be used.
     */
    static File getDirectory() throws SQLException {
        try {
            final String property = System.getProperty(HsqlEpsgDatabase.DIRECTORY_KEY);
            if (property != null) {
                final File directory = new File(property);
                /*
                 * Creates the directory if needed (mkdir), but NOT the parent
                 * directories (mkdirs) because a missing parent directory may
                 * be a symptom of an installation problem. For example if
                 * 'directory' is a subdirectory in the temporary directory
                 * (~/tmp/), this temporary directory should already exists. If
                 * it doesn't, an administrator should probably looks at this
                 * problem.
                 */
                if (directory.isDirectory() || directory.mkdir()) {
                    return directory;
                }
            }
        } catch (SecurityException e) {
            /*
             * Can't fetch the base directory from system properties. Fallback
             * on the default temporary directory.
             */
        }
        File directory = new File(System.getProperty("java.io.tmpdir", "."), "Geotools");
        if (directory.isDirectory() || directory.mkdir()) {
            directory = new File(directory, "Databases/HSQL");
            if (directory.isDirectory() || directory.mkdirs()) {
                return directory;
            }
        }
        throw new SQLException("Can't write to the database directory.");
    }

    static boolean dataExists(javax.sql.DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return dataExists(connection);
        }
    }

    /**
     * Returns {@code true} if the database contains data. This method returns {@code false} if an empty EPSG database
     * has been automatically created by HSQL and not yet populated.
     */
    static boolean dataExists(final Connection connection) throws SQLException {
        final DatabaseMetaData metaData = connection.getMetaData();
        try (final ResultSet tables = metaData.getTables(
                null, null, "EPSG" + metaData.getSearchStringEscape() + "_%", new String[] {"TABLE"})) {
            return tables.next();
        }
    }
}
