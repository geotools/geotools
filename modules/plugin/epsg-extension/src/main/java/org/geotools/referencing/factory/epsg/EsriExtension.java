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

import java.net.URL;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.geotools.factory.Hints;
import org.geotools.metadata.iso.citation.Citations;


/**
 * Extends the EPSG database with {@linkplain CoordinateReferenceSystem Coordinate Reference Systems}
 * defined by ESRI. Those CRS will be registered both in {@code "ESRI"} and {@code "EPSG"} name space.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class EsriExtension extends FactoryUsingWKT {
    /**
     * The default filename to read. This file will be searched in the
     * {@code org/geotools/referencing/factory/espg} directory in the
     * classpath or in a JAR file.
     *
     * @see #getDefinitionsURL
     */
    public static final String FILENAME = "esri.properties";

    /**
     * Constructs an authority factory using the default set of factories.
     */
    public EsriExtension() {
        this(null);
    }

    /**
     * Constructs an authority factory using a set of factories created from the specified hints.
     * This constructor recognizes the {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS},
     * {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM}
     * {@code FACTORY} hints.
     */
    public EsriExtension(final Hints hints) {
        super(hints, DEFAULT_PRIORITY - 5);
    }

    /**
     * Returns the set of authorities to use as identifiers for the CRS to be created.
     * The default implementation returns {@linkplain Citations#ESRI ESRI} and
     * {@linkplain Citations#EPSG EPSG} authorities.
     */
    @Override
    protected Citation[] getAuthorities() {
        return new Citation[] {
            Citations.ESRI,
            Citations.EPSG
        };
    }

    /**
     * Returns the URL to the property file that contains CRS definitions.
     * The default implementation returns the URL to the {@value #FILENAME} file.
     *
     * @return The URL, or {@code null} if none.
     */
    @Override
    protected URL getDefinitionsURL() {
        return EsriExtension.class.getResource(FILENAME);
    }

    /**
     * Prints a list of codes that duplicate the ones provided in the {@link DefaultFactory}.
     * The factory tested is the one registered in {@link ReferencingFactoryFinder}.  By default, this
     * is this {@code EsriExtension} class backed by the {@value #FILENAME} property file.
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
     */
    public static void main(final String[] args) throws FactoryException {
        main(args, EsriExtension.class);
    }
}
