/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.quality;

import org.opengis.metadata.quality.GriddedDataPositionalAccuracy;


/**
 * Closeness of gridded data position values to values accepted as or being true.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class GriddedDataPositionalAccuracyImpl extends PositionalAccuracyImpl
        implements GriddedDataPositionalAccuracy
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -3852184823135498458L;

    /**
     * Constructs an initially empty gridded data positional acccuracy.
     */
    public GriddedDataPositionalAccuracyImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public GriddedDataPositionalAccuracyImpl(final GriddedDataPositionalAccuracy source) {
        super(source);
    }
}
