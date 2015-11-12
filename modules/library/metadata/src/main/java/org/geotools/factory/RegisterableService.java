/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.factory;

/**
 * An optional interface that may be provided by service provider objects that will be registered
 * with a <code>FactoryRegistry</code>. If this interface is present, notification of registration
 * and deregistration will be performed.
 *
 * @since 15.0
 * @see FactoryRegistry
 */
public interface RegisterableService {

    /**
     * Called when an object implementing this interface is added to the given
     * <code>category</code> of the given <code>registry</code>. The object may already be 
     * registered under another category or categories.
     *
     * @param registry a <code>FactoryRegistry</code> where this object has been registered.
     * @param category a <code>Class</code> object indicating the registry category under which 
     * this object has been registered.
     */
    void onRegistration(FactoryRegistry registry, Class<?> category);

    /**
     * Called when an object implementing this interface is removed from the given
     * <code>category</code> of the given <code>registry</code>. The object may still be
     * registered under another category or categories.
     *
     * @param registry a <code>FactoryRegistry</code> from which this object is being
     * (wholly or partially) deregistered.
     * @param category a <code>Class</code> object indicating the registry category from which
     * this object is being deregistered.
     */
    void onDeregistration(FactoryRegistry registry, Class<?> category);
}
