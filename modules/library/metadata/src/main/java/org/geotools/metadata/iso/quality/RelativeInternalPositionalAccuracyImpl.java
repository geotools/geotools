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

import org.opengis.metadata.quality.RelativeInternalPositionalAccuracy;


/**
 * Closeness of the relative positions of features in the scope to their respective
 * relative positions accepted as or being true.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class RelativeInternalPositionalAccuracyImpl extends PositionalAccuracyImpl
        implements RelativeInternalPositionalAccuracy
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8216355887797408106L;

    /**
     * Constructs an initially empty relative internal positional accuracy.
     */
    public RelativeInternalPositionalAccuracyImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public RelativeInternalPositionalAccuracyImpl(final RelativeInternalPositionalAccuracy source) {
        super(source);
    }
}
