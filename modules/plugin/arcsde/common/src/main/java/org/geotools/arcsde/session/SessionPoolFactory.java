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
 *
 */
package org.geotools.arcsde.session;

import java.io.IOException;

/**
 * {@link SessionPool} factory.
 * 
 * @author Gabriel Roldan
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/arcsde/datastore/src/main
 *         /java/org/geotools/arcsde/pool/SessionPoolFactory.java $
 * @version $Id$
 */
public class SessionPoolFactory implements ISessionPoolFactory {

    /** singleton pool factory */
    private static final ISessionPoolFactory singleton = new SessionPoolFactory();

    /**
     * Creates a new SdeConnectionPoolFactory object.
     */
    private SessionPoolFactory() {
        // intentionally blank
    }

    /**
     * Returns a connection pool factory instance
     * 
     * @return the connection pool factory singleton
     */
    public synchronized static ISessionPoolFactory getInstance() {
        return singleton;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.arcsde.session.ISessionPoolFactory#createPool(org.geotools.arcsde.session.
     * ArcSDEConnectionConfig)
     */
    public synchronized ISessionPool createPool(ArcSDEConnectionConfig config) throws IOException {
        ISessionPool pool;

        // the new pool will be populated with config.minConnections
        // connections
        if (config.getMaxConnections() != null && config.getMaxConnections() == 1) {
            // engage experimental single connection mode!
            pool = new ArcSDEConnectionReference(config);
        } else {
            pool = new SessionPool(config);
        }

        return pool;
    }
}
