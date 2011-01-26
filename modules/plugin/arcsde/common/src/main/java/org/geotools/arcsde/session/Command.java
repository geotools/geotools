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

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;

/**
 * Runnable used to interact with an ArcSDEConnection.
 * <p>
 * Instances of this class can be sent to {@link Session#issue(Command)} in order to be executed. A
 * {@code Command} has exclusive access to the {@link SeConnection connection} for the duration of
 * its {@code execute} method. This facility is used to prevent a series of complicated locks and
 * try/catch/finally code.
 * </p>
 * 
 * @author Jody Garnett
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/common/src/main/java/org/geotools
 *         /arcsde/session/Command.java $
 */
public abstract class Command<R> {
    /**
     * Executed to operate on an SeConnection, a Command is scheduled for execution on a Session.
     * <p>
     * Please keep in mind that a Command should be short in duration; you are sharing this
     * SeConnection with other threads.
     * 
     * @param session
     *            the Session the command is being executed inside
     * @param connection
     *            the session's connection, used to interact with ArcSDE
     * @return the result of the command execution, or null if the command is not meant to return
     *         anything (a command meant to return something should fail if not able to)
     */
    public abstract R execute(ISession session, SeConnection connection) throws SeException,
            IOException;
}
