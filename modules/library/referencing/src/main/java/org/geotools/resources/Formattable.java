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
 */
package org.geotools.resources;

import org.geotools.referencing.wkt.Formatter;


/**
 * Interface for object that can be formatted as
 * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
 * Known Text</cite> (WKT), but can't extends {@link org.geotools.referencing.wkt.Formattable}. This
 * interface is especially used for {@code AffineTransform2D} implementation. This interface is not
 * public because the {@code formatWKT(Formatter)} method usually has a protected access.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @todo Consider renaming {@link org.geotools.referencing.wkt.Formattable} as
 *       {@code AbstractFormattable} and move this interface in the wkt package.
 */
public interface Formattable {
    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name (e.g. "GEOGCS").
     *
     * @see org.geotools.referencing.wkt.Formattable#formatWKT(Formatter)
     */
    String formatWKT(Formatter formatter);
}
