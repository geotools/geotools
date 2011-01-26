/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio;

import java.io.File;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.extent.GeographicBoundingBox;

/**
 * A class storing utilities methods and constants.
 * 
 * @author Simone Giannecchini, GeoSolutions
 */
class GridCoverageUtilities {

    // ////////////////////////////////////////////////////////////////////////
    //    
    // Constant fields
    //    
    // ////////////////////////////////////////////////////////////////////////
    /** The default world file extension */
    final static String DEFAULT_WORLDFILE_EXT = ".wld";

    /** The system-dependent default name-separator character. */
    final static char SEPARATOR = File.separatorChar;

    final static String IMAGEREAD = "ImageRead";
    
    final static String IMAGEREADMT = "ImageReadMT";

    /**
     * Utility class.
     */
    private GridCoverageUtilities() {

    }

    /**
     * Builds a {@link ReferencedEnvelope} from a {@link GeographicBoundingBox}.
     * This is useful in order to have an implementation of {@link BoundingBox}
     * from a {@link GeographicBoundingBox} which strangely does implement
     * {@link GeographicBoundingBox}.
     * 
     * @param geographicBBox
     *                the {@link GeographicBoundingBox} to convert.
     * @return an instance of {@link ReferencedEnvelope}.
     */
    public static ReferencedEnvelope getReferencedEnvelopeFromGeographicBoundingBox(
            final GeographicBoundingBox geographicBBox) {
        ensureNonNull("GeographicBoundingBox", geographicBBox);
        return new ReferencedEnvelope(geographicBBox.getEastBoundLongitude(),
                geographicBBox.getWestBoundLongitude(), geographicBBox
                        .getSouthBoundLatitude(), geographicBBox
                        .getNorthBoundLatitude(), DefaultGeographicCRS.WGS84);
    }

    /**
     * Makes sure that an argument is non-null.
     * 
     * @param name
     *                Argument name.
     * @param object
     *                User argument.
     * @throws IllegalArgumentException
     *                 if {@code object} is null.
     */
    private static void ensureNonNull(final String name, final Object object)
            throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }
}
