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

import org.geotools.arcsde.session.ISession;

import com.esri.sde.sdk.client.SeStreamOp;

/**
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *         http://svn.geotools.org/trunk/modules/plugin/arcsde/datastore/src/main/java/org/geotools
 *         /arcsde/data/versioning/ArcSdeVersionHandler.java $
 */
public interface ArcSdeVersionHandler {

    /**
     * Sets up the stream to work over the version/state needed
     * 
     * @param streamOperation
     * @throws IOException
     */
    void setUpStream(ISession session, SeStreamOp streamOperation) throws IOException;

    /**
     * Called when a single edit operation that was settled up with {@link #setUpStream(SeStreamOp)}
     * fails
     * 
     * @throws IOException
     */
    void editOperationFailed(SeStreamOp editOperation) throws IOException;

    /**
     * Called after successful execution of any single edit (SeInsert, SeUpdate, SeDelete)
     * operation.
     * 
     * @param editOperation
     * @throws IOException
     */
    void editOperationWritten(SeStreamOp editOperation) throws IOException;

    /**
     * Causes the current version to synchronize with the current edit state.
     * 
     * @throws IOException
     */
    void commitEditState() throws IOException;

    /**
     * Called when a transaction is being rolled back
     * 
     * @throws IOException
     */
    void rollbackEditState() throws IOException;

    /**
     * Null object to handle non versioned tables, does nothing.
     */
    ArcSdeVersionHandler NONVERSIONED_HANDLER = new ArcSdeVersionHandler() {

        public void commitEditState() throws IOException {
            // do nothing, not a versioned table
        }

        public void editOperationFailed(SeStreamOp editOperation) throws IOException {
            // do nothing, not a versioned table
        }

        public void editOperationWritten(SeStreamOp editOperation) throws IOException {
            // do nothing, not a versioned table
        }

        public void rollbackEditState() throws IOException {
            // do nothing, not a versioned table
        }

        public void setUpStream(ISession session, SeStreamOp streamOperation) throws IOException {
            // do nothing, not a versioned table
        }
    };
}
