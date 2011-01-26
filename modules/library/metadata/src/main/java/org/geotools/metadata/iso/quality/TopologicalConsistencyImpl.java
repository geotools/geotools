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

import org.opengis.metadata.quality.TopologicalConsistency;


/**
 * Correctness of the explicitly encoded topological characteristics of the dataset as
 * described by the scope.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class TopologicalConsistencyImpl extends LogicalConsistencyImpl
        implements TopologicalConsistency
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -255014076679068944L;

    /**
     * Constructs an initially empty topological consistency.
     */
    public TopologicalConsistencyImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public TopologicalConsistencyImpl(final TopologicalConsistency source) {
        super(source);
    }
}
