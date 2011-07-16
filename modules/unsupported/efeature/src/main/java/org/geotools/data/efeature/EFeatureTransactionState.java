/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;

/**
 * @author kengu - 7. juli 2011
 *
 */
public class EFeatureTransactionState implements State {

    private WeakReference<Transaction> eTx;
    private WeakReference<EFeatureDataStore> eDataStore;
    
    private ChangeRecorder eRecorder;
    private ChangeDescription eDescription;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    public EFeatureTransactionState(EFeatureDataStore eDataStore) {
        //
        // Store weak reference to data store
        //
        this.eDataStore = new WeakReference<EFeatureDataStore>(eDataStore);
        //
        // Create change recording capabilities
        //
        this.eRecorder = new ChangeRecorder();
        //
        // End current recording (initialize the stepwise write recording cycle)
        //
        this.eDescription = this.eRecorder.endRecording();
    }

    // ----------------------------------------------------- 
    //  State implementation
    // -----------------------------------------------------
    
    public Transaction getTransaction() {
        return eTx.get();
    }
    
    @Override
    public void setTransaction(Transaction eTx) {
        this.eTx = new WeakReference<Transaction>(eTx);
    }

    public EFeatureDataStore eDataStore() {
        return eDataStore.get();
    }
    
    @Override
    public void addAuthorization(String AuthID) throws IOException {
        // TODO who does this fit into the picture?
        
    }

    @Override
    public void commit() throws IOException {
        
    }

    @Override
    public void rollback() throws IOException {
        // TODO Auto-generated method stub
        
    }

}
