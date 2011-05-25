/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.session;

import java.io.IOException;

import org.geotools.arcsde.jndi.ArcSDEConnectionFactory;

/**
 * Factory interface for {@link ISessionPool}.
 * <p>
 * A specific {@link ISessionPool} factory can be injected when using JNDI through the
 * {@code sessionPoolFactory} parameter. See {@link ArcSDEConnectionFactory}.
 * </p>
 * 
 * @author Gabriel Roldan (OpenGeo)
 * 
 *
 * @source $URL$
 * @version $Id$
 * @since 2.5.7
 */
public interface ISessionPoolFactory {

    /**
     * Creates a connection pool factory for the given connection parameters.
     * 
     * @param config
     *             contains the connection parameters and pool preferences
     * @return a pool for the given connection parameters, wether it already existed or had to be
     *         created.
     * @throws IOException
     *             if the pool needs but can't be created
     */
    ISessionPool createPool(ArcSDEConnectionConfig config) throws IOException;

}
