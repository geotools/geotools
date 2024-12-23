/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.proj;

/**
 * Simple interface for objects that can be formatted as PROJ String (using the provided {@link PROJFormatter} instance
 */
public interface PROJFormattable {

    /**
     * Format the inner part of a PROJFormattable object.
     *
     * <p>This method is automatically invoked by {@link PROJFormatter#append(PROJFormattable)}.
     *
     * <p>For example for a element ({@link org.geotools.referencing.operation.DefaultOperationMethod}) of type
     * Projection, the formatter will invoke this method to prepend the "+proj=" String for completing the PROJ String
     * before appending the Projection Name (e.g. lcc for a Lambert Conformal Conic)
     *
     * @param formatter The PROJFormatter to use.
     * @return The proj String of the PROJ element type if any. (e.g. +ellps= for named ellipsoids, +datum= for named
     *     datums).
     */
    String formatPROJ(PROJFormatter formatter);
}
