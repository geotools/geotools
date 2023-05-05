/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.iau;

import java.net.URL;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.factory.epsg.FactoryUsingWKT;
import org.geotools.util.factory.Hints;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Extends the EPSG database with {@linkplain CoordinateReferenceSystem Coordinate Reference
 * Systems} defined by IAU. Those CRS will be registered both in {@code "IAU"} and {@code
 * "IAU_2015"} name space.
 */
public class IAUAuthorityFactory extends FactoryUsingWKT {
    /**
     * The default filename to read. This file will be searched in the {@code
     * org/geotools/referencing/factory/espg} directory in the classpath or in a JAR file.
     *
     * @see #getDefinitionsURL
     */
    public static final String FILENAME = "iau.properties";

    /** Constructs an authority factory using the default set of factories. */
    public IAUAuthorityFactory() {
        this(null);
    }

    /**
     * Constructs an authority factory using a set of factories created from the specified hints.
     * This constructor recognizes the {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS},
     * {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM}
     * {@code FACTORY} hints.
     */
    public IAUAuthorityFactory(final Hints hints) {
        super(hints, DEFAULT_PRIORITY - 5);
    }

    /**
     * Returns the set of authorities to use as identifiers for the CRS to be created. The default
     * implementation returns the {@linkplain Citations#IAU} authority.
     */
    @Override
    protected Citation[] getAuthorities() {
        return new Citation[] {Citations.IAU};
    }

    /**
     * Returns the URL to the property file that contains CRS definitions. The default
     * implementation returns the URL to the {@value #FILENAME} file.
     *
     * @return The URL, or {@code null} if none.
     */
    @Override
    protected URL getDefinitionsURL() {
        return IAUAuthorityFactory.class.getResource(FILENAME);
    }
}
