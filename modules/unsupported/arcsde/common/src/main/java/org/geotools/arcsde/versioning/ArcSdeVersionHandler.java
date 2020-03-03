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

import com.esri.sde.sdk.client.SeStreamOp;
import java.io.IOException;
import org.geotools.arcsde.session.ISession;

/**
 * @author Gabriel Roldan (TOPP)
 * @since 2.5.x
 */
public interface ArcSdeVersionHandler {

    /** Sets up the stream to work over the version/state needed */
    void setUpStream(ISession session, SeStreamOp streamOperation) throws IOException;

    /**
     * Called when a single edit operation that was settled up with {@link #setUpStream(SeStreamOp)}
     * fails
     */
    void editOperationFailed(SeStreamOp editOperation) throws IOException;

    /**
     * Called after successful execution of any single edit (SeInsert, SeUpdate, SeDelete)
     * operation.
     */
    void editOperationWritten(SeStreamOp editOperation) throws IOException;

    /** Causes the current version to synchronize with the current edit state. */
    void commitEditState() throws IOException;

    /** Called when a transaction is being rolled back */
    void rollbackEditState() throws IOException;

    /** Null object to handle non versioned tables, does nothing. */
    ArcSdeVersionHandler NONVERSIONED_HANDLER =
            new ArcSdeVersionHandler() {

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

                public void setUpStream(ISession session, SeStreamOp streamOperation)
                        throws IOException {
                    // do nothing, not a versioned table
                }
            };
}
