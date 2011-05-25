/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;
import java.util.jar.Attributes.Name;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.imageio.spi.ServiceRegistry;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;

import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.factory.FactoryRegistry;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.DeferredAuthorityFactory;
import org.geotools.referencing.factory.FactoryNotFoundException;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * Base class for EPSG factories to be registered in {@link ReferencingFactoryFinder}.
 * Various subclasses are defined for different database backends: Access, PostgreSQL,
 * HSQL, <cite>etc.</cite>.
 * <p>
 * This class has the following responsibilities:
 * <ul>
 *   <li>aquire a DataSource (using JNDI or otherwise)</li>
 *   <li>specify a worker class that will talk to the database in
 *     the event of a cache miss. The class will be specific to the delect of SQL
 *     used by the database hosting the EPSG tables.</li>
 * </ul>
 * Please note we are working with <b>the same</b> tables as defined by EPSG. The only
 * thing that changes is the database used to host these tables.
 * <p>
 * Subclasses should override the following methods:
 * <ul>
 *   <li>{@linkplain #createDataSource} used to aquire a DataSource</li>
 *   <li>{@link #createBackingStore} instance capable to speak that database syntax</li>
 * </ul>
 * <p>
 * Users should not creates instance of this class directly. They should invoke one of
 * <code>{@linkplain ReferencingFactoryFinder}.getFooAuthorityFactory("EPSG")</code> methods instead.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class ThreadedEpsgFactory extends DeferredAuthorityFactory
        implements CRSAuthorityFactory, CSAuthorityFactory, DatumAuthorityFactory,
                   CoordinateOperationAuthorityFactory
{
    /**
     * The default JDBC {@linkplain DataSource data source} name in JNDI.
     * This is the name used if no other name were specified through the
     * {@link Hints#EPSG_DATA_SOURCE EPSG_DATA_SOURCE} hint.
     *
     * @see #createDataSource
     */
    public static final String DATASOURCE_NAME = "java:comp/env/jdbc/EPSG";

    /**
     * {@code true} if automatic registration of {@link #datasourceName} is allowed.
     * Set to {@code false} for now because the registration has not been correctly
     * tested in JEE environment.
     *
     * @todo Consider removing completly the code related to JNDI binding. In such
     *       case, this field and the {@link #registerInto} field would be removed.
     */
    private static final boolean ALLOW_REGISTRATION = false;

    /**
     * The default priority level for this factory.
     */
    static final int PRIORITY = MAXIMUM_PRIORITY - 10;

    /**
     * The factories to be given to the backing store.
     */
    private final ReferencingFactoryContainer factories;

    /**
     * The context where to register {@link #datasource}, or {@code null} if it should
     * not be registered. This is used only as a way to pass "hiden" return value between
     * {@link #createDataSource()} and {@link #createBackingStore()}.
     */
    private transient InitialContext registerInto;

    /**
     * The data source name. If it was not specified by the {@link Hints#EPSG_DATA_SOURCE
     * EPSG_DATA_SOURCE} hint, then this is the {@value #DATASOURCE_NAME} value.
     */
    private String datasourceName;

    /**
     * The data source, or {@code null} if the connection has not yet been etablished.
     */
    private DataSource datasource;

    /**
     * Constructs an authority factory using the default set of factories.
     */
    public ThreadedEpsgFactory() {
        this(null);
    }

    /**
     * Constructs an authority factory with the default priority.
     */
    public ThreadedEpsgFactory(final Hints userHints) {
        this(userHints, PRIORITY);
    }

    /**
     * Constructs an authority factory using a set of factories created from the specified hints.
     * This constructor recognizes the {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS},
     * {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM}
     * {@code FACTORY} hints, in addition of {@link Hints#EPSG_DATA_SOURCE EPSG_DATA_SOURCE}.
     *
     * @param userHints An optional set of hints, or {@code null} if none.
     * @param priority The priority for this factory, as a number between
     *        {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *        {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     */
    public ThreadedEpsgFactory(final Hints userHints, final int priority) {
        super(userHints, priority);

        Object hint = (userHints == null) ? null : userHints.get(Hints.EPSG_DATA_SOURCE);

        if (hint == null) {
          datasourceName = DATASOURCE_NAME;
          //datasourceName = GeoTools.fixName(DATASOURCE_NAME);
          hints.put(Hints.EPSG_DATA_SOURCE, datasourceName);
        } else if (hint instanceof String) {
            datasourceName = (String) hint;
            //datasourceName = GeoTools.fixName(datasourceName);
            hints.put(Hints.EPSG_DATA_SOURCE, datasourceName);
        } else if (hint instanceof Name) {
            Name name = (Name) hint;
            hints.put(Hints.EPSG_DATA_SOURCE, name);
            datasourceName = name.toString();
            //datasourceName = GeoTools.fixName(name.toString());
        } else if (hint instanceof DataSource) {
            datasource = (DataSource) hint;
            hints.put(Hints.EPSG_DATA_SOURCE, datasource);
            datasourceName = DATASOURCE_NAME;
            //datasourceName = GeoTools.fixName(DATASOURCE_NAME);
        }
        factories = ReferencingFactoryContainer.instance(userHints);
        setTimeout(30*60*1000L); // Close the connection after at least 30 minutes of inactivity.
    }

    /**
     * Returns the authority for this EPSG database.
     * This authority will contains the database version in the {@linkplain Citation#getEdition
     * edition} attribute, together with the {@linkplain Citation#getEditionDate edition date}.
     */
    @Override
    public Citation getAuthority() {
        final Citation authority = super.getAuthority();
        return (authority!=null) ? authority : Citations.EPSG;
    }

    /**
     * Returns the data source for the EPSG database. If no data source has been previously
     * {@linkplain #setDataSource set}, then this method invokes {@link #createDataSource}.
     * <strong>Note:</strong> invoking this method may force immediate connection to the EPSG
     * database.
     *
     * @return The data source.
     * @throws SQLException if the connection to the EPSG database failed.
     *
     * @see #setDataSource
     * @see #createDataSource
     */
    public final synchronized DataSource getDataSource() throws SQLException {
        if (datasource == null) {
            // Force the creation of the underlying backing store. It will invokes
            // (indirectly) createBackingStore, which will fetch the DataSource.
            if (!super.isAvailable()) {
                // Connection failed, but the exception is not available.
                datasource = null;
                throw new SQLException(Errors.format(ErrorKeys.NO_DATA_SOURCE));
            }
        }
        return datasource;
    }

    /**
     * Set the data source for the EPSG database. If an other EPSG database was already in use,
     * it will be disconnected. Users should not invoke this method on the factory returned by
     * {@link ReferencingFactoryFinder}, since it could have a system-wide effect.
     *
     * @param  datasource The new datasource.
     * @throws SQLException if an error occured.
     */
    public synchronized void setDataSource(final DataSource datasource) throws SQLException {
        if (datasource != this.datasource) {
            try {
                dispose();
            } catch (FactoryException exception) {
                final Throwable cause = exception.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                }
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                // Not really an SQL exception, but we should not reach this point anyway.
                final SQLException e = new SQLException(exception.getLocalizedMessage());
                e.initCause(exception); // TODO: inline when we will be allowed to target Java 6.
                throw e;
            }
            this.datasource = datasource;
        }
    }

    /**
     * Setup a data source for a connection to the EPSG database. This method is invoked by
     * {@link #getDataSource()} when no data source has been {@linkplain #setDataSource
     * explicitly set}. The default implementation searchs for a {@link DataSource} instance
     * binded to the {@link Hints#EPSG_DATA_SOURCE} name
     * (<code>{@value #DATASOURCE_NAME}</code> by default) using <cite>Java Naming and
     * Directory Interfaces</cite> (JNDI). If no data source were found, then this method
     * returns {@code null}.
     * <p>
     * Subclasses override this method in order to initialize a default data source when none were
     * found with JNDI. For example {@code plugin/epsg-access} defines a default data source using
     * the JDBC-ODBC bridge, which expects an "{@code EPSG}" database registered as an ODBC data
     * source (see the {@linkplain org.geotools.referencing.factory.epsg package javadoc} for
     * installation instructions). Example for a PostgreSQL data source:
     *
     * <blockquote><pre>
     * protected DataSource createDataSource() throws SQLException {
     *     DataSource candidate = super.createDataSource();
     *     if (candidate instanceof Jdbc3SimpleDataSource) {
     *         return candidate;
     *     }
     *     Jdbc3SimpleDataSource ds = new Jdbc3SimpleDataSource();
     *     ds.setServerName("localhost");
     *     ds.setDatabaseName("EPSG");
     *     ds.setUser("postgre");
     *     return ds;
     * }
     * </pre></blockquote>
     *
     * @return The EPSG data source, or {@code null} if none where found.
     * @throws SQLException if an error occured while creating the data source.
     */
    protected DataSource createDataSource() throws SQLException {
        InitialContext context = null;
        DataSource     source  = null;
        try {
            context = GeoTools.getInitialContext(new Hints(hints));
            source = (DataSource) context.lookup(datasourceName);
        } catch (IllegalArgumentException exception) {
         // Fall back on 'return null' below.
        } catch (NoInitialContextException exception) {
            // Fall back on 'return null' below.
        } catch (NamingException exception) {
            registerInto = context;
            // Fall back on 'return null' below.
        } 
        return source;
    }

    /**
     * Creates the backing store for the specified data source. This method usually returns
     * a new instance of {@link AccessDialectEpsgFactory} or {@link AnsiDialectEpsgFactory}.
     * Subclasses may override this method in order to returns an instance tuned for the
     * SQL syntax of the underlying database. Example for a PostgreSQL data source:
     *
     * <blockquote><pre>
     * protected AbstractAuthorityFactory createBackingStore(Hints hints) throws SQLException {
     *     return new AnsiDialectEpsgFactory(hints, getDataSource().getConnection());
     * }
     * </pre></blockquote>
     *
     * @param  hints A map of hints, including the low-level factories to use for CRS creation.
     *         This argument should be given unchanged to {@code DirectEpsgFactory} constructor.
     * @return The {@linkplain DirectEpsgFactory EPSG factory} using SQL queries appropriate
     *         for this data source.
     * @throws SQLException if connection to the database failed.
     */
    protected AbstractAuthorityFactory createBackingStore(final Hints hints) throws SQLException {
        final DataSource source = getDataSource();
        final Connection connection = source.getConnection();
        final String quote = connection.getMetaData().getIdentifierQuoteString();
        if (quote.equals("\"")) {
            /*
             * PostgreSQL quotes the indentifiers with "..." while MS-Access quotes the
             * identifiers with [...], so we use the identifier quote string metadata as
             * a way to distinguish the two cases. However I'm not sure that it is a robust
             * criterion. Subclasses should always override as a safety.
             */
            return new FactoryUsingAnsiSQL(hints, connection);
        }
        return new FactoryUsingSQL(hints, connection);
    }

    /**
     * Gets the EPSG factory implementation connected to the database. This method is invoked
     * automatically by {@link #createBackingStore()}.
     *
     * @return The connection to the EPSG database.
     * @throws FactoryException if no data source were found.
     * @throws SQLException if this method failed to etablish a connection.
     *
     * @todo Inline this method into {@link #createBackingStore()} after we removed the
     *       deprecated code.
     */
    private AbstractAuthorityFactory createBackingStore0() throws FactoryException, SQLException {
        assert Thread.holdsLock(this);
        final Hints sourceHints = new Hints(hints);
        sourceHints.putAll(factories.getImplementationHints());
        if (datasource != null) {
            return createBackingStore(sourceHints);
        }
        /*
         * Try to gets the DataSource from JNDI. In case of success, it will be tried
         * for a connection before any DataSource declared in META-INF/services/.
         */
        DataSource source;
        final InitialContext context;
        try {
            source  = createDataSource();
            context = registerInto;
        } finally {
            registerInto = null;
        }
        if (source == null) {
            throw new FactoryNotFoundException(Errors.format(ErrorKeys.NO_DATA_SOURCE));
        }
        final AbstractAuthorityFactory factory;
        try {
            datasource = source;
            factory = createBackingStore(sourceHints);
        } finally {
            datasource = null;
        }
        /*
         * We now have a working connection. If a naming directory is running but didn't contains
         * the "jdbc/EPSG" entry, add it now. In such case, a message is prepared and logged.
         */
        LogRecord record;
        if (ALLOW_REGISTRATION && context != null) {
            try {
                context.bind(datasourceName, source);
                record = Loggings.format(Level.INFO,
                        LoggingKeys.CREATED_DATASOURCE_ENTRY_$1, datasourceName);
            } catch (NamingException exception) {
                record = Loggings.format(Level.WARNING,
                        LoggingKeys.CANT_BIND_DATASOURCE_$1, datasourceName);
                record.setThrown(exception);
            }
            log(record);
        }
        this.datasource = source;  // Stores the data source only after success.
        return factory;
    }

    /**
     * Creates the backing store authority factory.
     *
     * @return The backing store to uses in {@code createXXX(...)} methods.
     * @throws FactoryException if the constructor failed to connect to the EPSG database.
     *         This exception usually has a {@link SQLException} as its cause.
     */
    protected AbstractAuthorityFactory createBackingStore() throws FactoryException {
        final AbstractAuthorityFactory factory;
        String product = '<' + Vocabulary.format(VocabularyKeys.UNKNOW) + '>';
        String url = product;
        try {
            factory = createBackingStore0();
            if (factory instanceof DirectEpsgFactory) {
                final DatabaseMetaData info = ((DirectEpsgFactory) factory).getConnection().getMetaData();
                product = info.getDatabaseProductName();
                url     = info.getURL();
            }
        } catch (SQLException exception) {
            throw new FactoryException(Errors.format(ErrorKeys.CANT_CONNECT_DATABASE_$1, "EPSG"),
                                       exception);
        }
        log(Loggings.format(Level.CONFIG, LoggingKeys.CONNECTED_EPSG_DATABASE_$2, url, product));
        if (factory instanceof DirectEpsgFactory) {
            ((DirectEpsgFactory) factory).buffered = this;
        }
        return factory;
    }

    /**
     * For internal use by {@link #createFactory()} and {@link #createBackingStore()} only.
     */
    private static void log(final LogRecord record) {
        record.setSourceClassName(ThreadedEpsgFactory.class.getName());
        record.setSourceMethodName("createBackingStore"); // The public caller.
        record.setLoggerName(LOGGER.getName());
        LOGGER.log(record);
    }

    /**
     * Returns {@code true} if the backing store can be disposed now. This method is invoked
     * automatically after the amount of time specified by {@link #setTimeout} if the factory
     * were not used during that time.
     *
     * @param backingStore The backing store in process of being disposed.
     */
    @Override
    protected boolean canDisposeBackingStore(final AbstractAuthorityFactory backingStore) {
        if (backingStore instanceof DirectEpsgFactory) {
            return ((DirectEpsgFactory) backingStore).canDispose();
        }
        return super.canDisposeBackingStore(backingStore);
    }

}
