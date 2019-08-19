/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import org.opengis.filter.expression.Function;

/**
 * Apply color replacement to an external graphic.
 *
 * <p>Can be used to indicate the background color to make transparent; or to swap colors around as
 * needed.
 */
public interface ColorReplacement extends org.opengis.style.ColorReplacement {

    /** Function providing recoding of values. */
    Function getRecoding();

    /** @param function Recoding function to use */
    void setRecoding(Function function);
}
