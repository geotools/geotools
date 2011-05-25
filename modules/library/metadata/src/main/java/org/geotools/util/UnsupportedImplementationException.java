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
package org.geotools.util;

import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Throws when an operation can't use arbitrary implementation of an interface, and
 * a given instance doesn't meet the requirement. For example this exception may be
 * thrown when an operation requires a Geotools implementation of a
 * <A HREF="http://geoapi.sourceforge.net">GeoAPI</A> interface.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class UnsupportedImplementationException extends UnsupportedOperationException {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -649050339146622730L;

    /**
     * Constructs an exception with the specified detail message.
     *
     * @param message The detail message.
     */
    public UnsupportedImplementationException(final String message) {
        super(message);
    }

    /**
     * Constructs an exception with an error message formatted for the specified class.
     *
     * @param classe The unexpected implementation class.
     */
    public UnsupportedImplementationException(final Class<?> classe) {
        super(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, classe));
    }

    /**
     * Constructs an exception with an error message formatted for the specified class
     * and a cause.
     *
     * @param classe The unexpected implementation class.
     * @param cause The cause for the exception.
     */
    public UnsupportedImplementationException(final Class<?>  classe, final Exception cause) {
        super(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, classe), cause);
    }
}
