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
package org.geotools.arcsde.versioning;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.logging.Loggers;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.Commands.GetVersionCommand;
import org.geotools.arcsde.session.ISession;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeObjectId;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeState;
import com.esri.sde.sdk.client.SeStreamOp;
import com.esri.sde.sdk.client.SeVersion;

/**
 * Handles a versioned table when in auto commit mode, meaning it sets up streams to edit directly
 * the version indicated by the provided version name.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *         http://svn.geotools.org/trunk/modules/plugin/arcsde/datastore/src/main/java/org/geotools
 *         /arcsde/data/versioning/AutoCommitVersionHandler.java $
 */
public class AutoCommitVersionHandler implements ArcSdeVersionHandler {

    private static final Logger LOGGER = Loggers
            .getLogger(AutoCommitVersionHandler.class.getName());

    private SeVersion version;

    private final GetVersionCommand getVersionCommand;

    private final String versionName;

    public AutoCommitVersionHandler(final String versionName) throws IOException {
        this.versionName = versionName;
        this.getVersionCommand = new GetVersionCommand(versionName);
    }

    public void setUpStream(final ISession session, final SeStreamOp streamOperation)
            throws IOException {

        session.issue(new Command<Void>() {
            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("setting up stream for version " + versionName + " in "
                            + streamOperation.getClass().getName());
                }

                if (version == null) {
                    version = session.issue(getVersionCommand);
                }

                if (!(streamOperation instanceof SeQuery)) {
                    LOGGER.finer("StreamOp is not query, verifying an SeState can be used");

                    // create a new state for the operation only if its not a
                    // simple query
                    final SeState currentState;
                    final SeObjectId currStateId = version.getStateId();
                    currentState = new SeState(connection, currStateId);

                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.finer(versionName + " version state: " + currStateId.longValue()
                                + ", parent: " + currentState.getParentId().longValue()
                                + ", open: " + currentState.isOpen() + ", owner: "
                                + currentState.getOwner() + ", current user: "
                                + connection.getUser());
                    }

                    final String currUser = connection.getUser();
                    final String stateOwner = currentState.getOwner();

                    if (!(currentState.isOpen() && currUser.equals(stateOwner))) {
                        LOGGER.finer("Creating new state for the operation");
                        SeState newState = session.createChildState(currStateId.longValue());
                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer("Setting version " + versionName + "to new state "
                                    + newState.getId().longValue());
                        }
                        version.changeState(newState.getId());
                    }
                }

                SeObjectId differencesId = new SeObjectId(SeState.SE_NULL_STATE_ID);
                // version.getInfo();
                SeObjectId currentStateId = version.getStateId();
                streamOperation.setState(currentStateId, differencesId,
                        SeState.SE_STATE_DIFF_NOCHECK);
                return null;
            }
        });
    }

    public void editOperationWritten(SeStreamOp editOperation) throws IOException {
        //
    }

    public void editOperationFailed(SeStreamOp editOperation) throws IOException {
        //
    }

    /**
     * This method should not be called, but {@link #editOperationFailed(SeStreamOp)} instead, as
     * this is a handler for auto commit mode
     * 
     * @throws UnsupportedOperationException
     * @see {@link ArcSdeVersionHandler#rollbackEditState()}
     */
    public void commitEditState() throws IOException {
        throw new UnsupportedOperationException("commit shouldn't be called for"
                + " an autocommit versioning handler ");
    }

    /**
     * This method should not be called, but {@link #editOperationWritten(SeStreamOp)} instead, as
     * this is a handler for auto commit mode
     * 
     * @throws UnsupportedOperationException
     * @see {@link ArcSdeVersionHandler#rollbackEditState()}
     */
    public void rollbackEditState() throws IOException {
        throw new UnsupportedOperationException("rollback shouldn't be called for"
                + " an autocommit versioning handler ");
    }

}
