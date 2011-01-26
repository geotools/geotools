/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;


/**
 * @deprecated Moved to {@link org.geotools.referencing.operation.builder} package.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Remi Eve
 * @author Martin Desruisseaux (IRD)
 * @author Alessio Fabiani
 */
public class LocalizationGrid extends org.geotools.referencing.operation.builder.LocalizationGrid {
    /**
     * Constructs an initially empty localization grid. All "real worlds"
     * coordinates are initially set to {@code (NaN,NaN)}.
     *
     * @param width  Number of grid's columns.
     * @param height Number of grid's rows.
     */
    public LocalizationGrid(final int width, final int height) {
        super(width, height);
    }
}
