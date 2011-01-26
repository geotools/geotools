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

import org.opengis.metadata.quality.LogicalConsistency;


/**
 * Degree of adherence to logical rules of data structure, attribution and relationships (data
 * structure can be conceptual, logical or physical).
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class LogicalConsistencyImpl extends ElementImpl implements LogicalConsistency {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -2470752876057569947L;

    /**
     * Constructs an initially empty logical consistency.
     */
    public LogicalConsistencyImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public LogicalConsistencyImpl(final LogicalConsistency source) {
        super(source);
    }
}
