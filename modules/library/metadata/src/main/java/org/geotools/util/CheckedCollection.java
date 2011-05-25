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
package org.geotools.util;

import java.util.Collection;


/**
 * Collection that ensures that all elements are assignable to a given base type.
 * The base {@linkplain #getElement type} is usually specified at collection
 * construction time.
 *
 * @param <E> The type of elements in the collection.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public interface CheckedCollection<E> extends Collection<E> {
    /**
     * Returns the element type.
     *
     * @return The element type.
     */
    Class<E> getElementType();
}
