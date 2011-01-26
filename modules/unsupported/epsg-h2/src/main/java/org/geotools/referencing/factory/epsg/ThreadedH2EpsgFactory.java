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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.sql.DataSource;

import org.geotools.factory.Hints;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.util.Version;
import org.geotools.util.logging.Logging;
import org.h2.jdbcx.JdbcDataSource;



/**
 * Connection to the EPSG database in H2 database engine format using JDBC. The EPSG
 * database can be downloaded from <A HREF="http://www.epsg.org">http://www.epsg.org</A>.
 * The SQL scripts (modified for the HSQL syntax as <A HREF="doc-files/HSQL.html">explained
 * here</A>) are bundled into this plugin. The database version is given in the
 * {@linkplain org.opengis.metadata.citation.Citation#getEdition edition attribute}
 * of the {@linkplain org.opengis.referencing.AuthorityFactory#getAuthority authority}.
 * The HSQL database is read only.
 * <P>
 * <H3>Implementation note</H3>
 * The SQL scripts are executed the first time a connection is required. The database
 * is then created as cached tables ({@code HSQL.properties} and {@code HSQL.data} files)
 * in a temporary directory. Future connections to the EPSG database while reuse the cached
 * tables, if available. Otherwise, the scripts will be executed again in order to recreate
 * them.
 * <p>
 * If the EPSG database should be created in a different directory (or already exists in that
 * directory), it may be specified as a {@linkplain System#getProperty(String) system property}
 * nammed {@value #DIRECTORY_KEY}.
 *
 * @since 2.6
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Didier Richard
 * @author Andrea Aime
 */
public class ThreadedH2EpsgFactory extends ThreadedEpsgFactory {
    /**
     * Current version of EPSG-h2 plugin. This is usually the same version number than the
     * one in the EPSG database bundled in this plugin. However this field may contains
     * additional minor version number if there is some changes related to the EPSG-H2
     * plugin rather then the EPSG database itself (for example additional database index).
     */
    public static final Version VERSION = new Version("7.5.0");
    
    /**
     * The name of the ZIP file to read in order to create the cached database.
     */
    private static final String ZIP_FILE = "EPSG.zip";
    
    /**
     * The successful database creation marker
     */
    static final String MARKER_FILE = "EPSG_creation_marker.txt";
    
    /**
     * The database creation lock file
     */
    static final String LOCK_FILE = "EPSG_creation_lock.txt";

    /**
     * The key for fetching the database directory from {@linkplain System#getProperty(String)
     * system properties}.
     */
    public static final String DIRECTORY_KEY = "EPSG-H2.directory";

    /**
     * The database name.
     */
    public static final String DATABASE_NAME = "EPSG";

    /**
     * The prefix to put in front of URL to the database.
     */
    static final String PREFIX = "jdbc:h2:";

    /**
     * The logger name.
     */
    private static final String LOGGER = "org.geotools.referencing.factory.epsg";

    /**
     * Creates a new instance of this factory. If the {@value #DIRECTORY_KEY}
     * {@linkplain System#getProperty(String) system property} is defined and contains
     * the name of a directory with a valid {@linkplain File#getParent parent}, then the
     * {@value #DATABASE_NAME} database will be saved in that directory. Otherwise, a
     * temporary directory will be used.
     */
    public ThreadedH2EpsgFactory() {
        this(null);
    }

    /**
     * Creates a new instance of this data source using the specified hints. The priority
     * is set to a lower value than the {@linkplain FactoryOnAccess}'s one in order to give
     * precedence to the Access-backed database, if presents. Priorities are set that way
     * because:
     * <ul>
     *   <li>The MS-Access format is the primary EPSG database format.</li>
     *   <li>If a user downloads the MS-Access database himself, he probably wants to use it.</li>
     * </ul>
     */
    public ThreadedH2EpsgFactory(final Hints hints) {
        super(hints, PRIORITY + 1);
    }

    /**
     * Returns the default directory for the EPSG database. If the {@value #DIRECTORY_KEY}
     * {@linkplain System#getProperty(String) system property} is defined and contains the
     * name of a directory with a valid {@linkplain File#getParent parent}, then the
     * {@value #DATABASE_NAME} database will be saved in that directory. Otherwise,
     * a temporary directory will be used.
     */
    private static File getDirectory() {
        try {
            final String property = System.getProperty(DIRECTORY_KEY);
            if (property != null) {
                final File directory = new File(property);
                /*
                 * Creates the directory if needed (mkdir), but NOT the parent directories (mkdirs)
                 * because a missing parent directory may be a symptom of an installation problem.
                 * For example if 'directory' is a subdirectory in the temporary directory (~/tmp/),
                 * this temporary directory should already exists. If it doesn't, an administrator
                 * should probably looks at this problem.
                 */
                if (directory.isDirectory() || directory.mkdir()) {
                    return directory;
                }
            }
        } catch (SecurityException e) {
            /*
             * Can't fetch the base directory from system properties.
             * Fallback on the default temporary directory.
             */
        }
        return getTemporaryDirectory();
    }
    /**
     * Tests of the EPSG database is unpacked into the required directory.
     * <p>
     * This method exists to allow client applications to check if the EPSG
     * database is unpacked and ready to go; or if more time shoudld be
     * alloted to let the database be constructed.
     * @return true if directory intended for h2 exists (assumed from a previous run)
     */
    public static boolean isUnpacked(){
    	try {
            final String property = System.getProperty(DIRECTORY_KEY);
            if (property != null) {
                final File directory = new File(property);
                if (directory.exists() && directory.isDirectory()) {
                    return true;
                }
            }
        } catch (SecurityException e) {
        }
        File temp = new File(System.getProperty("java.io.tmpdir", "."), "Geotools");
        File directory = new File( temp, "Databases/EPSG-H2-" + VERSION + "/" );
        if (directory.exists() && directory.isDirectory()) {
            return true;
        }
        return false;
    }
    /**
     * Returns the directory to uses in the temporary directory folder.
     */
    private static File getTemporaryDirectory() {
        File directory = new File(System.getProperty("java.io.tmpdir", "."), "Geotools");
        if (directory.isDirectory() || directory.mkdir()) {
            directory = new File(directory, "Databases/EPSG-H2-" + VERSION + "/");
            if (directory.isDirectory() || directory.mkdirs()) {
                return directory;
            }
        }
        return null;
    }

    /**
     * Extract the directory from the specified data source, or {@code null} if this
     * information is not available.
     */
    private static File getDirectory(final DataSource source) {
        if (source instanceof JdbcDataSource) {
            String path = ((JdbcDataSource) source).getURL();
            if (path!=null && PREFIX.regionMatches(true, 0, path, 0, PREFIX.length())) {
                path = path.substring(PREFIX.length());
                if( path.indexOf(';') != -1 ){
                    path = path.substring(0, path.indexOf(';'));
                }
                return new File(path).getParentFile();
            }
        }
        return null;
    }

    /**
     * Returns a data source for the HSQL database.
     */
    protected DataSource createDataSource() throws SQLException {
        DataSource candidate = super.createDataSource();
        if (candidate instanceof JdbcDataSource) {
            return candidate;
        }
        final JdbcDataSource source = new JdbcDataSource();
        File directory = getDirectory();
        if (directory != null) {
            /*
             * Constructs the full path to the HSQL database. Note: we do not use
             * File.toURI() because HSQL doesn't seem to expect an encoded URL
             * (e.g. "%20" instead of spaces).
             */
            final StringBuilder url = new StringBuilder(PREFIX);
            final String path = directory.getAbsolutePath().replace(File.separatorChar, '/');
            if (path.length()==0 || path.charAt(0)!='/') {
                url.append('/');
            }
            url.append(path);
            
            if (url.charAt(url.length()-1) != '/') {
                url.append('/');
            }
            url.append(DATABASE_NAME);
            // no need for validation query, saves some work
            url.append(";AUTO_RECONNECT=TRUE;CACHE_SIZE=131072;CACHE_TYPE=TQ");
            source.setURL((url.toString()));
            source.setUser("sa");
            source.setPassword("");
            assert directory.equals(getDirectory(source)) : url;
        }
        /*
         * If the temporary directory do not exists or can't be created, lets the 'database'
         * attribute unset. If the user do not set it explicitly (through JNDI or by overrding
         * this method), an exception will be thrown when 'createBackingStore()' will be invoked.
         */
        source.setUser("SA"); // System administrator. No password.
        return source;
    }

    /**
     * Returns {@code true} if the database contains data. This method returns {@code false}
     * if an empty EPSG database has been automatically created by HSQL and not yet populated.
     */
    private static boolean dataExists(File directory) throws SQLException {
        // check if the marker file is there, and all the other database files as well
        // (as some windows cleanup tools delete the .data file only)
        return new File(directory, MARKER_FILE).exists() &&
               new File(directory, DATABASE_NAME + ".data.db").exists() &&
               new File(directory, DATABASE_NAME + ".index.db").exists();
    }

    /**
     * Returns the backing-store factory for HSQL syntax. If the cached tables are not available,
     * they will be created now from the SQL scripts bundled in this plugin.
     *
     * @param  hints A map of hints, including the low-level factories to use for CRS creation.
     * @return The EPSG factory using HSQL syntax.
     * @throws SQLException if connection to the database failed.
     */
    protected AbstractAuthorityFactory createBackingStore(final Hints hints) throws SQLException {
        final DataSource source = getDataSource();
        final File directory    = getDirectory(source);
        directory.mkdirs();
        if (!dataExists(directory)) {
            FileLock lock = null;
            try {
                // get an exclusive lock
                lock = acquireLock(directory);
                
                // if after getting the lock the database is still incomplete let's work on it
                if(!dataExists(directory)) {
                    /*
                     * HSQL has created automatically an empty database. We need to populate it.
                     * Executes the SQL scripts bundled in the JAR. In theory, each line contains
                     * a full SQL statement. For this plugin however, we have compressed "INSERT
                     * INTO" statements using Compactor class in this package.
                     */
                    final Logger logger = Logging.getLogger(LOGGER);
                    final LogRecord record = Loggings.format(Level.INFO,
                            LoggingKeys.CREATING_CACHED_EPSG_DATABASE_$1, VERSION);
                    record.setLoggerName(logger.getName());
                    logger.log(record);
                
                    ZipInputStream zin = new ZipInputStream(ThreadedH2EpsgFactory.class.getResourceAsStream(ZIP_FILE));
                    ZipEntry ze = null;
                    byte[] buf = new byte[1024];
                    int read = 0;
                    while ((ze = zin.getNextEntry()) != null) {
                      File file = new File(directory, ze.getName());
                      if( file.exists() ){
                          file.delete();
                      }                   
                      FileOutputStream fout = new FileOutputStream(file);
                      while((read = zin.read(buf)) > 0) {
                        fout.write(buf, 0, read);
                      }
                      zin.closeEntry();
                      fout.close();
                    }
                    zin.close();
                    
                    // mark the successful creation
                    File marker = new File(directory, MARKER_FILE);
                    if( marker.exists() ){
                        marker.delete();
                    }
                    marker.createNewFile();
                    
                    setReadOnly(directory);
                }
            } catch (IOException exception) {
                SQLException e = new SQLException(Errors.format(ErrorKeys.CANT_READ_$1, ZIP_FILE));
                e.initCause(exception); // TODO: inline cause when we will be allowed to target Java 6.
                throw e;
            } finally {
                if(lock != null) {
                    try {
                        lock.release();
                        lock.channel().close();
                        new File(directory, LOCK_FILE).delete();
                    } catch(IOException e) {
                        // does not matter, was just cleanup
                    }
                }
            }
            
        }
        FactoryUsingAnsiSQL factory = new FactoryUsingAnsiSQL(hints, getDataSource().getConnection());
        factory.setValidationQuery("CALL NOW()");
        return factory;
    }

    /**
     * 
     * @param directory
     * @return
     * @throws IOException
     */
    FileLock acquireLock(File directory) throws IOException {
        // Get a file channel for the file
        File file = new File(directory, LOCK_FILE);
        FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
    
        // Use the file channel to create a lock on the file.
        // This method blocks until it can retrieve the lock.
        FileLock lock = channel.lock();
    
        // Try acquiring the lock without blocking. This method returns
        // null or throws an exception if the file is already locked.
        while(!lock.isValid()) {
            try {
                lock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                // File is already locked in this thread or virtual machine
            }
            // wait for the other process to unlock it, should take a couple of seconds
            try {
                Thread.sleep(500);
            } catch(InterruptedException e) {
                // someone waked us earlier, no problem
            }
        }
        
        return lock;
    }
    
    private void setReadOnly(final File directory) {
        if( directory == null ) return;
        for (File file : directory.listFiles()) {
            if(file.isDirectory()) {
                setReadOnly(file);
            } else if(file.getName().endsWith(".log.db"))
                file.delete();
            else
                file.setReadOnly();
        }
    }
}
