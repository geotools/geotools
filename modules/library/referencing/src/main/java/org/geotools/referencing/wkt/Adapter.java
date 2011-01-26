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

// Geotools dependencies
import org.geotools.util.UnsupportedImplementationException;


/**
 * Adapter for implementations which doesn't extends {@link Formattable}. This includes
 * especially {@link org.geotools.referencing.operation.transform.AffineTransform2D}.
 * This method looks for a {@code toWKT()} method using reflection.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class Adapter extends Formattable {
    /**
     * The object to format as WKT.
     */
    private final Object object;

    /**
     * Create an adapter for the specified object.
     */
    public Adapter(final Object object) {
        this.object = object;
    }

    /**
     * Try to format the wrapped object as WKT. If the adapter fails to find a way to format
     * the object as WKT, then an {@link UnsupportedImplementationException} is thrown.
     */
    protected String formatWKT(final Formatter formatter) {
        if (object instanceof org.geotools.resources.Formattable) {
            return ((org.geotools.resources.Formattable) object).formatWKT(formatter);
        }
        final Class classe = object.getClass();
        final String wkt;
        try {
            wkt = (String) classe.getMethod("toWKT", (Class[])null).invoke(object, (Object[])null);
        } catch (Exception cause) {
            final UnsupportedImplementationException exception;
            exception = new UnsupportedImplementationException(classe);
            exception.initCause(cause);
            throw exception;
        }
        // TODO: Not yet implemented. We should insert the WKT in the formatter
        //       as pre-formatted text, and returns {@code null}.
        throw new UnsupportedImplementationException(classe);
    }
}
