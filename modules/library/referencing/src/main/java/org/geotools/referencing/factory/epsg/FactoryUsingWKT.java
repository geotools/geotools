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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.Map;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.net.MalformedURLException;
import java.net.URL;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.geotools.factory.Hints;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.DeferredAuthorityFactory;
import org.geotools.referencing.factory.FactoryNotFoundException;
import org.geotools.referencing.factory.PropertyAuthorityFactory;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.io.TableWriter;
import org.geotools.io.IndentedLineWriter;
import org.geotools.resources.Arguments;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.logging.Logging;


/**
 * Authority factory for {@linkplain CoordinateReferenceSystem Coordinate Reference Systems}
 * beyong the one defined in the EPSG database. This factory is used as a fallback when a
 * requested code is not found in the EPSG database, or when there is no connection at all
 * to the EPSG database. The additional CRS are defined as <cite>Well Known Text</cite> in
 * a property file located by default in the {@code org.geotools.referencing.factory.epsg}
 * package, and whose name should be {@value #FILENAME}. If no property file is found, the
 * factory won't be activated. The property file can also be located in a custom directory;
 * See {@link #getDefinitionsURL()} for more details.
 * <p>
 * This factory can also be used to provide custom extensions or overrides to a main EPSG factory.
 * In order to provide a custom extension file, override the {@link #getDefinitionsURL()} method.
 * In order to make the factory be an override, change the default priority by using the
 * two arguments constructor (this factory defaults to {@link ThreadedEpsgFactory#PRIORITY} - 10,
 * so it's used as an extension).
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Jody Garnett
 * @author Rueben Schulz
 * @author Andrea Aime
 */
public class FactoryUsingWKT extends DeferredAuthorityFactory implements CRSAuthorityFactory {
    /**
     * The authority. Will be created only when first needed.
     *
     * @see #getAuthority
     */
    private Citation authority;

    /**
     * The default filename to read. The default {@code FactoryUsingWKT} implementation will
     * search for the first occurence of this file in the following places:
     * <p>
     * <ul>
     *   <li>In the directory specified by the
     *       {@value org.geotools.factory.GeoTools#CRS_DIRECTORY_KEY} system property.</li>
     *   <li>In every {@code org/geotools/referencing/factory/espg} directories found on the
     *       classpath.</li>
     * </ul>
     * <p>
     * The filename part before the extension ({@code "epsg"}) denotes the authority namespace
     * where to register the content of this file. The user-directory given by the system property
     * may contains other property files for other authorities, like {@code "esri.properties"},
     * but those additional authorities are not handled by the default {@code FactoryUsingWKT}
     * class.
     *
     * @see #getDefinitionsURL
     */
    public static final String FILENAME = "epsg.properties";

    /**
     * The factories to be given to the backing store.
     */
    private final ReferencingFactoryContainer factories;

    /**
     * Default priority for this factory.
     *
     * @since 2.4
     * @deprecated We will try to replace the priority mechanism by a better
     *             one in a future Geotools version.
     */
    protected static final int DEFAULT_PRIORITY = ThreadedEpsgFactory.PRIORITY - 10;

    /**
     * Directory scanned for extra definitions.
     */
    private final File directory;

    /**
     * Constructs an authority factory using the default set of factories.
     */
    public FactoryUsingWKT() {
        this(null);
    }

    /**
     * Constructs an authority factory using a set of factories created from the specified hints.
     * This constructor recognizes the {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS},
     * {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM}
     * {@code FACTORY} hints.
     */
    public FactoryUsingWKT(final Hints userHints) {
        this(userHints, DEFAULT_PRIORITY);
    }

    /**
     * Constructs an authority factory using the specified hints and priority.
     */
    protected FactoryUsingWKT(final Hints userHints, final int priority) {
        super(userHints, priority);
        factories = ReferencingFactoryContainer.instance(userHints);
        Object hint = null;
        if (userHints != null) {
            hint = userHints.get(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY);
        }
        if (hint instanceof File) {
            directory = (File) hint;
        } else if (hint instanceof String) {
            directory = new File((String) hint);
        } else {
            directory = null;
        }
        hints.put(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY, directory);
        // Disposes the cached property file after at least 15 minutes of inactivity.
        setTimeout(15 * 60 * 1000L);
    }

    /**
     * Returns the authority. The default implementation returns the first citation returned
     * by {@link #getAuthorities()}, with the addition of identifiers from all additional
     * authorities returned by the above method.
     *
     * @see #getAuthorities
     */
    @Override
    public synchronized Citation getAuthority() {
        // No need to synchronize; this is not a big deal if we create this object twice.
        if (authority == null) {
            final Citation[] authorities = getAuthorities();
            switch (authorities.length) {
                case 0: authority = Citations.EPSG; break;
                case 1: authority = authorities[0]; break;
                default: {
                    final CitationImpl c = new CitationImpl(authorities[0]);
                    final Collection<Identifier> identifiers = c.getIdentifiers();
                    for (int i=1; i<authorities.length; i++) {
                        identifiers.addAll(authorities[i].getIdentifiers());
                    }
                    c.freeze();
                    authority = c;
                    break;
                }
            }
        }
        return authority;
    }

    /**
     * Returns the set of authorities to use as {@linkplain CoordinateReferenceSystem#getIdentifiers
     * identifiers} for the CRS to be created. This set is given to the
     * {@linkplain PropertyAuthorityFactory#PropertyAuthorityFactory(ReferencingFactoryContainer,
     * Citation[], URL) properties-backed factory constructor}.
     * <p>
     * The default implementation returns a singleton containing only {@linkplain Citations#EPSG
     * EPSG}. Subclasses should override this method in order to enumerate all relevant authorities,
     * with {@linkplain Citations#EPSG EPSG} in last position. For example {@link EsriExtension}
     * returns {{@linkplain Citations#ESRI ESRI}, {@linkplain Citations#EPSG EPSG}}.
     *
     * @since 2.4
     */
    protected Citation[] getAuthorities() {
        return new Citation[] {
            Citations.EPSG
        };
    }

    /**
     * Returns the URL to the property file that contains CRS definitions.
     * The default implementation performs the following search path:
     * <ul>
     *   <li>If a value is set for the {@value #CRS_DIRECTORY_KEY} system property key,
     *       then the {@value #FILENAME} file will be searched in this directory.</li>
     *   <li>If no value is set for the above-cited system property, or if no {@value #FILENAME}
     *       file was found in that directory, then the first {@value #FILENAME} file found in
     *       any {@code org/geotools/referencing/factory/epsg} directory on the classpath will
     *       be used.</li>
     *   <li>If no file was found on the classpath neither, then this factory will be disabled.</li>
     * </ul>
     *
     * @return The URL, or {@code null} if none.
     */
    protected URL getDefinitionsURL() {
        try {
            if (directory != null) {
                final File file = new File(directory, FILENAME);
                if (file.isFile()) {
                    return file.toURI().toURL();
                }
            }
        } catch (SecurityException exception) {
            Logging.unexpectedException(LOGGER, exception);
        } catch (MalformedURLException exception) {
            Logging.unexpectedException(LOGGER, exception);
        }
        return FactoryUsingWKT.class.getResource(FILENAME);
    }

    /**
     * Creates the backing store authority factory.
     *
     * @return The backing store to uses in {@code createXXX(...)} methods.
     * @throws FactoryNotFoundException if the no {@code epsg.properties} file has been found.
     * @throws FactoryException if the constructor failed to find or read the file.
     *         This exception usually has an {@link IOException} as its cause.
     */
    protected AbstractAuthorityFactory createBackingStore() throws FactoryException {
        try {
            URL url = getDefinitionsURL();
            if (url == null) {
                throw new FactoryNotFoundException(Errors.format(
                        ErrorKeys.FILE_DOES_NOT_EXIST_$1, FILENAME));
            }
            final Iterator<? extends Identifier> ids = getAuthority().getIdentifiers().iterator();
            final String authority = ids.hasNext() ? ids.next().getCode() : "EPSG";
            final LogRecord record = Loggings.format(Level.CONFIG,
                    LoggingKeys.USING_FILE_AS_FACTORY_$2, url.getPath(), authority);
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
            return new PropertyAuthorityFactory(factories, getAuthorities(), url);
        } catch (IOException exception) {
            throw new FactoryException(Errors.format(ErrorKeys.CANT_READ_$1, FILENAME), exception);
        }
    }

    /**
     * Returns a factory of the given type.
     */
    private static final <T extends AbstractAuthorityFactory> T getFactory(final Class<T> type) {
        return type.cast(ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG",
                new Hints(Hints.CRS_AUTHORITY_FACTORY, type)));
    }

    /**
     * Prints a list of codes that duplicate the ones provided by {@link ThreadedEpsgFactory}.
     * This is used for implementation of {@linkplain #main main method} in order to check
     * the content of the {@value #FILENAME} file (or whatever property file used as backing
     * store for this factory) from the command line.
     *
     * @param  out The writer where to print the report.
     * @return The set of duplicated codes.
     * @throws FactoryException if an error occured.
     *
     * @since 2.4
     */
    protected Set reportDuplicatedCodes(final PrintWriter out) throws FactoryException {
        final AbstractAuthorityFactory sqlFactory = getFactory(ThreadedEpsgFactory.class);
        final Vocabulary resources = Vocabulary.getResources(null);
        out.println(resources.getLabel(VocabularyKeys.COMPARE_WITH));
        try {
            final IndentedLineWriter w = new IndentedLineWriter(out);
            w.setIndentation(4);
            w.write(sqlFactory.getBackingStoreDescription());
            w.flush();
        } catch (IOException e) {
            // Should never happen, since we are writting to a PrintWriter.
            throw new AssertionError(e);
        }
        out.println();
        final Set<String> wktCodes   = this.      getAuthorityCodes(IdentifiedObject.class);
        final Set<String> sqlCodes   = sqlFactory.getAuthorityCodes(IdentifiedObject.class);
        final Set<String> duplicated = new TreeSet<String>();
        for (String code : wktCodes) {
            code = code.trim();
            if (sqlCodes.contains(code)) {
                duplicated.add(code);
                /*
                 * Note: we don't use wktCodes.retainsAll(sqlCode) because the Set implementations
                 *       are usually not the standard ones, but rather some implementations backed
                 *       by a connection to the resources of the underlying factory. We also close
                 *       the connection after this loop for the same reason.  In addition, we take
                 *       this opportunity for sorting the codes.
                 */
            }
        }
        if (duplicated.isEmpty()) {
            out.println(resources.getString(VocabularyKeys.NO_DUPLICATION_FOUND));
        } else {
            for (final String code : duplicated) {
                out.print(resources.getLabel(VocabularyKeys.DUPLICATED_VALUE));
                out.println(code);
            }
        }
        return duplicated;
    }

    /**
     * Prints a list of CRS that can't be instantiated. This is used for implementation of
     * {@linkplain #main main method} in order to check the content of the {@value #FILENAME}
     * file (or whatever property file used as backing store for this factory) from the command
     * line.
     *
     * @param  out The writer where to print the report.
     * @return The set of codes that can't be instantiated.
     * @throws FactoryException if an error occured while
     *         {@linkplain #getAuthorityCodes fetching authority codes}.
     *
     * @since 2.4
     */
    protected Set reportInstantiationFailures(final PrintWriter out) throws FactoryException {
        final Set<String> codes = getAuthorityCodes(CoordinateReferenceSystem.class);
        final Map<String,String> failures = new TreeMap<String,String>();
        for (final String code : codes) {
            try {
                createCoordinateReferenceSystem(code);
            } catch (FactoryException exception) {
                failures.put(code, exception.getLocalizedMessage());
            }
        }
        if (!failures.isEmpty()) {
            final TableWriter writer = new TableWriter(out, " ");
            for (final Map.Entry<String,String> entry : failures.entrySet()) {
                writer.write(entry.getKey());
                writer.write(':');
                writer.nextColumn();
                writer.write(entry.getValue());
                writer.nextLine();
            }
            try {
                writer.flush();
            } catch (IOException e) {
                // Should not happen, since we are writting to a PrintWriter
                throw new AssertionError(e);
            }
        }
        return failures.keySet();
    }

    /**
     * Prints a list of codes that duplicate the ones provided in the {@link ThreadedEpsgFactory}.
     * The factory tested is the one registered in {@link ReferencingFactoryFinder}. By default,
     * this is this {@code FactoryUsingWKT} class backed by the {@value #FILENAME} property file.
     * This method can be invoked from the command line in order to check the content of the
     * property file. Valid arguments are:
     * <p>
     * <table>
     *   <tr><td>{@code -test}</td><td>Try to instantiate all CRS and reports any failure
     *       to do so.</td></tr>
     *   <tr><td>{@code -duplicated}</td><td>List all codes from the WKT factory that are
     *       duplicating a code from the SQL factory.</td></tr>
     * </table>
     *
     * @param  args Command line arguments.
     * @throws FactoryException if an error occured.
     *
     * @since 2.4
     */
    public static void main(final String[] args) throws FactoryException {
        main(args, FactoryUsingWKT.class);
    }

    /**
     * Implementation of the {@link #main} method, shared by subclasses.
     */
    static void main(String[] args, final Class<? extends FactoryUsingWKT> type)
            throws FactoryException
    {
        final Arguments arguments = new Arguments(args);
        Locale.setDefault(arguments.locale);
        final boolean duplicated  = arguments.getFlag("-duplicated");
        final boolean instantiate = arguments.getFlag("-test");
        args = arguments.getRemainingArguments(0);
        final FactoryUsingWKT factory = getFactory(type);
        if (duplicated) {
            factory.reportDuplicatedCodes(arguments.out);
        }
        if (instantiate) {
            factory.reportInstantiationFailures(arguments.out);
        }
        factory.dispose();
    }
}
