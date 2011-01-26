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
 * This SessionPool makes a maximum of *one* Connection available to the calling application.
 * <p>
 * Why? ArcSDEConnections are both expensive to set up and expensive in cache. We still use an
 * ObjectCache internally in order to control time out behavior.
 * <p>
 * The trick is this time that Transaction.AUTO_COMMIT is only treated as suggestion for read only
 * code. The system will allow one transaction to be underway at any point, and will hand out that
 * connection to code that knows how to ask.
 * <p>
 * This is an aggressive experiment designed to cut down the number of connections needed.
 * <p>
 * 
 * @author Jody Garnett
 * @since 2.5
 */
final class ArcSDEConnectionReference extends SessionPool {
    /**
     * Our "cached" session used to issue read only commands.
     */
    ISession cached;

    protected ArcSDEConnectionReference(ArcSDEConnectionConfig config) throws IOException {
        super(config);
        if (config.getMaxConnections().intValue() != 1) {
            throw new IllegalArgumentException(
                    "ConnectionReference is only allowed to manage one connection.");
        }
    }

    @Override
    public ISession getSession(final boolean transactional) throws IOException,
            UnavailableConnectionException {
        if (cached == null) {
            ISession session = super.getSession(transactional); // this will block if session is
                                                                // already in use
            this.cached = new SessionWrapper(session) {
                @Override
                public void dispose() {
                    // ignore
                }
            };
        }
        return cached;
    }
}
