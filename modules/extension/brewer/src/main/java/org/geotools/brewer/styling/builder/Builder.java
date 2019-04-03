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
 *
 */

package org.geotools.brewer.styling.builder;

/**
 * Builder interface used to impose consistency on Builder implementations.
 *
 * @param <T> class of object under construction
 */
public interface Builder<T> {
    /**
     * Configure the Builder to produce <code>null</code>.
     *
     * <p>This method allows a Builder to be used as a placeholder; in its unset state the build()
     * method will produce <code>null</code>. If any of the builder methods are used the builder
     * will produce a result.
     *
     * @return Builder configured to build <code>null</code>
     */
    Builder<T> unset();
    /**
     * Configure the Builder to produce a default result.
     *
     * @return Builder configured to produce a default result.
     */
    Builder<T> reset();
    /**
     * Configure the Builder to produce a copy of the provided original.
     *
     * @param original Original, if null this will behave the same as unset()
     * @return Builder configured to produce the provided original
     */
    Builder<T> reset(T original);

    /**
     * Created object, may be null if unset()
     *
     * @return Created object may be null if unset()
     */
    T build();
}
