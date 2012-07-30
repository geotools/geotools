/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.impl;

import static org.geotools.data.wfs.internal.Loggers.MODULE;

import java.io.IOException;
import java.util.logging.Level;

import org.geotools.data.Diff;
import org.geotools.data.store.DiffTransactionState;

/**
 * Transaction state responsible for holding an in memory {@link Diff} of any modifications.
 */
class WFSLocalTransactionState extends DiffTransactionState {

    private WFSContentState state;

    /**
     * Transaction state responsible for holding an in memory {@link Diff}.
     * 
     * @param state
     *            ContentState for the transaction
     */
    public WFSLocalTransactionState(WFSContentState state) {
        super(state, new WFSDiff());
        this.state = state;
    }

    @Override
    public WFSDiff getDiff() {
        return (WFSDiff) super.getDiff();
    }

    WFSContentState getState() {
        return state;
    }

    /**
     * We don't do any actual commit here, but let the {@link WFSRemoteTransactionState} do it all
     * for all the types changed inside the transaction.
     * 
     * @see org.geotools.data.store.DiffTransactionState#commit()
     */
    @Override
    public synchronized void commit() throws IOException {
        if (MODULE.isLoggable(Level.FINER)) {
            MODULE.finer(getClass().getSimpleName() + "::commit(): doing nothing, letting "
                    + WFSRemoteTransactionState.class.getSimpleName()
                    + " do the job for the whole DataStore");
        }
    }
}