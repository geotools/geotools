/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationAuthorityFactory;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.LoggingKeys;
import org.geotools.metadata.i18n.Loggings;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.DeferredAuthorityFactory;
import org.geotools.referencing.factory.FactoryNotFoundException;
import org.geotools.referencing.factory.PropertyCoordinateOperationAuthorityFactory;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Authority factory that holds user-defined {@linkplain CoordinateOperation Coordinate Operations}.
 *
 * <p>This factory can be used as a replacement for Coordinate Operations when there is no access to a complete EPSG
 * database. Or can be used to override the coordinate operations defined in EPSG if assigned a higher priority.
 *
 * <p>The Coordinate Operations are defined as <cite>Well Known Text</cite> math transforms (see
 * {@link PropertyCoordinateOperationAuthorityFactory} for format specification and examples).
 *
 * <p>Property file name is {@value #FILENAME}, and its possible locations are described {@linkplain #FILENAME here}. If
 * no property file is found, the factory won't be activated.
 *
 * <p>If an operation is not found in the properties file, this factory will delegate creation on a fallback factory.
 * The fallback factory is the next registered {@link CoordinateOperationAuthorityFactory} after this one in the
 * {@linkplain org.geotools.util.factory.AbstractFactory#priority priority} chain.
 *
 * @version $Id$
 * @author Oscar Fonts
 */
public class CoordinateOperationFactoryUsingWKT extends DeferredAuthorityFactory
        implements CoordinateOperationAuthorityFactory {
    /**
     * The authority. Will be created only when first needed.
     *
     * @see #getAuthority
     */
    protected Citation authority;

    /**
     * The default filename to read. The default {@code FactoryUsingWKT} implementation will search for the first
     * occurence of this file in the following places:
     *
     * <p>
     *
     * <ul>
     *   <li>In the directory specified by the {@value org.geotools.util.factory.GeoTools#CRS_AUTHORITY_EXTRA_DIRECTORY}
     *       system property.
     *   <li>In every {@code org/geotools/referencing/factory/espg} directories found on the classpath.
     * </ul>
     *
     * <p>
     *
     * @see #getDefinitionsURL
     */
    public static final String FILENAME = "epsg_operations.properties";

    /** Priority for this factory */
    public static final int PRIORITY = NORMAL_PRIORITY - 20;

    /** The factories to be given to the backing store. */
    protected final ReferencingFactoryContainer factories;

    /** Directory scanned for extra definitions. */
    protected final String directory;

    /** An alternate factory to be used when the primary one doesn't find an operation */
    protected CoordinateOperationAuthorityFactory fallbackAuthorityFactory = null;

    /** Just a flag not to search more than once */
    protected boolean fallbackAuthorityFactorySearched = false;

    /** Constructs an authority factory using the default set of factories. */
    public CoordinateOperationFactoryUsingWKT() {
        this(null, PRIORITY);
    }

    /** Constructs an authority factory using a set of factories created from the specified hints. */
    public CoordinateOperationFactoryUsingWKT(Hints userHints) {
        this(userHints, PRIORITY);
    }

    /** Constructs an authority factory using the specified hints and priority. */
    public CoordinateOperationFactoryUsingWKT(final Hints userHints, final int priority) {
        super(userHints, priority);
        factories = ReferencingFactoryContainer.instance(userHints);

        // Search for user CRS_AUTHORITY_EXTRA_DIRECTORY hint, or use system default value.
        Object directoryHint = null;
        if (userHints != null && userHints.get(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY) != null) {
            directoryHint = userHints.get(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY);
        } else if (Hints.getSystemDefault(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY) != null) {
            directoryHint = Hints.getSystemDefault(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY);
        }
        if (directoryHint != null) {
            directory = directoryHint.toString();
            hints.put(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY, directory);
        } else {
            directory = null;
        }
    }

    @Override
    public synchronized Citation getAuthority() {
        if (authority == null) {
            authority = Citations.EPSG;
        }
        return authority;
    }

    /**
     * Creates the backing store authority factory.
     *
     * @return The backing store to uses in {@code createXXX(...)} methods.
     * @throws FactoryNotFoundException if the {@code properties} file has not been found.
     * @throws FactoryException if the constructor failed to find or read the file. This exception usually has an
     *     {@link IOException} as its cause.
     */
    @Override
    protected AbstractAuthorityFactory createBackingStore() throws FactoryException {
        try {
            URL url = getDefinitionsURL();
            if (url == null) {
                throw new FactoryNotFoundException(MessageFormat.format(ErrorKeys.FILE_DOES_NOT_EXIST_$1, FILENAME));
            }
            final Iterator<? extends Identifier> ids =
                    getAuthority().getIdentifiers().iterator();
            final String authority = ids.hasNext() ? ids.next().getCode() : "EPSG";
            final LogRecord record =
                    Loggings.format(Level.CONFIG, LoggingKeys.USING_FILE_AS_FACTORY_$2, url.getPath(), authority);
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
            return new PropertyCoordinateOperationAuthorityFactory(factories, this.getAuthority(), url);
        } catch (IOException exception) {
            throw new FactoryException(MessageFormat.format(ErrorKeys.CANT_READ_$1, FILENAME), exception);
        }
    }

    /**
     * Returns the URL to the property file that contains Operation definitions. The default implementation performs the
     * following search path:
     *
     * <ul>
     *   <li>If a value is set for the {@value GeoTools#CRS_AUTHORITY_EXTRA_DIRECTORY} system property key, then the
     *       {@value #FILENAME} file will be searched in this directory.
     *   <li>If no value is set for the above-cited system property, or if no {@value #FILENAME} file was found in that
     *       directory, then the first {@value #FILENAME} file found in any
     *       {@code org/geotools/referencing/factory/epsg} directory on the classpath will be used.
     *   <li>If no file was found on the classpath neither, then this factory will be disabled.
     * </ul>
     *
     * @return The URL, or {@code null} if none.
     */
    protected URL getDefinitionsURL() {
        try {
            if (directory != null) {
                final File file = new File(directory, FILENAME);
                if (file.isFile()) {
                    return file.toURI().toURL(); // TODO
                }
            }
        } catch (SecurityException | MalformedURLException exception) {
            Logging.unexpectedException(LOGGER, exception);
        }
        return this.getClass().getResource(FILENAME);
    }

    /**
     * Creates operations from {@linkplain CoordinateReferenceSystem coordinate reference system} codes.
     *
     * <p>This method searches in the {@linkplain #FILENAME properties file} for operations.
     *
     * <p>If not found there, it will create operations from a fallback factory (see
     * {@link #getFallbackAuthorityFactory}).
     *
     * @param sourceCRS Coded value of source coordinate reference system.
     * @param targetCRS Coded value of target coordinate reference system.
     * @return The operations from {@code sourceCRS} to {@code targetCRS}.
     * @throws NoSuchAuthorityCodeException if a specified code was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    @Override
    @SuppressWarnings("UnsynchronizedOverridesSynchronized")
    public Set<CoordinateOperation> createFromCoordinateReferenceSystemCodes(String sourceCRS, String targetCRS)
            throws NoSuchAuthorityCodeException, FactoryException {
        Set<CoordinateOperation> coordops = super.createFromCoordinateReferenceSystemCodes(sourceCRS, targetCRS);
        if (coordops.isEmpty()) {
            // If not found, delegate to the fallback factory.
            CoordinateOperationAuthorityFactory fallback = getFallbackAuthorityFactory();
            if (fallback != null) {
                coordops = fallback.createFromCoordinateReferenceSystemCodes(sourceCRS, targetCRS);
            }
        }
        return coordops;
    }

    /**
     * Creates an operation from a single operation code.
     *
     * <p>This method searches in the {@linkplain #FILENAME properties file} for operations.
     *
     * <p>If not found there, it will create operations from a fallback factory (see
     * {@link #getFallbackAuthorityFactory}).
     *
     * @param code Coded value for operation.
     * @return The operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws NoSuchAuthorityCodeException if a specified code was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    @Override
    @SuppressWarnings("UnsynchronizedOverridesSynchronized")
    public CoordinateOperation createCoordinateOperation(String code)
            throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateOperation coordop = super.createCoordinateOperation(code);
        if (coordop == null) {
            CoordinateOperationAuthorityFactory fallback = getFallbackAuthorityFactory();
            if (fallback != null) {
                coordop = fallback.createCoordinateOperation(code);
            }
        }
        return coordop;
    }

    /**
     * Gets the next available {@link CoordinateOperationAuthorityFactory} in the priority list.
     *
     * @return the alternative CoordinateOperationAuthorityFactory.
     * @throws NoSuchAuthorityCodeException if a specified code was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    protected CoordinateOperationAuthorityFactory getFallbackAuthorityFactory()
            throws NoSuchAuthorityCodeException, FactoryException {

        if (!fallbackAuthorityFactorySearched) { // Search once
            CoordinateOperationAuthorityFactory candidate = null;

            // These hints are to prevent infinite recursion when called
            // from OrderedAxisAuthorityFactory. See "noForce(Hints)"
            // from AuthorityBackedFactory.
            // See also: http://jira.codehaus.org/browse/GEOT-1161
            Hints h = new Hints();
            h.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE);
            h.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS, Boolean.FALSE);
            h.put(Hints.FORCE_STANDARD_AXIS_UNITS, Boolean.FALSE);

            Set<CoordinateOperationAuthorityFactory> factories =
                    ReferencingFactoryFinder.getCoordinateOperationAuthorityFactories(h);
            Iterator<CoordinateOperationAuthorityFactory> it = factories.iterator();

            // Skip factories with higher priority than me.
            while (it.hasNext()) {
                candidate = it.next();
                if (candidate == this) {
                    break;
                }
            }

            // Get the next one for this same authority
            while (it.hasNext()) {
                candidate = it.next();
                if (!(candidate instanceof CoordinateOperationFactoryUsingWKT)
                        && candidate
                                .getAuthority()
                                .getTitle()
                                .equals(this.getAuthority().getTitle())) {
                    fallbackAuthorityFactory = candidate;
                    break;
                }
            }
            fallbackAuthorityFactorySearched = true;
        }

        return fallbackAuthorityFactory;
    }
}
