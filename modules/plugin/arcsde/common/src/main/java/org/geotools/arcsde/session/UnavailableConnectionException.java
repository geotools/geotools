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

/**
 * Exception thrown when a free SDE connection can't be obtained after the calling thread was
 * waiting an available connection for <code>SdeConnectionPool instance's getMaxWaitTime()</code>
 * milliseconds
 * 
 * @author Gabriel Roldan
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/pool/UnavailableArcSDEConnectionException.java $
 * @version $Id$
 */
public class UnavailableConnectionException extends Exception {

    private static final long serialVersionUID = -7964603239735118491L;

    /**
     * Creates a new UnavailableArcSDEConnectionException object.
     * 
     * @param usedConnections
     * @param config
     */
    UnavailableConnectionException(final int usedConnections, final ArcSDEConnectionConfig config) {
        super("The maximun of " + usedConnections + " to " + config.toString()
                + " has been reached");
    }
}
