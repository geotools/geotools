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
package org.geotools.referencing.wkt;

// J2SE dependencies
import java.lang.reflect.Modifier;

// Geotools dependencies
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * Thrown by {@link Formattable#toWKT} when an object can't be formatted as WKT.
 * A formatting may fails because an object is too complex for the WKT format
 * capability (for example an {@linkplain org.geotools.referencing.crs.DefaultEngineeringCRS
 * engineering CRS} with different unit for each axis), or because only some specific
 * implementations can be formatted as WKT.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see Formatter#setInvalidWKT
 */
public class UnformattableObjectException extends UnsupportedOperationException {
    /**
     * The type of the object that can't be formatted.
     */
    private final Class unformattable;

    /**
     * Constructs an exception with the specified detail message.
     *
     * @param message The detail message.
     *
     * @deprecated Replaced by {@link #UnformattableObjectException(String, Class)}.
     */
    public UnformattableObjectException(final String message) {
        super(message);
        unformattable = Object.class;
    }

    /**
     * Constructs an exception with the specified detail message.
     *
     * @param message The detail message. If {@code null}, a default message will be created.
     * @param unformattable The type of the object that can't be formatted.
     *
     * @since 2.4
     */
    public UnformattableObjectException(final String message, final Class unformattable) {
        super(message);
        this.unformattable = unformattable;
    }

    /**
     * Returns the type of the object that can't be formatted. This is often an OpenGIS
     * interface rather than the implementation class. For example if a engineering CRS
     * uses different unit for each axis, then this method may return
     * <code>{@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem}.class</code>.
     * It doesn't mean that no CRS can be formatted; only that a particular instance of it
     * can't. Other possible classes are {@link org.opengis.referencing.datum.ImageDatum},
     * {@link org.opengis.referencing.crs.ProjectedCRS}, <cite>etc</cite>.
     *
     * @since 2.4
     */
    public Class getUnformattableClass() {
        return unformattable;
    }

    /**
     * Returns the detail message. A default message is formatted
     * if none was specified at construction time.
     */
    public String getMessage() {
        String message = super.getMessage();
        if (message == null) {
            Class c = unformattable;
            while (!Modifier.isPublic(c.getModifiers())) {
                final Class candidate = c.getSuperclass();
                if (candidate == null) {
                    break;
                }
                c = candidate;
            }
            return Errors.format(ErrorKeys.INVALID_WKT_FORMAT_$1, c);
        }
        return message;
    }
}
