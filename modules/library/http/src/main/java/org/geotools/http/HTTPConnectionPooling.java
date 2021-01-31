/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http;

import java.io.Closeable;

/**
 * Interface to support clients that uses a connection pool
 *
 * @author Roar Brænden
 */
public interface HTTPConnectionPooling extends HTTPBehavior, Closeable {

    /**
     * Max connections kept in the pool
     *
     * @return
     */
    int getMaxConnections();

    /**
     * Sets max connections kept in the pool
     *
     * @param maxConnections
     */
    void setMaxConnections(int maxConnections);
}
